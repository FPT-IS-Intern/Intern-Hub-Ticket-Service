package com.intern.hub.ticket.infra.service;

import com.intern.hub.ticket.core.domain.port.InternalUploadDirectPort;
import com.intern.hub.ticket.core.domain.port.StorageLifecyclePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageObjectLifecycleManager implements StorageLifecyclePort {

    private final InternalUploadDirectPort fileStorageRepository;

    public void cleanupOnRollback(String key, Long actorId) {
        if (!hasText(key)) {
            return;
        }

        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_ROLLED_BACK) {
                            safeDelete(key, actorId);
                        }
                    }
                });
    }

    public void deleteAfterCommit(String key, Long actorId) {
        if (!hasText(key)) {
            return;
        }

        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            safeDelete(key, actorId);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        safeDelete(key, actorId);
                    }
                });
    }

    private void safeDelete(String key, Long actorId) {
        try {
            fileStorageRepository.deleteFile(key, actorId);
        } catch (Exception ex) {
            log.error("Failed to delete storage object key {}", key, ex);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
