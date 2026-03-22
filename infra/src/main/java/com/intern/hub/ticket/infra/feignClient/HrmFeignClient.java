package com.intern.hub.ticket.infra.feignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.infra.feignClient.dto.HrmUserResponse;

@FeignClient(name = "hrm-service", url = "${hrm.service.url:http://localhost:8082}")
public interface HrmFeignClient {

    @GetMapping(value = "/hrm/internal/users/search")
    ResponseApi<List<HrmUserResponse>> searchUsers(@RequestParam("query") String query);

    default ResponseApi<List<HrmUserResponse>> searchUsersFallback(String query, Throwable t) {
        // Log the error and return empty list or handle as per business requirements
        return ResponseApi.ok(List.of());
    }
}
