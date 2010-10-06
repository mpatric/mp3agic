package com.mpatric.mp3agic.app;

import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mpatric.mp3agic.Version;


public class Mp3Move extends BaseApp {
	
	static String filename = null;
	static String destination = null;
	
	protected Mp3Move() {
	}
	
	public Mp3Move(String filename, String destination) {
		try {
			if (!directoryExists(destination)) throw new BaseException("Destination directory \"" + destination + "\" cannot be found");
			String dest = makeDirectory(filename, destination);
			String destFilename = moveFile(filename, dest);
			printOut(destFilename);
		} catch (BaseException e) {
			printError("ERROR processing \"" + extractFilename(filename) + "\" - " + e.getDetailedMessage());
		} catch (Exception e) {
			printError("ERROR processing \"" + extractFilename(filename) + "\" - " + formatExceptionMessage(e));
		}
	}

	private boolean directoryExists(String destination) {
		File f = new File(destination);
		return f.exists();
	}

	private String makeDirectory(String filename, String destination) throws UnsupportedTagException, InvalidDataException, IOException {
		ID3Wrapper id3Wrapper = createId3Wrapper(filename);
		String artist = stripString(id3Wrapper.getArtist());
		String album = stripString(id3Wrapper.getAlbum());
		String dest = destination;
		if (dest.charAt(dest.length() - 1) != '/') dest += "/";
		if (artist != null && artist.length() > 0) {
			dest += artist;
			dest += '/';
			mkdir(dest);
			if (album != null && album.length() > 0) {
				dest += album;
				mkdir(dest);
			}
		}
		return dest;
	}

	private void mkdir(String dest) {
		File f = new File(dest);
		f.mkdir();
	}

	private String stripString(String s) {
		if (s == null) return null;
		return BufferTools.substitute(s, "/", "");
	}

	private String moveFile(String filename, String dest) throws BaseException {
		if (!directoryExists(dest)) throw new BaseException("Could not find or create directory \"" + dest + "\"");
		File srcFile = new File(filename);
		String destFilename = dest + "/" + srcFile.getName();
		File destFile = new File(destFilename);
		if (!srcFile.renameTo(destFile)) throw new BaseException("Could not move file \"" + filename + "\" to \"" + dest + "\"");
		return destFilename;
	}

	protected ID3Wrapper createId3Wrapper(String filename) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3file = new Mp3File(filename, false);
		ID3Wrapper id3Wrapper = new ID3Wrapper(mp3file.getId3v1Tag(), mp3file.getId3v2Tag());
		return id3Wrapper;
	}

	public static void main(String[] args) {
		if (! parseArgs(args)) {
			usage();
		} else {
			new Mp3Move(filename, destination);
		}
	}
	
	protected static boolean parseArgs(String args[]) {
		if (args.length < 2) {
			return false;
		}
		filename = args[0];
		destination = args[1];
		return true;
	}

	private static void usage() {
		System.out.println("mp3move [mp3agic " + Version.asString() + "]");
	}
}
