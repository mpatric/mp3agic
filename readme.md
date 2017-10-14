# mp3agic
[![Build Status](https://travis-ci.org/mpatric/mp3agic.png?branch=master)](https://travis-ci.org/mpatric/mp3agic)
[![Coverage Status](https://coveralls.io/repos/mpatric/mp3agic/badge.svg?branch=master)](https://coveralls.io/r/mpatric/mp3agic?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mpatric/mp3agic/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mpatric/mp3agic)
[![Build status](https://ci.appveyor.com/api/projects/status/c7k4fnxnjlmr7y7p?svg=true)](https://ci.appveyor.com/project/mpatric/mp3agic)

A java library for reading mp3 files and reading / manipulating the ID3 tags (ID3v1 and ID3v2.2 through ID3v2.4).

See [mp3agic-examples](https://github.com/mpatric/mp3agic-examples "mp3agic-examples") for example applications that use this library - including a simple set of command-line tools that perform tasks like printing mp3 and ID3 details, renaming mp3 files using details from the ID3 tags, retagging mp3 files, attaching images to and extracting images from mp3 files.

Releases are available via Maven central. To add a dependency to mp3agic, use:

```xml
<dependency>
  <groupId>com.mpatric</groupId>
  <artifactId>mp3agic</artifactId>
  <version>0.9.1</version>
</dependency>
```

## Some features

* 100% Java
* read low-level mpeg frame data
* read, write, add and remove ID3v1 and ID3v2 tags (ID3v2.3 and ID3v2.4)
* read obsolete 3-letter ID3v2.2 tags (but not write them)
* correctly read VBR files by looking at the actual mpeg frames
* read and write embedded images (such as album art)
* add or remove custom messages between the end of the mpeg frames and the ID3v1 tag
* unicode support

## Development

mp3agic uses various tools to ease the development process.
* [Maven](http://maven.apache.org/) is used to resolve dependencies and to build mp3agic.
* [Travis CI](https://travis-ci.org/mpatric/mp3agic) is used as a continuous integration server.
* [Sonar](http://nemo.sonarqube.org/dashboard/index/com.mpatric:mp3agic) is used for static code analysis (updated every saturday).


### Building

To build mp3agic, you will need:

* [JDK 8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - Oracle or OpenJDK
* [maven](http://maven.apache.org/) - Version 3 recommended

After installing these tools simply run 'mvn clean package' and find the jar in the target folder.

Other Useful maven lifecycles:

* clean - remove binaries, docs and temporary build files
* compile - compile the library
* test - run all unit tests
* package - package compiled code into a jar

## How to use it

Some sample code follows for performing common operations; it is not an exhaustive list of all the functionality.
More can be learned from looking at the javadocs and the code itself, or at the examples in [mp3agic-examples](https://github.com/mpatric/mp3agic-examples).

### Opening an mp3 file

```java
Mp3File mp3file = new Mp3File("SomeMp3File.mp3");
System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
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
  System.out.println("Lyrics: " + id3v2Tag.getLyrics());
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
id3v2Tag.setLyrics("Some lyrics");
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
* mp3agic uses [editorconfig](http://editorconfig.org/) to keep code formatted in a consistent way. Please make sure that your IDE follows the [config file](.editorconfig)
* Add tests! This is important so the code you've added doesn't get unintentionally broken in the future
* Make sure the existing tests pass
* Commit and do not mess with version or history
* Submit a pull request

Thanks for sharing!

## Copyright

Copyright (c) 2006-2017 Michael Patricios. See [mit-license.txt](mit-license.txt) for details.
