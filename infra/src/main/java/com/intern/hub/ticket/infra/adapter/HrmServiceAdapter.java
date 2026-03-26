package com.intern.hub.ticket.infra.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.core.domain.model.HrmUserSearchResponse;
import com.intern.hub.ticket.core.domain.port.HrmServicePort;
import com.intern.hub.ticket.infra.feignClient.client.HrmFeignClient;
import com.intern.hub.ticket.infra.feignClient.dto.request.HrmUserSearchResponseInfra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HrmServiceAdapter implements HrmServicePort {

    private static final int LARGE_LIST_THRESHOLD = 500;

    private final HrmFeignClient hrmFeignClient;

    @Override
    public List<Long> searchUsers(String nameOrEmail) {
        if (nameOrEmail == null || nameOrEmail.isBlank()) {
            log.debug("[HrmServiceAdapter] Empty keyword — returning empty list without calling HRM");
            return Collections.emptyList();
        }

        String trimmed = nameOrEmail.trim();
        log.debug("[HrmServiceAdapter] Searching HRM for keyword='{}'", trimmed);

        ResponseApi<List<HrmUserSearchResponseInfra>> response;
        try {
            response = hrmFeignClient.searchUsers(trimmed);
        } catch (Exception ex) {
            log.warn("[HrmServiceAdapter] HRM search threw unexpected exception: {}", ex.getMessage());
            return Collections.emptyList();
        }

        if (response == null || response.data() == null) {
            log.warn("[HrmServiceAdapter] HRM returned null response for keyword='{}' — returning empty list", trimmed);
            return Collections.emptyList();
        }

        List<Long> userIds = response.data().stream()
                .map(HrmUserSearchResponseInfra::getId)
                .filter(id -> id != null)
                .toList();

        if (userIds.size() > LARGE_LIST_THRESHOLD) {
            log.info("[HrmServiceAdapter] Large result set: {} userIds for keyword='{}'. "
                    + "Consider adding additional filters (status, role) to narrow results.",
                    userIds.size(), trimmed);
        } else {
            log.debug("[HrmServiceAdapter] Found {} userIds for keyword='{}'", userIds.size(), trimmed);
        }

        return userIds;
    }

    @Override
    public Map<Long, HrmUserSearchResponse> getUsersByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            ResponseApi<List<HrmUserSearchResponseInfra>> response = hrmFeignClient.getUsersByIds(userIds);
            if (response == null || response.data() == null) {
                log.warn("[HrmServiceAdapter] getUsersByIds returned null — returning empty map");
                return Collections.emptyMap();
            }
            return response.data().stream()
                    .filter(u -> u.getId() != null)
                    .collect(Collectors.toMap(
                            HrmUserSearchResponseInfra::getId,
                            this::toCoreDto,
                            (a, b) -> a));
        } catch (Exception ex) {
            log.error("[HrmServiceAdapter] getUsersByIds failed: {}", ex.getMessage(), ex);
            return Collections.emptyMap();
        }
    }

    private HrmUserSearchResponse toCoreDto(HrmUserSearchResponseInfra infra) {
        return HrmUserSearchResponse.builder()
                .id(infra.getId())
                .fullName(infra.getFullName())
                .email(infra.getEmail())
                .build();
    }
}
