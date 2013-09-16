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

@Table(name = "Users")
public class User extends Model {
    @Column(name = "Username")
    public String username;

    @Column(name = "Name")
    public String name;

    @Column(name = "Email")
    public String email;

    @Column(name = "TwitterName")
    public String twittername;

    @Column(name = "BirthDate")
    public String birthdate;

    @Column(name = "Avatar")
    public String avatar;

    @Column(name = "AboutMe")
    public String aboutme;

    public User(){
        super();
    }

    public User(
            String username,
            String name,
            String email,
            String twittername,
            String birthdate,
            String avatar,
            String aboutme
    ){
        super();
        this.username = username;
        this.name = name;
        this.email = email;
        this.twittername = twittername;
        this.birthdate = birthdate;
        this.avatar = avatar;
        this.aboutme = aboutme;
    }
}