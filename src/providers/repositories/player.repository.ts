import { Injectable } from '@angular/core';
import { Team } from '../../models/team.model';
import { Player } from '../../models/player.model';
import { DatabaseRepository } from "./database.repository";
import { MySQLiteHelper } from '../../utils/MySQLiteHelper';
import * as squel from 'squel';

@Injectable()
export class PlayerRepository {

    constructor(private databaseRepository: DatabaseRepository) {}

    async addPlayerToTeam(team: Team, player: Player): Promise<void> {
        await this.databaseRepository.open();
        let sqlcommand = squel.insert()
        .into(MySQLiteHelper.PLAYERS_TABLE)
        .set(MySQLiteHelper.PLAYERS_COL_TEAM_ID, team.id)
        .set(MySQLiteHelper.PLAYERS_COL_ID_CONTACT, player.id)
        .toString();
        await this.databaseRepository.getDb().executeSql(sqlcommand, {});
    }

    async addPlayersToTeam(team: Team, players: Player[]): Promise<void> {
        await this.databaseRepository.open();
        let fields = [];
        players.forEach((player: Player) => {
          let field = new Object;
          field[MySQLiteHelper.PLAYERS_COL_TEAM_ID] = team.id;
          field[MySQLiteHelper.PLAYERS_COL_ID_CONTACT] = player.id;
          fields.push(field);
        });
        let sqlcommand = squel.insert()
        .into(MySQLiteHelper.PLAYERS_TABLE)
        .setFieldsRows(fields)
        .toString();
        await this.databaseRepository.getDb().executeSql(sqlcommand, {});
    }

    async getAllContactIdsByTeam(team : Team): Promise<string[]> {
        await this.databaseRepository.open();
        let sqlcommand = squel.select()
        .from(MySQLiteHelper.PLAYERS_TABLE)
        .toString();
        let resultSet = await this.databaseRepository.getDb().executeSql(sqlcommand, {});
        var contactIds : string[] = [];
        for(let i =0; i < resultSet.rows.length; i++){
            let entity = resultSet.rows.item(i);
            contactIds.push(entity[MySQLiteHelper.PLAYERS_COL_ID_CONTACT]);
        }
        return contactIds;
    }

    async deletePlayerFromTeam(team: Team, player: Player): Promise<void> {
        await this.databaseRepository.open();
        var sqlcommand = squel.delete()
        .from(MySQLiteHelper.PLAYERS_TABLE)
        .where(MySQLiteHelper.PLAYERS_COL_TEAM_ID + " = ?", team.id)
        .where(MySQLiteHelper.PLAYERS_COL_ID_CONTACT + " = ?", player.id)
        .toString();
        await this.databaseRepository.getDb().executeSql(sqlcommand, {});
    }
}
