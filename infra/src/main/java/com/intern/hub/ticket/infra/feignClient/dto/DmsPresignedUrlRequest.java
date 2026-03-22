package com.intern.hub.ticket.infra.feignClient.dto;

public record DmsPresignedUrlRequest(String fileName, String contentType, Long fileSize) {
}
