package com.intern.hub.ticket.core.port.in;

import java.util.List;

import com.intern.hub.ticket.core.domain.command.EvidenceDto;

public interface GetEvidenceUseCase {
    List<EvidenceDto> getEvidences(Long ticketId);
}
