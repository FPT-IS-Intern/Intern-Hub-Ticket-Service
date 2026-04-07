package com.intern.hub.ticket.infra.feignClient.client;

import com.intern.hub.library.common.dto.ResponseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "news-service", url = "${service.news.url:http://localhost:8080}")
public interface NewsFeignClient {

    @PostMapping("/news/internal/tickets/{ticketId}/approved")
    ResponseApi<String> notifyNewsApproved(
            @PathVariable("ticketId") Long ticketId,
            @RequestHeader("X-Internal-Secret") String internalSecret);
}
