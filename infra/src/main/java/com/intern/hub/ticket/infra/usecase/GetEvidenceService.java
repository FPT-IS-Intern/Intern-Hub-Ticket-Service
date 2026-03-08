package com.intern.hub.ticket.infra.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.command.EvidenceDto;
import com.intern.hub.ticket.core.port.in.GetEvidenceUseCase;
import com.intern.hub.ticket.core.port.repository.EvidenceRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetEvidenceService implements GetEvidenceUseCase {

    private final EvidenceRepository evidenceRepository;

    @Override
    public List<EvidenceDto> getEvidences(Long ticketId) {
        return evidenceRepository.findByTicketId(ticketId).stream()
                .map(evidence -> EvidenceDto.builder()
                        .evidenceId(evidence.getEvidenceId())
                        .ticketId(evidence.getTicketId())
                        .evidenceFolder(evidence.getEvidenceFolder())
                        .evidenceUrl(evidence.getEvidenceUrl())
                        .uploadedAt(evidence.getUploadedAt())
                        .fileType(evidence.getFileType())
                        .fileSize(evidence.getFileSize())
                        .status(evidence.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
