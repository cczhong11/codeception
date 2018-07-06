import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Events } from 'ionic-angular';
import { HTTP } from '@ionic-native/http';
import { Camera, CameraOptions } from '@ionic-native/camera';
import { Geolocation } from '@ionic-native/geolocation';
import { StatusBar } from '@ionic-native/status-bar';
import { MediaCapture, MediaFile, CaptureError, CaptureVideoOptions } from '@ionic-native/media-capture';
import { Transfer, TransferObject, FileUploadOptions } from '@ionic-native/transfer';
import { FileChooser } from '@ionic-native/file-chooser';
import { FileOpener } from '@ionic-native/file-opener';
import { File } from '@ionic-native/file';
import { ProgressBarComponent } from '../../components/progress-bar/progress-bar';

@Component({
  selector: 'page-camera',
  templateUrl: 'camera.html'
})
  
export class CameraPage {
  data: any = {};
  image: any = {};
  videoID: any;
  fileData: any;
  loadProgress: any = 0;

  constructor(public navCtrl: NavController,
    public events: Events,
    private http: HTTP,
    private camera: Camera,
    private geolocation: Geolocation,
    private statusBar: StatusBar,
    public fileChooser: FileChooser,
    private transfer: Transfer,
    private mediaCapture: MediaCapture,
    private fileOpener: FileOpener,
    private file: File) {
  }

  public photos: any;
  public base64Image: string;

  ngOnInit() {
    this.photos = [];
  }

  goToPicture(params) {
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
      this.loadProgress = 0;
      this.base64Image = "data:image/jpeg;base64," + imageData;
      //this.base64Image = imageData;
      // this.photos.push(this.base64Image);
      // this.photos.reverse();
      if (this.base64Image !== null) {
        //http post update here
        this.statusBar.overlaysWebView(true);
        var link = 'https://codeception.azurewebsites.net/uploadImage';
        let payload = this.base64Image;
        
        let headers = { 'Content-Type': 'text' };
        this.http.setDataSerializer('utf8');
        this.loadProgress = 20;

        this.http.post(link, payload, headers)
          .then(data => {
            let lat = 80;
            let lng = 80;
            console.log("Success!");
            console.log(data);
            this.geolocation.getCurrentPosition().then((resp) => {
              this.loadProgress = 60;
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

              this.http.post('https://codeception.azurewebsites.net/upload', uploadPayload, uploadHeader)
                .then(data => {
                  this.loadProgress = 100;
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
////////////////////////////////////////////////////////////////////////////////////////////////////
  }
  goToVideo(params) {
    if (!params) params = {};
    console.log("Video Time!");

    let options: CaptureVideoOptions = { limit: 1, duration: 10 };

    this.mediaCapture.captureVideo(options)
      .then((VideoData: MediaFile[]) => {

      this.loadProgress = 0;
      console.log(VideoData)
      if (VideoData !== null) {
        var i, path, len, fname;

        for (i = 0, len = VideoData.length; i < len; i += 1) {
          path = VideoData[i].fullPath;
          fname = VideoData[i].name;
        }

        console.log(path);

        this.videoID = path;
        this.statusBar.overlaysWebView(true);
        var link = 'https://cynosurenode.azurewebsites.net';

        let payload = VideoData;
        console.log("THIS IS THE UPLOADED VIDEO DATA");
        console.log(VideoData);

        console.log(fname);
        console.log(this.file.tempDirectory);

        var videoHeaders = {"content-type": "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW"};
        this.loadProgress = 20;

        this.http.uploadFile(link, {}, {"content-type": "multipart/form-data"}, "file://" + path, "test")
          .then(fileData => {
          console.log('File uploaded');
          console.log(fileData.data);
          console.log(String.fromCharCode.apply(null, new Uint16Array(this.fileData)));
          let vidHeader = { 'Content-Type': 'video/quicktime' };
          let headers = { 'Content-Type': 'text' };
          this.http.setDataSerializer('utf8');
          let lat = 80;
          let lng = 80;
          console.log("Success!");
          console.log(fileData.data);
          this.loadProgress = 40;

          this.geolocation.getCurrentPosition().then(resp => {
            this.loadProgress = 60;
            lat = resp.coords.latitude;
            lng = resp.coords.longitude;
            console.log(lat, lng);

            let uploadPayload = {
              x: lng,
              y: lat,
              username: "mmds",
              filelink: fileData.data
            }

            console.log(uploadPayload);

            let uploadHeader = { 'Content-Type': 'text' };
            this.http.setDataSerializer('json');

            this.http.post('https://codeception.azurewebsites.net/upload', uploadPayload, uploadHeader)
              .then(data => {
              this.loadProgress = 100;
              console.log("Uploaded to CosmosDB");
              })
              .catch(error => {
              console.log("Oooooooooooops!");
              });
            //Call to your logic HERE
            console.log("End of success location");
          }).catch((error) => {
            alert(error);
          });
          console.log("After getCurrentPosition()");
        })
        .catch(e => console.log('Error opening file', e));
          //http post update here
        }
      },
    (err: CaptureError) => console.error(err)
    );
  }
}
