package com.intern.hub.ticket.core.usecase;

import java.time.LocalDate;

import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.UploadEvidenceCommand;
import com.intern.hub.ticket.core.domain.dto.EvidenceDto;
import com.intern.hub.ticket.core.domain.model.Evidence;
import com.intern.hub.ticket.core.domain.model.EvidenceStatus;
import com.intern.hub.ticket.core.port.in.UploadEvidenceUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.EvidenceRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UploadEvidenceService implements UploadEvidenceUseCase {

        private final TicketRepository ticketRepository;
        private final EvidenceRepository evidenceRepository;
        private final IdGenerator idGenerator;

        @Override
        public EvidenceDto uploadEvidence(UploadEvidenceCommand command) {
                // Validate ticket exists
                ticketRepository.findById(command.ticketId())
                                .orElseThrow(() -> new NotFoundException("Ticket not found"));

                Evidence evidence = Evidence.builder()
                                .evidenceId(idGenerator.nextId())
                                .ticketId(command.ticketId())
                                .evidenceFolder(command.evidenceFolder())
                                .evidenceUrl(command.evidenceUrl())
                                .uploadedAt(LocalDate.now())
                                .fileType(command.fileType())
                                .fileSize(command.fileSize())
                                .status(EvidenceStatus.UPLOADED) // Assuming initial status
                                .version(1)
                                .build();

                evidenceRepository.save(evidence);

                return EvidenceDto.builder()
                                .evidenceId(evidence.getEvidenceId())
                                .ticketId(evidence.getTicketId())
                                .evidenceFolder(evidence.getEvidenceFolder())
                                .evidenceUrl(evidence.getEvidenceUrl())
                                .uploadedAt(evidence.getUploadedAt())
                                .fileType(evidence.getFileType())
                                .fileSize(evidence.getFileSize())
                                .status(evidence.getStatus())
                                .build();
        }
}
