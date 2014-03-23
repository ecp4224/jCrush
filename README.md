#jCrush [![Build Status](https://drone.io/github.com/hypereddie10/jCrush/status.png)](https://drone.io/github.com/hypereddie10/jCrush/latest)
A java library for [mediacru.sh][1]



##What is jCrush?
jCrush is a java library that exposes the HTTP API that [mediacru.sh][2] offers.


##Building and Testing
JUnit 4 is used for unit testing and maven is used for building
So just run 'mvn clean install' in the project's root directory to complie and test jCrush

##Requesting new Features
If this library ever gets out-of-date due to API updates to [mediacru.sh][1], simply open up an issue in the issue track with your feature request and support will be considered and added. Or, you could always fork and pull request.

##Usage and Examples

###File Retrieving
**Exposes** https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihash
```java
  try {
      MediaCrushFile file = JCrush.getFileInfo("CPvuR5lRhmS0");
  } catch (IOException e) {
      //File retrieval failed..
	  return;
  }
  double compression = file.getCompression(); //Get compression
  
  CrushedFile orginalFile = file.getOriginalFile(); //Get the original file
  String file = originalFile.getFile();
  FileType type = orginalFile.getFileType(); //Returns a jcrush.model.FileType enum
  
  CrushedFile[] files = file.getFiles(); //Returns an array of crushed files
  
  String hash = file.getHash(); //Get the file hash
```

You can also add unlimited parameters using the getFileInfos method
```java
  try {
      MediaCrushFile[] files = JCrush.getFileInfos("CPvuR5lRhmS0", "tVWMM_ziA3nm", ...);
  } catch (IOException e) {
      //File retrieval failed..
	  return;
  }
```

**See Also:** [JCrush.getFile][3], [JCrush.getFiles][4]

###File Status
**Exposes** https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashstatus
```java
  try {
      MediaCrushFile file = JCrush.getFileStatus("CPvuR5lRhmS0");
  } catch (IOException e) {
      //File retrieval failed..
	  return;
  }
  FileStatus = file.getStatus(); //Returns a jcrush.model.FileStatus enum
```

**See Also:** [JCrush.getFile][3], [JCrush.getFiles][4]

###Checking if a file exists
**Exposes** https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashexists
```java
  try {
      boolean result = JCrush.doesExist("CPvuR5lRhmS0");
  } catch (IOException e) {
      //Checking failed, server might be offline..
	  return;
  }
  if (result == true) {
     //The file does exist
  }
```

**See Also:** [JCrush.getFile][3], [JCrush.getFiles][4]

###File Uploading via files
**Exposes** https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apiuploadfile
```java
  File f = new File("myFile.gif");
  try {
      String newHash = JCrush.uploadFile(f);
  } catch (IOException e) {
      //File uploading failed..
	  return;
  } catch (FileUploadFailedException e) {
      //File failed to upload
      //This exception is only thrown for one of the reasons specified in the API documentation
      return;
  }
  
  //OR
  try {
      String newHash = JCrush.uploadFile("myImage.png");
  } catch (IOException e) {
      //File uploading failed..
	  return;
  } catch (FileUploadFailedException e) {
      //File failed to upload
      //This exception is only thrown for one of the reasons specified in the API documentation
      return;
  }
```

###File Uploading via URL
**Exposes** https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apiuploadurl
```java
  try {
      String newHash = JCrush.uploadFileViaURL("http://www.example.com/myimage.gif");
  } catch (IOException e) {
      //File uploading failed..
	  return;
  } catch (FileUploadFailedException e) {
      //File failed to upload
      //This exception is only thrown for one of the reasons specified in the API documentation
      return;
  }
  
  //OR
  try {
      URL url = new URL("http://www.example.com/myimage.gif");
      String newHash = JCrush.uploadFileViaURL(url);
  } catch (IOException e) {
      //File uploading failed..
	  return;
  } catch (FileUploadFailedException e) {
      //File failed to upload
      //This exception is only thrown for one of the reasons specified in the API documentation
      return;
  }
```

###File Deleting
**Exposes** https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashdelete
```java
  try {
      JCrush.delete("CPvuR5lRhmS0");
  } catch (IOException e) {
      //File deletion failed..
	  return;
  }
```

###getFile (convenience method)
**Exposes:**

https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihash
https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashstatus
https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashexists
```java
  try {
      MediaCrushFile file = JCrush.getFile("CPvuR5lRhmS0");
  } catch (IOException e) {
      //File retrieval failed..
	  return;
  }
  double compression = file.getCompression(); //Get compression
  
  CrushedFile orginalFile = file.getOriginalFile(); //Get the original file
  String file = originalFile.getFile();
  FileType type = orginalFile.getFileType(); //Returns a jcrush.model.FileType enum
  
  CrushedFile[] files = file.getFiles(); //Returns an array of crushed files
  
  String hash = file.getHash(); //Get the file hash
  FileStatus = file.getStatus(); //Returns a jcrush.model.FileStatus enum
```

**Notes:** This is a convenience method. It returns a MediaCrushFile object with all info attached and does not throw an exception 
when the file does not exist. When the hash specified does not exist, this method simply returns null. However, an exception
may be thrown if the server responds in an abnormal way.

###getFiles (convenience method)
**Exposes:**

https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihash
https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashstatus
https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashexists
```java
  MediaCrushFile[] files = JCrush.getFiles("CPvuR5lRhmS0", "tVWMM_ziA3nm", ...);
```

**Notes:** This is a convenience method. It returns an array of MediaCrushFile objects with all info attached and does not throw an exception 
when the file does not exist. It allows infinate amount of parameters, and also allows a String array to be passed. 
When one of the hash's specified does not exist, it's value in the array is just set to null. Unlike the getFile method, this method never
throws an exception, if an exception was thrown, then it's value in the array is set to null.

###Configuring JCrush


####Changing the Server API URL
Change the server URL where the API resides. By default,
it uses https://www.mediacru.sh/api
```java
  JCrush.changeApiURL("https://www.mediacru.sh/api/"); // Your server URL here
```

####Setting JCrush to be async
Coming soon


##License
This project is Open Source under the MIT Open Source License.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


[1]: https://github.com/MediaCrush/MediaCrush
[2]: https://mediacru.sh/docs/API
[3]: https://github.com/hypereddie10/jCrush#getfile-convenience-method
[4]: https://github.com/hypereddie10/jCrush#getfiles-convenience-method
