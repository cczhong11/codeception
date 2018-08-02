// call the packages we need
var express = require('express'); // call express

var http = require('http');
var request = require("request");
var azure = require('azure-storage');
var multiparty = require('multiparty');
var formidable = require("formidable");
var multer = require('multer')
var MulterAzureStorage = require('multer-azure-storage')

var app = express(); // define our app using express

var port = process.env.PORT || 8080; // set our port

var upload = multer({
    storage: new MulterAzureStorage({
        azureStorageConnectionString: 'DefaultEndpointsProtocol=https;AccountName=cynosure;AccountKey=xxxxxxxxxxxxxxxxxxxxxxxxxx+yA==;EndpointSuffix=core.windows.net',
        azureStorageAccessKey: 'xxxxxxxxxxxxxxxxxx',
        azureStorageAccount: 'cynosure',
        containerName: 'cynosure',
        containerSecurity: 'blob'
    }),
    onError : function(err, next) {
        console.log('error', err);
        next(err);
      }
});

app.post('/', upload.any(), function(req, res) {
    // // get uploaded template file
    const payload = req.files;
    // Detect if file was sent
    if (undefined === payload) {
        res.status(400).json({ok:false, err:'No file uploaded'});
    }
    console.log(payload);
    console.log(payload[0].url);
    res.send(payload[0].url);
});

app.listen(port);