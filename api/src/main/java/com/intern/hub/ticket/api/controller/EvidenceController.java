package com.intern.hub.ticket.api.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.ticket.api.dto.request.PresignedUrlReq;
import com.intern.hub.ticket.api.dto.response.EvidenceDto;
import com.intern.hub.ticket.api.dto.response.PresignedUrlDto;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import com.intern.hub.ticket.core.domain.model.command.FileCommand;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket/evidences")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class EvidenceController {

        private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "pdf", "docx");
        private static final long MAX_FILE_SIZE = 2 * 1024 * 1024L; // 2MB

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

        @PostMapping("/upload")
        public ResponseApi<String> uploadMultipartFile(
                        @RequestParam("file") MultipartFile file,
                        @RequestParam("destinationPath") String destinationPath) {
                Long actorId = UserContext.requiredUserId();

                FileCommand fileCommand = validateAndToFileCommand(file);
                String objectKey = evidenceUsecase.uploadFile(fileCommand, destinationPath, actorId);

                return ResponseApi.ok(objectKey);
        }

        private FileCommand validateAndToFileCommand(MultipartFile file) {
                if (file == null || file.isEmpty()) {
                        throw new BadRequestException("bad.request", "File must not be empty");
                }
                if (file.getSize() > MAX_FILE_SIZE) {
                        throw new BadRequestException(
                                "bad.request",
                                "File vượt quá dung lượng cho phép (tối đa 2MB)");
                }
                String extension = getFileExtension(file.getOriginalFilename());
                if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                        throw new BadRequestException(
                                "bad.request",
                                "Định dạng file không được hỗ trợ. Chỉ chấp nhận: .jpg, .png, .pdf, .docx");
                }
                try {
                        return new FileCommand(
                                file.getBytes(),
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getSize());
                } catch (Exception e) {
                        throw new BadRequestException("bad.request", "Không thể đọc nội dung file");
                }
        }

        private String getFileExtension(String filename) {
                if (filename == null || !filename.contains(".")) {
                        return "";
                }
                return filename.substring(filename.lastIndexOf(".") + 1);
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
