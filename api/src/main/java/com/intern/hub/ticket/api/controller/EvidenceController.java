package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.api.dto.request.PresignedUrlReq;
import com.intern.hub.ticket.api.dto.response.EvidenceDto;
import com.intern.hub.ticket.api.dto.response.PresignedUrlDto;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/evidences")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class EvidenceController {

        private final EvidenceUsecase evidenceUsecase;

        @PostMapping("/presigned-url")
        public ResponseApi<PresignedUrlDto> generatePresignedUrl(@Valid @RequestBody PresignedUrlReq request) {
                PresignedUrlModel model = evidenceUsecase.getPresignedUrl(
                        request.fileName(),
                        request.contentType(),
                        request.fileSize());
                return ResponseApi.ok(new PresignedUrlDto(model.uploadUrl(), model.tempKey()));
        }

        @GetMapping("/{ticketId}")
        public ResponseApi<List<EvidenceDto>> getEvidences(@PathVariable Long ticketId) {
                List<EvidenceDto> dtos = evidenceUsecase.getEvidences(ticketId).stream()
                                .map(this::mapToDto)
                                .toList();
                return ResponseApi.ok(dtos);
        }

        /**
         * Upload a single evidence file via multipart. Uploads to DMS storage and saves the evidence record to DB.
         *
         * @param file            the multipart file to upload
         * @param ticketId        the ticket ID to associate the evidence with
         * @param destinationPath the storage destination path within DMS
         */
        @PostMapping("/upload")
        public ResponseApi<EvidenceDto> uploadMultipartFile(
                        @RequestParam("file") MultipartFile file,
                        @RequestParam("ticketId") Long ticketId,
                        @RequestParam("destinationPath") String destinationPath) {
                Long actorId = UserContext.requiredUserId();
                EvidenceModel savedEvidence = evidenceUsecase.uploadFile(file, destinationPath, ticketId, actorId);
                return ResponseApi.ok(mapToDto(savedEvidence));
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
