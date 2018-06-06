package com.example.abhishek.booksapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    private static String BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?";
    BooksAdapter adapter;
    private EditText searchText;
    private ProgressBar progressBar;
    private String searchQuery = "";
    private TextView emptyTextView;
    private ImageView emptyImageView;
    private LinearLayout emptyView;
    LoaderManager loaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
            searchQuery = savedInstanceState.getString("SAVED_QUERY");

        setUiElements();

        setLoaderManager();

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String queryInField = searchText.getText().toString();
                    if (!queryInField.isEmpty() && !searchQuery.toLowerCase().equals(queryInField.toLowerCase())
                            && getNetworkInfo()) {

                        searchQuery = queryInField;
                        loadContent(loaderManager);

                    } else if (!getNetworkInfo()) {

                        adapter.clear();
                        searchQuery = "";
                        setNoInternetForEmptyView();

                    }

                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    handled = true;
                }

                return handled;
            }
        });

    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {


        Uri uri = Uri.parse(BOOKS_API_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        if (!searchQuery.isEmpty()) {
            uriBuilder.appendQueryParameter("q", searchQuery);
        }

        return new BooksLoader(MainActivity.this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {

        adapter.clear();
        progressBar.setVisibility(View.GONE);

        if (data != null && !data.isEmpty() && getNetworkInfo()) {
            adapter.addAll(data);
        } else if (!getNetworkInfo()) {
            setNoInternetForEmptyView();
        } else {
            setNoDataForEmptyView();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        adapter.clear();
    }

    private void setUiElements() {

        List<Books> booksList = new ArrayList<>();
        adapter = new BooksAdapter(MainActivity.this, booksList);
        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        searchText = (EditText) findViewById(R.id.query);

        emptyView = (LinearLayout) findViewById(R.id.empty_view);
        emptyImageView = (ImageView) emptyView.getChildAt(0);
        emptyTextView = (TextView) emptyView.getChildAt(1);
        emptyView.setVisibility(View.GONE);

        listView.setEmptyView(emptyView);
    }

    private void loadContent(LoaderManager loaderManager) {

        adapter.clear();
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loaderManager.restartLoader(1, null, this);

    }

    private void setLoaderManager() {
        loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);
    }

    private boolean getNetworkInfo() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    private void setNoInternetForEmptyView() {
        emptyTextView.setText(getString(R.string.no_internet));
        emptyImageView.setImageResource(R.drawable.nointernet);
    }

    private void setNoDataForEmptyView() {
        emptyTextView.setText(getString(R.string.no_data));
        emptyImageView.setImageResource(R.drawable.nodata);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SAVED_QUERY", searchQuery);
    }
}
