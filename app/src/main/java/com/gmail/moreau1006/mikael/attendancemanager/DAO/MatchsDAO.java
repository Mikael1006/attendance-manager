package com.gmail.moreau1006.mikael.attendancemanager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Player;
import com.gmail.moreau1006.mikael.attendancemanager.Model.Team;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mika on 22/09/17.
 */

public class MatchsDAO {

    // Champs de la base de données
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.MATCHS_COL_ID,
            MySQLiteHelper.MATCHS_COL_DATE, MySQLiteHelper.MATCHS_COL_DATE_RDV,
            MySQLiteHelper.MATCHS_COL_OPPONENT, MySQLiteHelper.MATCHS_COL_HOME,
            MySQLiteHelper.MATCHS_COL_TEAM_ID};
    private TeamsDAO teamsDAO;
    private PlayersDAO playersDAO;

    public MatchsDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
        teamsDAO = new TeamsDAO(context);
        playersDAO = new PlayersDAO(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Match createMatch(Date dateMatch, Date dateRdv, String opponent, boolean home, Team team) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.MATCHS_COL_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateMatch));
        values.put(MySQLiteHelper.MATCHS_COL_DATE_RDV, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateRdv));
        values.put(MySQLiteHelper.MATCHS_COL_OPPONENT, opponent);
        values.put(MySQLiteHelper.MATCHS_COL_HOME, home);
        values.put(MySQLiteHelper.MATCHS_COL_TEAM_ID, team.getId());

        long insertId = database.insert(MySQLiteHelper.MATCHS_TABLE, null, values);

        Cursor cursor = database.query(MySQLiteHelper.MATCHS_TABLE,
                allColumns, MySQLiteHelper.MATCHS_COL_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Match match = cursorToMatch(cursor);
        cursor.close();

        // On ajoute la team car elle n'est pas ajoutée avec le curseur.
        match.setTeam(team);

        return match;
    }

    public Match createMatch(Match match) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.MATCHS_COL_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(match.getDateMatch()));
        values.put(MySQLiteHelper.MATCHS_COL_DATE_RDV, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(match.getDateRdv()));
        values.put(MySQLiteHelper.MATCHS_COL_OPPONENT, match.getOpponent());
        values.put(MySQLiteHelper.MATCHS_COL_HOME, match.isHome());
        values.put(MySQLiteHelper.MATCHS_COL_TEAM_ID, match.getTeam().getId());

        long insertId = database.insert(MySQLiteHelper.MATCHS_TABLE, null, values);

        Cursor cursor = database.query(MySQLiteHelper.MATCHS_TABLE,
                allColumns, MySQLiteHelper.MATCHS_COL_ID + " = " + insertId, null,
                null, null, null);

        // add all the invitations
        List<Player> invitedPlayers = match.getInvitedPlayers();
        Team team = match.getTeam();

        cursor.moveToFirst();
        match = cursorToMatch(cursor);
        cursor.close();

        // On ajoute la team car elle n'est pas ajoutée avec le curseur.
        match.setTeam(team);
        match.setInvitedPlayers(invitedPlayers);

        if (invitedPlayers != null){
            for(int i = 0; i < invitedPlayers.size(); i++){
                values = new ContentValues();
                values.put(MySQLiteHelper.INVITATION_COL_PLAYER_ID, invitedPlayers.get(i).getId());
                values.put(MySQLiteHelper.INVITATION_COL_MATCH_ID, match.getId());
                values.put(MySQLiteHelper.INVITATION_COL_RESPONSE, invitedPlayers.get(i).isAttendant());

                database.insert(MySQLiteHelper.INVITATION_TABLE, null, values);
            }
        }

        return match;
    }

    public void deleteMatch(Match match) {
        long id = match.getId();
        database.delete(MySQLiteHelper.MATCHS_TABLE, MySQLiteHelper.MATCHS_COL_ID
                + " = " + id, null);
        database.delete(MySQLiteHelper.INVITATION_TABLE, MySQLiteHelper.INVITATION_COL_MATCH_ID
                + " = " + id, null);
    }

    public List<Match> getAllMatchs() {
        List<Match> matchs = new ArrayList<Match>();

        Cursor cursor = database.query(MySQLiteHelper.MATCHS_TABLE,
                allColumns, null, null, null, null, MySQLiteHelper.MATCHS_COL_DATE +" DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Match match = cursorToMatch(cursor);

            teamsDAO.open();
            Team team = teamsDAO.getTeamById(match.getIdTeam());
            match.setTeam(team);
            teamsDAO.close();

            playersDAO.open();
            List<Player> players = playersDAO.getPlayersByMatch(match);
            match.setInvitedPlayers(players);
            playersDAO.close();

            matchs.add(match);
            cursor.moveToNext();
        }
        // close cursor
        cursor.close();

        return matchs;
    }

    public List<Match> getAllFutursMatchs() {
        List<Match> matchs = new ArrayList<Match>();

        Cursor cursor = database.query(MySQLiteHelper.MATCHS_TABLE,
                allColumns, MySQLiteHelper.MATCHS_COL_DATE + " >= datetime('now')",
                null, null, null, MySQLiteHelper.MATCHS_COL_DATE +" DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Match match = cursorToMatch(cursor);

            teamsDAO.open();
            Team team = teamsDAO.getTeamById(match.getIdTeam());
            match.setTeam(team);
            teamsDAO.close();

            playersDAO.open();
            List<Player> players = playersDAO.getPlayersByMatch(match);
            match.setInvitedPlayers(players);
            playersDAO.close();

            matchs.add(match);
            cursor.moveToNext();
        }
        // fermeture du curseur
        cursor.close();

        return matchs;
    }

    public void invitePlayerToMatch(Player player, Match match){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.INVITATION_COL_PLAYER_ID, player.getId());
        values.put(MySQLiteHelper.INVITATION_COL_MATCH_ID, match.getId());
        values.put(MySQLiteHelper.INVITATION_COL_RESPONSE, player.isAttendant());

        database.insert(MySQLiteHelper.INVITATION_TABLE, null, values);
    }

    public Match updateInvitation(Match match, Player player){

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.INVITATION_COL_MATCH_ID, match.getId());
        values.put(MySQLiteHelper.INVITATION_COL_PLAYER_ID, player.getId());
        values.put(MySQLiteHelper.INVITATION_COL_RESPONSE, player.isAttendant());

        database.update(MySQLiteHelper.INVITATION_TABLE, values,
                MySQLiteHelper.INVITATION_COL_MATCH_ID + " = " + match.getId() +
                        " AND " + MySQLiteHelper.INVITATION_COL_PLAYER_ID + " = " + player.getId(), null);

        Cursor cursor = database.query(MySQLiteHelper.MATCHS_TABLE,
                allColumns, MySQLiteHelper.MATCHS_COL_ID + " = " + match.getId(), null,
                null, null, null);

        cursor.moveToFirst();
        match = cursorToMatch(cursor);
        cursor.close();

        teamsDAO.open();
        Team team = teamsDAO.getTeamById(match.getIdTeam());
        match.setTeam(team);
        teamsDAO.close();

        playersDAO.open();
        List<Player> players = playersDAO.getPlayersByMatch(match);
        match.setInvitedPlayers(players);
        playersDAO.close();

        return match;
    }

    private Match cursorToMatch(Cursor cursor) {
        Match match = new Match();

        match.setId(cursor.getLong(0));

        String dateStr = cursor.getString(1);
        Date date;

        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = iso8601Format.parse(dateStr);
            match.setDateMatch(date);
        } catch (ParseException e) {
            //Log.e(TAG, "Parsing ISO8601 datetime failed", e);
        }

        dateStr = cursor.getString(2);

        try {
            date = iso8601Format.parse(dateStr);
            match.setDateRdv(date);
        } catch (ParseException e) {
            //Log.e(TAG, "Parsing ISO8601 datetime failed", e);
        }

        match.setOpponent(cursor.getString(3));
        match.setHome(cursor.getInt(4) > 0);

        long idTeam = cursor.getLong(5);

        match.setIdTeam(idTeam);

        return match;
    }
}
