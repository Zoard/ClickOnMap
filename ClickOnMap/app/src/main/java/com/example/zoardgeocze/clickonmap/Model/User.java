package com.example.zoardgeocze.clickonmap.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by ZoardGeocze on 04/06/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    private String id; // TODO: A idéia é utilizar um UUID, por enquanto está sendo utilizado o próprio email do usuário
    private String email;
    private String name;
    private String password;
    private Character type;
    private String registerDate;

    //public User() {}

    public User(String id, String email, String name, String password, String registerDate) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.type = 'C';//TODO: Rever o que siginifica esse tipo de usuário
        this.registerDate = registerDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

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
