package com.memetro.android.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Countries")
public class Country extends Model {
    @Column(name = "Name")
    public String name;

    @Column(name = "Created")
    public String created;


    public Country(){
        super();
    }

    public Country(
            String name,
            String created
    ){
        super();
        this.name = name;
        this.created = created;
    }
}