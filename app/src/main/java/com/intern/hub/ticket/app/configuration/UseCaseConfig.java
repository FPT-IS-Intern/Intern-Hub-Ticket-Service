package com.intern.hub.ticket.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.RuleEvaluatorPort;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.port.TicketTaskPermissionPort;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.CreateTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;
import com.intern.hub.ticket.core.domain.usecase.GetTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketTypeApproverUseCase;
import com.intern.hub.ticket.core.domain.usecase.TicketTypeUseCase;
import com.intern.hub.ticket.core.domain.usecase.impl.ApproveTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.CreateTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.EvidenceUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.GetTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.ManageTicketTypeApproverUseCaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.TicketTypeUseCaseImpl;

@Configuration
public class UseCaseConfig {

    @Bean
    public ApproveTicketUsecase approveTicketUsecase(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake,
            TicketTaskPermissionPort permissionPort) {
        return new ApproveTicketUsecaseImpl(
                ticketRepository,
                ticketApprovalRepository,
                ticketEventPublisher,
                snowflake,
                permissionPort);
    }

    @Bean
    public TicketTypeUseCase ticketTypeUseCase(TicketTypeRepository ticketTypeRepository) {
        return new TicketTypeUseCaseImpl(ticketTypeRepository);
    }

    @Bean
    public CreateTicketUsecase createTicketUsecase(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake,
            RuleEvaluatorPort ruleEvaluatorPort) {
        return new CreateTicketUsecaseImpl(ticketRepository, ticketTypeRepository, ticketEventPublisher, snowflake,
                ruleEvaluatorPort);
    }

    @Bean
    public EvidenceUsecase evidenceUsecase(
            EvidenceRepository evidenceRepository,
            TicketRepository ticketRepository,
            Snowflake snowflake) {
        return new EvidenceUsecaseImpl(evidenceRepository, ticketRepository, snowflake);
    }

    @Bean
    public GetTicketUsecase getTicketUsecase(TicketRepository ticketRepository) {
        return new GetTicketUsecaseImpl(ticketRepository);
    }

    @Bean
    public ManageTicketTypeApproverUseCase manageTicketTypeApproverUseCase(
            TicketTypeApproverRepository approverRepository,
            Snowflake snowflake) {
        return new ManageTicketTypeApproverUseCaseImpl(approverRepository, snowflake);
    }
}
