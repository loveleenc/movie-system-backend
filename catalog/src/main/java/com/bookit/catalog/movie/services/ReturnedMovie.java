package com.bookit.catalog.movie.services;

class ReturnedMovie {
    private String Plot;
    private String imdbRating;
    private String Title;

    public ReturnedMovie(String Plot, String imdbRating, String Title) {
        this.Plot = Plot;
        this.imdbRating = imdbRating;
        this.Title = Title;
    }

    public String getPlot() {
        return Plot;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getTitle() {
        return Title;
    }

    @Override
    public String toString() {
        return "plot is: " + Plot + ", imdbRating: " + imdbRating + "Title: " + Title;
    }
}
