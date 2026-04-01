package com.intern.hub.ticket.infra.adapter;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.core.domain.model.BranchModel;
import com.intern.hub.ticket.core.domain.port.BoPortalServicePort;
import com.intern.hub.ticket.infra.feignClient.client.BoPortalFeignClient;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.BoPortalBranchResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoPortalServiceAdapter implements BoPortalServicePort {

    private final BoPortalFeignClient boPortalFeignClient;

    @Override
    public List<BranchModel> getAllBranches() {
        log.debug("[BoPortalServiceAdapter] Fetching all branches from BoPortal Service");

        ResponseApi<List<BoPortalBranchResponse>> response;
        try {
            response = boPortalFeignClient.getAllBranches();
        } catch (Exception ex) {
            log.warn("[BoPortalServiceAdapter] BoPortal getAllBranches threw exception: {}", ex.getMessage());
            return Collections.emptyList();
        }

        if (response == null || response.data() == null) {
            log.warn("[BoPortalServiceAdapter] BoPortal returned null response — returning empty list");
            return Collections.emptyList();
        }

        List<BranchModel> branches = response.data().stream()
                .map(this::toBranchModel)
                .toList();

        log.info("[BoPortalServiceAdapter] Fetched {} branches from BoPortal Service", branches.size());
        return branches;
    }

    private BranchModel toBranchModel(BoPortalBranchResponse response) {
        return BranchModel.builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .isActive(response.getIsActive())
                .build();
    }
}
