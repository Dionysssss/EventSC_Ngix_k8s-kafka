package com.example.project2_login.model;

import com.google.gson.annotations.SerializedName;

public class ValidationStatusResponse {

    @SerializedName("validation-status")
    private String validationStatus;

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
}