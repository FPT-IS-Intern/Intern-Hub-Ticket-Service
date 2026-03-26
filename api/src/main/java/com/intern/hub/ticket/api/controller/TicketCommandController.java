package com.intern.hub.ticket.api.controller;

import java.util.List;
import java.util.Set;

import com.intern.hub.ticket.api.dto.response.StatCardApiResponse;
import com.intern.hub.ticket.api.mapper.TicketApiMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
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
import com.intern.hub.ticket.core.domain.model.command.RejectTicketCommand;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(origins = "http://localhost:4221")
public class TicketCommandController {

        private final ApproveTicketUsecase approveTicketUsecase;
        private final TicketUsecase ticketUsecase;
        private final TicketApiMapper ticketApiMapper;

        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseApi<TicketResponse> createTicket(
                        @RequestPart("request") @Valid CreateTicketRequest request,
                        @RequestPart(value = "evidences", required = false) MultipartFile[] evidences) {

                log.info("request: {}", request);
                log.info("files: {}", evidences);
                Long userId = UserContext.requiredUserId();

                CreateTicketCommand command = new CreateTicketCommand(
                                userId,
                                request.ticketTypeId(),
                                request.payload(),
                                evidences);

                TicketModel createdTicket = ticketUsecase.create(command);
                return ResponseApi.ok(new TicketResponse(createdTicket.getTicketId(), createdTicket.getStatus()));
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

        @GetMapping("/stat-card-data")
        public ResponseApi<StatCardApiResponse> getStatCardDate() {
                return ResponseApi.ok(ticketApiMapper.toStatCardApiResponse(ticketUsecase.getStatCardData()));
        }
}
