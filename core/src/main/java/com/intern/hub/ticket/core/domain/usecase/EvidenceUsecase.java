package com.intern.hub.ticket.core.domain.usecase;

import java.util.List;

import com.intern.hub.ticket.core.domain.model.EvidenceModel;
import com.intern.hub.ticket.core.domain.model.PresignedUrlModel;
import org.springframework.web.multipart.MultipartFile;

public interface EvidenceUsecase {

    List<EvidenceModel> getEvidences(Long ticketId);

    PresignedUrlModel getPresignedUrl(String fileName, String contentType, Long fileSize);

    /**
     * Upload a file to DMS storage and save the evidence record to DB.
     *
     * @param file            the multipart file to upload
     * @param destinationPath the storage destination path
     * @param ticketId        the ticket ID to associate the evidence with
     * @param actorId         the user ID performing the upload
     * @return the saved EvidenceModel with evidenceId and all metadata
     */
    EvidenceModel uploadFile(MultipartFile file, String destinationPath, Long ticketId, Long actorId);
}