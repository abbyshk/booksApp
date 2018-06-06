package com.example.abhishek.booksapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.List;

/**
 * Created by abhishek on 03/05/17.
 */

public class BookData {

    private static String LOG_TAG = BookData.class.getName();
    private static String NOT_SPECIFIED = "Not specified";

    private BookData() {

    }

    public static List<Books> getBooksData(String requestUrl) {

        URL url = makeUrl(requestUrl);
        if (url == null) {
            return null;
        }

        String jsonResponse = null;

        try {
            jsonResponse = httpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in getting json response" + e.getMessage());
        }

        List<Books> books = getBooksFromJson(jsonResponse);

        return books;
    }

    private static URL makeUrl(String reqUrl) {
        URL url = null;

        try {
            url = new URL(reqUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error in creating url" + e.getMessage());
        }
        return url;
    }

    private static String httpRequest(URL queryUrl) throws IOException {
        String jsonResponse = "";

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) queryUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readStream(inputStream);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in reading from stream" + e.getMessage());
        } finally {

            if (inputStream != null) {
                inputStream.close();
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return jsonResponse;
    }

    private static String readStream(InputStream inputStream) throws IOException {

        StringBuilder jsonString = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            CharSequence line = bufferedReader.readLine();

            while (line != null) {
                jsonString.append(line);
                line = bufferedReader.readLine();
            }
        }
        return jsonString.toString();
    }

    private static Bitmap getBitmap(String imageUrl) {

        URL url = makeUrl(imageUrl);

        if (url == null) {
            return null;
        }

        Bitmap bookImage = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                bookImage = BitmapFactory.decodeStream(inputStream);
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "Error in getting image " + e.getMessage());

        } finally {

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error in creating bitmap" + e.getMessage());
                }
            }

        }

        return bookImage;

    }


    private static List<Books> getBooksFromJson(String jsonResponse) {

        List<Books> books = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);

            String value = NOT_SPECIFIED;
            String description = NOT_SPECIFIED;
            String publisher = NOT_SPECIFIED;
            String date = NOT_SPECIFIED;
            String genre = NOT_SPECIFIED;
            Bitmap bookImage = null;
            double rating = -1;

            if (!jsonResponse.isEmpty()) {

                JSONArray items = root.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {

                    JSONObject itemsAtIndex = items.getJSONObject(i);

                    JSONObject volumeInfo = itemsAtIndex.getJSONObject("volumeInfo");

                    String bookTitle = volumeInfo.getString("title");

                    if (volumeInfo.has("authors"))
                        value = volumeInfo.getJSONArray("authors").getString(0);

                    if (volumeInfo.has("description"))
                        description = volumeInfo.getString("description");

                    if (volumeInfo.has("publishedDate"))
                        date = volumeInfo.getString("publishedDate");

                    if (volumeInfo.has("publisher"))
                        publisher = volumeInfo.getString("publisher");


                    if (volumeInfo.has("averageRating"))
                        rating = volumeInfo.getDouble("averageRating");

                    if (volumeInfo.has("categories"))
                        genre = volumeInfo.getJSONArray("categories").getString(0);

                    if (volumeInfo.has("imageLinks")) {

                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                        String imageUrl = imageLinks.getString("thumbnail");
                        bookImage = getBitmap(imageUrl);

                    }

                    books.add(new Books(bookTitle, value, description,
                            publisher, rating, genre, date, bookImage));

                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in parsing JSON" + e.getMessage());
        }


        return books;
    }

}
