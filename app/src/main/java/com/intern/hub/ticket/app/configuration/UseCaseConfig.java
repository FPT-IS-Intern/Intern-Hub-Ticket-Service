package com.intern.hub.ticket.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.port.DmsPort;
import com.intern.hub.ticket.core.domain.port.EvidenceRepository;
import com.intern.hub.ticket.core.domain.port.HrmServicePort;
import com.intern.hub.ticket.core.domain.port.InternalUploadDirectPort;
import com.intern.hub.ticket.core.domain.port.RuleEvaluatorPort;
import com.intern.hub.ticket.core.domain.port.StorageLifecyclePort;
import com.intern.hub.ticket.core.domain.port.TicketApprovalRepository;
import com.intern.hub.ticket.core.domain.port.TicketEventPublisher;
import com.intern.hub.ticket.core.domain.port.TicketGlobalApproverRepository;
import com.intern.hub.ticket.core.domain.port.TicketRepository;
import com.intern.hub.ticket.core.domain.port.TicketTaskPermissionPort;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;
import com.intern.hub.ticket.core.domain.port.TicketTypeRepository;
import com.intern.hub.ticket.core.domain.service.DocumentService;
import com.intern.hub.ticket.core.domain.service.TicketTemplateValidator;
import com.intern.hub.ticket.core.domain.usecase.ApproveTicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.EvidenceUsecase;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketGlobalApproverUseCase;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketTypeApproverUseCase;
import com.intern.hub.ticket.core.domain.usecase.TicketTypeUseCase;
import com.intern.hub.ticket.core.domain.usecase.TicketUsecase;
import com.intern.hub.ticket.core.domain.usecase.impl.ApproveTicketUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.EvidenceUsecaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.ManageTicketGlobalApproverUseCaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.ManageTicketTypeApproverUseCaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.TicketTypeUseCaseImpl;
import com.intern.hub.ticket.core.domain.usecase.impl.TicketUseCaseImpl;
import com.intern.hub.ticket.infra.service.DocumentServiceImpl;
import com.intern.hub.ticket.infra.service.StorageObjectLifecycleManager;

@Configuration
public class UseCaseConfig {

    @Bean
    public ApproveTicketUsecase approveTicketUsecase(
            TicketRepository ticketRepository,
            TicketApprovalRepository ticketApprovalRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake,
            TicketTaskPermissionPort permissionPort,
            HrmServicePort hrmServicePort) {
        return new ApproveTicketUsecaseImpl(
                ticketRepository,
                ticketApprovalRepository,
                ticketEventPublisher,
                snowflake,
                permissionPort,
                hrmServicePort);
    }

    @Bean
    public TicketTypeUseCase ticketTypeUseCase(TicketTypeRepository ticketTypeRepository) {
        return new TicketTypeUseCaseImpl(ticketTypeRepository);
    }

    @Bean
    public TicketUsecase ticketUsecase(
            TicketRepository ticketRepository,
            TicketTypeRepository ticketTypeRepository,
            TicketEventPublisher ticketEventPublisher,
            Snowflake snowflake,
            RuleEvaluatorPort ruleEvaluatorPort,
            TicketTemplateValidator ticketTemplateValidator,
            TicketApprovalRepository ticketApprovalRepository,
            EvidenceUsecase evidenceUsecase,
            HrmServicePort hrmServicePort,
            InternalUploadDirectPort internalUploadDirectPort,
            EvidenceRepository evidenceRepository) {
        return new TicketUseCaseImpl(
                ticketRepository,
                ticketTypeRepository,
                ticketEventPublisher,
                snowflake,
                ruleEvaluatorPort,
                ticketTemplateValidator,
                ticketApprovalRepository,
                evidenceUsecase,
                hrmServicePort,
                internalUploadDirectPort,
                evidenceRepository);
    }

    @Bean
    public EvidenceUsecase evidenceUsecase(
            EvidenceRepository evidenceRepository,
            DmsPort dmsPort,
            InternalUploadDirectPort internalUploadDirectPort,
            Snowflake snowflake,
            StorageLifecyclePort storageLifecyclePort) {
        return new EvidenceUsecaseImpl(
                evidenceRepository, dmsPort, internalUploadDirectPort, snowflake, storageLifecyclePort);
    }

    @Bean
    public DocumentService documentService(
            EvidenceRepository evidenceRepository,
            InternalUploadDirectPort internalUploadDirectPort,
            StorageLifecyclePort storageLifecyclePort,
            Snowflake snowflake) {
        return new DocumentServiceImpl(evidenceRepository, internalUploadDirectPort, storageLifecyclePort, snowflake);
    }

    @Bean
    public ManageTicketTypeApproverUseCase manageTicketTypeApproverUseCase(
            TicketTypeApproverRepository approverRepository,
            Snowflake snowflake) {
        return new ManageTicketTypeApproverUseCaseImpl(approverRepository, snowflake);
    }

    @Bean
    public ManageTicketGlobalApproverUseCase manageTicketGlobalApproverUseCase(
            TicketGlobalApproverRepository approverRepository) {
        return new ManageTicketGlobalApproverUseCaseImpl(approverRepository);
    }

    @Bean
    public TicketTemplateValidator ticketTemplateValidator() {
        return new TicketTemplateValidator();
    }
}
