package com.intern.hub.ticket.infra.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.infra.feignClient.dto.DmsConfirmRequest;
import com.intern.hub.ticket.infra.feignClient.dto.DmsPresignedUrlRequest;
import com.intern.hub.ticket.infra.feignClient.dto.DmsPresignedUrlResponse;

@FeignClient(name = "dms-service", url = "${dms.service.url}")
public interface DmsFeignClient {

    // Gọi API của DMS, kẹp thêm Secret Key
    @PostMapping(value = "/dms/internal/presigned/url", headers = "X-Internal-Secret=${security.internal-secret}")
    ResponseApi<DmsPresignedUrlResponse> getPresignedUrl(@RequestBody DmsPresignedUrlRequest request);

    @PostMapping(value = "/dms/internal/presigned/confirm", headers = "X-Internal-Secret=${security.internal-secret}")
    ResponseApi<Void> confirmUpload(@RequestBody DmsConfirmRequest request);
}
