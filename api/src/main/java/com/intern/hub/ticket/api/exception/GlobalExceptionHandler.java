package com.intern.hub.ticket.api.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.ForbiddenException;
import com.intern.hub.library.common.exception.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors()
                                .stream()
                                .map(fe -> Map.of("field", fe.getField(), "message",
                                                fe.getDefaultMessage() != null ? fe.getDefaultMessage() : ""))
                                .collect(Collectors.toList());
                log.info("Validation failed requestId={} method={} path={} errors={}",
                                MDC.get("requestId"), request.getMethod(), request.getRequestURI(), errors);
                return ResponseEntity.badRequest().body(Map.of(
                                "success", false,
                                "code", "VALIDATION_ERROR",
                                "message", "Validation failed",
                                "data", errors,
                                "requestId", requestIdOrEmpty()));
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<Map<String, Object>> handleBadRequest(
                        BadRequestException ex,
                        HttpServletRequest request) {
                log.warn("Bad request requestId={} method={} path={} msg={}",
                                MDC.get("requestId"), request.getMethod(), request.getRequestURI(), ex.getMessage());
                return ResponseEntity.badRequest().body(Map.of(
                                "success", false,
                                "code", "BAD_REQUEST",
                                "message", ex.getMessage(),
                                "requestId", requestIdOrEmpty()));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, Object>> handleIllegalArgument(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {
                log.warn("Illegal argument requestId={} method={} path={} msg={}",
                                MDC.get("requestId"), request.getMethod(), request.getRequestURI(), ex.getMessage());
                return ResponseEntity.badRequest().body(Map.of(
                                "success", false,
                                "code", "BAD_REQUEST",
                                "message", ex.getMessage(),
                                "requestId", requestIdOrEmpty()));
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleNotFound(
                        NotFoundException ex,
                        HttpServletRequest request) {
                log.warn("Not found requestId={} method={} path={} msg={}",
                                MDC.get("requestId"), request.getMethod(), request.getRequestURI(), ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                                "success", false,
                                "code", "NOT_FOUND",
                                "message", ex.getMessage(),
                                "requestId", requestIdOrEmpty()));
        }

        @ExceptionHandler(ForbiddenException.class)
        public ResponseEntity<Map<String, Object>> handleForbidden(
                        ForbiddenException ex,
                        HttpServletRequest request) {
                log.warn("Forbidden requestId={} method={} path={} msg={}",
                                MDC.get("requestId"), request.getMethod(), request.getRequestURI(), ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                                "success", false,
                                "code", "FORBIDDEN",
                                "message", ex.getMessage(),
                                "requestId", requestIdOrEmpty()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleAll(Exception ex, HttpServletRequest request) {
                log.error("Unhandled exception requestId={} method={} path={}",
                                MDC.get("requestId"), request.getMethod(), request.getRequestURI(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                                "success", false,
                                "code", "INTERNAL_ERROR",
                                "message", "Internal server error",
                                "requestId", requestIdOrEmpty()));
        }

        private String requestIdOrEmpty() {
                String requestId = MDC.get("requestId");
                return requestId != null ? requestId : "";
        }
}
