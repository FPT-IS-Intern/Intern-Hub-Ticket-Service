package com.intern.hub.ticket.core.domain.usecase.impl;

import org.springframework.transaction.annotation.Transactional;

import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.model.TicketTypeApproverModel;
import com.intern.hub.ticket.core.domain.port.TicketTypeApproverRepository;
import com.intern.hub.ticket.core.domain.usecase.ManageTicketTypeApproverUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManageTicketTypeApproverUseCaseImpl implements ManageTicketTypeApproverUseCase {


    private final TicketTypeApproverRepository approverRepository;
    private final Snowflake snowflake;

    @Override
    @Transactional
    public void assignApprover(Long ticketTypeId, Long approverId, Long adminId) {
        if (approverRepository.exists(ticketTypeId, approverId)) {
            throw new ConflictDataException("conflict.data", "Người dùng này đã có quyền duyệt loại vé này rồi!");
        }

        TicketTypeApproverModel model = TicketTypeApproverModel.builder()
                .ticketTypeApproverId(snowflake.next())
                .ticketTypeId(ticketTypeId)
                .approverId(approverId)
                .build();

        approverRepository.save(model);
    }

    @Override
    public void removeApprover(Long ticketTypeId, Long approverId) {
        approverRepository.delete(ticketTypeId, approverId);
    }
}