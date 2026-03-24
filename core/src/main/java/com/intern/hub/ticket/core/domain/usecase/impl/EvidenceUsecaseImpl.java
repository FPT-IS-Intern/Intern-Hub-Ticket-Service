package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import com.intern.hub.ticket.core.domain.model.command.FileCommand;
import com.intern.hub.ticket.core.domain.port.DmsPort;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.InternalUploadDirectPort;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class EvidenceUsecaseImpl implements EvidenceUsecase {

    private static final long MAX_EVIDENCE_SIZE_BYTES = 20 * 1024 * 1024L; // 20MB
    private static final String EVIDENCE_CONTENT_TYPE_REGEX =
            "image/(png|jpeg|jpg)|application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private final EvidenceRepository evidenceRepository;
    private final DmsPort dmsPort;
    private final InternalUploadDirectPort internalUploadDirectPort;

    public PresignedUrlModel getPresignedUrl(String fileName, String contentType, Long fileSize) {
        return dmsPort.generatePresignedUrl(fileName, contentType, fileSize);
    }

    @Override
    public String uploadFile(FileCommand file, String destinationPath, Long actorId) {
        return internalUploadDirectPort.uploadFile(
                file,
                destinationPath,
                actorId,
                MAX_EVIDENCE_SIZE_BYTES,
                EVIDENCE_CONTENT_TYPE_REGEX);
    }

    @Override
    public List<EvidenceModel> getEvidences(Long ticketId) {
        return evidenceRepository.findByTicketId(ticketId);
    }
}