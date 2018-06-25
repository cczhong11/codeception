import { Component, ViewChild, ElementRef } from '@angular/core';
import { IonicPage } from 'ionic-angular';
import { NavController } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';
import { HTTP } from '@ionic-native/http';
declare var google;
declare var MarkerClusterer;

@Component({
  selector: 'page-maps',
  templateUrl: 'maps.html'
})
export class MapsPage {
  @ViewChild('map') mapElement: ElementRef;
  map: any;
  data: any={};
  arrayofkeys:any=[];
  arrayofpos:any=[];
  pic:any=[];
  constructor(public navCtrl: NavController, public geolocation: Geolocation,private http: HTTP) {
 
  }
 
  ionViewDidLoad(){
    this.loadMap();
  }
 
  loadMap(){
 
    this.geolocation.getCurrentPosition().then((position) => {
      var lat = position.coords.latitude;
      var lng = position.coords.longitude;
      let latLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
      
      let mapOptions = {
        center: latLng,
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      }
 
      this.map = new google.maps.Map(this.mapElement.nativeElement, mapOptions);
      var marker = new google.maps.Marker({position: latLng, map: this.map});
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
          this.data = JSON.parse(data.data);
          var i: number = 0;
          this.arrayofkeys=[];
          this.arrayofpos=[];
          this.pic=[]
          for (i; i < Object.keys(this.data.data).length; i++) {
            console.log(this.data.data[i]);
            this.arrayofkeys.push(this.data.data[i].time);
            this.pic.push(this.data.data[i].raw);
            let latLng = new google.maps.LatLng(this.data.data[i]["y"], this.data.data[i]["x"]);
            this.arrayofpos.push(latLng);
          }
          console.log("Data Headers", data.headers);
          console.log(this.arrayofpos);
          console.log(this.arrayofkeys);
       
          var markers = this.arrayofpos.map(function(location, i) {
            var m = new google.maps.Marker({
              position: location,
              label: this.arrayofkeys[i]
            });
            let content = "<img src="+this.pic[i]+">";   
            this.addInfoWindow(m,content);
            return m;
          },this);
          var markerCluster = new MarkerClusterer(this.map, markers,
            {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
        
    }, (err) => {
      console.log(err);
    });
 
  });


}
addInfoWindow(marker, content){
 
  let infoWindow = new google.maps.InfoWindow({
    content: content
  });
 
  google.maps.event.addListener(marker, 'click', () => {
    infoWindow.open(this.map, marker);
  });
 
}
}