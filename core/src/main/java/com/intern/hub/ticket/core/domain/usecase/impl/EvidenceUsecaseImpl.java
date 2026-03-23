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

    private final EvidenceRepository evidenceRepository;
    private final DmsPort dmsPort;
    private final InternalUploadDirectPort internalUploadDirectPort;

    public PresignedUrlModel getPresignedUrl(String fileName, String contentType, Long fileSize) {
        return dmsPort.generatePresignedUrl(fileName, contentType, fileSize);
    }

    @Override
    public String uploadFile(FileCommand file, String destinationPath, Long actorId) {
        return internalUploadDirectPort.upload(file, destinationPath, actorId);
    }

    @Override
    public List<EvidenceModel> getEvidences(Long ticketId) {
        return evidenceRepository.findByTicketId(ticketId);
    }
}