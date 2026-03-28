package com.intern.hub.ticket.infra.feignClient.client;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.HrmUserByIdResponse;
import com.intern.hub.ticket.infra.feignClient.dto.request.HrmUserSearchResponseInfra;

@FeignClient(name = "hrm-service", url = "http://localhost:8081")
//@FeignClient(name = "hrm-service", url = "${service.hrm.url:http://localhost:8082}")
public interface HrmFeignClient {

    @GetMapping(value = "/hrm/internal/users/search")
    ResponseApi<List<HrmUserSearchResponseInfra>> searchUsers(@RequestParam("query") String query);

    default ResponseApi<List<HrmUserSearchResponseInfra>> searchUsersFallback(String query, Throwable t) {
        return ResponseApi.ok(Collections.emptyList());
    }

    @PostMapping(value = "/hrm/internal/users/internal/filter-for-ticket")
    ResponseApi<PaginatedData<HrmUserSearchResponseInfra>> filterUsersPaginated(
            @RequestBody FilterUsersRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    default ResponseApi<PaginatedData<HrmUserSearchResponseInfra>> filterUsersPaginatedFallback(
            FilterUsersRequest request, int page, int size, Throwable t) {
        return ResponseApi.ok(PaginatedData.<HrmUserSearchResponseInfra>builder()
                .items(Collections.emptyList())
                .totalItems(0L)
                .totalPages(0)
                .build());
    }

    /**
     * Batch-fetch user info theo danh sách userIds.
     * HRM endpoint: POST /hrm/internal/users/by-ids
     */
    @PostMapping(value = "/hrm/internal/users/by-ids")
    ResponseApi<List<HrmUserSearchResponseInfra>> getUsersByIds(@RequestBody List<Long> userIds);

    default ResponseApi<List<HrmUserSearchResponseInfra>> getUsersByIdsFallback(List<Long> userIds, Throwable t) {
        return ResponseApi.ok(Collections.emptyList());
    }

    /**
     * Lấy thông tin user theo userId.
     * HRM endpoint: GET /hrm/internal/users/{userId}
     */
    @GetMapping(value = "/hrm/internal/users/{userId}")
    ResponseApi<HrmUserByIdResponse> getUserByIdInternal(@PathVariable("userId") Long userId);

    record FilterUsersRequest(
            String keyword,
            List<String> sysStatuses,
            List<String> roles,
            List<String> positions
    ) {}
}

