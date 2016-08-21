package com.rainbow.trakmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        String url = getIntent().getStringExtra("url");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

//Picasso.with(getApplicationContext()).setIndicatorsEnabled(true);

        //Set the image
        Picasso.with(getApplicationContext())

                .load(url).tag("trak")
                .noFade()
                .placeholder(R.drawable.defaultimg)
                .fit()
                .into(imageView);
        //Rating
        TextView rating = (TextView) findViewById(R.id.rating);
        rating.setText(getResources().getString(R.string.rating) + " " + getIntent().getStringExtra("rating"));

        //Genre data
        TextView genreData = (TextView) findViewById(R.id.genreData);
        genreData.setText(getIntent().getStringExtra("genres"));

        //Release date
        TextView dateData = (TextView) findViewById(R.id.dateData);
        dateData.setText(getIntent().getStringExtra("releasedate"));

        //Tag line data
        TextView tagLineData = (TextView) findViewById(R.id.taglineData);
        tagLineData.setText(getIntent().getStringExtra("tagline"));

        //Movie title

        String titleStr = getIntent().getStringExtra("title");
        setTitle(titleStr);//Set the activity title as the movie name

        //Overview
        TextView overviewData = (TextView) findViewById(R.id.overviewData);
        overviewData.setText(getIntent().getStringExtra("overview"));// + "\n");


    }


}
