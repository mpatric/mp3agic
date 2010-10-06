package com.mpatric.mp3agic.app;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.Version;


public class Mp3Pics extends BaseApp {

	private Mp3File mp3file;
	
	protected Mp3Pics() {
	}

	protected Mp3Pics(String filename) {
		try {
			mp3file = new Mp3File(filename);
			extractPics();
		} catch (BaseException e) {
			printError("ERROR processing " + extractFilename(filename) + " - " + e.getDetailedMessage());
		} catch (Exception e) {
			printError("ERROR processing " + extractFilename(filename) + " - " + formatExceptionMessage(e));
		}
	}
	
	protected void extractPics() throws IOException {
		ID3v2 id3v2tag = mp3file.getId3v2Tag();
		if (id3v2tag != null) {
			String mimeType = id3v2tag.getAlbumImageMimeType();
			byte[] data = id3v2tag.getAlbumImage();
			String filename = chooseFilename(mimeType);
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(filename, "rw");
				file.write(data);
			} finally {
				try {
					if (file != null) {
						printOut("  Extracted " + filename);
						file.close();
					}
				} catch (IOException e) {
					// do nothing
				}
			}
		}
	}
	
	private String chooseFilename(String mimeType) {
		String extension;
		int idx;
		if ((idx = mimeType.indexOf('/')) > 0) extension = "." + mimeType.substring(idx + 1).toLowerCase();
		else mimeType = extension = "." + mimeType.toLowerCase(); 
		ID3Wrapper id3Wrapper = new ID3Wrapper(mp3file.getId3v1Tag(), mp3file.getId3v2Tag());
		String path = extractPath(mp3file.getFilename());
		String baseFilename = path + toCompressedString(id3Wrapper.getArtist()) + "-" + toCompressedString(id3Wrapper.getAlbum());
		String filename;
		if (!fileExists(filename = baseFilename + extension)) return filename;
		int i = 1;
		while (true) {
			if (!fileExists(filename = baseFilename + Integer.toString(i) + extension)) return filename;
			i++;
		}
	}
	
	private String toCompressedString(String s) {
		StringBuffer compressed = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || (ch == '&') || (ch == '+') || (ch == '(') || (ch == ')')) {
				compressed.append(ch);
			}
		}
		return compressed.toString();
	}

	private boolean fileExists(String string) {
		File f = new File(string);
		return f.exists();
	}

	protected String escapeQuotes(String s) {
		return BufferTools.substitute(s, "\"", "\"\"");
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			usage();
		} else {
			String filename = args[0];
			new Mp3Pics(filename);
		}
	}

	private static void usage() {
		System.out.println("mp3pics [mp3agic " + Version.asString() + "]");
	}
}
