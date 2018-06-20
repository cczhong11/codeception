import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { MomentsPage } from '../pages/moments/moments';
import { MapsPage } from '../pages/maps/maps';
import { SettingsPage } from '../pages/settings/settings';
import { TabsControllerPage } from '../pages/tabs-controller/tabs-controller';
import { CameraPage } from '../pages/camera/camera';
import { SignupPage } from '../pages/signup/signup';
import { LoginPage } from '../pages/login/login';
import { Camera } from '@ionic-native/camera';
import { BlobModule } from 'angular-azure-blob-service';


import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

@NgModule({
  declarations: [
    MyApp,
    MomentsPage,
    MapsPage,
    SettingsPage,
    TabsControllerPage,
    CameraPage,
    SignupPage,
    LoginPage
  ],
  imports: [
    BrowserModule,
    BlobModule.forRoot(),
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    MomentsPage,
    MapsPage,
    SettingsPage,
    TabsControllerPage,
    CameraPage,
    SignupPage,
    LoginPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    Camera,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}