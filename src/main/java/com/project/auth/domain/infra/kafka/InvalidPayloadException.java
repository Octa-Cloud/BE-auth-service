package com.project.auth.domain.infra.kafka;

public class InvalidPayloadException extends RuntimeException {
    public InvalidPayloadException(String msg) { super(msg); }
    public InvalidPayloadException(String msg, Throwable cause) { super(msg, cause); }
}