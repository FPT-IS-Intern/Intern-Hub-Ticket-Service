package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.api.dto.request.UploadEvidenceRequest;
import com.intern.hub.ticket.core.domain.command.UploadEvidenceCommand;
import com.intern.hub.ticket.core.domain.dto.EvidenceDto;
import com.intern.hub.ticket.core.port.in.GetEvidenceUseCase;
import com.intern.hub.ticket.core.port.in.UploadEvidenceUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets/{ticketId}/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final UploadEvidenceUseCase uploadEvidenceUseCase;
    private final GetEvidenceUseCase getEvidenceUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // @Authenticated
    public ResponseApi<EvidenceDto> uploadEvidence(
            @PathVariable Long ticketId,
            @Valid @RequestBody UploadEvidenceRequest request) {

        UploadEvidenceCommand command = UploadEvidenceCommand.builder()
                .ticketId(ticketId)
                .evidenceFolder(request.getEvidenceFolder())
                .evidenceUrl(request.getEvidenceUrl())
                .fileType(request.getFileType())
                .fileSize(request.getFileSize())
                .build();

        return ResponseApi.ok(uploadEvidenceUseCase.uploadEvidence(command));
    }

    @GetMapping
    // @Authenticated
    public ResponseApi<List<EvidenceDto>> getEvidences(@PathVariable Long ticketId) {
        return ResponseApi.ok(getEvidenceUseCase.getEvidences(ticketId));
    }
}
