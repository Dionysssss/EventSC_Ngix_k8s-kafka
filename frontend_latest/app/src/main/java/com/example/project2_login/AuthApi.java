package com.example.project2_login;

import com.example.project2_login.model.AuthRequest;
import com.example.project2_login.model.AuthResponse;
import com.example.project2_login.model.Comment;
import com.example.project2_login.model.CommentRequest;
import com.example.project2_login.model.CommentResponse;
import com.example.project2_login.model.MagicLinkRequest;
import com.example.project2_login.model.MagicLinkResponse;
import com.example.project2_login.model.MagicLinkVerificationRequest;
import com.example.project2_login.model.MagicLinkVerificationResponse;
import com.example.project2_login.model.ReceivedEvent;
import com.example.project2_login.model.RegisterRequest;
import com.example.project2_login.model.RegisterResponse;
import com.example.project2_login.model.Event;
import com.example.project2_login.model.EventResponse;
import com.example.project2_login.model.EventRequest;
import com.example.project2_login.model.UserIdRequest;
import com.example.project2_login.model.UserResponse;
import com.example.project2_login.model.ValidationCountResponse;
import com.example.project2_login.model.ValidationStatusResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    @POST("/auth/login")
    Call<AuthResponse> login(@Body AuthRequest authRequest);

    @POST("/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("/auth/send-magic-link")
    Call<MagicLinkResponse> sendMagicLink(@Body MagicLinkRequest request);

    @POST("/auth/verify-magic-link")
    Call<MagicLinkVerificationResponse> verifyMagicLink(@Body MagicLinkVerificationRequest request);

    @POST("/auth/save-magic-link")
    Call<MagicLinkVerificationResponse> saveMagicLink(@Body MagicLinkVerificationRequest request);

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

    // Confirm an event
//    @POST("/events/{eventId}/confirm")
//    Call<EventResponse> confirmEvent(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // Withdraw confirmation of an event
//    @HTTP(method = "DELETE", path = "/events/{eventId}/confirm", hasBody = true)
//    Call<EventResponse> withdrawConfirmation(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // Report an event as false
//    @POST("/events/{eventId}/report-false")
//    Call<EventResponse> reportEventAsFalse(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // Withdraw a false report
//    @HTTP(method = "DELETE", path = "/events/{eventId}/report-false", hasBody = true)
//    Call<EventResponse> withdrawFalseReport(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // Confirm an event with `confirm = 1` for confirmation
//    @POST("/events/{eventId}/confirm")
//    Call<EventResponse> confirmEvent(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest, @Query("confirm") int confirm);
//
//    // Report an event as false with `confirm = 0`
//    @POST("/events/{eventId}/confirm")
//    Call<EventResponse> reportEventAsFalse(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest, @Query("confirm") int confirm);
//
//    // Withdraw confirmation (confirm = 1) or false report (confirm = 0) based on context
//    @HTTP(method = "DELETE", path = "/events/{eventId}/confirm", hasBody = true)
//    Call<EventResponse> withdrawConfirmationOrFalseReport(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest, @Query("confirm") int confirm);
//
//    // Retrieve validation counts for an event
//    @GET("/events/{eventId}/validation-counts")
//    Call<ValidationCountResponse> getValidationCounts(@Path("eventId") int eventId);

    // Confirm an event
    @POST("/events/{eventId}/confirm")
    Call<EventResponse> confirmEvent(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // Withdraw confirmation for an event (DELETE with a body)
    @HTTP(method = "DELETE", path = "/events/{eventId}/confirm", hasBody = true)
    Call<EventResponse> withdrawConfirmation(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // Report an event as false
    @POST("/events/{eventId}/report-false")
    Call<EventResponse> reportEventAsFalse(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // Withdraw false report for an event (DELETE with a body)
    @HTTP(method = "DELETE", path = "/events/{eventId}/report-false", hasBody = true)
    Call<EventResponse> withdrawFalseReport(@Path("eventId") int eventId, @Body UserIdRequest userIdRequest);

    // 5. Retrieve Validation Counts for an Event
    @GET("/events/{eventId}/validation-counts")
    Call<ValidationCountResponse> getValidationCounts(
            @Path("eventId") int eventId
    );

    @GET("/auth/{id}")
    Call<UserResponse> getUserById(@Path("id") int id);

    @GET("/events/{eventId}")
    Call<Event> getEventById(@Path("eventId") int eventId);

    @GET("/events/{eventId}/validation-status/{userId}")
    Call<ValidationStatusResponse> getValidationStatus(
            @Path("eventId") int eventId,
            @Path("userId") int userId
    );


}