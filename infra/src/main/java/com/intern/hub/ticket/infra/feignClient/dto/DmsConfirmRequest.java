package com.intern.hub.ticket.infra.feignClient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DmsConfirmRequest(
    @JsonProperty("tempKey") 
    String tempKey,

    @JsonProperty("destinationPath") 
    String destinationPath
) {}
