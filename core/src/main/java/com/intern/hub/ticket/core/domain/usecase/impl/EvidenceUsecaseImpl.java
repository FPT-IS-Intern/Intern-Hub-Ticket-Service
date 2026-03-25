package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;
import com.intern.hub.ticket.core.domain.port.DmsPort;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.InternalUploadDirectPort;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
public class EvidenceUsecaseImpl implements EvidenceUsecase {

    private static final long MAX_EVIDENCE_SIZE_BYTES = 20 * 1024 * 1024L; // 20MB
    private static final String EVIDENCE_CONTENT_TYPE_REGEX =
            "image/(png|jpeg|jpg)|application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private final EvidenceRepository evidenceRepository;
    private final DmsPort dmsPort;
    private final InternalUploadDirectPort internalUploadDirectPort;
    private final Snowflake snowflake;

    @Override
    public PresignedUrlModel getPresignedUrl(String fileName, String contentType, Long fileSize) {
        return dmsPort.generatePresignedUrl(fileName, contentType, fileSize);
    }

    @Override
    public EvidenceModel uploadFile(MultipartFile file, String destinationPath, Long ticketId, Long actorId) {
        String objectKey = internalUploadDirectPort.uploadFile(
                file,
                destinationPath,
                actorId,
                MAX_EVIDENCE_SIZE_BYTES,
                EVIDENCE_CONTENT_TYPE_REGEX);

        log.info("File uploaded to DMS with objectKey: {} for ticketId: {}", objectKey, ticketId);

        EvidenceModel evidenceModel = EvidenceModel.builder()
                .evidenceId(snowflake.next())
                .ticketId(ticketId)
                .evidenceKey(objectKey)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .status(EvidenceStatus.UPLOADED)
                .build();

        return evidenceRepository.save(evidenceModel);
    }

    @Override
    public List<EvidenceModel> getEvidences(Long ticketId) {
        return evidenceRepository.findByTicketId(ticketId);
    }
}
