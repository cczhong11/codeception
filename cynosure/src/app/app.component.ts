import { Component, ViewChild } from '@angular/core';
import { Platform, Nav } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { Events } from 'ionic-angular';

import { LoginPage } from '../pages/login/login';
import { SignupPage } from '../pages/signup/signup';
import { CameraPage } from '../pages/camera/camera';

import { TabsControllerPage } from '../pages/tabs-controller/tabs-controller';



@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) navCtrl: Nav;
    rootPage:any = TabsControllerPage;

  constructor(platform: Platform,
    statusBar: StatusBar,
    splashScreen: SplashScreen,
    public events: Events) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });

    // events.subscribe('user:login', () => {
    //   this.uploadPicture();
    // });
  }

  uploadPicture() {

  } uploadVideo(params) {
    if (!params) params = {};
    console.log("Video Time!");
  }
  ////////////////////////////////////////////

  goToLogin(params){
    if (!params) params = {};
    this.navCtrl.setRoot(LoginPage);
  } goToSignup(params){
    if (!params) params = {};
    this.navCtrl.setRoot(SignupPage);
  } goToMoments(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(TabsControllerPage);
  } goToCamera(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(CameraPage);
  }
}
