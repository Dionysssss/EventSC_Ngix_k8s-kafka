package com.example.project2_login;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2_login.model.Comment;
import com.example.project2_login.model.UserResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> comments;
    private Map<Integer, String> userIdToEmailMap = new HashMap<>(); // Cache for userId to email mapping

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, parent, false);
        }

        TextView commentContent = convertView.findViewById(R.id.comment_content);
        TextView commentAuthor = convertView.findViewById(R.id.comment_author);
        TextView commentTimestamp = convertView.findViewById(R.id.comment_timestamp);

        Comment comment = comments.get(position);
        commentContent.setText(comment.getContent());

        // Format the timestamp to remove the 'T' character
        String formattedTimestamp = comment.getTimestamp().toString().replace("T", " ");
        commentTimestamp.setText(formattedTimestamp);

        int userId = comment.getPostedBy();
        if (userIdToEmailMap.containsKey(userId)) {
            // If email is cached, display it directly
            commentAuthor.setText("Posted by: " + userIdToEmailMap.get(userId));
        } else {
            // Fetch the email if it's not in cache
            fetchUserEmail(userId, commentAuthor);
        }

        return convertView;
    }




    private void fetchUserEmail(int userId, TextView commentAuthorTextView) {
        ApiClient.getAuthApi().getUserById(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String email = response.body().getEmail();
                    userIdToEmailMap.put(userId, email); // Cache the email

                    // Update the TextView with the fetched email
                    commentAuthorTextView.setText("Posted by: " + email);
                } else {
                    commentAuthorTextView.setText("Posted by: Unknown user");
                    Toast.makeText(context, "Failed to retrieve email for userId: " + userId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                commentAuthorTextView.setText("Posted by: Unknown user");
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
