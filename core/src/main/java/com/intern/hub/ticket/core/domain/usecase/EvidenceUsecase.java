package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;

public interface EvidenceUsecase {

    List<EvidenceModel> getEvidences(Long ticketId);

    PresignedUrlModel getPresignedUrl(String fileName, String contentType, Long fileSize);
}