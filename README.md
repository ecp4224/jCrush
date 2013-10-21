#jCrush
A java library for [mediacru.sh][1]



##What is jCrush?
jCrush is a java library that exposes the HTTP API that [mediacru.sh][2] offers.


##Building and Testing
JUnit 4 is using for unit testing and maven is used for building
So just run 'mvn clean install' in the project's root directory.

##Usage and Examples
###File Retrieving
Exposes https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihash
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

See Also: [JCrush.getFile][3], [JCrush.getFiles][4]

###File Status
Exposes https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashstatus
```java
  try {
      MediaCrushFile file = JCrush.getFileStatus("CPvuR5lRhmS0");
  } catch (IOException e) {
      //File retrieval failed..
	  return;
  }
  FileStatus = file.getStatus(); //Returns a jcrush.model.FileStatus enum
```

See Also: [JCrush.getFile][3], [JCrush.getFiles][4]

###Checking if a file exists
Exposes https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashexists
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

###File Uploading via files
Exposes https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apiuploadfile
```java
  File f = new File("myFile.gif");
  String newHash = JCrush.uploadFile(f);
  
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
Exposes https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apiuploadurl
```java
  //Coming soon
```

###File Deleting
Exposes https://github.com/MediaCrush/MediaCrush/blob/master/docs/api.md#apihashdelete
```java
  try {
      JCrush.delete("CPvuR5lRhmS0");
  } catch (IOException e) {
      //File deletion failed..
	  return;
  }
```

###getFile (convenience method)
Exposes:
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

You can also add unlimited parameters using the getFileInfos method
```java
  try {
      MediaCrushFile[] files = JCrush.getFiles("CPvuR5lRhmS0", "tVWMM_ziA3nm", ...);
  } catch (IOException e) {
      //File retrieval failed..
	  return;
  }
```

Notes: These are a convenience method. They return a MediaCrushFile object with all info attached and does not throw an exception 
when the file does not exist. When the hash specified does not exist, this method simply returns null.
However, in getFile, an exception can be thrown if the server responded in an abnormal way. BUT, getFiles never throws an exception, it simply sets the file to null.


[1]: https://github.com/MediaCrush/MediaCrush
[2]: https://mediacru.sh/docs/API
