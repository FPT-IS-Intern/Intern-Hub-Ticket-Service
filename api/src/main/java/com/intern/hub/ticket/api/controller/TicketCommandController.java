package com.intern.hub.ticket.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.starter.security.annotation.Authenticated;
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
import com.intern.hub.ticket.core.domain.model.command.EvidenceCommand;
import com.intern.hub.ticket.core.domain.model.command.RejectTicketCommand;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TicketCommandController {

        private final ApproveTicketUsecase approveTicketUsecase;
        private final TicketUsecase ticketUsecase;

        @PostMapping
//        @Authenticated
        public ResponseApi<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
                
                //Long userId = 123L;
                Long userId = UserContext.requiredUserId(); 

                // 2. Map từ Request sang Command (Dùng Stream để map mảng evidences)
                List<EvidenceCommand> evidenceCommands = request.evidences() == null ? List.of()
                                : request.evidences().stream()
                                                .map(e -> {
                                                        if (e == null) {
                                                                throw new BadRequestException("bad.request",
                                                                                "Evidence item must not be null");
                                                        }
                                                        return new EvidenceCommand(e.tempKey(), e.destinationPath(), e.fileType(),
                                                                        e.fileSize());
                                                })
                                                .toList();

                CreateTicketCommand command = new CreateTicketCommand(
                                userId,
                                request.ticketTypeId(),
                                request.payload(),
                                evidenceCommands);

                // 3. Thực thi nghiệp vụ
                TicketModel createdTicket = ticketUsecase.create(command);

                // 4. Trả về kết quả
                return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
        }

        @PostMapping("/{ticketId}/approve")
//        @Authenticated
        // @HasPermission(action = Action.REVIEW, resource = "ticket")
        public ResponseApi<?> approveTicket(
                        @PathVariable Long ticketId,
                        @Valid @RequestBody ApproveTicketRequest request) {

                Long approverId = UserContext.requiredUserId();
                //Long approverId = 123L;

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
//        @Authenticated
        // @HasPermission(action = Action.REVIEW, resource = "ticket")
        public ResponseApi<?> rejectTicket(
                        @PathVariable Long ticketId,
                        @Valid @RequestBody ApproveTicketRequest request) {

                Long approverId = UserContext.requiredUserId();
                //Long approverId = 123L;

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
//        @Authenticated
        // @HasPermission(action = Action.REVIEW, resource = "ticket")
        public ResponseApi<BulkApproveResponse> bulkApprove(
                        @Valid @RequestBody BulkApproveTicketRequest request) {

                Long approverId = UserContext.requiredUserId();
                //Long approverId = 123L;

                BulkApproveTicketCommand command = new BulkApproveTicketCommand(
                                request.idempotencyKey(),
                                request.tickets(),
                                approverId,
                                request.comment());
                BulkApproveResponse response = approveTicketUsecase.bulkApprove(command);

                return ResponseApi.ok(response);
        }

}
