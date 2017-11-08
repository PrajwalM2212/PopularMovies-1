package com.example.prajwalm.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ClickListenerInterface {

    // Enter your API_KEY in String API_VALUE below;
    private ImageAdapter adapter;
    private RecyclerView recyclerView;
    private static final String moviesPopularUrlString = "http://api.themoviedb.org/3/movie/popular?";
    private static final String moviesTopRatedUrlString ="http://api.themoviedb.org/3/movie/top_rated?";
    private final String API_KEY = "api_key" ;
    private final String API_VALUE ="Enter Your Api Key Here";
    private final String moviePopularUrl;
    private final String movieRatedUrl;
    private boolean topRated=false;
    private boolean popular =true;
    TextView networkView;
    TextView mEmptyView;




    {



        Uri.Builder builder = Uri.parse(moviesPopularUrlString).buildUpon();
        builder.appendQueryParameter(API_KEY,API_VALUE);
        Uri uri =builder.build();
             moviePopularUrl =uri.toString();


    }

    {


        Uri.Builder builder = Uri.parse(moviesTopRatedUrlString).buildUpon();
        builder.appendQueryParameter(API_KEY,API_VALUE);
        Uri uri =builder.build();
        movieRatedUrl =uri.toString();



    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         networkView = (TextView) findViewById(R.id.network_text);
         mEmptyView = (TextView)findViewById(R.id.empty_view);



        if(checkNetwork(this)) {
            new Task().execute(moviePopularUrl);
        }else{

           networkView.setVisibility(View.VISIBLE);


        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public  static  boolean checkNetwork(Context context){


        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo= connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();



    }

    @Override
    public void onItemClick(int position) {


        String pos = String.valueOf(position);
        String Url=null ;
        if(popular){
            Url = moviePopularUrl;
        }else if(topRated){
            Url = movieRatedUrl;
        }
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, pos);
            intent.putExtra("Url",Url);
            startActivity(intent);



    }


    private class Task extends android.os.AsyncTask<String, Void, ArrayList<JsonResults>> {
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.pb);



        protected void onPreExecute() {


            mProgressBar.setVisibility(View.VISIBLE);
            networkView.setVisibility(View.INVISIBLE);





        }

        @Override
        protected ArrayList<JsonResults> doInBackground(String... strings) {

            ArrayList<JsonResults> titleResult = null;
            String json;
            try {

                    json = ImageGetter.httpUrlRequest(strings[0]);



                    titleResult = ImageGetter.JsonExtraction(json);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return titleResult;
        }


        @Override
        protected void onPostExecute(ArrayList<JsonResults> titleResult) {



                if(titleResult!=null) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                    adapter = new ImageAdapter(MainActivity.this, titleResult, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                }


        }


    }


    private class TaskRated extends android.os.AsyncTask<String, Void, ArrayList<JsonResults>> {
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.pb);

        protected void onPreExecute() {


            mEmptyView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            networkView.setVisibility(View.INVISIBLE);


        }

        @Override
        protected ArrayList<JsonResults> doInBackground(String... strings) {

            ArrayList<JsonResults> titleResult = null;
            String json;
            try {

                    json = ImageGetter.httpUrlRequest(strings[0]);


                titleResult = ImageGetter.JsonExtraction(json);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return titleResult;
        }


        protected void onPostExecute(ArrayList<JsonResults> titleResult) {


            if(titleResult!=null) {
                mEmptyView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                adapter = new ImageAdapter(MainActivity.this, titleResult, MainActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
            }


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

     getMenuInflater().inflate(R.menu.menu,menu);

      return true;


    }

    public void onBackPressed(){

        if(topRated) {
            topRated=false;
            popular=true;
            new Task().execute(moviePopularUrl);
        }else{
            super.onBackPressed();
        }
    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item){


        int id = item.getItemId();

       if(id==R.id.top_rated) {

           if(checkNetwork(this)) {
               new TaskRated().execute(movieRatedUrl);
               topRated = true;
               popular = false;

           }else{

               Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show();

           }
           return true;

       }else if(id==R.id.popular) {

           if(checkNetwork(this)) {
               new Task().execute(moviePopularUrl);
               popular = true;
               topRated = false;
           }else{

               Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show();

           }

           return true;

       }


        return super.onOptionsItemSelected(item);








    }






}