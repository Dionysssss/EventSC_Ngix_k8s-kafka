package com.example.project2_login;

import com.example.project2_login.model.AuthRequest;
import com.example.project2_login.model.AuthResponse;
import com.example.project2_login.model.ForgotPasswordRequest;
import com.example.project2_login.model.ForgotPasswordResponse;
import com.example.project2_login.model.RegisterRequest;
import com.example.project2_login.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/auth/login")
    Call<AuthResponse> login(@Body AuthRequest authRequest);

    @POST("/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("/auth/forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);
}