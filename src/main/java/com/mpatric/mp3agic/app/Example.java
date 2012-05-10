package com.mpatric.mp3agic.app;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Example {

	public static void main(String[] args) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		Mp3File mp3file = new Mp3File("src/test/resources/v1andv23tagswithalbumimage.mp3");
		
        System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
        System.out.println("Bitrate: " + mp3file.getLengthInSeconds() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
        System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
        System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
        System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
        System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
        
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
        
        ID3v1 id3v1Tag;
        if (mp3file.hasId3v1Tag()) {
        	id3v1Tag =  mp3file.getId3v1Tag();
        } else {
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
        }
        
        if (mp3file.hasId3v2Tag()) {
        	ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            byte[] imageData = id3v2Tag.getAlbumImage();
            if (imageData != null) {
				String mimeType = id3v2Tag.getAlbumImageMimeType();
				System.out.println("Mime type: " + mimeType);
				// Write image to file - can determine appropriate file extension from the mime type
				RandomAccessFile file = new RandomAccessFile("album-artwork", "rw");
				file.write(imageData);
				file.close();
            }
        }
        
        ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
        	id3v2Tag =  mp3file.getId3v2Tag();
        } else {
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
	}
}
