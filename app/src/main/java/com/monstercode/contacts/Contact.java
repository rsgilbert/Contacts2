package com.monstercode.contacts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "contacts")
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate=true)
    private int id;

    @ColumnInfo(name="firstname")
    private String firstname;

    @ColumnInfo(name="lastname")
    private String lastname;

    @ColumnInfo(name="tel")
    private String tel;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name = "designation")
    private String designation;

    @ColumnInfo(name="site")
    private int site;

    /**
     * Getters and setters
     **/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

}
