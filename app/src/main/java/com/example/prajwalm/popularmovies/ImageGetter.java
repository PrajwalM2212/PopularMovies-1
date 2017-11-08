package com.example.prajwalm.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


class ImageGetter {
    private static final String LOG_TAG = ImageGetter.class.getSimpleName();


    private ImageGetter() {

    }


     static ArrayList<JsonResults> JsonExtraction(String jsonResponse) {


        ArrayList<JsonResults> jsonResultsArrayList = new ArrayList<>();


        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {

                JsonResults jsonResults = new JsonResults();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                jsonResults.title = jsonObject1.getString("title");
                jsonResults.bitmapUrl = jsonObject1.getString("poster_path");
                jsonResults.id = jsonObject1.getString("id");
                jsonResultsArrayList.add(jsonResults);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResultsArrayList;

/*
 Uri.Builder builder = Uri.parse("http://image.tmdb.org/t/p/w185/").buildUpon();
 builder.appendPath(bitmapString);
 builder.build();
 URL bitmapUrl;

 //bitmapUrl =new URL(builder.toString());**/


    }


    private static URL createUrl(String UrlString) {


        Uri.Builder builder = Uri.parse(UrlString).buildUpon();
        builder.build();

        URL url = null;

        try {

            url = new URL(builder.toString());

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedException");
        }

        return url;
    }


// --Commented out by Inspection START (06/08/17, 9:17 AM):
//    public static Bitmap httpUrlImageRequest(String imageString) throws IOException {
//
//
//        Uri.Builder builder = Uri.parse("http://image.tmdb.org/t/p/w185/").buildUpon();
//        builder.appendPath(imageString);
//        builder.build();
//        URL url;
//
//        url = new URL(builder.toString());
//
//
//        Bitmap bitmap = null;
//
//
//        HttpURLConnection urlConnection = null;
//        InputStream input = null;
//        try {
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setConnectTimeout(1500);
//            urlConnection.setReadTimeout(1000);
//            urlConnection.connect();
//            if (urlConnection.getResponseCode() == 200) {
//                input = urlConnection.getInputStream();
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
//                bitmap = BitmapFactory.decodeStream(input);
//
//            } else {
//                Log.e(LOG_TAG, urlConnection.getResponseMessage());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (input != null) {
//                input.close();
//            }
//        }
//
//        return bitmap;
//
//    }
// --Commented out by Inspection STOP (06/08/17, 9:17 AM)


     static String httpUrlRequest(String query) throws IOException {

        String jsonResponse = "";
        URL url = createUrl(query);


        if (url == null) {
            return jsonResponse;
        }


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }


        return jsonResponse;

    }


    private static String readFromStream(InputStream inputStream) throws IOException {


        StringBuilder output = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        String line = bufferedReader.readLine();

        while (line != null) {
            output.append(line);
            line = bufferedReader.readLine();
        }

        return output.toString();


    }


     static ArrayList<DetailsMap> JsonValues(String jsonResponse, int position) {


        //ArrayList<String> list = new ArrayList<>();
        ArrayList<DetailsMap> details=new ArrayList<>();
        DetailsMap detailsMap = new DetailsMap();



        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("results");


            for(int i=0 ;i<jsonArray.length();i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                detailsMap.title = jsonObject1.getString("original_title");
                detailsMap.overView =jsonObject1.getString("overview");
                detailsMap.releaseDate =jsonObject1.getString("release_date");
                detailsMap.userRating =jsonObject1.getString("vote_average");
                detailsMap.imageUrl=jsonObject1.getString("poster_path");
                details.add(detailsMap);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return details;


    }


}






