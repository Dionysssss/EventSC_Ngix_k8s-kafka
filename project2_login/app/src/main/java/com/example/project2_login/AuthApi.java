package com.example.project2_login;

import com.example.project2_login.model.AuthRequest;
import com.example.project2_login.model.AuthResponse;
import com.example.project2_login.model.Comment;
import com.example.project2_login.model.CommentRequest;
import com.example.project2_login.model.CommentResponse;
import com.example.project2_login.model.ForgotPasswordRequest;
import com.example.project2_login.model.ForgotPasswordResponse;
import com.example.project2_login.model.ReceivedEvent;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    @POST("/auth/login")
    Call<AuthResponse> login(@Body AuthRequest authRequest);

    @POST("/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("/auth/forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    // Retrieve all events
    @GET("/events")
    Call<List<ReceivedEvent>> getAllEvents();

    // Create a new event
    @POST("/events")
    Call<EventResponse> createEvent(@Body EventRequest eventRequest);

    // Edit event
    @PUT("/events/{eventId}")
    Call<EventResponse> updateEvent(@Path("eventId") int eventId, @Body EventRequest eventRequest);

    // Create a new comment for an event
    @POST("/comments")
    Call<CommentResponse> createComment(@Body CommentRequest commentRequest);

    // Retrieve all comments for a specific event
    @GET("/comments")
    Call<List<Comment>> getComments(@Query("eventId") int eventId);
}