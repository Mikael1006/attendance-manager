import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TeamCreatePage } from './team-create';
import { TranslateModule } from "@ngx-translate/core";

@NgModule({
  declarations: [
    TeamCreatePage,
  ],
  imports: [
    IonicPageModule.forChild(TeamCreatePage),
    TranslateModule.forChild()
  ],
})
export class TeamCreatePageModule {}
