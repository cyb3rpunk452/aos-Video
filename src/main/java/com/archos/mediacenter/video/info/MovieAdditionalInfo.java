package com.archos.mediacenter.video.info;

import java.util.HashMap;

public class MovieAdditionalInfo {
    private String tagline;
    private String release_date;
    private String vote_count;
    private String vote_average;
    private HashMap<Integer,String> productionCompanyHashMap;
    private String writers;
    private String producers;
    private String mpaa_rating;


    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getProducers() {
        return producers;
    }

    public void setProducers(String producers) {
        this.producers = producers;
    }

    public String getMpaa_rating() {
        return mpaa_rating;
    }

    public void setMpaa_rating(String mpaa_rating) {
        this.mpaa_rating = mpaa_rating;
    }

    public MovieAdditionalInfo(String tagline, String release_date, String vote_count, String vote_average, HashMap<Integer, String> productionCompanyHashMap, String writers, String producers, String mpaa_rating) {
        this.tagline = tagline;
        this.release_date = release_date;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.productionCompanyHashMap = productionCompanyHashMap;
        this.writers = writers;
        this.producers = producers;
        this.mpaa_rating = mpaa_rating;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public HashMap<Integer, String> getProductionCompanyHashMap() {
        return productionCompanyHashMap;
    }

    public void setProductionCompanyHashMap(HashMap<Integer, String> productionCompanyHashMap) {
        this.productionCompanyHashMap = productionCompanyHashMap;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }
}
