package com.gmail.moreau1006.mikael.attendancemanager.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mika on 16/09/17.
 *
 * inspiré de http://vogella.developpez.com/tutoriels/android/utilisation-base-donnees-sqlite/
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

//    passer en shell sur le device :
//    ~/Android/Sdk/platform-tools/adb -s 28d3df2c shell
//     acces database without rooting
//    https://stackoverflow.com/questions/13006315/how-to-access-data-data-folder-in-android-device
//    run-as com.gmail.moreau1006.mikael.attendancemanager
//    cd /data/data/com.gmail.moreau1006.mikael.attendancemanager
//
//    ./adb shell
//    run-as com.gmail.moreau1006.mikael.attendancemanager
//    chmod 666 databases/attendance_manager.db
//    exit
//    cp /data/data/com.gmail.moreau1006.mikael.attendancemanager/databases/attendance_manager.db /sdcard/
//    exit
//    ./adb pull /sdcard/attendance_manager.db /home/mika/Téléchargements/attendance_manager.db

    public static final String PLAYERS_TABLE = "Players";
    public static final String PLAYERS_COL_ID = "_id";
    public static final String PLAYERS_COL_NAME = "Name";
    public static final String PLAYERS_COL_NUMBER = "Number";
    public static final String PLAYERS_COL_TEAM_ID = "idTeam";

    public static final String TEAMS_TABLE = "Teams";
    public static final String TEAMS_COL_ID = "_id";
    public static final String TEAMS_COL_NAME = "Name";

    public static final String MATCHS_TABLE = "Matchs";
    public static final String MATCHS_COL_ID = "_id";
    public static final String MATCHS_COL_DATE = "Date";
    public static final String MATCHS_COL_DATE_RDV = "DateRDV";
    public static final String MATCHS_COL_OPPONENT = "Adversaire";
    public static final String MATCHS_COL_HOME = "Domicile";
    public static final String MATCHS_COL_TEAM_ID = "idTeam";


    public static final String INVITATION_TABLE = "Invitations";
    public static final String INVITATION_COL_PLAYER_ID = "idPlayer";
    public static final String INVITATION_COL_MATCH_ID = "idMatch";
    public static final String INVITATION_COL_RESPONSE = "Response";

    private static final String DATABASE_NAME = "attendance_manager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_BDD_PLAYERS = "CREATE TABLE " + PLAYERS_TABLE + " ("
            + PLAYERS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLAYERS_COL_NAME + " TEXT NOT NULL, "
            + PLAYERS_COL_NUMBER + " TEXT NOT NULL, " + PLAYERS_COL_TEAM_ID + " INTEGER);\n";

    private static final String CREATE_BDD_TEAMS = "CREATE TABLE " + TEAMS_TABLE + " ("
            + TEAMS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEAMS_COL_NAME + " TEXT NOT NULL);\n";

    private static final String CREATE_BDD_MATCHS = "CREATE TABLE " + MATCHS_TABLE + " ("
            + MATCHS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MATCHS_COL_DATE + " DATE, "
            + MATCHS_COL_DATE_RDV + " DATE NOT NULL, " + MATCHS_COL_OPPONENT + " TEXT,"
            + MATCHS_COL_HOME + " BOOLEAN," + MATCHS_COL_TEAM_ID + " INTEGER);\n";

    private static final String CREATE_BDD_INVITATIONS = "CREATE TABLE " + INVITATION_TABLE + " ("
            + INVITATION_COL_PLAYER_ID + " INTEGER, " + INVITATION_COL_MATCH_ID + " INTEGER, "
            + INVITATION_COL_RESPONSE + " BOOLEAN);\n";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD_PLAYERS);
        db.execSQL(CREATE_BDD_TEAMS);
        db.execSQL(CREATE_BDD_MATCHS);
        db.execSQL(CREATE_BDD_INVITATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + PLAYERS_TABLE + ";");
        db.execSQL("DROP TABLE " + TEAMS_TABLE + ";");
        db.execSQL("DROP TABLE " + MATCHS_TABLE + ";");
        db.execSQL("DROP TABLE " + INVITATION_TABLE + ";");
        onCreate(db);
    }
}

