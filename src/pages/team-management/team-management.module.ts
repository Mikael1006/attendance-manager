import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TeamManagementPage } from './team-management';
import { TranslateModule } from "@ngx-translate/core";

@NgModule({
  declarations: [
    TeamManagementPage,
  ],
  imports: [
    IonicPageModule.forChild(TeamManagementPage),
    TranslateModule.forChild()
  ],
})
export class TeamManagementPageModule {}
