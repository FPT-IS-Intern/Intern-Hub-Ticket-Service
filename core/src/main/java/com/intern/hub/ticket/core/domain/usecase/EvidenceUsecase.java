package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.command.UploadEvidenceCommand;

public interface EvidenceUsecase {
    EvidenceModel uploadEvidence(UploadEvidenceCommand command);

    List<EvidenceModel> getEvidences(Long ticketId);
}