package com.gmail.moreau1006.mikael.attendancemanager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Match;
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

    public MatchsDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
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

        return match;
    }

    public void deleteMatch(Match match) {
        long id = match.getId();
        System.out.println("Match deleted with id: " + id);
        database.delete(MySQLiteHelper.MATCHS_TABLE, MySQLiteHelper.MATCHS_COL_ID
                + " = " + id, null);
    }

    public List<Match> getAllMatchs() {
        List<Match> matchs = new ArrayList<Match>();

        Cursor cursor = database.query(MySQLiteHelper.MATCHS_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Match macth = cursorToMatch(cursor);
            matchs.add(macth);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return matchs;
    }

    public List<Match> getAllFutureMatchs() {
        List<Match> matchs = new ArrayList<Match>();

        Date today = new Date();

        Cursor cursor = database.query(MySQLiteHelper.MATCHS_TABLE,
                allColumns, MySQLiteHelper.MATCHS_COL_DATE + " >= " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today), null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Match macth = cursorToMatch(cursor);
            matchs.add(macth);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return matchs;
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

        // On récupère l'équipe
        Cursor cursorteam = database.query(MySQLiteHelper.TEAMS_TABLE,
                allColumns, MySQLiteHelper.TEAMS_COL_ID + " = " + cursor.getLong(5), null,
                null, null, null);

        cursorteam.moveToFirst();
        Team team = cursorToTeam(cursorteam);
        cursorteam.close();

        match.setTeam(team);

        return match;
    }

    private Team cursorToTeam(Cursor cursor) {
        Team team = new Team();
        team.setId(cursor.getLong(0));
        team.setName(cursor.getString(1));
        return team;
    }
}
