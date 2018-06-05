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
  contacts: {value: Player; checked: boolean;}[] = [];
  passSelectedContacts;


  constructor(public navCtrl: NavController,
      public navParams: NavParams,
      public platform: Platform,
      private playerService: PlayerService) {
    this.passSelectedContacts = this.navParams.get("passSelectedContacts");
    console.log(typeof(this.passSelectedContacts));

    // mock if it's on browser
    if(this.platform.is('core')){
      let contactsfound : Player[] = []
      let contact1 : Player = new Player();
      contact1.name = 'Mika';
      contact1.preferedPhoneNumber = '0664002954';
      contactsfound.push(contact1);
      let contact2 : Player = new Player();
      contact2.name = '';
      contact2.preferedPhoneNumber = '0664002950';
      contactsfound.push(contact2);
      let contact3 : Player = new Player();
      contact3.name = 'Emilie';
      contact3.preferedPhoneNumber = '0664002951';
      contactsfound.push(contact3);
      contactsfound.forEach((contact: Player, index) => {
        this.contacts.push({value: contact, checked: false});
      });
      this.groupedContacts = this.groupContacts(this.contacts);
    }
    else
    {
      this.playerService.getAllPlayers().then((contactsfound: Player[]) => {
        if(contactsfound.length == 0){
          this.noContactFound = true;
        }else{
          this.noContactFound = false;
        }
        contactsfound.forEach((contact: Player, index) => {
          this.contacts.push({value: contact, checked: false});
        });
        this.groupedContacts = this.groupContacts(this.contacts);
      });
    }
  }

  /**
   * Update the list of contacts
   */
  findContact(ev:any) {
    let contactsfound = this.contacts.filter(
      (contact) => {
        return contact.value.name.toUpperCase()
      .indexOf(ev.target.value.toUpperCase()) >= 0;
    });
    this.groupedContacts = this.groupContacts(contactsfound);
  }

  /**
   * The selection is validated and quit the page
   */
  done() {
    let selectedContacts = this.contacts.filter(
      (contact) => {
        return contact.checked;
    });
    let selectedValue: Player[] = [];
    selectedContacts.forEach((contact, index) => selectedValue.push(contact.value));
    console.log(selectedContacts.length);
    // pass the selected contacts to the previous page
    this.passSelectedContacts(selectedValue).then(()=> {
      this.navCtrl.pop();
    });
  }

  /**
   * Group the list of contacts found by letters
   */
  private groupContacts(contacts: {value: Player; checked: boolean;}[]) : any[] {

    // sort contacts in alphabeltical order
    contacts.sort(function(a, b) {
      if(a.value.name.length==0) return 1;
      if(b.value.name.length==0) return -1;
      return a.value.name < b.value.name ? -1 : 1;
    });
    let currentLetter;
    let currentContacts;
    let groupedContacts = [];

    contacts.forEach((contact: {value: Player; checked: boolean;}, index) => {
      if(contact.value.name.length == 0){
        currentLetter = "Unnamed";

        let newGroup = {
          letter: currentLetter,
          contacts: []
        };

        currentContacts = newGroup.contacts;
        groupedContacts.push(newGroup);
      }else if(contact.value.name.charAt(0).toUpperCase() != currentLetter){
        currentLetter = contact.value.name.charAt(0).toUpperCase();

        let newGroup = {
          letter: currentLetter,
          contacts: []
        };

        currentContacts = newGroup.contacts;
        groupedContacts.push(newGroup);
      }

      currentContacts.push(contact);
    });
    return groupedContacts;
  }
}
