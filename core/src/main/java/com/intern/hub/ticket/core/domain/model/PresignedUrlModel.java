package com.intern.hub.ticket.core.domain.model;

public record PresignedUrlModel(String uploadUrl, String tempKey, Long fileSize) {
}
