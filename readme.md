# mp3agic

A java library for reading mp3 files and reading / manipulating the ID3 tags (ID3v1 and ID3v2.2 through ID3v2.4).

See [mp3agic-examples](https://github.com/mpatric/mp3agic-examples "mp3agic-examples") for example applications that use this library - including a simple set of command-line tools that perform tasks like printing mp3 and ID3 details, renaming mp3 files using details from the ID3 tags, retagging mp3 files, attaching images to and extracting images from mp3 files.

This version of the library is not published to maven central yet. 
I won't do it unless mpatrick decides these changes shouldn't be merged, if he decides to merge them then wait for him to upload a new release.
In the mean time, clone the repo and install on the local maven repository using ```gradle install```.

# Warning
## This build is a custom build
Do NOT use unless you know what you are doing. That said, only 3 tests are failing (Removing custom tags) due to the fact
that the underlying file read technique changed from reading a file directly to operating on it entirely with an on-memory 
byte array.

This decouples the actual tag reading from where the mp3 data must come from, enabling usage on scenarios where data is not stored
on files, like the cloud or using asynchronous I/O.

Dropping in this version of the library instead of mpatrick's one **should** work, but i can't guarantee it
as i have not tested it myself (Unless you delete custom tags, as those tests are failing).
Mp3File has gained 3 new constructors that accept a byte array as a data source, the API hasn't changed in any other way.
This also means that the examples below will work with the ```byte[]``` constructors, but i'm lazy enough not to update them.

I have also added a gradle build file, just because gradle is awesome and maven is too much boilerplate to maintain.
The pom file has been wiped out as i do **not** plan to maintain a maven xml file for no benefit.

## Some features

* 100% Java
* read low-level mpeg frame data
* read, write, add and  ~~remove~~ ID3v1 and ID3v2 tags (ID3v2.3 and ID3v2.4)
* read obsolete 3-letter ID3v2.2 tags (but not write them)
* correctly read VBR files by looking at the actual mpeg frames
* read and write embedded images (such as album art)
* add or remove custom messages between the end of the mpeg frames and the ID3v1 tag
* unicode support

## Development

mp3agic ~~uses~~ used to employ various tools to ease the development process, now it's just gradle (At least for now, who knows...).


### Building

To build mp3agic, you will need:

* [JDK 6+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - Oracle or OpenJDK
* [Gradle](http://www.gradle.org/)

After installing these tools simply run 'gradle build' and find the jar in the target folder.

Other Useful gradle tasks:

* clean - remove binaries, docs and temporary build files
* build - compile and test the library

## How to use it

Some sample code follows for performing common operations; it is not an exhaustive list of all the functionality.
More can be learned from looking at the javadocs and the code itself, or at the examples in [mp3agic-examples](https://github.com/mpatric/mp3agic-examples).

### Opening an mp3 file

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
System.out.println("Bitrate: " + mp3file.getLengthInSeconds() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
```

### Saving an mp3 file

```java
mp3file.save("MyMp3File.mp3");
```

### Removing ID3 and custom tags from an mp3 file

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
if (mp3file.hasId3v1Tag()) {
  mp3file.removeId3v1Tag();
}
if (mp3file.hasId3v2Tag()) {
  mp3file.removeId3v2Tag();
}
if (mp3file.hasCustomTag()) {
  mp3file.removeCustomTag();
}
mp3file.save("Mp3FileWithoutTags.mp3");
```

### Getting ID3v1 values

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
if (mp3file.hasId3v1Tag()) {
  ID3v1 id3v1Tag = mp3file.getId3v1Tag();
  System.out.println("Track: " + id3v1Tag.getTrack());
  System.out.println("Artist: " + id3v1Tag.getArtist());
  System.out.println("Title: " + id3v1Tag.getTitle());
  System.out.println("Album: " + id3v1Tag.getAlbum());
  System.out.println("Year: " + id3v1Tag.getYear());
  System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
  System.out.println("Comment: " + id3v1Tag.getComment());
}
```

### Setting ID3v1 values

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
ID3v1 id3v1Tag;
if (mp3file.hasId3v1Tag()) {
  id3v1Tag =  mp3file.getId3v1Tag();
} else {
  // mp3 does not have an ID3v1 tag, let's create one..
  id3v1Tag = new ID3v1Tag();
  mp3file.setId3v1Tag(id3v1Tag);
}
id3v1Tag.setTrack("5");
id3v1Tag.setArtist("An Artist");
id3v1Tag.setTitle("The Title");
id3v1Tag.setAlbum("The Album");
id3v1Tag.setYear("2001");
id3v1Tag.setGenre(12);
id3v1Tag.setComment("Some comment");
    mp3file.save("MyMp3File.mp3");
```

### Getting ID3v2 frame values

Convenience methods are included to easily get common ID3v2 frames. If you wish to get frame data that does not have convenience methods, or if you wish to access meta-data on frames, direct reading of frames is possible (see further down on this page).

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
if (mp3file.hasId3v2Tag()) {
  ID3v2 id3v2Tag = mp3file.getId3v2Tag();
  System.out.println("Track: " + id3v2Tag.getTrack());
  System.out.println("Artist: " + id3v2Tag.getArtist());
  System.out.println("Title: " + id3v2Tag.getTitle());
  System.out.println("Album: " + id3v2Tag.getAlbum());
  System.out.println("Year: " + id3v2Tag.getYear());
  System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
  System.out.println("Comment: " + id3v2Tag.getComment());
  System.out.println("Composer: " + id3v2Tag.getComposer());
  System.out.println("Publisher: " + id3v2Tag.getPublisher());
  System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
  System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
  System.out.println("Copyright: " + id3v2Tag.getCopyright());
  System.out.println("URL: " + id3v2Tag.getUrl());
  System.out.println("Encoder: " + id3v2Tag.getEncoder());
  byte[] albumImageData = id3v2Tag.getAlbumImage();
  if (albumImageData != null) {
    System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
    System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
  }
}
```

### Getting ID3v2 album artwork

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
if (mp3file.hasId3v2Tag()) {
  ID3v2 id3v2Tag = mp3file.getId3v2Tag();
  byte[] imageData = id3v2Tag.getAlbumImage();
  if (imageData != null) {
    String mimeType = id3v2Tag.getAlbumImageMimeType();
    // Write image to file - can determine appropriate file extension from the mime type
    RandomAccessFile file = new RandomAccessFile("album-artwork", "rw");
    file.write(data);
    file.close();
  }
}
```
### Setting ID3v2 field values

Convenience methods are included to easily set common ID3v2 tags. Text encoding is chosen appropriately for strings (generally ISO8859-1 or UTF-16). If you wish to set frame data that does not have convenience methods, or if you wish to specify text encoding, or set meta-data on frames, direct writing of frames is possible (see below).

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
ID3v2 id3v2Tag;
if (mp3file.hasId3v2Tag()) {
  id3v2Tag = mp3file.getId3v2Tag();
} else {
  // mp3 does not have an ID3v2 tag, let's create one..
  id3v2Tag = new ID3v24Tag();
  mp3file.setId3v2Tag(id3v2Tag);
}
id3v2Tag.setTrack("5");
id3v2Tag.setArtist("An Artist");
id3v2Tag.setTitle("The Title");
id3v2Tag.setAlbum("The Album");
id3v2Tag.setYear("2001");
id3v2Tag.setGenre(12);
id3v2Tag.setComment("Some comment");
id3v2Tag.setComposer("The Composer");
id3v2Tag.setPublisher("A Publisher");
id3v2Tag.setOriginalArtist("Another Artist");
id3v2Tag.setAlbumArtist("An Artist");
id3v2Tag.setCopyright("Copyright");
id3v2Tag.setUrl("http://foobar");
id3v2Tag.setEncoder("The Encoder");
mp3file.save("MyMp3File.mp3");
```

### Reading and writing ID3v2 frames directly

Frame IDs are defined in the [ID3v2 specification](http://www.id3.org/Developer_Information "ID3v2 specification"). Frames can be read from an ID3v2 tag using these. 

For now, the best approach to access frames not yet supported by convenience methods is to extend the `AbstractID3v2Tag` class, following the pattern for other frames already done. Please fork the project on github and submit a pull request if you add anything useful.

Code to read and write these frames more easily is planned. Watch this space.

## Contributing

If you have added a feature or fixed a bug in mp3agic please submit a pull request as follows:

* Fork the project
* Write the code for your feature or bug fix
* Please don't auto-format the code or make wholesale whitespace changes as it makes seeing what has changed more difficult
* Add tests! This is important so the code you've added doesn't get unintentionally broken in the future
* Make sure the existing tests pass
* Commit and do not mess with version or history
* Submit a pull request

Thanks for sharing!

## Copyright

Copyright (c) 2006-2014 Michael Patricios. See mit-license.txt for details.
