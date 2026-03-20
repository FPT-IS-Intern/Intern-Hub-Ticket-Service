package com.intern.hub.ticket.api.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.library.common.exception.InternalErrorException;
import com.intern.hub.ticket.core.domain.port.FileStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/file-storage")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    /**
     * 1. Upload file lên MinIO/S3
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseApi<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        try {
            String fileKey = fileStorageService.uploadFile(
                    file.getBytes(),
                    file.getOriginalFilename(),
                    file.getContentType());

            return ResponseApi.ok(Map.of("fileKey", fileKey));
        } catch (Exception e) {
            throw new InternalErrorException("Không thể upload file: " + e.getMessage());
        }
    }

    /**
     * 2. Download file về máy (dành cho Sếp xem minh chứng)
     */
    /*
     * @GetMapping("/download/{fileKey}")
     * public ResponseEntity<byte[]> download(@PathVariable String fileKey) {
     * byte[] data = fileStorageService.downloadFile(fileKey);
     * 
     * return ResponseEntity.ok()
     * .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileKey
     * + "\"")
     * .contentType(MediaType.APPLICATION_OCTET_STREAM)
     * .body(data);
     * }
     */

    /**
     * 3. Xóa file khi User đổi ý không muốn nộp ảnh đó nữa
     */
    /*
     * @DeleteMapping("/{fileKey}")
     * public ResponseEntity<String> delete(@PathVariable String fileKey) {
     * fileStorageService.deleteFile(fileKey);
     * return ResponseEntity.ok("Đã xóa file: " + fileKey);
     * }
     */
}