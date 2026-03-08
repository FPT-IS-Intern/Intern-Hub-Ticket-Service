package com.intern.hub.ticket.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public ApproveTicketService approveTicketService(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            IdGenerator idGenerator) {
        return new ApproveTicketService(ticketRepository, ticketApprovalRepository, idGenerator);
    }

    @Bean
    public CancelTicketService cancelTicketService(TicketRepository ticketRepository) {
        return new CancelTicketService(ticketRepository);
    }

    @Bean
    public GetAllTicketsService getAllTicketsService(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository) {
        return new GetAllTicketsService(ticketRepository, ticketTypeRepository);
    }

    @Bean
    public GetApprovalHistoryService getApprovalHistoryService(TicketApprovalRepository ticketApprovalRepository) {
        return new GetApprovalHistoryService(ticketApprovalRepository);
    }

    @Bean
    public GetEvidenceService getEvidenceService(EvidenceRepository evidenceRepository) {
        return new GetEvidenceService(evidenceRepository);
    }

    @Bean
    public GetPendingTicketsService getPendingTicketsService(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository) {
        return new GetPendingTicketsService(ticketRepository, ticketTypeRepository);
    }

    @Bean
    public GetTicketDetailService getTicketDetailService(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository,
            LeaveRequestRepository leaveRequestRepository,
            RemoteRequestRepository remoteRequestRepository,
            EvidenceRepository evidenceRepository,
            TicketApprovalRepository ticketApprovalRepository) {
        return new GetTicketDetailService(
                ticketRepository,
                ticketTypeRepository,
                leaveRequestRepository,
                remoteRequestRepository,
                evidenceRepository,
                ticketApprovalRepository);
    }

    @Bean
    public GetTicketTypesService getTicketTypesService(TicketTypeRepository ticketTypeRepository) {
        return new GetTicketTypesService(ticketTypeRepository);
    }

    @Bean
    public GetUserTicketsService getUserTicketsService(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository) {
        return new GetUserTicketsService(ticketRepository, ticketTypeRepository);
    }

    @Bean
    public LeaveRequestService leaveRequestService(
            TicketRepository ticketRepository,
            LeaveRequestRepository leaveRequestRepository,
            TicketTypeRepository ticketTypeRepository,
            IdGenerator idGenerator) {
        return new LeaveRequestService(ticketRepository, leaveRequestRepository, ticketTypeRepository, idGenerator);
    }

    @Bean
    public RejectTicketService rejectTicketService(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            IdGenerator idGenerator) {
        return new RejectTicketService(ticketRepository, ticketApprovalRepository, idGenerator);
    }

    @Bean
    public RemoteRequestService remoteRequestService(
            TicketRepository ticketRepository,
            RemoteRequestRepository remoteRequestRepository,
            TicketTypeRepository ticketTypeRepository,
            IdGenerator idGenerator) {
        return new RemoteRequestService(ticketRepository, remoteRequestRepository, ticketTypeRepository, idGenerator);
    }

    @Bean
    public UploadEvidenceService uploadEvidenceService(
            TicketRepository ticketRepository,
            EvidenceRepository evidenceRepository,
            IdGenerator idGenerator) {
        return new UploadEvidenceService(ticketRepository, evidenceRepository, idGenerator);
    }
}
