package com.intern.hub.ticket.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.ticket.api.dto.request.ApproveTicketRequest;
import com.intern.hub.ticket.api.dto.request.BulkApproveTicketRequest;
import com.intern.hub.ticket.api.dto.request.CreateTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketResponse;
import com.intern.hub.ticket.api.util.UserContext;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.ApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveResponse;
import com.intern.hub.ticket.core.domain.model.command.BulkApproveTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.FileCommand;
import com.intern.hub.ticket.core.domain.model.command.RejectTicketCommand;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class TicketCommandController {

        private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "pdf", "docx");
        private static final long MAX_FILE_SIZE = 2 * 1024 * 1024L; // 2MB

        private final ApproveTicketUsecase approveTicketUsecase;
        private final TicketUsecase ticketUsecase;

        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseApi<TicketResponse> createTicket(
                        @RequestPart("request") @Valid CreateTicketRequest request,
                        @RequestPart(value = "evidences", required = false) MultipartFile[] evidences) {

                Long userId = UserContext.requiredUserId();
                FileCommand[] fileCommands = toFileCommands(evidences);

                CreateTicketCommand command = new CreateTicketCommand(
                                userId,
                                request.ticketTypeId(),
                                request.payload(),
                                fileCommands);

                TicketModel createdTicket = ticketUsecase.create(command);
                return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
        }

        private FileCommand[] toFileCommands(MultipartFile[] files) {
                if (files == null || files.length == 0) {
                        return null;
                }
                return Arrays.stream(files)
                                .map(this::validateAndToFileCommand)
                                .toArray(FileCommand[]::new);
        }

        private FileCommand validateAndToFileCommand(MultipartFile file) {
                if (file == null || file.isEmpty()) {
                        throw new BadRequestException("bad.request", "Evidence file must not be empty");
                }

                if (file.getSize() > MAX_FILE_SIZE) {
                        throw new BadRequestException(
                                "bad.request",
                                "File \"" + file.getOriginalFilename() + "\" vượt quá dung lượng cho phép (tối đa 2MB)");
                }

                String extension = getFileExtension(file.getOriginalFilename());
                if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                        throw new BadRequestException(
                                "bad.request",
                                "File \"" + file.getOriginalFilename() + "\" có định dạng không được hỗ trợ. Chỉ chấp nhận: .jpg, .png, .pdf, .docx");
                }

                try {
                        return new FileCommand(
                                file.getBytes(),
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getSize());
                } catch (Exception e) {
                        throw new BadRequestException("bad.request", "Không thể đọc nội dung file: " + file.getOriginalFilename());
                }
        }

        private String getFileExtension(String filename) {
                if (filename == null || !filename.contains(".")) {
                        return "";
                }
                return filename.substring(filename.lastIndexOf(".") + 1);
        }

        @PostMapping("/{ticketId}/approve")
        public ResponseApi<?> approveTicket(
                        @PathVariable Long ticketId,
                        @Valid @RequestBody ApproveTicketRequest request) {
                Long approverId = UserContext.requiredUserId();
                ApproveTicketCommand command = new ApproveTicketCommand(
                                ticketId,
                                approverId,
                                request.comment(),
                                request.idempotencyKey(),
                                request.version());
                approveTicketUsecase.approve(command);
                return ResponseApi.noContent();
        }

        @PostMapping("/{ticketId}/reject")
        public ResponseApi<?> rejectTicket(
                        @PathVariable Long ticketId,
                        @Valid @RequestBody ApproveTicketRequest request) {
                Long approverId = UserContext.requiredUserId();
                RejectTicketCommand command = new RejectTicketCommand(
                                ticketId,
                                approverId,
                                request.comment(),
                                request.idempotencyKey(),
                                request.version());
                approveTicketUsecase.reject(command);
                return ResponseApi.noContent();
        }

        @PostMapping("/bulk-approve")
        public ResponseApi<BulkApproveResponse> bulkApprove(
                        @Valid @RequestBody BulkApproveTicketRequest request) {
                Long approverId = UserContext.requiredUserId();
                BulkApproveTicketCommand command = new BulkApproveTicketCommand(
                                request.idempotencyKey(),
                                request.tickets(),
                                approverId,
                                request.comment());
                BulkApproveResponse response = approveTicketUsecase.bulkApprove(command);
                return ResponseApi.ok(response);
        }
}
