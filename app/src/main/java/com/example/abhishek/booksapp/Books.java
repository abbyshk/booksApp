package com.example.abhishek.booksapp;

import android.graphics.Bitmap;

/**
 * Created by abhishek on 03/05/17.
 */

public class Books {

    private String bookTitle;
    private String authorName;
    private String description;
    private String publisher;
    private String date;
    private String genre;
    private double rating;
    private Bitmap image;

    public Books(String bookTitle, String authorName, String description, String publisher,
                 double rating, String genre,
                 String date, Bitmap image) {
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.description = description;
        this.publisher = publisher;
        this.date = date;
        this.rating = rating;
        this.genre = genre;
        this.image = image;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

}
