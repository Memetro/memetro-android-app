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

    public String toString() {
        return this.name;
    }
}