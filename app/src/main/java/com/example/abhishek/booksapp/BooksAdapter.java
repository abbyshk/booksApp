package com.example.abhishek.booksapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by abhishek on 03/05/17.
 */

public class BooksAdapter extends ArrayAdapter<Books> {


    public BooksAdapter(@NonNull Context context, @NonNull List<Books> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View booksListLayout = convertView;

        if (booksListLayout == null) {

            booksListLayout = LayoutInflater.from(getContext()).inflate(R.layout.book_adapter, null);

        }

        Books bookData = getItem(position);

        TextView bookTitle = (TextView) booksListLayout.findViewById(R.id.book_title);
        TextView authorName = (TextView) booksListLayout.findViewById(R.id.author_name);
        TextView description = (TextView) booksListLayout.findViewById(R.id.description);
        TextView publisher = (TextView) booksListLayout.findViewById(R.id.publisher);
        TextView ratingField = (TextView) booksListLayout.findViewById(R.id.rating_value);
        TextView genre = (TextView) booksListLayout.findViewById(R.id.genre_value);
        ImageView image = (ImageView) booksListLayout.findViewById(R.id.book_image);

        bookTitle.setText(bookData.getBookTitle());
        authorName.setText("by " + bookData.getAuthorName());
        description.setText(bookData.getDescription());
        publisher.setText(bookData.getPublisher() + ", " + bookData.getDate());

        double ratingValue = bookData.getRating();
        String rating = "Not specified";

        if (ratingValue != -1) {

            DecimalFormat ratingFormat = new DecimalFormat("0.0");
            rating = ratingFormat.format(ratingValue);

        }

        ratingField.setText(rating);
        genre.setText(bookData.getGenre());

        Bitmap bookImage = bookData.getImage();

        if (bookImage != null) {
            image.setImageBitmap(bookImage);
        }

        return booksListLayout;
    }
}
