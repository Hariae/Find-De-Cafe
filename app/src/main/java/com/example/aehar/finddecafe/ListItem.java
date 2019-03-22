package com.example.aehar.finddecafe;

/**
 * Created by aehar on 3/21/2019.
 */
public class ListItem {
    private String Name;
    private String Vicinity;
    private Double Rating;

    public ListItem(String name, String vicinity, Double rating) {
        Name = name;
        Vicinity = vicinity;
        Rating = rating;
    }

    public String getName() {
        return Name;
    }

    public String getVicinity() {
        return Vicinity;
    }

    public Double getRating(){return Rating;}
}
