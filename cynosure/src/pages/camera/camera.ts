import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Events } from 'ionic-angular';

@Component({
  selector: 'page-camera',
  templateUrl: 'camera.html'
})
  
export class CameraPage {

  constructor(public navCtrl: NavController, public events: Events) {
  }
  goToPicture(params) {
    if (!params) params = {};
    console.log("Photo Time!");
    this.events.publish('user:login');
  } goToVideo(params) {
    if (!params) params = {};
    console.log("Video Time!");
  }

}
