import { BaseEntity } from "./base-entity";
import { IContactField } from '@ionic-native/contacts';

export class Player implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public preferedPhoneNumber?: string,
        public phoneNumbers?: IContactField[],
    ) {
    }
}
