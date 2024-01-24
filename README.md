# photo-album-app


This application allows users to upload photos to the Amazon S3 platform. Then photo can be transfered to Google Photos while is accepted or reject it if needed.

### Features

- **Amazon S3 Integration:** 
  Users can easily upload photos to the Amazon S3 platform through the application interface.

- **Google Photos Integration:**
  Accepted photos, following the review process, are automatically transferred to the designated album on Google Photos.

- **Email Notifications:**
  Users get notification about status of reviewed photo.

- **MySQL Database Integration:**
  The application utilizes a MySQL database for efficient storage and retrieval of photo-related data.

## Configuration
### Google Photos

To configure the integration with Google Photos, go to <a href='https://developers.google.com/photos/library/guides/get-started-java'>this link</a>
and follow the provided instructions to generate a `credentials.json` file. Ensure that you place this file in the src/main/resources directory.

After launching the server, a URL will be displayed in the console logs. Open this URL in a web browser, where you will be prompted to log in to
your Google account and grant the required permissions for the integration. Once this process is successfully completed, a token will be stored
on the server, eliminating the need for repeated log-ins unless the token is manually revoked.

### Amazon S3

To configure the integration with Amazon S3, create a `credentials_S3.properties` file and place it in the src/main/resources directory.
This file should have an accessKey and secret. Example of this file:

```
accessKey="your s3 accesKey"
secret="your s3 secret"
```

Within the application.properties file, there is a required property `bucketName`, which defines the S3 bucket name
to push content from.

## Swagger

Swagger UI is available at `http://localhost:8080/swagger-ui/index.html#/`. It describes the webservice endpoints and allows usage directly in the browser.

