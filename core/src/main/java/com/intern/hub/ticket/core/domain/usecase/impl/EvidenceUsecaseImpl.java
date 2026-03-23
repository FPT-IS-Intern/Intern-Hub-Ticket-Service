package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import com.intern.hub.ticket.core.domain.port.DmsPort;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvidenceUsecaseImpl implements EvidenceUsecase {

    private final EvidenceRepository evidenceRepository;
    private final DmsPort dmsPort;

    public PresignedUrlModel getPresignedUrl(String fileName, String contentType, Long fileSize) {
        // validate fileSize ở đây (VD: không quá 5MB)
        return dmsPort.generatePresignedUrl(fileName, contentType, fileSize);
    }

    @Override
    public List<EvidenceModel> getEvidences(Long ticketId) {
        return evidenceRepository.findByTicketId(ticketId);
    }
}