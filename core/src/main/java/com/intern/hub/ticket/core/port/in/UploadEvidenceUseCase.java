package com.intern.hub.ticket.core.port.in;

import com.intern.hub.ticket.core.domain.command.EvidenceDto;
import com.intern.hub.ticket.core.domain.command.UploadEvidenceCommand;

public interface UploadEvidenceUseCase {
    EvidenceDto uploadEvidence(UploadEvidenceCommand command);
}
