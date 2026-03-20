package com.intern.hub.ticket.core.domain.port;

public interface FileStorageService {
    String uploadFile(byte[] fileBytes, String fileName, String contentType);

    void deleteFile(String fileKey);

    byte[] downloadFile(String fileKey);
}
