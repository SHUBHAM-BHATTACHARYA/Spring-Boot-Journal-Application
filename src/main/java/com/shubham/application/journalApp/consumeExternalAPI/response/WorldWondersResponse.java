package com.shubham.application.journalApp.consumeExternalAPI.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class WorldWondersResponse {
    private String name;
    private String summary;
    private String location;
    @JsonProperty("build_year")
    private int buildYear;
    @JsonProperty("time_period")
    private String timePeriod;
    private Links links;
    private ArrayList<String> categories;

    @Getter
    @Setter
    public class Links{
        private String wiki;
        private String britannica;
        @JsonProperty("google_maps")
        private String googleMaps;
        @JsonProperty("trip_advisor")
        private String tripAdvisor;
        private ArrayList<String> images;
    }
}


