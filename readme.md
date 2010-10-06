# mp3agic

A library for reading mp3 files and parsing the ID3 tags (ID3v1 and ID3v2.1 through ID3v2.4) as well as writing ID3 tags on mp3 files.

A simple set of command-line tools built on top of the library are included which perform tasks such including printing mp3 and id3 details, renaming mp3 files using details from the id3 tags,
retagging files, attaching images to mp3 files and extracting images from mp3 files.

## Building

You will need:

* j2sdk 1.5.0 or higher
* ant 1.6 or higher
* A bash shell to run the command-line scripts (if you're on MacOS or a flavor of Unix, you're set, if you're on Windows, try [Cygwin](http://www.cygwin.com/ "Cygwin") or rewrite the bash scripts as bat/cmd files).

Settings you might need to change in ant.properties:

* junit-jar should point to junit.jar on your local system
* deploy-dir should point to the directory on your local system you wish to deploy to

Useful ant targets:

* clean - remove binaries, docs and temporary build files
* build - compiles the library and applications, runs all unit tests
* deploy - copies the jar and scripts to the deploy directory defined in ant.properties
* doc - generate the javadocs

## Some example code

<code>
        String filename = "SomeMp3File.mp3";
        Mp3File mp3file = new Mp3File(filename);
        System.out.println("Length of this mp3 in seconds is: " + mp3file.getLengthInSeconds());

        if (mp3file.hasId3v1Tag()) {
          ID3v1 id3v1tag = mp3file.getId3v1Tag();
          System.out.println("The artist in the ID3v1 tag is: " + id3v1tag.getArtist());
        }

        if (mp3file.hasId3v2Tag()) {
          ID3v2 id3v2tag = mp3file.getId3v2Tag();
          System.out.println("This file has an ID3v2 tag, version: " + id3v2tag.getVersion());
          System.out.println("The artist in the ID3v2 tag is: " + id3v2tag.getArtist());
  
          id3v2tag.setArtist("A new artist");
          mp3file.save("ASavedMp3File.mp3");
          System.out("Saved mp3 file with new name and artist set to 'A new artist'");
        }
</code>

## Copyright

Copyright (c) 2006-2010 Michael Patricios. See mit-license.txt for details.