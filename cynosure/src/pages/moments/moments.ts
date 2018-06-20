import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HTTP } from '@ionic-native/http';

@Component({
  selector: 'page-moments',
  templateUrl: 'moments.html'
})
export class MomentsPage {

  constructor(public navCtrl: NavController, private http: HTTP) {
  }

  data: any = {};

  ngOnInit() {
  }

  doRefresh(refresher) {
    console.log('Begin async operation', refresher);
    var link = 'https://codeception.azurewebsites.net/get';
    let payload = {
      "x": 123.2,
      "y": 121.2,
      "username": "mmds",
      "filelink": "http://dsa"
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
        console.log(data.headers);
      })
      .catch(error => {
        console.log("Failed!");
        console.log(error.status);
        console.log(error.error); // error message as string
        console.log(error.headers);
      });
    
    setTimeout(() => {
      console.log('Async operation has ended');
      refresher.complete();
    }, 2000);
  }
}
