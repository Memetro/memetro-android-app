package com.memetro.android.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Stations")
public class Station extends Model {
    @Column(name = "Latitude")
    public float latitude;

    @Column(name = "Longitude")
    public float longitude;

    @Column(name = "Name")
    public String name;

    @Column(name = "Created")
    public String created;


    public Station(){
        super();
    }

    public Station(
            float latitude,
            float longitude,
            String name,
            String created
    ){
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.created = created;
    }
}