import { HttpClient, HttpClientModule} from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { MyApp } from './app.component';
import { DatabaseRepository } from '../providers/repositories/database.repository';
import { ContactRepository } from '../providers/repositories/contact.repository';
import { TeamService } from '../providers/services/team.service';
import { PlayerService } from '../providers/services/player.service';
import { TeamRepository } from "../providers/repositories/team.repository";
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { SQLite } from '@ionic-native/sqlite';
// import { SQLiteMock } from '../mocks/sqlite.mock';
import { Contacts } from '@ionic-native/contacts';

// The translate loader needs to know where to load i18n files
// in Ionic's static asset pipeline.
export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    MyApp
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp
  ],
  providers: [
    StatusBar,
    SplashScreen,
    DatabaseRepository,
    TeamRepository,
    ContactRepository,
    TeamService,
    PlayerService,
    Contacts,
    SQLite,
    // {provide: SQLite, useClass: SQLiteMock},
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}
