package com.memetro.android.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Cities")
public class City extends Model {
    @Column(name = "CountryId")
    public int country_id;

    @Column(name = "Name")
    public String name;

    @Column(name = "Created")
    public String created;


    public City(){
        super();
    }

    public City(
            int country_id,
            String name,
            String created
    ){
        super();
        this.country_id = country_id;
        this.name = name;
        this.created = created;
    }
}