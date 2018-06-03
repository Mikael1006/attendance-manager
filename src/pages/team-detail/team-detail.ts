import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { PickContactsPage } from '../pages'

@IonicPage()
@Component({
  selector: 'page-team-detail',
  templateUrl: 'team-detail.html',
})
export class TeamDetailPage {

  PickContactsPage : any = PickContactsPage;

  constructor(public navCtrl: NavController,
      public navParams: NavParams) {
  }
}
