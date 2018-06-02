import { Injectable } from '@angular/core';
import { SQLite, SQLiteObject } from '@ionic-native/sqlite';
import { MySQLiteHelper } from '../../utils/MySQLiteHelper'

@Injectable()
export class DatabaseRepository {

  private db : SQLiteObject;
  private promisedb : Promise<SQLiteObject>;

  constructor(protected sqlite: SQLite) {
    this.db = null;
    this.promisedb = this.sqlite
      .create({
        name: MySQLiteHelper.DATABASE_NAME,
        location: 'default'
      });
    this.promisedb.then((db: SQLiteObject) => {this.db = db;});
  }

  public open() : Promise<SQLiteObject>{
    return this.promisedb;
  }

  public getDb() : SQLiteObject {
    return this.db;
  }
}
