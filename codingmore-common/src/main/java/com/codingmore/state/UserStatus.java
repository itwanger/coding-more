package com.codingmore.state;


public enum UserStatus {
    ENABLE(0),
    DISABLED(1);
    private int status;

    UserStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
