package com.memetro.android.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Transports")
public class Transport extends Model {
    @Column(name = "Name")
    public String name;

    @Column(name = "Icon")
    public int icon;


    public Transport(){
        super();
    }

    public Transport(
            String name,
            int icon
    ){
        super();
        this.name = name;
        this.icon = icon;
    }
}