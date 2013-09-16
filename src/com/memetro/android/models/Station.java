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