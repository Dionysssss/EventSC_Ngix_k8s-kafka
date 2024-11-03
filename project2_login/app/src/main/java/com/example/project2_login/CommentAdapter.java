package com.example.project2_login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project2_login.R;
import com.example.project2_login.model.Comment;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> comments;

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
        commentAuthor.setText("User ID: " + comment.getPostedBy());
        commentTimestamp.setText(comment.getTimestamp().toString());

        return convertView;
    }
}