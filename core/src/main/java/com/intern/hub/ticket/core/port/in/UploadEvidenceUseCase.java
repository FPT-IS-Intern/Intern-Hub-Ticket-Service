package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.command.UploadEvidenceCommand;
import com.intern.hub.ticket.core.domain.dto.EvidenceDto;

public interface UploadEvidenceUseCase {
    EvidenceDto uploadEvidence(UploadEvidenceCommand command);
}
