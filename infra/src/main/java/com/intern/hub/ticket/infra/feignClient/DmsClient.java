package com.intern.hub.ticket.infra.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.infra.feignClient.dto.ConfirmUploadRequest;
import com.intern.hub.ticket.infra.feignClient.dto.PresignedUrlRequest;
import com.intern.hub.ticket.infra.feignClient.dto.PresignedUrlResponse;

@FeignClient(name = "dms-service", url = "${dms.service.url}")
public interface DmsClient {

    // 1. Xin URL Upload tạm thời
    @PostMapping(value = "/dms/internal/presigned/url", headers = "X-Internal-Secret=${security.internal-secret}")
    ResponseApi<PresignedUrlResponse> getPresignedUrl(@RequestBody PresignedUrlRequest request);

    // 2. Xác nhận đã upload xong
    @PostMapping(value = "/dms/internal/presigned/confirm", headers = "X-Internal-Secret=${security.internal-secret}")
    ResponseApi<Void> confirmUpload(@RequestBody ConfirmUploadRequest request);
}
