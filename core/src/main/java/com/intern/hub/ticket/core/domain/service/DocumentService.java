package com.intern.hub.ticket.core.domain.service;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    /**
     * Get all evidence documents for a ticket.
     *
     * @param ticketId the ticket ID
     * @return list of evidence models
     */
    List<EvidenceModel> getDocuments(Long ticketId);

    /**
     * Replace all evidence documents for a ticket.
     * Deletes existing evidence files from storage and DB, then uploads and saves new ones.
     *
     * @param ticketId         the ticket ID
     * @param actorId          the user ID performing the replacement
     * @param destinationPath  the storage destination path
     * @param files            the new files to upload
     */
    void replaceDocuments(Long ticketId, Long actorId, String destinationPath, List<MultipartFile> files);
}
