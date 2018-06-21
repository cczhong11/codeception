import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HTTP } from '@ionic-native/http';
import { Geolocation } from '@ionic-native/geolocation';
import {
  Injector,
  ComponentFactoryResolver,
  ApplicationRef
} from '@angular/core';
import { StatusBar } from '@ionic-native/status-bar';

@Component({
  selector: 'page-moments',
  templateUrl: 'moments.html'
})
export class MomentsPage {

  constructor(public navCtrl: NavController,
    private http: HTTP,
    private geolocation: Geolocation,
    private componentFactoryResolver: ComponentFactoryResolver,
    private statusBar: StatusBar) {
  }

  data: any = {};
  public arrayofkeys = [];

  ngOnInit() {
    this.arrayofkeys = [];
    let lat = 80;
    let lng = 80;

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
          // console.log("Success!");
          // console.log(data.status);
          // console.log(data.data); // data received by server
          this.data = JSON.parse(data.data);
          // console.log(this.data);
          // console.log(this.data.data[0]);
          // console.log("Number of entries inside Array " + Object.keys(this.data.data).length);
          var i: number = 0;
          for (i; i < Object.keys(this.data.data).length; i++) {
            // console.log(this.data.data[i]);
            this.arrayofkeys.push(this.data.data[i]);
          }
          // console.log("Data Headers", data.headers);
          console.log(this.arrayofkeys);
          console.log(this.arrayofkeys.length);
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
          // console.log("Success!");
          // console.log(data.status);
          // console.log(data.data); // data received by server
          this.data = JSON.parse(data.data);
          // console.log(this.data);
          // console.log(this.data.data[0]);
          // console.log("Number of entries inside Array " + Object.keys(this.data.data).length);
          var i: number = 0;
          for (i; i < Object.keys(this.data.data).length; i++) {
            // console.log(this.data.data[i]);
            this.arrayofkeys.push(this.data.data[i]);
          }
          // console.log("Data Headers", data.headers);
          console.log(this.arrayofkeys);
          console.log(this.arrayofkeys.length);
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
    }, 6000);
  }
}
