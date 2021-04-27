package com.usccsci571dhruv.uscfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {

    private String author;
    private String stars;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();

        this.author = intent.getStringExtra("author");
        this.stars = intent.getStringExtra("stars");
        this.content = intent.getStringExtra("content");

        TextView tv_author = findViewById(R.id.activity_review_author);
        TextView tv_stars = findViewById(R.id.activity_review_stars);
        TextView tv_content = findViewById(R.id.activity_review_content);

        tv_author.setText(author);
        tv_stars.setText(stars);
        tv_content.setText(content);
    }
}