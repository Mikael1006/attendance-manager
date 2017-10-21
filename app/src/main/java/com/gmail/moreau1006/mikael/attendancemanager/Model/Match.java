package com.gmail.moreau1006.mikael.attendancemanager.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mika on 16/09/17.
 */

public class Match implements Serializable {

    private long id;
    private long idTeam;
    private Team team;
    private Date dateMatch;
    private Date dateRdv;
    private String opponent;
    private boolean home;
    private List<Player> players;

    public Match() {
    }

    public Match(Team team, Date dateMatch, Date dateRdv, String opponent, boolean home) {
        this.team = team;
        this.dateMatch = dateMatch;
        this.dateRdv = dateRdv;
        this.opponent = opponent;
        this.home = home;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(long idTeam) {
        this.idTeam = idTeam;
    }
}
