
export class MySQLiteHelper {

    public static readonly PLAYERS_TABLE : string = "Players";
    public static readonly PLAYERS_COL_ID_CONTACT : string = "idContact";
    public static readonly PLAYERS_COL_TEAM_ID : string = "idTeam";

    public static readonly TEAMS_TABLE : string = "Teams";
    public static readonly TEAMS_COL_ID : string = "_id";
    public static readonly TEAMS_COL_NAME : string = "Name";

    public static readonly MATCHS_TABLE : string = "Matchs";
    public static readonly MATCHS_COL_ID : string = "_id";
    public static readonly MATCHS_COL_DATE : string = "Date";
    public static readonly MATCHS_COL_DATE_RDV : string = "DateRDV";
    public static readonly MATCHS_COL_OPPONENT : string = "Adversaire";
    public static readonly MATCHS_COL_HOME : string = "Domicile";
    public static readonly MATCHS_COL_TEAM_ID : string = "idTeam";

    public static readonly INVITATION_TABLE : string = "Invitations";
    public static readonly INVITATION_COL_PLAYER_ID : string = "idPlayer";
    public static readonly INVITATION_COL_MATCH_ID : string = "idMatch";
    public static readonly INVITATION_COL_RESPONSE : string = "Response";

    public static readonly DATABASE_NAME : string = "attendance_manager.db";

    public static readonly CREATE_TABLE_PLAYERS : string = "CREATE TABLE IF NOT EXISTS " + MySQLiteHelper.PLAYERS_TABLE + " ("
            + MySQLiteHelper.PLAYERS_COL_ID_CONTACT + " TEXT NOT NULL, " + MySQLiteHelper.PLAYERS_COL_TEAM_ID + " INTEGER);\n";

    public static readonly CREATE_TABLE_TEAMS : string = "CREATE TABLE IF NOT EXISTS " + MySQLiteHelper.TEAMS_TABLE + " ("
            + MySQLiteHelper.TEAMS_COL_ID + " INTEGER, " + MySQLiteHelper.TEAMS_COL_NAME + " TEXT NOT NULL);\n";

    public static readonly CREATE_TABLE_MATCHS : string = "CREATE TABLE IF NOT EXISTS " + MySQLiteHelper.MATCHS_TABLE + " ("
            + MySQLiteHelper.MATCHS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MySQLiteHelper.MATCHS_COL_DATE + " DATE, "
            + MySQLiteHelper.MATCHS_COL_DATE_RDV + " DATE NOT NULL, " + MySQLiteHelper.MATCHS_COL_OPPONENT + " TEXT,"
            + MySQLiteHelper.MATCHS_COL_HOME + " BOOLEAN," + MySQLiteHelper.MATCHS_COL_TEAM_ID + " INTEGER);\n";

    public static readonly CREATE_TABLE_INVITATIONS : string = "CREATE TABLE IF NOT EXISTS " + MySQLiteHelper.INVITATION_TABLE + " ("
            + MySQLiteHelper.INVITATION_COL_PLAYER_ID + " INTEGER, " + MySQLiteHelper.INVITATION_COL_MATCH_ID + " INTEGER, "
            + MySQLiteHelper.INVITATION_COL_RESPONSE + " BOOLEAN);\n";
}
