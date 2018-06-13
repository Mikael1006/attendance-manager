import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TeamDetailPage } from './team-detail';
import { TranslateModule } from "@ngx-translate/core";

@NgModule({
  declarations: [
    TeamDetailPage,
  ],
  imports: [
    IonicPageModule.forChild(TeamDetailPage),
    TranslateModule.forChild()
  ],
})
export class TeamDetailPageModule {}
