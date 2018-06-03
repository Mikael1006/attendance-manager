import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PickContactsPage } from './pick-contacts';

@NgModule({
  declarations: [
    PickContactsPage,
  ],
  imports: [
    IonicPageModule.forChild(PickContactsPage),
  ],
})
export class PickContactsPageModule {}
