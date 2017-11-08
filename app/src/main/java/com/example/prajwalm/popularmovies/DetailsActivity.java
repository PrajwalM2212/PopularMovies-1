package com.example.prajwalm.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private int position;

    private TextView mTitleView;
    private TextView mOverView;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        mTitleView = (TextView) findViewById(R.id.title);
        mUserRating = (TextView) findViewById(R.id.user_rating);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mOverView = (TextView) findViewById(R.id.over_view);
        mImageView = (ImageView) findViewById(R.id.image);


        String url;

        Intent in = getIntent();
        // if (in.hasExtra(Intent.EXTRA_TEXT)) {
        //     mTextView.setText(in.getStringExtra(Intent.EXTRA_TEXT));

        //}

        position = Integer.parseInt(in.getStringExtra(Intent.EXTRA_TEXT));
        url = in.getStringExtra("Url");

        if(MainActivity.checkNetwork(this)) {

            new Sync().execute(url);
        }else{

            Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private class Sync extends AsyncTask<String, Void, ArrayList<DetailsMap>> {
        @Override
        protected ArrayList<DetailsMap> doInBackground(String... strings) {

            //ArrayList<String> title = null;
            ArrayList<DetailsMap> details = null;
            String json;
            try {

                json = ImageGetter.httpUrlRequest(strings[0]);


                details = ImageGetter.JsonValues(json, position);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return details;
        }


        protected void onPostExecute(ArrayList<DetailsMap> details) {


            if (details != null) {
                DetailsMap detailsMap = details.get(0);
                mTitleView.setText(detailsMap.title);
                mOverView.setText(detailsMap.overView);
                mReleaseDate.setText(detailsMap.releaseDate);
                mUserRating.setText(detailsMap.userRating);
                Uri.Builder builder = Uri.parse("http://image.tmdb.org/t/p/original" + detailsMap.imageUrl).buildUpon();
                Uri uri = builder.build();
                // TextView textView = holder.textView;
                //textView.setText(uri.toString());


                Picasso.with(DetailsActivity.this).load(uri).into(mImageView);


            }


        }

    }


}