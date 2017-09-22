package com.gmail.moreau1006.mikael.attendancemanager;

import java.util.Date;

/**
 * Created by mika on 16/09/17.
 */

public class Match {

    private long id;
//    private long idTeam;
    private Team team;
    private Date dateMatch;
    private Date dateRdv;
    private String adversaire;
    private boolean domicile;

    public Match() {
    }

    public Match(Team team, Date dateMatch, Date dateRdv, String adversaire, boolean domicile) {
        this.team = team;
        this.dateMatch = dateMatch;
        this.dateRdv = dateRdv;
        this.adversaire = adversaire;
        this.domicile = domicile;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Date getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(Date dateMatch) {
        this.dateMatch = dateMatch;
    }

    public Date getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(Date dateRdv) {
        this.dateRdv = dateRdv;
    }

    public String getAdversaire() {
        return adversaire;
    }

    public void setAdversaire(String adversaire) {
        this.adversaire = adversaire;
    }

    public boolean isDomicile() {
        return domicile;
    }

    public void setDomicile(boolean domicile) {
        this.domicile = domicile;
    }
}
