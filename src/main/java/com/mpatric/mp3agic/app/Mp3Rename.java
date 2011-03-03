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


public class Mp3Rename extends BaseApp {
	
	static String rename = null;
	static String filename = null;
	
	protected Mp3Rename() {
	}
	
	public Mp3Rename(String filename, String rename) {
		try {
			String newFilename = composeNewFilename(filename, rename);
			String newFilenameWithPathAndExtension = extractPath(filename) + newFilename + extractExtension(filename);
			renameFile(filename, newFilenameWithPathAndExtension);
			printOut(extractFilename(newFilenameWithPathAndExtension));
		} catch (BaseException e) {
			printError("ERROR processing \"" + extractFilename(filename) + "\" - " + e.getDetailedMessage());
		} catch (Exception e) {
			printError("ERROR processing \"" + extractFilename(filename) + "\" - " + formatExceptionMessage(e));
		}
	}
	
	protected String composeNewFilename(String filename, String rename) throws UnsupportedTagException, InvalidDataException, IOException {
		ID3Wrapper id3Wrapper = createId3Wrapper(filename);
		String newFilename = rename;
		newFilename = BufferTools.substitute(newFilename, "@N", formatTrack(id3Wrapper.getTrack()));
		newFilename = BufferTools.substitute(newFilename, "@A", id3Wrapper.getArtist());
		newFilename = BufferTools.substitute(newFilename, "@T", id3Wrapper.getTitle());
		newFilename = BufferTools.substitute(newFilename, "@L", id3Wrapper.getAlbum());
		newFilename = BufferTools.substitute(newFilename, "@Y", id3Wrapper.getYear());
		newFilename = BufferTools.substitute(newFilename, "@G", id3Wrapper.getGenreDescription());
		newFilename = BufferTools.substitute(newFilename, "?", null);
		newFilename = BufferTools.substitute(newFilename, "*", null);
		newFilename = BufferTools.substitute(newFilename, "/", null);
		newFilename = BufferTools.substitute(newFilename, "\\", null);
		newFilename = BufferTools.substitute(newFilename, "  ", " ");
		return newFilename;
	}

	private String formatTrack(String track) {
		if (track != null && track.length() > 0) {
			if (track.length() == 1) return "0" + track;
		}
		return track;
	}

	protected ID3Wrapper createId3Wrapper(String filename) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3file = new Mp3File(filename, false);
		ID3Wrapper id3Wrapper = new ID3Wrapper(mp3file.getId3v1Tag(), mp3file.getId3v2Tag());
		return id3Wrapper;
	}

	protected void renameFile(String filename, String newFilename) throws BaseException {
		if (! filename.equals(newFilename)) {
			File originalFile = new File(filename);
			File renamedFile = new File(newFilename);
			if (renamedFile.exists()) {
				throw new BaseException("File with destination name \"" + extractFilename(newFilename) + "\" already exists");
			}
			originalFile.renameTo(renamedFile);
		}
	}

	public static void main(String[] args) {
		if (! parseArgs(args)) {
			usage();
		} else {
			new Mp3Rename(filename, rename);
		}
	}
	
	protected static boolean parseArgs(String args[]) {
		if (args.length < 2) {
			return false;
		}
		rename = args[0];
		if (rename.indexOf('@') < 0) {
			return false;
		}
		filename = args[1];
		return true;
	}

	private static void usage() {
		System.out.println("mp3rename [mp3agic " + Version.asString() + "]");
	}
}
