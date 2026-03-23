package com.intern.hub.ticket.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PresignedUrlReq(
        @NotBlank(message = "Tên file không được để trống") @Size(max = 255, message = "Tên file quá dài, tối đa 255 ký tự") String fileName,

        @NotBlank(message = "Tên file không được để trống") String destinationPath,

        @NotBlank(message = "Định dạng file (ContentType) không được để trống") @Pattern(regexp = "^(image/jpeg|image/png|application/pdf)$", message = "Hệ thống chỉ hỗ trợ upload file định dạng JPG, PNG hoặc PDF") String contentType,

        @NotNull(message = "Kích thước file không được để trống") @Min(value = 1, message = "Kích thước file không hợp lệ (phải lớn hơn 0 bytes)") @Max(value = 52428800, message = "Dung lượng file vượt quá giới hạn 50MB của hệ thống") Long fileSize) {
}
