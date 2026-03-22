package com.intern.hub.ticket.infra.feignClient.dto;

public record PresignedUrlResponse(String presignedUrl, String objectKey) {
}
