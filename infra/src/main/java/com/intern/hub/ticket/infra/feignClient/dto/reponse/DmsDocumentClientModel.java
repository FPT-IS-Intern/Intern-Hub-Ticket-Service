package com.intern.hub.ticket.infra.feignClient.dto.reponse;

public record DmsDocumentClientModel(
        Long id,
        String objectKey,
        String originalFileName,
        String contentType,
        Long fileSize,
        Object status,
        Long actorId,
        Integer version,
        Object createdAt,
        Object updatedAt) {
}
