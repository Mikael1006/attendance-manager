import { Injectable } from '@angular/core';
import { Team } from '../../models/team.model';
import { DatabaseRepository } from "./database.repository";
import { MySQLiteHelper } from '../../utils/MySQLiteHelper';
import * as squel from 'squel';

@Injectable()
export class TeamRepository {

    constructor(private databaseRepository: DatabaseRepository) {}

    async createTeam(team: Team): Promise<void> {
        await this.databaseRepository.open();
        let sqlcommand = squel.insert()
        .into(MySQLiteHelper.TEAMS_TABLE)
        .set(MySQLiteHelper.TEAMS_COL_NAME, team.name)
        .toString();
        await this.databaseRepository.getDb().executeSql(sqlcommand, {});
    }

    async updateTeam(team: Team): Promise<void> {
        await this.databaseRepository.open();
        let sqlcommand = squel.update()
        .table(MySQLiteHelper.TEAMS_TABLE)
        .set(MySQLiteHelper.TEAMS_COL_NAME, team.name)
        .toString();
        await this.databaseRepository.getDb().executeSql(sqlcommand, {});
    }

    async findTeam(id: number): Promise<Team> {
        await this.databaseRepository.open();
        let sqlcommand = squel.select()
        .from(MySQLiteHelper.TEAMS_TABLE)
        .where(MySQLiteHelper.TEAMS_COL_ID + " = ?", id)
        .toString();
        let resultSet = await this.databaseRepository.getDb().executeSql(sqlcommand, {});
        let entity = resultSet.rows.item(0);
        if(resultSet.rows.length > 0){
            return new Team(
              entity[MySQLiteHelper.TEAMS_COL_ID],
              entity[MySQLiteHelper.TEAMS_COL_NAME]
            );
        }
    }

    async getAllTeams(): Promise<Team[]> {
        await this.databaseRepository.open();
        let sqlcommand = squel.select()
        .from(MySQLiteHelper.TEAMS_TABLE)
        .toString();
        let resultSet = await this.databaseRepository.getDb().executeSql(sqlcommand, {});
        var teams : Team[] = [];
        for(let i =0; i < resultSet.rows.length; i++){
            let entity = resultSet.rows.item(i);
            teams.push(new Team(
              entity[MySQLiteHelper.TEAMS_COL_ID],
              entity[MySQLiteHelper.TEAMS_COL_NAME]
            ));
        }
        return teams;
    }

    async deleteTeam(id: number): Promise<void> {
        await this.databaseRepository.open();
        var sqlcommand = squel.delete()
        .from(MySQLiteHelper.TEAMS_TABLE)
        .where(MySQLiteHelper.TEAMS_COL_ID + " = ?", id)
        .toString();
        await this.databaseRepository.getDb().executeSql(sqlcommand, {});
    }
}
