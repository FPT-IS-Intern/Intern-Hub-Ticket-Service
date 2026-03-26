package com.intern.hub.ticket.core.domain.port;

/**
 * Port interface for managing storage object lifecycle in transactions.
 * Implementations handle cleanup after rollback and deletion after commit.
 */
public interface StorageLifecyclePort {

    /**
     * Register a file key to be deleted if the current transaction rolls back.
     * Safe to call even if no transaction is active.
     */
    void cleanupOnRollback(String key, Long actorId);

    /**
     * Register a file key to be deleted after the current transaction commits successfully.
     * Safe to call even if no transaction is active.
     */
    void deleteAfterCommit(String key, Long actorId);
}
