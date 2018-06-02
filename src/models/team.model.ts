import { BaseEntity } from "./base-entity";

export class Team implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}
