package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.command.FileCommand;

public interface InternalUploadDirectPort {

    String uploadFile(
            FileCommand fileCommand, String keyPrefix, Long actorId, Long maxSizeBytes, String contentTypeRegex);
}
