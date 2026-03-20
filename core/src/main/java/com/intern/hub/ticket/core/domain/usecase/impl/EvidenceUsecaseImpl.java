package com.intern.hub.ticket.core.domain.usecase.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.command.UploadEvidenceCommand;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvidenceUsecaseImpl implements EvidenceUsecase {

    private final EvidenceRepository evidenceRepository;
    private final TicketRepository ticketRepository;
    private final Snowflake snowflake;

    @Override
    @Transactional
    public EvidenceModel uploadEvidence(UploadEvidenceCommand command) {
        ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new NotFoundException("resource.not.found", "Ticket not found"));

        EvidenceModel model = EvidenceModel.builder()
                .evidenceId(snowflake.next())
                .ticketId(command.ticketId())
                .evidenceKey(command.evidenceKey())
                .fileType(command.fileType())
                .fileSize(command.fileSize())
                .status(EvidenceStatus.UPLOADED)
                .build();

        return evidenceRepository.save(model);
    }

    @Override
    public List<EvidenceModel> getEvidences(Long ticketId) {
        return evidenceRepository.findByTicketId(ticketId);
    }
}