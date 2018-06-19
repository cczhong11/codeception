import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Camera, CameraOptions } from '@ionic-native/camera';

@Component({
  selector: 'page-camera',
  templateUrl: 'camera.html'
})
export class CameraPage {
  public photos: any;
  public base64Image: string;
  
  constructor(public navCtrl: NavController, private camera: Camera) {
  }
  goToPicture(params) {

    //define cam options
    const options: CameraOptions = {
      quality: 50, // picture quality
      destinationType: this.camera.DestinationType.DATA_URL,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE
    }
    
    if (!params) params = {};
    console.log("Picture Time!");
    this.camera.getPicture(options).then((imageData) => {
      this.base64Image = "data:image/jpeg;base64," + imageData;
      this.photos.push(this.base64Image);
      this.photos.reverse();
    }, (err) => {
      console.log(err);
    });
    
  } goToVideo(params) {
    if (!params) params = {};
    console.log("Video Time!");
  } 
  
  
}
