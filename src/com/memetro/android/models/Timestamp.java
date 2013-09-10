package com.memetro.android.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Timestamps")
public class Timestamp extends Model {
    @Column(name = "TableName")
    public String table_name;

    @Column(name = "Timestamp")
    public String timestamp;


    public Timestamp(){
        super();
    }

    public Timestamp(
            String table_name,
            String timestamp
    ){
        super();
        this.table_name = table_name;
        this.timestamp = timestamp;
    }
}