import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ToastController, AlertController } from 'ionic-angular';
import { PickContactsPage } from '../pages';
import { Player } from '../../models/player.model';
import { Team } from '../../models/team.model';
import { PlayerService } from '../../providers/services/player.service';
import { TranslateService } from "@ngx-translate/core";

@IonicPage()
@Component({
  selector: 'page-team-detail',
  templateUrl: 'team-detail.html',
})
export class TeamDetailPage {

  PickContactsPage : any = PickContactsPage;
  players: Player[] = [];
  team: Team;
  onError: string;
  alertTitle: string;
  doneButton: string;
  cancelButton: string;
  deletePlayerSuccess: string;
  playerUsedError: string;
  errorConnectDatabase: string;

  constructor(public navCtrl: NavController,
      public playerService: PlayerService,
      private toastCtrl: ToastController,
      private translateService: TranslateService,
      private alertCtrl: AlertController,
      public navParams: NavParams) {
    this.translateService.get('PLAYER_MANAGEMENT_ON_ERROR').subscribe((value) => {this.onError = value;});
    this.translateService.get('DONE_BUTTON').subscribe((value) => {this.doneButton = value;});
    this.translateService.get('CANCEL_BUTTON').subscribe((value) => {this.cancelButton = value;});
    this.translateService.get('PLAYER_MANAGEMENT_DELETE_SUCESS').subscribe((value) => {this.deletePlayerSuccess = value;});
    this.translateService.get('PLAYER_MANAGEMENT_PLAYER_USED_ERROR').subscribe((value) => {this.playerUsedError = value;});
    this.translateService.get('DATABASE_ERROR').subscribe((value) => {this.errorConnectDatabase = value;});
    this.team = this.navParams.get("team");
  }

  ionViewWillEnter() {
    this.loadAll();
  }

  // refresh the list of team
  doRefresh(refresher) {
    this.playerService.getPlayersByTeam(this.team).then(
        (players: Player[]) => {
            this.players = players;
            refresher.complete();
        },
        (reason: any) => {
          this.printToast(this.onError);
          refresher.complete();
        }
    );
  }

  loadAll() {
    this.playerService.getPlayersByTeam(this.team).then(
        (players: Player[]) => {
            this.players = players;
        },
        (reason: any) => {
          this.printToast(this.onError);
          console.log(reason);
        }
    );
  }

  // Shows an alert, delete the team needs to be confirm
  deletePlayer(player: Player){
    this.translateService.get('PLAYER_MANAGEMENT_DELETE_ALERT_TITLE', {player: player.name}).subscribe((value: string) => {this.alertTitle = value;});

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
            this.playerService.deletePlayerFromTeam(this.team, player).then(
              // sucess
              () => {
                this.printToast(this.deletePlayerSuccess);

                // delete the team from the list
                const index: number = this.players.indexOf(player);
                if (index !== -1) {
                  this.players.splice(index, 1);
                }
              },
              // error
              (reason: any) => {
                console.log(reason);
                this.printToast(this.playerUsedError);
              }
            );
          }
        }
      ]
    });
    alert.present();
  }

  pickContacts(){
    var self = this;
    let passSelectedContacts = function(selectedContacts: Player[]) {
     return new Promise((resolve, reject) => {
       self.playerService.addPlayersToTeam(self.team, selectedContacts);
       resolve();
      });
     }
    this.navCtrl.push(PickContactsPage,{
        passSelectedContacts: passSelectedContacts
    });
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
    this.navCtrl.pop();
  }
}
