package com.intern.hub.ticket.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketTypeUseCase;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketTypeApproverUseCase;
import com.intern.hub.ticket.core.domain.usecase.GetTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.impl.ApproveTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.CreateTicketTypeUseCaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.CreateTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.EvidenceUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.ManageTicketTypeApproverUseCaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.GetTicketUsecaseImpl;

@Configuration
public class UseCaseConfig {

    @Bean
    @Transactional
    public ApproveTicketUsecase approveTicketUsecase(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake,
            TicketTypeApproverRepository ticketTypeApproverRepository) { 
        return new ApproveTicketUsecaseImpl(
                ticketRepository, 
                ticketApprovalRepository, 
                ticketEventPublisher, 
                snowflake, 
                ticketTypeApproverRepository 
        );
    }

    @Bean
    @Transactional
    public CreateTicketTypeUseCase createTicketTypeUseCase(TicketTypeRepository ticketTypeRepository) {
        return new CreateTicketTypeUseCaseImpl(ticketTypeRepository);
    }

    @Bean
    @Transactional
    public CreateTicketUsecase createTicketUsecase(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake) {
        return new CreateTicketUsecaseImpl(ticketRepository, ticketTypeRepository, ticketEventPublisher, snowflake);
    }

    @Bean
    @Transactional
    public EvidenceUsecase evidenceUsecase(
            EvidenceRepository evidenceRepository,
            TicketRepository ticketRepository,
            Snowflake snowflake) {
        return new EvidenceUsecaseImpl(evidenceRepository, ticketRepository, snowflake);
    }

    @Bean
    @Transactional(readOnly = true)
    public GetTicketUsecase getTicketUsecase(TicketRepository ticketRepository) {
        return new GetTicketUsecaseImpl(ticketRepository);
    }

    @Bean
    @Transactional
    public ManageTicketTypeApproverUseCase manageTicketTypeApproverUseCase(
            TicketTypeApproverRepository approverRepository,
            Snowflake snowflake) {
        return new ManageTicketTypeApproverUseCaseImpl(approverRepository, snowflake);
    }
}
