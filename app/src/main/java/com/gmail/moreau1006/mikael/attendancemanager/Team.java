package com.gmail.moreau1006.mikael.attendancemanager;

import java.io.Serializable;

/**
 * Created by mika on 16/09/17.
 */

public class Team implements Serializable {

    private long id;
    private String name;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
