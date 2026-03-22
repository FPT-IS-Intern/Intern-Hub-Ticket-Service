package com.intern.hub.ticket.infra.adapter;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.core.domain.port.HrmServicePort;
import com.intern.hub.ticket.infra.feignClient.HrmFeignClient;
import com.intern.hub.ticket.infra.feignClient.dto.HrmUserResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HrmServiceAdapter implements HrmServicePort {

    private final HrmFeignClient hrmFeignClient;

    @Override
    public List<Long> searchUsers(String nameOrEmail) {
        if (nameOrEmail == null || nameOrEmail.isBlank()) {
            return Collections.emptyList();
        }
        ResponseApi<List<HrmUserResponse>> response = hrmFeignClient.searchUsers(nameOrEmail);
        if (response == null || response.data() == null) {
            return Collections.emptyList();
        }
        return response.data().stream()
                .map(HrmUserResponse::getId)
                .toList();
    }
}
