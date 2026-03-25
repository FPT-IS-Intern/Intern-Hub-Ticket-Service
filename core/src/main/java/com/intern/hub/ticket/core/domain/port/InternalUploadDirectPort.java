package com.intern.hub.ticket.core.domain.port;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InternalUploadDirectPort {

    /**
     * Upload a single file to DMS microservice via internal API.
     * Mirrors HRM's FileStoragePort pattern.
     *
     * @param file            the multipart file to upload
     * @param destinationPath the storage destination path (e.g., "tickets/123/evidences")
     * @param actorId         the user ID performing the upload
     * @param maxSizeBytes    maximum allowed file size in bytes
     * @param contentTypeRegex regex to validate file content type
     * @return the objectKey returned by DMS
     */
    String uploadFile(MultipartFile file, String destinationPath, Long actorId,
                      Long maxSizeBytes, String contentTypeRegex);

    /**
     * Upload multiple files to DMS microservice via internal API.
     * Each file is uploaded individually and validated against size/type constraints.
     *
     * @param files           the list of multipart files to upload
     * @param baseDestinationPath base storage destination path
     * @param maxSizeBytes    maximum allowed file size in bytes per file
     * @param contentTypeRegex regex to validate file content type
     * @return list of objectKeys returned by DMS for each uploaded file
     */
    List<String> uploadFiles(MultipartFile[] files, String baseDestinationPath,
                             Long maxSizeBytes, String contentTypeRegex);
}
