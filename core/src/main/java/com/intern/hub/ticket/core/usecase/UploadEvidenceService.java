package com.intern.hub.ticket.core.usecase;

import java.time.OffsetDateTime;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.ticket.core.domain.command.UploadEvidenceCommand;
import com.intern.hub.ticket.core.domain.dto.EvidenceDto;
import com.intern.hub.ticket.core.domain.model.Evidence;
import com.intern.hub.ticket.core.domain.model.enums.EvidenceStatus;
import com.intern.hub.ticket.core.port.in.UploadEvidenceUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.EvidenceRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
public class UploadEvidenceService implements UploadEvidenceUseCase {

        private final TicketRepository ticketRepository;
        private final EvidenceRepository evidenceRepository;
        private final IdGenerator idGenerator;

        @Override
        public EvidenceDto uploadEvidence(UploadEvidenceCommand command) {
                if (command.evidenceUrl() == null || command.evidenceUrl().isBlank()) {
                        throw new BadRequestException("evidenceUrl cannot be empty");
                }
                if (command.evidenceFolder() == null || command.evidenceFolder().isBlank()) {
                        throw new BadRequestException("evidenceFolder cannot be empty");
                }
                if (command.fileType() == null || command.fileType().isBlank()) {
                        throw new BadRequestException("fileType cannot be empty");
                }
                if (command.fileSize() == null || command.fileSize() <= 0) {
                        throw new BadRequestException("fileSize must be greater than 0");
                }

                // Validate ticket exists
                ticketRepository.findById(command.ticketId())
                                .orElseThrow(() -> new NotFoundException("Ticket not found"));

                Evidence evidence = Evidence.builder()
                                .evidenceId(idGenerator.nextId())
                                .ticketId(command.ticketId())
                                .evidenceFolder(command.evidenceFolder())
                                .evidenceUrl(command.evidenceUrl())
                                .uploadedAt(OffsetDateTime.now())
                                .fileType(command.fileType())
                                .fileSize(command.fileSize())
                                .status(EvidenceStatus.UPLOADED)
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
