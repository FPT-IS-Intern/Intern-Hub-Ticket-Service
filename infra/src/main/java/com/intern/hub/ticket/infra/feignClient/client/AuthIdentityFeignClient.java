package com.intern.hub.ticket.infra.feignClient.client;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.RolePermissionResponse;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.UserRoleInfraResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "auth", url = "${service.auth.url}")
public interface AuthIdentityFeignClient {
    @GetMapping("/auth/internal/authz/roles/by-user/{userId}")
    ResponseApi<UserRoleInfraResponse> getRoleByUserId(@PathVariable Long userId);


    @GetMapping("/auth/internal/authz/roles/{roleId}/permissions")
    ResponseApi<List<RolePermissionResponse>> getRolePermissions(@PathVariable Long roleId);
}
