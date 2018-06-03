import { Injectable } from '@angular/core';
import { Contacts, Contact, IContactField, ContactFieldType, ContactFindOptions } from '@ionic-native/contacts';

@Injectable()
export class ContactRepository {

    constructor(private contacts: Contacts) {}

    createContact(team: Contact): Promise<void> {
        return;
    }

    findContact(id: string): Promise<Contact[]> {
      return this.contacts.find(['id'], {filter: id, multiple: false});
    }

    findContactsByName(name: string): Promise<Contact[]> {
      let fields: ContactFieldType[] = ['displayName', 'name.familyName',
                                        'nickname', 'name.givenName',
                                        'name.middleName', 'name.formatted'];
      return this.contacts.find(fields, {filter: name, multiple: true});
    }

    getAllContacts(): Promise<Contact[]> {
      return this.contacts.find(['displayName']);
    }
}
