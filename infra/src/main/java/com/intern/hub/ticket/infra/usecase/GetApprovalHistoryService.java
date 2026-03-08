package com.intern.hub.ticket.infra.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.ticket.core.domain.command.TicketApprovalDto;
import com.intern.hub.ticket.core.port.in.GetApprovalHistoryUseCase;
import com.intern.hub.ticket.core.port.repository.TicketApprovalRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetApprovalHistoryService implements GetApprovalHistoryUseCase {

    private final TicketApprovalRepository ticketApprovalRepository;

    @Override
    public List<TicketApprovalDto> getApprovalHistory(Long ticketId) {
        return ticketApprovalRepository.findByTicketId(ticketId).stream()
                .map(approval -> TicketApprovalDto.builder()
                        .approvalId(approval.getApprovalId())
                        .ticketId(approval.getTicketId())
                        .approverId(approval.getApproverId())
                        .action(approval.getAction())
                        .comment(approval.getComment())
                        .actionAt(approval.getActionAt())
                        .status(approval.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
