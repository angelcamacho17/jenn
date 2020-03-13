package com.example.server.payload;

public class ApiResponse {
    private Boolean success;
    private String message;
    private Long objectId;
    
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public ApiResponse(Boolean success, String message, Long objectId) {
        this.success = success;
        this.message = message;
        this.objectId = objectId;
    }
    
    public Boolean getSuccess() {
        return success;
    }
    
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getObjectId() {
        return objectId;
    }
    
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}