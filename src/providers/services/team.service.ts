import { Injectable } from '@angular/core';
import { Team } from '../../models/team.model';
import { TeamRepository } from "../repositories/team.repository";

@Injectable()
export class TeamService {

    constructor(private teamRepository: TeamRepository) {}

    createTeam(team: Team): Promise<void> {
        return this.teamRepository.createTeam(team);
    }

    updateTeam(team: Team): Promise<void> {
        return this.teamRepository.updateTeam(team);
    }

    findTeam(id: number): Promise<Team> {
        return this.teamRepository.findTeam(id);
    }

    getAllTeams(): Promise<Team[]> {
        return this.teamRepository.getAllTeams();
    }

    deleteTeam(id: number): Promise<void> {
        return this.teamRepository.deleteTeam(id);
    }
}
