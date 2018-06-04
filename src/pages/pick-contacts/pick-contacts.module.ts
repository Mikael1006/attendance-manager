import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PickContactsPage } from './pick-contacts';
import { TranslateModule } from "@ngx-translate/core";

@NgModule({
  declarations: [
    PickContactsPage,
  ],
  imports: [
    IonicPageModule.forChild(PickContactsPage),
    TranslateModule.forChild()
  ],
})
export class PickContactsPageModule {}
