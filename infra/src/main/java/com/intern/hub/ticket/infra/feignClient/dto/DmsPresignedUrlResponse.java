package com.intern.hub.ticket.infra.feignClient.dto;

public record DmsPresignedUrlResponse(String uploadUrl, String objectKey) {
}
