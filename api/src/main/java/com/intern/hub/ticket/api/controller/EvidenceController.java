package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.intern.hub.library.common.dto.ResponseApi;
//import com.intern.hub.starter.security.annotation.Authenticated;
import com.intern.hub.ticket.api.dto.request.PresignedUrlReq;
import com.intern.hub.ticket.api.dto.response.EvidenceDto;
import com.intern.hub.ticket.api.dto.response.PresignedUrlDto;
import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/evidences")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvidenceController {

    private final EvidenceUsecase evidenceUsecase;

    @PostMapping("/presigned-url")
//    @Authenticated
    public ResponseApi<PresignedUrlDto> generatePresignedUrl(@Valid @RequestBody PresignedUrlReq request) {

        // Gọi thẳng vào core
        PresignedUrlModel model = evidenceUsecase.getPresignedUrl(
                request.fileName(),
                request.contentType(),
                request.fileSize());

        // Trả kết quả về Frontend
        return ResponseApi.ok(new PresignedUrlDto(model.uploadUrl(), model.tempKey()));
    }

    /**
     * Lấy danh sách minh chứng của một Ticket
     */
    @GetMapping
//    @Authenticated
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
