package com.gmail.moreau1006.mikael.attendancemanager.Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private List<Player> invitedPlayers;

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

    public List<Player> getInvitedPlayers() {
        return invitedPlayers;
    }

    public void setInvitedPlayers(List<Player> invitedPlayers) {
        this.invitedPlayers = invitedPlayers;
    }

    public long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(long idTeam) {
        this.idTeam = idTeam;
    }

    public String getDefaultSms(){
        DateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yyyy", Locale.FRENCH);
        DateFormat timeFormat = new SimpleDateFormat("HH'h'mm", Locale.FRENCH);
        String dateMatch = dateFormat.format(getDateMatch());
        String timeRdv = timeFormat.format(getDateRdv());
        String timeMatch = timeFormat.format(getDateMatch());
        String home;
        if(isHome()){
            home = "Domicile";
        }else{
            home = "Exterieur";
        }

        String sms =   "CONVOCATION\n"
                + "RDV\t\t\t: "  + timeRdv + "\n"
                + "MATCH\t\t: "  + dateMatch + "\n"
                + "HEURE\t\t: "  + timeMatch + "\n"
                + "ADVERSAIRE\t: " + getOpponent() + "\n"
                + "LIEU\t\t: " + home + "\n"
                ;
        return sms;
    }

    public String prepareSms(String sms){
        sms = getSmsCode() + "\n"
                + sms
                + "***************\n"
                + "VOTRE REPONSE\n"
                + "format:\n"
                + getSmsCode() + "#present" + "\n"
                + "OU\n"
                + getSmsCode() + "#absent" + "\n"
                + "ex: " + getSmsCode() + "#present je prends les maillots";
        return sms;
    }

    public String getSmsCode(){
        return "#match-" + getId();
    }
}
