package com.cabapro.development.service.report;

public class ReportException extends RuntimeException {
    public ReportException(String message) { super(message); }
    public ReportException(String message, Throwable cause) { super(message, cause); }
}