package com.example.project2_login;

import com.example.project2_login.model.AuthRequest;
import com.example.project2_login.model.AuthResponse;
import com.example.project2_login.model.ForgotPasswordRequest;
import com.example.project2_login.model.ForgotPasswordResponse;
import com.example.project2_login.model.MagicLinkRequest;
import com.example.project2_login.model.MagicLinkResponse;
import com.example.project2_login.model.RegisterRequest;
import com.example.project2_login.model.RegisterResponse;
import com.example.project2_login.model.Event;
import com.example.project2_login.model.EventResponse;
import com.example.project2_login.model.EventRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @POST("/auth/login")
    Call<AuthResponse> login(@Body AuthRequest authRequest);

    @POST("/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("/auth/forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    // Magic Link endpoint
    @POST("/auth/send-magic-link")
    Call<MagicLinkResponse> sendMagicLink(@Body MagicLinkRequest magicLinkRequest);

    // Verify Magic Link endpoint
    @GET("/auth/verify-magic-link")
    Call<AuthResponse> verifyMagicLink(@Query("token") String token);

    // Retrieve all events
    @GET("/events")
    Call<List<Event>> getAllEvents();

    // Create a new event
    @POST("/events")
    Call<EventResponse> createEvent(@Body EventRequest eventRequest);

    // Add a comment
//    @POST("/comments")
//    Call<CommentResponse> addComment(
//            @Header("Authorization") String token,
//            @Body CommentRequest commentRequest
//    );
//
//    // Retrieve comments for an event
//    @GET("/comments")
//    Call<List<Comment>> getComments(
//            @Query("event_id") String eventId
//    );
}