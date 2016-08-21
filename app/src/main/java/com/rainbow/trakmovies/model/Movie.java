package com.rainbow.trakmovies.model;

import java.text.DecimalFormat;

/**
 * Created by Meera on 12/10/15.
 */
public class Movie {
    public double rating;
    public int votes;
    public String language;
    public String title;
    public String updatedAt;
    public String tagline;
    public String certification;
    public Ids ids;
    public String homepage;
    public String released;
    public String[] available_translations;
    public int year;
    public Images images;
    public int runtime;
    public String overview;
    public String trailer;
    public String[] genres;

    //Convert the movie rating to one decimal point
    public String getRating() {

        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(rating);
    }
}
