package com.example.abhishek.booksapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by abhishek on 03/05/17.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private String url;

    public BooksLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {

        if (url == null) return null;

        else {

            return BookData.getBooksData(url);

        }

    }
}
