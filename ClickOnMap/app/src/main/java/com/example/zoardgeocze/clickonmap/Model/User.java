package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;

/**
 * Created by ZoardGeocze on 04/06/17.
 */

public class User implements Serializable {

    private String login;
    private String name;
    private String password;
    private Character type;
    private String registerDate;

    public User(String login, String name, String password, String registerDate) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.type = 'C';
        this.registerDate = registerDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getType() {
        return type;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
}
