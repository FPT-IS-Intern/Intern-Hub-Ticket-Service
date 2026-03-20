package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.api.dto.request.UploadEvidenceRequest;
import com.intern.hub.ticket.api.dto.response.EvidenceDto;
import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.command.UploadEvidenceCommand;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/{ticketId}/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceUsecase evidenceUsecase;

    @PostMapping
    // @Authenticated
    public ResponseApi<EvidenceDto> attachEvidence(
            @PathVariable Long ticketId,
            @Valid @RequestBody UploadEvidenceRequest request) {

        UploadEvidenceCommand command = new UploadEvidenceCommand(
                ticketId,
                request.evidenceKey(),
                request.fileType(),
                request.fileSize());

        EvidenceModel savedModel = evidenceUsecase.uploadEvidence(command);
        return ResponseApi.ok(mapToDto(savedModel));
    }

    /**
     * Lấy danh sách minh chứng của một Ticket
     */
    @GetMapping
    // @Authenticated
    public ResponseApi<List<EvidenceDto>> getEvidences(@PathVariable Long ticketId) {
        List<EvidenceDto> dtos = evidenceUsecase.getEvidences(ticketId).stream()
                .map(this::mapToDto)
                .toList();
        return ResponseApi.ok(dtos);
    }

    private EvidenceDto mapToDto(EvidenceModel model) {
        return new EvidenceDto(
                model.getEvidenceId(),
                model.getTicketId(),
                model.getEvidenceKey(),
                model.getFileType(),
                model.getFileSize(),
                model.getStatus(),
                model.getCreatedAt(),
                model.getUpdatedAt(),
                model.getCreatedBy(),
                model.getUpdatedBy());
    }
}
