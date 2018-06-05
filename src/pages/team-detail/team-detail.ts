import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { PickContactsPage } from '../pages';
import { Player } from '../../models/player.model';

@IonicPage()
@Component({
  selector: 'page-team-detail',
  templateUrl: 'team-detail.html',
})
export class TeamDetailPage {

  PickContactsPage : any = PickContactsPage;
  selectedContacts: Player[] = [];

  constructor(public navCtrl: NavController,
      public navParams: NavParams) {
  }

  pickContacts(){
    var self = this;
    let passSelectedContacts = function(selectedContacts: Player[]) {
     return new Promise((resolve, reject) => {
       self.selectedContacts = selectedContacts;
       resolve();
      });
     }
    console.log(typeof(passSelectedContacts));
    this.navCtrl.push(PickContactsPage,{
        passSelectedContacts: passSelectedContacts
    });
  }
}
