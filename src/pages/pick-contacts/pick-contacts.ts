import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Platform } from 'ionic-angular';
import { PlayerService } from '../../providers/services/player.service';
import { Player } from '../../models/player.model';

@IonicPage()
@Component({
  selector: 'page-pick-contacts',
  templateUrl: 'pick-contacts.html',
})
export class PickContactsPage {
  groupedContacts = [];
  noContactFound: boolean = false;
  selectedContacts: Player[] = [];

  constructor(public navCtrl: NavController,
      public navParams: NavParams,
      public platform: Platform,
      private playerService: PlayerService) {
    this.playerService.getAllPlayers().then((contactsfound: Player[]) => {
      if(contactsfound.length == 0){
        this.noContactFound = true;
      }else{
        this.noContactFound = false;
      }
      this.groupedContacts = this.groupContacts(contactsfound);
    });
  }

  /**
   * Update the list of contacts
   */
  findContact(ev:any) {

    // mock if it's on browser
    if(this.platform.is('core')){
      let contacts : Player[] = []
      let contact1 : Player = new Player();
      contact1.name = 'Mika';
      contact1.preferedPhoneNumber = '0664002954';
      contacts.push(contact1);
      let contact2 : Player = new Player();
      contact2.name = '';
      contact2.preferedPhoneNumber = '0664002950';
      contacts.push(contact2);
      let contact3 : Player = new Player();
      contact3.name = 'Emilie';
      contact3.preferedPhoneNumber = '0664002951';
      contacts.push(contact3);
      this.groupedContacts = this.groupContacts(contacts);
    }
    else
    {
      this.playerService.findPlayersByName(ev.target.value).then((contactsfound: Player[]) => {
        if(contactsfound.length == 0){
          this.noContactFound = true;
        }else{
          this.noContactFound = false;
        }
        this.groupedContacts = this.groupContacts(contactsfound);
      });
    }
  }

  /**
   * Group the list of contacts found by letters
   */
  private groupContacts(contacts: Player[]) : any[] {

    // sort contacts in alphabeltical order
    contacts .sort(function(a, b) {
      if(a.name.length==0) return 1;
      if(b.name.length==0) return -1;
      return a.name < b.name ? -1 : 1;
    });
    let currentLetter;
    let currentContacts;
    let groupedContacts = [];

    contacts.forEach((player: Player, index) => {
      if(player.name.length == 0){
        currentLetter = "Unnamed";

        let newGroup = {
          letter: currentLetter,
          contacts: []
        };

        currentContacts = newGroup.contacts;
        groupedContacts.push(newGroup);
      }else if(player.name.charAt(0).toUpperCase() != currentLetter){
        currentLetter = player.name.charAt(0).toUpperCase();

        let newGroup = {
          letter: currentLetter,
          contacts: []
        };

        currentContacts = newGroup.contacts;
        groupedContacts.push(newGroup);
      }

      currentContacts.push({value: player, checked: false});
    });
    return groupedContacts;
  }
}
