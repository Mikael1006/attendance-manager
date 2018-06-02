import { Component } from '@angular/core';
import { Platform, Config } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { TranslateService } from '@ngx-translate/core';
import { FirstRunPage } from '../pages/pages';
import { DatabaseRepository } from "../providers/repositories/database.repository";
import { MySQLiteHelper } from '../utils/MySQLiteHelper'

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  rootPage:any = FirstRunPage;

  constructor(platform: Platform,
    statusBar: StatusBar,
    splashScreen: SplashScreen,
  private translate: TranslateService,
  private config: Config,
  private databaseRepository: DatabaseRepository
) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
      this.initDatabase().then(
        () => {console.log("Database initialized")},
        (reason : any) => {console.log(reason)}
      );
    });

    this.initTranslate();
  }

  initTranslate() {
    // Set the default language for translation strings, and the current language.
    this.translate.setDefaultLang('fr');
    this.translate.use('fr'); // Set your language here

    this.translate.get(['BACK_BUTTON_TEXT']).subscribe(values => {
      this.config.set('ios', 'backButtonText', values.BACK_BUTTON_TEXT);
    });
  }

  async initDatabase() : Promise<void> {
    await this.databaseRepository.open();
    await this.databaseRepository.getDb().executeSql(MySQLiteHelper.CREATE_TABLE_PLAYERS, {});
    await this.databaseRepository.getDb().executeSql(MySQLiteHelper.CREATE_TABLE_TEAMS, {});
    await this.databaseRepository.getDb().executeSql(MySQLiteHelper.CREATE_TABLE_MATCHS, {});
    await this.databaseRepository.getDb().executeSql(MySQLiteHelper.CREATE_TABLE_INVITATIONS, {});
  }

}
