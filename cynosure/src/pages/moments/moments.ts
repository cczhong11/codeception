import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HTTP } from '@ionic-native/http';
import { Geolocation } from '@ionic-native/geolocation';
import { StreamingMedia, StreamingVideoOptions } from '@ionic-native/streaming-media';

import {
  Injectable,
  Injector,
  ComponentFactoryResolver,
  EmbeddedViewRef,
  ApplicationRef
} from '@angular/core';
import { AlertController } from 'ionic-angular';

@Component({
  selector: 'page-moments',
  templateUrl: 'moments.html'
})
export class MomentsPage {

  constructor(public navCtrl: NavController,
    private http: HTTP,
    private geolocation: Geolocation,
    private componentFactoryResolver: ComponentFactoryResolver,
    private appRef: ApplicationRef,
    private injector: Injector,
    private alertCtrl: AlertController,
    private streamingMedia: StreamingMedia) {
  }

  data: any = {};
  public arrayofkeys = [];

  ngOnInit() {
  }
  showmore(key){
    this.http.get(key.filelink, "", "")
        .then(data => {
            let alert; 
            if(key.filelink.includes('jpeg')){
                alert = this.alertCtrl.create({
                    title: 'Detail',
                    subTitle: '<img src='+data.data+'>',
                    buttons: ['Ok']
                  });
            }else{
              console.log("FULL DATA");
              console.log(data);
              console.log("DATA.DATA");
              console.log(data.data);
              console.log("DATA PROPERTIES");
              console.log(Object.getOwnPropertyNames(data));
              let options: StreamingVideoOptions = {
                successCallback: () => { console.log('Video played') },
                errorCallback: (e) => { console.log('Error streaming') },
                orientation: 'landscape'
              };
              let tempLink = key.filelink.replace('https', 'http');
              this.streamingMedia.playVideo(tempLink, options);
              let alertz; 
              alertz = this.alertCtrl.create({
                title: 'Detail',
                subTitle: '<img src='+data.data+'>',
                buttons: ['Ok']
              });
            }
              alert.present();
        });
  }
  doRefresh(refresher) {

    this.arrayofkeys = [];
    let lat = 80;
    let lng = 80;
    console.log('Begin async operation', refresher);

    this.geolocation.getCurrentPosition().then((resp) => {
      lat = resp.coords.latitude;
      lng = resp.coords.longitude;
      console.log(lat, lng);
      var link = 'https://codeception.azurewebsites.net/get';
      let payload = {
        "x": lng,
        "y": lat,
        "username": "mmds"
      };
      // let headers = new Headers({ 'Content-Type': 'application/json' });
      // let options = new RequestOptions({ headers: headers });
      let headers = { 'Content-Type': 'application/json' };
      this.http.setDataSerializer('json');
      this.http.post(link, payload, headers)
        .then(data => {
          console.log("Success!");
          console.log(data.status);
          console.log(data.data); // data received by server
          this.data = JSON.parse(data.data);
          console.log(this.data);
          console.log(this.data.data[0]);
          console.log("Number of entries inside Array " + Object.keys(this.data.data).length);
          var i: number = 0;
          for (i; i < Object.keys(this.data.data).length; i++) {
            console.log(this.data.data[i]);
            this.arrayofkeys.push(this.data.data[i]);
          }
          console.log("Data Headers", data.headers);
          console.log(this.arrayofkeys);
        })
        .catch(error => {
          console.log("Failed!");
          console.log(error.status);
          console.log(error.error); // error message as string
          console.log(error.headers);
        });
    }).catch((error) => {
      alert(error);
      });
    
    setTimeout(() => {
      console.log('Async operation has ended');
      refresher.complete();
    }, 2000);
  }
}
