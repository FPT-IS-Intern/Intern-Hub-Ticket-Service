package com.intern.hub.ticket.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class TicketStatisticApiResponse {
    int workOnSite;
    int workOffSite;
    int workFromHome;
}
