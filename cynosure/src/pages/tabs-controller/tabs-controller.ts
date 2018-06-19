import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { MomentsPage } from '../moments/moments';
import { MapsPage } from '../maps/maps';
import { SettingsPage } from '../settings/settings';

@Component({
  selector: 'page-tabs-controller',
  templateUrl: 'tabs-controller.html'
})
export class TabsControllerPage {

  tab1Root: any = MomentsPage;
  tab2Root: any = MapsPage;
  tab3Root: any = SettingsPage;
  constructor(public navCtrl: NavController) {
  }
  goToMoments(params){
    if (!params) params = {};
    this.navCtrl.push(MomentsPage);
  }goToMaps(params){
    if (!params) params = {};
    this.navCtrl.push(MapsPage);
  }
}
