/*
 * Copyright 2013 Nytyr [me at nytyr dot me]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memetro.android.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Alerts")
public class Alert extends Model {

    @Column(name = "AlertId")
    public Long alertId;

    @Column(name = "Description")
    public String description;

    @Column(name = "Date")
    public String date;

    @Column(name = "Username")
    public String username;

    @Column(name = "Latitude")
    public Double latitude;

    @Column(name = "Longitude")
    public Double longitude;

    @Column(name = "Line")
    public String line;

    @Column(name = "City")
    public String city;

    @Column(name = "Station")
    public String station;

    @Column(name = "Transport")
    public String transport;

    @Column(name = "Icon")
    public String icon;

    public Alert(){
        super();
    }

    public Alert(
            Long alertId,
            String description,
            String date,
            String username,
            Double latitude,
            Double longitude,
            String line,
            String city,
            String station,
            String transport,
            String icon
    ){
        super();
        this.alertId = alertId;
        this.description = description;
        this.date = date;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.line = line;
        this.city = city;
        this.station = station;
        this.transport = transport;
        this.icon = icon;
    }

    public String toString() {
        return this.description;
    }
}