package com.intern.hub.ticket.infra.adapter;

import java.util.ArrayList;
import java.util.List;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.InternalErrorException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.utils.Snowflake;
import com.intern.hub.ticket.core.domain.port.InternalUploadDirectPort;
import com.intern.hub.ticket.infra.feignClient.client.InternalUploadDirectClient;
import com.intern.hub.ticket.infra.feignClient.dto.reponse.DmsDocumentClientModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InternalUploadDirectAdapter implements InternalUploadDirectPort {

    private final InternalUploadDirectClient dmsInternalFeignClient;
    private final Snowflake snowflake;

    /**
     * Upload a single file to DMS. Mirrors HRM's FileStorageAdapter.uploadFile pattern.
     */
    @Override
    public String uploadFile(
            MultipartFile file, String keyPrefix, Long actorId,
            Long maxSizeBytes, String contentTypeRegex
    ) {

        if (file.getSize() > maxSizeBytes) {
            throw new BadRequestException(
                    "file.size.exceeded",
                    "Dung lượng file vượt quá giới hạn " + (maxSizeBytes / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.matches(contentTypeRegex)) {
            throw new BadRequestException(
                    "file.type.invalid", "Định dạng file không hợp lệ. Yêu cầu: " + contentTypeRegex);
        }

        try {
            ResponseApi<DmsDocumentClientModel> response =
                    dmsInternalFeignClient.uploadFile(file, keyPrefix, actorId, false);

            if (response == null || response.data() == null || !hasText(response.data().objectKey())) {
                throw new InternalErrorException(
                        "storage.upload.error", "DMS không trả về thông tin file sau khi upload");
            }

            return response.data().objectKey();
        } catch (Exception e) {
            log.error("DMS upload failed for destination path {}", keyPrefix, e);
            throw new InternalErrorException(
                    "storage.upload.error", "Không thể upload file lên hệ thống lưu trữ");
        }
    }

    /**
     * Upload multiple files to DMS. Each file is uploaded individually.
     * Adapted from HRM single-file pattern for ticket's multi-file requirement.
     */
    @Override
    public List<String> uploadFiles(
            MultipartFile[] files, String baseDestinationPath,
            Long maxSizeBytes, String contentTypeRegex) {

        if (files == null || files.length == 0) {
            return List.of();
        }

        List<String> objectKeys = new ArrayList<>();
        Long actorId;
        for (MultipartFile file : files) {
            String destinationPath = baseDestinationPath + "/" + snowflake.next();
            actorId = snowflake.next();
            String objectKey = uploadFile(file, destinationPath, actorId, maxSizeBytes, contentTypeRegex);
            objectKeys.add(objectKey);
        }
        return objectKeys;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public void deleteFile(String key, Long actorId) {
        try {
            dmsInternalFeignClient.deleteFile(key, actorId);
        } catch (Exception e) {
            log.warn("DMS delete failed for key {}: {}", key, e.getMessage());
        }
    }
}
