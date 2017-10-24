package com.gmail.moreau1006.mikael.attendancemanager.Model;

import java.io.Serializable;

/**
 * Created by mika on 16/09/17.
 */

public class Player implements Serializable {

    private long id;
    private String name;
    private String numberPhone;
    private long idTeam;
    private Boolean attendant;

    public Player() {
    }

    public Player(String name, String numberPhone) {
        this.name = name;
        this.numberPhone = numberPhone;
    }

    public Player(String name, String numberPhone, long idTeam) {
        this.name = name;
        this.numberPhone = numberPhone;
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(long idTeam) {
        this.idTeam = idTeam;
    }

    public Boolean isAttendant() {
        return attendant;
    }

    public void setAttendant(Boolean attendant) {
        this.attendant = attendant;
    }
}
