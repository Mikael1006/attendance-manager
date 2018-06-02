import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController, ToastController } from 'ionic-angular';
import { TranslateService } from "@ngx-translate/core";
import { TeamService } from "../../providers/services/team.service";
import { Team } from "../../models/team.model";

@IonicPage()
@Component({
  selector: 'page-team-create',
  templateUrl: 'team-create.html',
})
export class TeamCreatePage {
  team: Team;
  addError: string;
  addSuccess: string;

  constructor(private viewCtrl: ViewController,
              private translateService: TranslateService,
              public navCtrl: NavController,
              public navParams: NavParams,
              private teamService: TeamService,
              private toastCtrl: ToastController) {

    this.translateService.get('SERVER_ERROR').subscribe((value) => {this.addError = value;});
    this.translateService.get('TEAM_CREATE_SUCCESS').subscribe((value) => {this.addSuccess = value;});
    this.team = new Team();
  }

  /**
   * The user cancelled, so we dismiss without sending data back.
   */
  cancel() {
    this.viewCtrl.dismiss();
  }

  /**
   * The user is done and wants to create the item, so return it
   * back to the presenter.
   */
  done() {
    this.teamService.createTeam(this.team).then(
      () => {
        this.printToast(this.addSuccess);
        this.viewCtrl.dismiss();
      },
      (reason : any) => this.printToast(this.addError)
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
}
