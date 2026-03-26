package com.intern.hub.ticket.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.InternalUploadDirectPort;
import com.intern.hub.ticket.core.domain.port.StorageLifecyclePort;
import com.intern.hub.ticket.core.domain.service.DocumentService;
import com.intern.hub.ticket.infra.persistence.entity.Evidence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private static final long MAX_EVIDENCE_SIZE_BYTES = 20 * 1024 * 1024L; // 20MB
    private static final String EVIDENCE_CONTENT_TYPE_REGEX =
            "image/(png|jpeg|jpg|gif)|application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document|text/plain";

    private final EvidenceRepository evidenceRepository;
    private final InternalUploadDirectPort fileStorageRepository;
    private final StorageLifecyclePort storageLifecyclePort;
    private final Snowflake snowflake;

    @Value("${aws.s3.max-evidence-total-size:20971520}")
    private Long maxTotalSize;

    @Override
    @Transactional(readOnly = true)
    public List<EvidenceModel> getDocuments(Long ticketId) {
        return evidenceRepository.findByTicketId(ticketId);
    }

    @Override
    @Transactional
    public void replaceDocuments(Long ticketId, Long actorId, String destinationPath, List<MultipartFile> files) {
        List<Evidence> existingDocuments = evidenceRepository.findByTicketId(ticketId).stream()
                .map(model -> Evidence.builder()
                        .id(model.getEvidenceId())
                        .ticketId(model.getTicketId())
                        .evidenceKey(model.getEvidenceKey())
                        .fileType(model.getFileType())
                        .fileSize(model.getFileSize())
                        .status(model.getStatus())
                        .build())
                .toList();

        for (Evidence existingDocument : existingDocuments) {
            String key = existingDocument.getEvidenceKey();
            if (key != null && !key.isBlank()) {
                storageLifecyclePort.deleteAfterCommit(key, actorId);
            }
        }
        if (!existingDocuments.isEmpty()) {
            evidenceRepository.deleteAllByTicketId(ticketId);
        }

        if (files == null || files.isEmpty()) {
            return;
        }

        long totalUploadSize = files.stream().mapToLong(MultipartFile::getSize).sum();
        if (totalUploadSize > maxTotalSize) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Tổng dung lượng file vượt quá giới hạn " + (maxTotalSize / 1024 / 1024) + "MB");
        }

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            String s3Key;
            try {
                s3Key = fileStorageRepository.uploadFile(
                        file,
                        destinationPath,
                        actorId,
                        MAX_EVIDENCE_SIZE_BYTES,
                        EVIDENCE_CONTENT_TYPE_REGEX);
            } catch (BadRequestException e) {
                throw e;
            } catch (Exception e) {
                log.error("Failed to upload file to DMS for ticket {}", ticketId, e);
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                        "Không thể upload file lên hệ thống lưu trữ: " + e.getMessage());
            }

            storageLifecyclePort.cleanupOnRollback(s3Key, actorId);

            EvidenceModel evidenceModel = EvidenceModel.builder()
                    .evidenceId(snowflake.next())
                    .ticketId(ticketId)
                    .evidenceKey(s3Key)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .status(EvidenceStatus.UPLOADED)
                    .build();

            evidenceRepository.save(evidenceModel);
        }
    }
}
