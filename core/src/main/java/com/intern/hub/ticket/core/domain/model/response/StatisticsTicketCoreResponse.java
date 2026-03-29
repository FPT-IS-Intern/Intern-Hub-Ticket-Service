package com.intern.hub.ticket.core.domain.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class StatisticsTicketCoreResponse {
    int workOnSite;
    int workOffSite;
    int workFromHome;
}
