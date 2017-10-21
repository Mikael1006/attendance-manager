package com.gmail.moreau1006.mikael.attendancemanager.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.moreau1006.mikael.attendancemanager.Model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mika on 19/09/17.
 */

public class TeamsDAO {

    // Champs de la base de données
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.TEAMS_COL_ID,
            MySQLiteHelper.TEAMS_COL_NAME};
    private PlayersDAO playersDAO;

    public TeamsDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Team createTeam(String name) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TEAMS_COL_NAME, name);

        long insertId = database.insert(MySQLiteHelper.TEAMS_TABLE, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TEAMS_TABLE,
                allColumns, MySQLiteHelper.TEAMS_COL_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Team newTeam = cursorToTeam(cursor);
        cursor.close();

        return newTeam;
    }

    public void deleteTeam(Team team) {
        long id = team.getId();
        System.out.println("Team deleted with id: " + id);
        database.delete(MySQLiteHelper.TEAMS_TABLE, MySQLiteHelper.TEAMS_COL_ID
                + " = " + id, null);
    }

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<Team>();

        Cursor cursor = database.query(MySQLiteHelper.TEAMS_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Team team = cursorToTeam(cursor);
            teams.add(team);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return teams;
    }

    public Team getTeamById(long idTeam){
        Cursor cursor = database.query(MySQLiteHelper.TEAMS_TABLE,
                allColumns, MySQLiteHelper.TEAMS_COL_ID + " = \"" + idTeam+"\"", null,
                null, null, null);

        cursor.moveToFirst();
        Team team;
        try{
            team = cursorToTeam(cursor);
        }catch (Exception e){
            team = null;
        }

        cursor.close();

        return team;
    }

    private Team cursorToTeam(Cursor cursor) {
        Team team = new Team();
        team.setId(cursor.getLong(0));
        team.setName(cursor.getString(1));
        return team;
    }

}
