package com.intern.hub.ticket.core.domain.port;

import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;

public interface DmsPort {
    // Xin link upload từ DMS
    PresignedUrlModel generatePresignedUrl(String fileName, String contentType, Long fileSize);

    // Xác nhận đã upload thành công
    void confirmUpload(String tempKey, String destinationPath);
}
