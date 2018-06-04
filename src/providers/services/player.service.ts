import { Injectable } from '@angular/core';
import { Player } from '../../models/player.model';
import { Contact } from '@ionic-native/contacts';
import { ContactRepository } from '../repositories/contact.repository';

@Injectable()
export class PlayerService {

    constructor(private contactRepository: ContactRepository) {}

    createPlayer(team: Player): Promise<void> {
        return;
    }

    async findPlayer(id: string): Promise<Player> {
      let player;
      let contacts = await this.contactRepository.findContact(id);
      if(contacts.length > 0){
        player = this.convertContactToPlayer(contacts[0]);
      }
      return player;
    }

    async findPlayersByName(name: string): Promise<Player[]> {
      let players: Player[] = [];
      let contacts = await this.contactRepository.findContactsByName(name);
      contacts.forEach((contact, index) => {
        players.push(this.convertContactToPlayer(contact));
      });
      return players;
    }

    async getAllPlayers(): Promise<Player[]> {
      let players: Player[] = [];
      let contacts = await this.contactRepository.getAllContacts();
      contacts.forEach((contact, index) => {
        players.push(this.convertContactToPlayer(contact));
      });
      return players;
    }

    private convertContactToPlayer(contact: Contact){
      let player: Player = new Player();

      let namesToGroupOn = ['displayName', 'name.familyName',
                            'nickname', 'name.givenName',
                            'name.middleName', 'name.formatted'];
      let contactName;
      for(let i = 0; i < namesToGroupOn.length && contactName == null; i++){
        contactName = contact[namesToGroupOn[i]];
      }
      if(contactName == null){
        contactName = '';
      }
      let phoneNumber;
      if(contact.phoneNumbers != null && contact.phoneNumbers.length > 0){
        // find the prefered phone number of the contact
        let prefered = contact.phoneNumbers.find(phoneNumber => phoneNumber.pref == true);
        phoneNumber = prefered == null ? contact.phoneNumbers[0] : prefered;
      }else{
        phoneNumber = {value: 'No phone number'};
      }

      player.name = contactName;
      player.preferedPhoneNumber = phoneNumber.value;
      player.phoneNumbers = contact.phoneNumbers;
      player.id = contact.id;

      return player;
    }
}
