package com.memetro.android.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Lines")
public class Line extends Model {

    @Column(name = "TransportId")
    public int transport_id;

    @Column(name = "Number")
    public int number;

    @Column(name = "Name")
    public String name;

    @Column(name = "Created")
    public String created;


    public Line(){
        super();
    }

    public Line(
            int transport_id,
            int number,
            String name,
            String created
    ){
        super();
        this.transport_id = transport_id;
        this.number = number;
        this.name = name;
        this.created = created;
    }
}