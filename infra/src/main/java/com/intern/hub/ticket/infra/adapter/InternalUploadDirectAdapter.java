package com.intern.hub.ticket.infra.adapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @Override
    public String upload(FileCommand file, String destinationPath, Long actorId) {
        try {
            MultipartFile multipartFile = new ByteArrayMultipartFile(
                    file.originalFilename(),
                    file.originalFilename(),
                    file.contentType(),
                    file.content());

            ResponseApi<DmsDocumentClientModel> response =
                    dmsInternalFeignClient.uploadFile(multipartFile, destinationPath, actorId, false);

            if (response == null || response.data() == null
                    || response.data().objectKey() == null || response.data().objectKey().isBlank()) {
                throw new InternalErrorException(
                        "storage.upload.error", "DMS không trả về thông tin file sau khi upload");
            }

            return response.data().objectKey();
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("DMS upload failed for destination path {}", destinationPath, e);
            throw new InternalErrorException(
                    "storage.upload.error", "Không thể upload file lên hệ thống lưu trữ");
        }
    }

    private static class ByteArrayMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] bytes;

        public ByteArrayMultipartFile(String name, String originalFilename, String contentType, byte[] bytes) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType != null ? contentType : "application/octet-stream";
            this.bytes = bytes;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return bytes == null || bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes != null ? bytes.length : 0;
        }

        @Override
        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            throw new UnsupportedOperationException("transferTo not supported");
        }
    }
}
