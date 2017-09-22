package com.gmail.moreau1006.mikael.attendancemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mika on 16/09/17.
 *
 * inspiré de http://vogella.developpez.com/tutoriels/android/utilisation-base-donnees-sqlite/
 */

public class PlayersDAO {

    // Champs de la base de données
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.PLAYERS_COL_ID,
            MySQLiteHelper.PLAYERS_COL_NAME, MySQLiteHelper.PLAYERS_COL_NUMBER, MySQLiteHelper.PLAYERS_COL_TEAM_ID};

    public PlayersDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Player createPlayer(String name, String numberPhone, long idTeam) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.PLAYERS_COL_NAME, name);
        values.put(MySQLiteHelper.PLAYERS_COL_NUMBER, numberPhone);
        values.put(MySQLiteHelper.PLAYERS_COL_TEAM_ID, idTeam);

        long insertId = database.insert(MySQLiteHelper.PLAYERS_TABLE, null, values);

        Cursor cursor = database.query(MySQLiteHelper.PLAYERS_TABLE,
                allColumns, MySQLiteHelper.PLAYERS_COL_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Player newPlayer = cursorToPlayer(cursor);
        cursor.close();

        return newPlayer;
    }

    public void deletePlayer(Player player) {
        long id = player.getId();
        System.out.println("Player deleted with id: " + id);
        database.delete(MySQLiteHelper.PLAYERS_TABLE, MySQLiteHelper.PLAYERS_COL_ID
                + " = " + id, null);
    }

    public List<Player> getPlayersByTeam(Team team) {
        List<Player> players = new ArrayList<Player>();

        Cursor cursor = database.query(MySQLiteHelper.PLAYERS_TABLE,
                allColumns, MySQLiteHelper.PLAYERS_COL_TEAM_ID + " = \"" + team.getId() +"\"", null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Player player = cursorToPlayer(cursor);
            players.add(player);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return players;
    }

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<Player>();

        Cursor cursor = database.query(MySQLiteHelper.PLAYERS_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Player player = cursorToPlayer(cursor);
            players.add(player);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return players;
    }

    public Player getPlayerByNumber(String number){

        Cursor cursor = database.query(MySQLiteHelper.PLAYERS_TABLE,
                allColumns, MySQLiteHelper.PLAYERS_COL_NUMBER + " = \"" + number+"\"", null,
                null, null, null);

        cursor.moveToFirst();
        Player player = cursorToPlayer(cursor);
        cursor.close();

        return player;
    }

    private Player cursorToPlayer(Cursor cursor) {
        Player player = new Player();
        player.setId(cursor.getLong(0));
        player.setName(cursor.getString(1));
        player.setNumberPhone(cursor.getString(2));
        player.setIdTeam(cursor.getLong(3));
        return player;
    }
}
