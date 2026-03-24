package com.intern.hub.ticket.infra.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.intern.hub.library.common.exception.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.InternalErrorException;
import com.intern.hub.ticket.core.domain.model.command.FileCommand;
import com.intern.hub.ticket.core.domain.port.InternalUploadDirectPort;
import com.intern.hub.ticket.infra.feignClient.InternalUploadDirectClient;
import com.intern.hub.ticket.infra.feignClient.dto.DmsDocumentClientModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InternalUploadDirectAdapter implements InternalUploadDirectPort {

    private final InternalUploadDirectClient dmsInternalFeignClient;

    public String uploadFile(
            FileCommand fileCommand, String keyPrefix, Long actorId,
            Long maxSizeBytes, String contentTypeRegex
    ) {
        if (fileCommand.size() > maxSizeBytes) {
            throw new BadRequestException(
                    "file.size.exceeded",
                    "Dung lượng file vượt quá giới hạn " + (maxSizeBytes / 1024 / 1024) + "MBs");
        }


        String contentType = fileCommand.contentType();
        if (contentType == null || !contentType.matches(contentTypeRegex)) {
            throw new BadRequestException(
                    "file.type.invalid", "Định dạng file không hợp lệ. Yêu cầu: " + contentTypeRegex);
        }

        MultipartFile multipartFile = new MockMultipartFile(
                fileCommand.originalFilename(),
                fileCommand.originalFilename(),
                contentType,
                fileCommand.content()
        );

        try {
            ResponseApi<DmsDocumentClientModel> response =
                    dmsInternalFeignClient.uploadFile(multipartFile, keyPrefix, actorId, false);

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

    private static class MockMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] bytes;

        public MockMultipartFile(String name, String originalFilename, String contentType, byte[] bytes) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.bytes = bytes;
        }

        @Override
        public String getName() { return name; }

        @Override
        public String getOriginalFilename() { return originalFilename; }

        @Override
        public String getContentType() { return contentType; }

        @Override
        public boolean isEmpty() { return bytes == null || bytes.length == 0; }

        @Override
        public long getSize() { return bytes != null ? bytes.length : 0; }

        @Override
        public byte[] getBytes() { return bytes; }

        @Override
        public InputStream getInputStream() { return new ByteArrayInputStream(bytes); }

        @Override
        public void transferTo(java.io.File dest) { throw new UnsupportedOperationException(); }
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
