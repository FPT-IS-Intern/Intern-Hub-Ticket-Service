package com.intern.hub.ticket.infra.feignClient.dto;

public record PresignedUrlRequest(String fileName, String contentType, Long fileSize) {
}
