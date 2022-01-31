package com.forum.exception;
import java.util.Date;
import java.util.Map;

public class ExceptionResponse {
  private Date timestamp;
  private Map<String, String> errors;
  private String message;
  private String details;

  public ExceptionResponse(Date timestamp, String message, String details, Map<String, String> errors) {
    super();
    this.errors = errors;
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDetails() {
    return details;
  }

}