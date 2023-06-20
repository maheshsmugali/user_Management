package com.pws.usermanagement.exception;


public class UserManagementException extends  Exception{

    private static final long serialVersionUID = 1L;

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public UserManagementException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}
