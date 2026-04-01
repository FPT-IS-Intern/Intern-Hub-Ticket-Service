package com.intern.hub.ticket.infra.feignClient.client;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.BoPortalBranchResponse;

@FeignClient(name = "bo-portal-service", url = "${service.boPortal.url:http://localhost:8080}")
public interface BoPortalFeignClient {

    /**
     * Lấy danh sách tất cả branches (công ty/chi nhánh) từ BoPortal Service.
     * BoPortal endpoint: GET /bo-portal/internal/branches
     */
    @GetMapping(value = "/bo-portal/internal/branches")
    ResponseApi<List<BoPortalBranchResponse>> getAllBranches();

    default ResponseApi<List<BoPortalBranchResponse>> getAllBranchesFallback(Throwable t) {
        return ResponseApi.ok(Collections.emptyList());
    }
}
