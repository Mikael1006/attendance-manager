package com.gmail.moreau1006.mikael.attendancemanager.Model;

import java.util.Date;

/**
 * Created by mika on 07/11/17.
 */

public class Sms {
    private String number;
    private String body;
    private Date date;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
