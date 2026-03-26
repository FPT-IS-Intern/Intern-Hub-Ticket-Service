package com.intern.hub.ticket.core.domain.port;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InternalUploadDirectPort {

    String uploadFile(
            MultipartFile file, String keyPrefix, Long actorId, Long maxSizeBytes, String contentTypeRegex);

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

    /**
     * Delete a file from DMS storage.
     *
     * @param key     the object key of the file to delete
     * @param actorId the user ID performing the deletion
     */
    void deleteFile(String key, Long actorId);
}
