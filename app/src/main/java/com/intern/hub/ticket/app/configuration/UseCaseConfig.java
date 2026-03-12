package com.intern.hub.ticket.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;
import com.intern.hub.ticket.core.domain.usecase.GetTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.impl.ApproveTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.CreateTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.EvidenceUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.GetTicketUsecaseImpl;

@Configuration
public class UseCaseConfig {

    @Bean
    public ApproveTicketUsecase approveTicketUsecase(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake) {
        return new ApproveTicketUsecaseImpl(ticketRepository, ticketApprovalRepository, ticketEventPublisher,
                snowflake);
    }

    @Bean
    public CreateTicketUsecase createTicketUsecase(
            TicketRepository ticketRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake) {
        return new CreateTicketUsecaseImpl(
                ticketRepository,
                ticketEventPublisher,
                snowflake);
    }

    @Bean
    public GetTicketUsecase getTicketUsecase(TicketRepository ticketRepository) {
        return new GetTicketUsecaseImpl(ticketRepository);
    }

    @Bean
    public EvidenceUsecase evidenceUsecase(
            EvidenceRepository evidenceRepository,
            TicketRepository ticketRepository,
            Snowflake snowflake) {
        return new EvidenceUsecaseImpl(evidenceRepository, ticketRepository, snowflake);
    }
}
