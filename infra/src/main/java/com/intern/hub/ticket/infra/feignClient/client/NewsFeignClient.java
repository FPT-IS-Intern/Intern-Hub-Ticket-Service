package com.intern.hub.ticket.infra.feignClient.client;

import com.intern.hub.library.common.dto.ResponseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "news-service", url = "${service.news.url:${services.gateway.url:http://localhost:8765}/api}")
public interface NewsFeignClient {

    @PostMapping("/news/internal/tickets/{ticketId}/approved")
    ResponseApi<String> notifyNewsApproved(
            @PathVariable("ticketId") Long ticketId,
            @RequestHeader("X-Internal-Secret") String internalSecret);

    @PostMapping("/news/internal/tickets/{ticketId}/rejected")
    ResponseApi<String> notifyNewsRejected(
            @PathVariable("ticketId") Long ticketId,
            @RequestHeader("X-Internal-Secret") String internalSecret);
}
