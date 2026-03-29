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
import com.intern.hub.ticket.infra.feignClient.dto.reponse.HrmUserByIdResponse;
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

        Map<Long, HrmUserSearchResponse> result = userIds.stream()
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        this::fetchUserById,
                        (a, b) -> a
                ));

        long nullCount = result.values().stream().filter(v -> v == null).count();
        if (nullCount > 0) {
            log.warn("[HrmServiceAdapter] getUsersByIds: {} out of {} userIds returned null from HRM",
                    nullCount, userIds.size());
        }

        return result;
    }

    private HrmUserSearchResponse fetchUserById(Long userId) {
        try {
            ResponseApi<HrmUserByIdResponse> response = hrmFeignClient.getUserByIdInternal(userId);
            if (response == null || response.data() == null) {
                log.warn("[HrmServiceAdapter] getUserByIdInternal returned null for userId={}", userId);
                return null;
            }
            HrmUserByIdResponse data = response.data();
            log.info("[HrmServiceAdapter] HRM /{}/ returned: fullName='{}', email='{}'",
                    userId, data.getFullName(), data.getEmail());
            return HrmUserSearchResponse.builder()
                    .id(data.getUserId())
                    .fullName(data.getFullName())
                    .email(data.getEmail())
                    .build();
        } catch (Exception ex) {
            log.error("[HrmServiceAdapter] getUserByIdInternal failed for userId={}: {}", userId, ex.getMessage());
            return null;
        }
    }

    @Override
    public HrmUserSearchResponse getUserById(Long userId) {
        if (userId == null) {
            log.debug("[HrmServiceAdapter] userId is null — returning null");
            return null;
        }

        try {
            ResponseApi<HrmUserByIdResponse> response = hrmFeignClient.getUserByIdInternal(userId);
            log.info("[HrmServiceAdapter] getUserById userId={}, response={}", userId, response);
            if (response == null || response.data() == null) {
                log.warn("[HrmServiceAdapter] getUserById returned null for userId={}", userId);
                return null;
            }
            HrmUserByIdResponse data = response.data();
            log.info("[HrmServiceAdapter] HRM /{}/ returned: fullName='{}', email='{}'",
                    userId, data.getFullName(), data.getEmail());
            return HrmUserSearchResponse.builder()
                    .id(data.getUserId())
                    .fullName(data.getFullName())
                    .email(data.getEmail())
                    .build();
        } catch (Exception ex) {
            log.warn("[HrmServiceAdapter] getUserById failed for userId={}: {}", userId, ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Long> getAllUserId() {
        return hrmFeignClient.getUserIdList().data();
    }

    @Override
    public void callHrmProfileApproved(Long ticketId, Map<String, Object> payload) {
        log.info("[HrmServiceAdapter] Calling HRM profile approved callback. ticketId={}", ticketId);
        try {
            HrmFeignClient.ApproveProfileTicketRequest request =
                    new HrmFeignClient.ApproveProfileTicketRequest(ticketId, payload);
            hrmFeignClient.onProfileTicketApproved(request);
            log.info("[HrmServiceAdapter] HRM profile approved callback succeeded. ticketId={}", ticketId);
        } catch (Exception ex) {
            log.error("[HrmServiceAdapter] HRM profile approved callback threw exception. ticketId={}: {}",
                    ticketId, ex.getMessage(), ex);
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

