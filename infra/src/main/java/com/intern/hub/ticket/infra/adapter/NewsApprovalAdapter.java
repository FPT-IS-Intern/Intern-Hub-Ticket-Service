package com.intern.hub.ticket.infra.adapter;

import com.intern.hub.ticket.core.domain.port.NewsApprovalPort;
import com.intern.hub.ticket.infra.feignClient.client.NewsFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsApprovalAdapter implements NewsApprovalPort {

    private final NewsFeignClient newsFeignClient;

    @Value("${security.internal-secret}")
    private String internalSecret;

    @Override
    public void notifyNewsApproved(Long ticketId) {
        if (ticketId == null) {
            return;
        }
        try {
            newsFeignClient.notifyNewsApproved(ticketId, internalSecret);
            log.info("[NewsApprovalAdapter] Notified news-service for approved ticketId={}", ticketId);
        } catch (Exception ex) {
            log.error("[NewsApprovalAdapter] Failed to notify news-service for ticketId={}: {}", ticketId, ex.getMessage());
        }
    }
}
