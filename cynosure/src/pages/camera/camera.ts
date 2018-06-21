import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Events } from 'ionic-angular';
import { HTTP } from '@ionic-native/http';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { Geolocation } from '@ionic-native/geolocation';

@Component({
  selector: 'page-camera',
  templateUrl: 'camera.html'
})
  
export class CameraPage {
  data: any = {};
  image: any = {};

  constructor(public navCtrl: NavController,
    public events: Events,
    private http: HTTP,
    private camera: Camera,
    private geolocation: Geolocation) {
  }

  public photos: any;
  public base64Image: string;

  ngOnInit() {
    this.photos = [];
  }

  goToPicture(params) {
    //define cam options
    const options: CameraOptions = {
      quality: 100, // picture quality
      destinationType: this.camera.DestinationType.DATA_URL,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE,
      correctOrientation: true
    }
    console.log("Picture Time!");

    this.camera.getPicture(options).then((imageData) => {
      this.base64Image = "data:image/jpeg;base64," + imageData;
      //this.base64Image = imageData;
      // this.photos.push(this.base64Image);
      // this.photos.reverse();
      if (this.base64Image !== null) {
        //http post update here
        //var link = 'https://codeception.azurewebsites.net/uploadImage';
        var link = 'http://127.0.0.1:3000/uploadImage';
        let payload = this.base64Image;
        
        let headers = { 'Content-Type': 'text' };
        this.http.setDataSerializer('utf8');

        this.http.post(link, payload, headers)
          .then(data => {
            let lat = 80;
            let lng = 80;
            console.log("Success!");
            console.log(data);
            this.geolocation.getCurrentPosition().then((resp) => {
              lat = resp.coords.latitude;
              lng = resp.coords.longitude;
              console.log(lat, lng);

              let uploadPayload = {
                x: lng,
                y: lat,
                username: "mmds",
                filelink: 'https://cynosure.blob.core.windows.net/cynosure/' + data.data
              }

              console.log(uploadPayload);

              let uploadHeader = { 'Content-Type': 'text' };
              this.http.setDataSerializer('json');

              //this.http.post('https://codeception.azurewebsites.net/upload', uploadPayload, uploadHeader)
              this.http.post('http://127.0.0.1:3000/upload', uploadPayload, uploadHeader)
                .then(data => {
                  console.log("Uploaded to CosmosDB");
                })
                .catch(error => {
                  console.log("Oooooooooooops!");
                });
              //Call to your logic HERE
            }).catch((error) => {
              alert(error);
              });
          })
          .catch( error => {
            console.log("Oooops!");
          });
      }
    }, (err) => {
      console.log(err);
      });
    //this.events.publish('user:login');
  } goToVideo(params) {
    if (!params) params = {};
    console.log("Video Time!");
  }
}
