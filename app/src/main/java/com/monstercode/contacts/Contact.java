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

    @ColumnInfo(name="tel1")
    private String tel1;

    @ColumnInfo(name="tel2")
    private String tel2;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name="siteId")
    private int siteId;

    @ColumnInfo(name="job")
    private String job;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;

    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
