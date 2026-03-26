package com.intern.hub.ticket.infra.feignClient.dto.request;

public record DmsPresignedUrlRequest(String fileName, String contentType, Long fileSize) {
}
