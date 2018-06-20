# codeception

An app help you know other associates photo based on GPS. 

## API

1. /upload
{x:xx,y:yy,user:name,file:filelink, tfilelink,**time:time**}

2. /get
{data:[{x:xx,y:yy,file:filelink,**time:time**},{x:xx,y:yy,file:filelink,**time:time**}....]}
3. uploadImage

## Backend 

- using undertow and CosmosDB to deploy backend service
- install maven and use 'mvn package' to compile code 
- run 'sudo java -cp target/backend.jar com.walmart.app.backend.MiniSite' to start server


### upload

- get post data
- connect cosmosDB using java sdk
- insert data

### get

- get post data
- connect cosmosDB and execute SQL to get data within 50m
- return json to user

## Frontend

- upload data to blob first
- upload file link to backend
- get data and download data from blob
