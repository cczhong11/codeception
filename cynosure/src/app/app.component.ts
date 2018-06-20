import { Component, ViewChild } from '@angular/core';
import { Platform, Nav } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { BlobService, UploadConfig, UploadParams } from 'angular-azure-blob-service'
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
    private camera: Camera,
    private blob: BlobService,
    public events: Events) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();
    });

    events.subscribe('user:login', () => {
      this.uploadPicture();
    });
  }

  //////////////
  //Photo Work//
  ////////////////////////////////////////////
  public photos: any;
  public base64Image: string;

  config: UploadConfig;
  percent: number;
  Config: UploadParams = {
    sas: '?sv=2017-11-09&ss=bfqt&srt=sco&sp=rwdlacup&se=2018-07-10T09:12:44Z&st=2018-06-20T01:12:44Z&spr=https&sig=fCOxOzcj%2BID8wZU5RDKfujylXMHGmeGlNPz9vQa0ndg%3D',
    storageAccount: 'cynosure',
    containerName: 'cynosure'
  };

  ngOnInit() {
    this.photos = [];
  }

  uploadPicture() {
    //define cam options
    const options: CameraOptions = {
      quality: 50, // picture quality
      destinationType: this.camera.DestinationType.DATA_URL,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE,
      correctOrientation: true
    }
    console.log("Picture Time!");

    this.camera.getPicture(options).then((imageData) => {
      this.base64Image = "data:image/png;base64," + imageData;
      //this.base64Image = imageData;
      this.photos.push(this.base64Image);
      this.photos.reverse();
      console.log(this.photos);
      if (this.photos !== null) {
        var f = new File([this.photos], "filename.png", { type: "image/png" });
        console.log("uploading");
        const baseUrl = this.blob.generateBlobUrl(this.Config, "filename.png");
        console.log(baseUrl);
        console.log(this.photos);
        this.config = {
          baseUrl: baseUrl,
          sasToken: this.Config.sas,
          blockSize: 1024 * 64, // OPTIONAL, default value is 1024 * 32
          file: f,
          complete: () => {
            console.log('Transfer completed !');
          },
          error: () => {
            console.log('Error !');
          },
          progress: (percent) => {
            this.percent = percent;
          }
        };
        this.blob.upload(this.config);
      }
    }, (err) => {
      console.log(err);
    });

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
