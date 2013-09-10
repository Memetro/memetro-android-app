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