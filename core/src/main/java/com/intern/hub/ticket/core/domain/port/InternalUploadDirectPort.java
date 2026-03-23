package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.command.FileCommand;

public interface InternalUploadDirectPort {

    /**
     * Upload file to DMS microservice via internal API.
     *
     * @param file the file data to upload
     * @param destinationPath the storage destination path (e.g., "tickets/12345/evidences")
     * @param actorId the user ID performing the upload
     * @return objectKey of the uploaded file
     */
    String upload(FileCommand file, String destinationPath, Long actorId);
}
