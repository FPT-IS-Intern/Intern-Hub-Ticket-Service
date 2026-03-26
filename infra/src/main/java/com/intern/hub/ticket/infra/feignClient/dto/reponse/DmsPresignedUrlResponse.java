package com.intern.hub.ticket.infra.feignClient.dto.reponse;

public record DmsPresignedUrlResponse(String uploadUrl, String objectKey) {
}
