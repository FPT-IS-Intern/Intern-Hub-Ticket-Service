package com.intern.hub.ticket.api.controller;

import java.util.List;
import java.util.Set;

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

        @GetMapping
        public ResponseApi<List<EvidenceDto>> getEvidences(@PathVariable Long ticketId) {
                List<EvidenceDto> dtos = evidenceUsecase.getEvidences(ticketId).stream()
                                .map(this::mapToDto)
                                .toList();
                return ResponseApi.ok(dtos);
        }

        /**
         * Upload a single file via multipart. Mirrors HRM pattern — passes MultipartFile
         * directly to the usecase without intermediate byte[] conversion.
         */
        @PostMapping("/upload")
        public ResponseApi<String> uploadMultipartFile(
                        @RequestParam("file") MultipartFile file,
                        @RequestParam("destinationPath") String destinationPath) {
                Long actorId = UserContext.requiredUserId();
                String objectKey = evidenceUsecase.uploadFile(file, destinationPath, actorId);
                return ResponseApi.ok(objectKey);
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
