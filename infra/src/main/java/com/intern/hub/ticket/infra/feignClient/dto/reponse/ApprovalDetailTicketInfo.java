package com.intern.hub.ticket.infra.feignClient.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ApprovalDetailTicketInfo {

    @JsonSerialize(using = ToStringSerializer.class)
    Long ticketId;
    // creator
    @JsonSerialize(using = ToStringSerializer.class)
    Long creatorId;
    Long createdAt;

    // approver level 1
    @JsonSerialize(using = ToStringSerializer.class)
    Long approverId1;
    Long approvedAt1;
    String status1;

    //approver level 2
    @JsonSerialize(using = ToStringSerializer.class)
    Long approverId2;
    Long approvedAt2;
    String status2;
}
