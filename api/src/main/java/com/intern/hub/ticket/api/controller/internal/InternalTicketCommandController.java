package com.intern.hub.ticket.api.controller.internal;

import java.util.Arrays;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.starter.security.annotation.Internal;
import com.intern.hub.ticket.api.dto.request.CreateTicketRequest;
import com.intern.hub.ticket.api.dto.response.TicketDetailDto;
import com.intern.hub.ticket.api.dto.response.TicketResponse;
import com.intern.hub.ticket.api.mapper.TicketApiMapper;
import com.intern.hub.ticket.core.domain.model.TicketModel;
import com.intern.hub.ticket.core.domain.model.command.CreateTicketCommand;
import com.intern.hub.ticket.core.domain.model.command.FileCommand;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${security.internal-path-prefix:/ticket/internal}")
@RequiredArgsConstructor
public class InternalTicketCommandController {

        private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "pdf", "docx");
        private static final long MAX_FILE_SIZE = 2 * 1024 * 1024L; // 2MB
        private static final Long SYSTEM_USER_ID = 0L;

        private final TicketUsecase ticketUsecase;
        private final TicketApiMapper ticketApiMapper;

        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @Internal
        public ResponseApi<TicketResponse> createTicketInternal(
                        @RequestPart("request") @Valid CreateTicketRequest request,
                        @RequestPart(value = "evidences", required = false) MultipartFile[] evidences) {

                FileCommand[] fileCommands = toFileCommands(evidences);

                CreateTicketCommand command = new CreateTicketCommand(
                                SYSTEM_USER_ID,
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

        @GetMapping("/{ticketId}")
        @Internal
        public ResponseApi<TicketDetailDto> getTicketDetailInternal(@PathVariable Long ticketId) {
                TicketModel model = ticketUsecase.getTicketDetail(ticketId);
                return ResponseApi.ok(ticketApiMapper.toDetailDto(model));
        }
}
