package com.intern.hub.ticket.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.intern.hub.ticket.core.port.in.ApproveTicketUseCase;
import com.intern.hub.ticket.core.port.in.CancelTicketUseCase;
import com.intern.hub.ticket.core.port.in.GetAllTicketsUseCase;
import com.intern.hub.ticket.core.port.in.GetApprovalHistoryUseCase;
import com.intern.hub.ticket.core.port.in.GetEvidenceUseCase;
import com.intern.hub.ticket.core.port.in.GetPendingTicketsUseCase;
import com.intern.hub.ticket.core.port.in.GetTicketDetailUseCase;
import com.intern.hub.ticket.core.port.in.GetTicketTypesUseCase;
import com.intern.hub.ticket.core.port.in.GetUserTicketsUseCase;
import com.intern.hub.ticket.core.port.in.LeaveRequestUseCase;
import com.intern.hub.ticket.core.port.in.RejectTicketUseCase;
import com.intern.hub.ticket.core.port.in.RemoteRequestUseCase;
import com.intern.hub.ticket.core.port.in.UploadEvidenceUseCase;
import com.intern.hub.ticket.core.port.out.IdGenerator;
import com.intern.hub.ticket.core.port.repository.EvidenceRepository;
import com.intern.hub.ticket.core.port.repository.LeaveRequestRepository;
import com.intern.hub.ticket.core.port.repository.RemoteRequestRepository;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;
import com.intern.hub.ticket.core.port.repository.TicketRepository;
import com.intern.hub.ticket.core.port.repository.TicketTypeRepository;
import com.intern.hub.ticket.core.usecase.ApproveTicketService;
import com.intern.hub.ticket.core.usecase.CancelTicketService;
import com.intern.hub.ticket.core.usecase.GetAllTicketsService;
import com.intern.hub.ticket.core.usecase.GetApprovalHistoryService;
import com.intern.hub.ticket.core.usecase.GetEvidenceService;
import com.intern.hub.ticket.core.usecase.GetPendingTicketsService;
import com.intern.hub.ticket.core.usecase.GetTicketDetailService;
import com.intern.hub.ticket.core.usecase.GetTicketTypesService;
import com.intern.hub.ticket.core.usecase.GetUserTicketsService;
import com.intern.hub.ticket.core.usecase.LeaveRequestService;
import com.intern.hub.ticket.core.usecase.RejectTicketService;
import com.intern.hub.ticket.core.usecase.RemoteRequestService;
import com.intern.hub.ticket.core.usecase.UploadEvidenceService;

@Configuration
public class UseCaseConfig {

    @Bean
    public GetTicketTypesUseCase getTicketTypesUseCase(TicketTypeRepository ticketTypeRepository) {
        return new GetTicketTypesService(ticketTypeRepository);
    }

    @Bean
    public GetUserTicketsUseCase getUserTicketsUseCase(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository) {
        return new GetUserTicketsService(ticketRepository, ticketTypeRepository);
    }

    @Bean
    public CancelTicketUseCase cancelTicketUseCase(TicketRepository ticketRepository) {
        return new CancelTicketService(ticketRepository);
    }

    @Bean
    public ApproveTicketUseCase approveTicketUseCase(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            IdGenerator idGenerator) {
        return new ApproveTicketService(ticketRepository, ticketApprovalRepository, idGenerator);
    }

    @Bean
    public RejectTicketUseCase rejectTicketUseCase(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            IdGenerator idGenerator) {
        return new RejectTicketService(ticketRepository, ticketApprovalRepository, idGenerator);
    }

    @Bean
    public GetApprovalHistoryUseCase getApprovalHistoryUseCase(TicketApprovalRepository ticketApprovalRepository) {
        return new GetApprovalHistoryService(ticketApprovalRepository);
    }

    @Bean
    public UploadEvidenceUseCase uploadEvidenceUseCase(
            TicketRepository ticketRepository,
            EvidenceRepository evidenceRepository,
            IdGenerator idGenerator) {
        return new UploadEvidenceService(ticketRepository, evidenceRepository, idGenerator);
    }

    @Bean
    public GetEvidenceUseCase getEvidenceUseCase(EvidenceRepository evidenceRepository) {
        return new GetEvidenceService(evidenceRepository);
    }

    @Bean
    public LeaveRequestUseCase leaveRequestUseCase(
            TicketRepository ticketRepository,
            LeaveRequestRepository leaveRequestRepository,
            TicketTypeRepository ticketTypeRepository,
            IdGenerator idGenerator) {
        return new LeaveRequestService(ticketRepository, leaveRequestRepository, ticketTypeRepository, idGenerator);
    }

    @Bean
    public RemoteRequestUseCase remoteRequestUseCase(
            TicketRepository ticketRepository,
            RemoteRequestRepository remoteRequestRepository,
            TicketTypeRepository ticketTypeRepository,
            IdGenerator idGenerator) {
        return new RemoteRequestService(ticketRepository, remoteRequestRepository, ticketTypeRepository, idGenerator);
    }

    @Bean
    public GetTicketDetailUseCase getTicketDetailUseCase(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository,
            LeaveRequestRepository leaveRequestRepository,
            RemoteRequestRepository remoteRequestRepository,
            EvidenceRepository evidenceRepository,
            TicketApprovalRepository ticketApprovalRepository) {
        return new GetTicketDetailService(ticketRepository, ticketTypeRepository, leaveRequestRepository,
                remoteRequestRepository, evidenceRepository, ticketApprovalRepository);
    }

    @Bean
    public GetPendingTicketsUseCase getPendingTicketsUseCase(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository) {
        return new GetPendingTicketsService(ticketRepository, ticketTypeRepository);
    }

    @Bean
    public GetAllTicketsUseCase getAllTicketsUseCase(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository) {
        return new GetAllTicketsService(ticketRepository, ticketTypeRepository);
    }
}
