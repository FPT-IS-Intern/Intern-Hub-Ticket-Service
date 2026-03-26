package com.intern.hub.ticket.infra.adapter;

import org.springframework.stereotype.Component;

import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import com.intern.hub.ticket.core.domain.port.DmsPort;
import com.intern.hub.ticket.infra.feignClient.client.DmsFeignClient;
import com.intern.hub.ticket.infra.feignClient.dto.request.DmsConfirmRequest;
import com.intern.hub.ticket.infra.feignClient.dto.request.DmsPresignedUrlRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DmsAdapter implements DmsPort {

    private final DmsFeignClient dmsFeignClient;

    @Override
    public PresignedUrlModel generatePresignedUrl(String fileName, String contentType, Long fileSize) {
        var request = new DmsPresignedUrlRequest(fileName, contentType, fileSize);
        var response = dmsFeignClient.getPresignedUrl(request);

        // Map từ DTO của Infra sang Model của Core
        return new PresignedUrlModel(
                response.data().uploadUrl(),
                response.data().objectKey(),
                fileSize);
    }

    @Override
    public void confirmUpload(String tempKey, String destinationPath) {
        dmsFeignClient.confirmUpload(new DmsConfirmRequest(tempKey, destinationPath));
    }
}

