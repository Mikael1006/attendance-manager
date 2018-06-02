import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, AlertController, ModalController } from 'ionic-angular';
import { Team } from "../../models/team.model";
import { TeamService } from "../../providers/services/team.service";
import { TranslateService } from "@ngx-translate/core";
import { TeamCreatePage, TeamDetailPage, MainPage } from '../pages'

@IonicPage()
@Component({
  selector: 'page-team-management',
  templateUrl: 'team-management.html',
})
export class TeamManagementPage {
  teams: Team[];
  onError: string;
  alertTitle: string;
  doneButton: string;
  cancelButton: string;
  deleteTeamSuccess: string;
  teamUsedError: string;
  errorConnectDatabase: string;

  constructor(private modalCtrl: ModalController,
              public navCtrl: NavController,
              public navParams: NavParams,
              private teamService: TeamService,
              private toastCtrl: ToastController,
              private translateService: TranslateService,
              private alertCtrl: AlertController) {
    this.translateService.get('TEAM_MANAGEMENT_ON_ERROR').subscribe((value) => {this.onError = value;});
    this.translateService.get('DONE_BUTTON').subscribe((value) => {this.doneButton = value;});
    this.translateService.get('CANCEL_BUTTON').subscribe((value) => {this.cancelButton = value;});
    this.translateService.get('TEAM_MANAGEMENT_DELETE_SUCESS').subscribe((value) => {this.deleteTeamSuccess = value;});
    this.translateService.get('TEAM_MANAGEMENT_TEAM_USED_ERROR').subscribe((value) => {this.teamUsedError = value;});
    this.translateService.get('DATABASE_ERROR').subscribe((value) => {this.errorConnectDatabase = value;});
  }

  ionViewWillEnter() {
    this.loadAll();
  }

  // refresh the list of team
  doRefresh(refresher) {
    this.teamService.getAllTeams().then(
        (teams: Team[]) => {
            this.teams = teams;
            refresher.complete();
        },
        (reason: any) => {
          this.printToast(this.onError);
          refresher.complete();
        }
    );
  }

  addTeam(){
    let addModal = this.modalCtrl.create(TeamCreatePage);
    addModal.onDidDismiss(() => {
      this.loadAll();
    });
    addModal.present();
  }

  // Shows an alert, delete the team needs to be confirm
  deleteTeam(team: Team){
    this.translateService.get('TEAM_MANAGEMENT_DELETE_ALERT_TITLE', {team: team.name}).subscribe((value: string) => {this.alertTitle = value;});

    // create alert
    let alert = this.alertCtrl.create({
      title: this.alertTitle,
      buttons: [
        // cancel button
        {
          text: this.cancelButton,
          role: 'cancel'
        },
        // confirm button
        {
          text: this.doneButton,
          // delete the team
          handler: () => {
            // delete the team
            this.teamService.deleteTeam(team.id).then(
              // sucess
              () => {
                this.printToast(this.deleteTeamSuccess);

                // delete the team from the list
                const index: number = this.teams.indexOf(team);
                if (index !== -1) {
                  this.teams.splice(index, 1);
                }
              },
              // error
              (reason: any) => {
                console.log(reason);
                this.printToast(this.teamUsedError);
              }
            );
          }
        }
      ]
    });
    alert.present();
  }

  openTeam(team: Team){
    this.navCtrl.push(TeamDetailPage, {team})
  }

  loadAll() {
    this.teamService.getAllTeams().then(
        (teams: Team[]) => {
            this.teams = teams;
        },
        (reason: any) => {
          this.printToast(this.onError);
          console.log(reason);
        }
    );
  }

  private printToast(toastMessage) {
    let toast = this.toastCtrl.create({
      message: toastMessage,
      duration: 3000,
      position: 'top'
    });
    toast.present();
  }

  cancel(){
    this.navCtrl.setRoot(MainPage);
  }

}
