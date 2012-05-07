package com.mpatric.mp3agic.app;

import java.io.UnsupportedEncodingException;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.Version;

public class Mp3Details extends BaseApp {
	
	private static final String CBR = "CBR";
	private static final String VBR = "VBR";
	private static final int MAX_CUSTOM_TAG_BYTES_TO_SHOW = 64;
	private static final int PAD_NAME_TO = 17;
	private Mp3File mp3file;
	
	protected Mp3Details() {
	}

	protected Mp3Details(String filename) {
		try {
			mp3file = new Mp3File(filename);
			show();
		} catch (BaseException e) {
			printError("ERROR processing " + extractFilename(filename) + " - " + e.getDetailedMessage());
		} catch (Exception e) {
			printError("ERROR processing " + extractFilename(filename) + " - " + formatExceptionMessage(e));
		}
	}
	
	protected void show() {
		StringBuffer buffer = new StringBuffer();
		showMp3Fields(buffer);
		showId3v1Fields(buffer);
		showId3v2Fields(buffer);
		showCustomTag(buffer);
		printOut(buffer.toString());
	}

	private void showMp3Fields(StringBuffer buffer) {
		buffer.append("MP3 Data\n");
		showField(buffer, "Filename", extractFilename(mp3file.getFilename()));
		showField(buffer, "Size", Long.toString(mp3file.getLength()));
		showField(buffer, "Length", formatTime(mp3file.getLengthInSeconds()));
		showField(buffer, "Version", mp3file.getVersion());
		showField(buffer, "Layer", mp3file.getLayer());
		showField(buffer, "Sample rate", Integer.toString(mp3file.getSampleRate()), "Hz");
		showField(buffer, "Bitrate", Integer.toString(mp3file.getBitrate()), "kbps (" + vbrString(mp3file.isVbr()) + ")");
		showField(buffer, "Channel mode", mp3file.getChannelMode());
	}
	
	private String formatTime(long seconds) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Long.toString(seconds / 60)).append(':');
		buffer.append(String.format("%02d", seconds % 60));
		return buffer.toString();
	}

	private String vbrString(boolean vbr) {
		if (vbr) return VBR;
		return CBR;
	}
	
	private void showId3v1Fields(StringBuffer buffer) {
		buffer.append("ID3v1 Data\n");
		ID3v1 id3v1tag = mp3file.getId3v1Tag();
		if (id3v1tag == null) {
			buffer.append("  NONE!\n");
		} else {
			showField(buffer, "Track", id3v1tag.getTrack());
			showField(buffer, "Artist", id3v1tag.getArtist());
			showField(buffer, "Title", id3v1tag.getTitle());
			showField(buffer, "Album", id3v1tag.getAlbum());
			showField(buffer, "Year", id3v1tag.getYear());
			showField(buffer, "Genre", id3v1tag.getGenreDescription());
			showField(buffer, "Comment", id3v1tag.getComment());
		}
	}
	
	private void showId3v2Fields(StringBuffer buffer) {
		ID3v2 id3v2tag = mp3file.getId3v2Tag();
		if (id3v2tag == null || !id3v2tag.getObseleteFormat()) {
			buffer.append("ID3v2 Data\n"); 
		} else {
			buffer.append("ID3v2 Data (Obselete 3-character format)\n");
		}
		if (id3v2tag == null) {
			buffer.append("  NONE!\n");
		} else {
			showField(buffer, "Track", id3v2tag.getTrack());
			showField(buffer, "Artist", id3v2tag.getArtist());
			showField(buffer, "Album", id3v2tag.getAlbum());
			showField(buffer, "Title", id3v2tag.getTitle());
			showField(buffer, "Year", id3v2tag.getYear());
			showField(buffer, "Genre", id3v2tag.getGenreDescription());
			showField(buffer, "Comment", id3v2tag.getComment());
			showField(buffer, "Composet", id3v2tag.getComposer());
			showField(buffer, "Original Artist", id3v2tag.getOriginalArtist());
			showField(buffer, "Copyright", id3v2tag.getCopyright());
			showField(buffer, "Url", id3v2tag.getUrl());
			showField(buffer, "Encoder", id3v2tag.getEncoder());
			showField(buffer, "Album Image", id3v2tag.getAlbumImageMimeType());
		}
	}
	
	private void showCustomTag(StringBuffer buffer) {
		buffer.append("Custom Tag\n");
		byte[] bytes = mp3file.getCustomTag();
		if (bytes == null) {
			buffer.append("  NONE!\n");
		} else {
			int length = Math.min(bytes.length, MAX_CUSTOM_TAG_BYTES_TO_SHOW);
			try {
				String s = BufferTools.byteBufferToString(bytes, 0, length);
				showLastField(buffer, "Data", BufferTools.asciiOnly(s), bytes.length > MAX_CUSTOM_TAG_BYTES_TO_SHOW ? "..." : null);
			} catch (UnsupportedEncodingException e) {
				buffer.append("  ?\n");
			}
		}
	}

	private void showField(StringBuffer buffer, String name, String field) {
		showField(buffer, name, field, false, null);
	}
	
	private void showField(StringBuffer buffer, String name, String field, String units) {
		showField(buffer, name, field, false, units);
	}
	
	private void showLastField(StringBuffer buffer, String name, String field, String units) {
		showField(buffer, name, field, true, units);
	}

	private void showField(StringBuffer buffer, String name, String field, boolean last, String units) {
		buffer.append("  ").append(padString(name, ":", PAD_NAME_TO)).append(field);
		if (units != null) buffer.append(" ").append(units);
		buffer.append("\n");
	}

	private String padString(String str, String appendStr, int padTo) {
		StringBuffer buffer;
		if (str == null) buffer = new StringBuffer();
		else buffer = new StringBuffer(str);
		if (appendStr != null) buffer.append(appendStr);
		while (buffer.length() < padTo) {
			buffer.append(' ');
		}
		return buffer.toString();
	}

	protected String escapeQuotes(String s) {
		return BufferTools.substitute(s, "\"", "\"\"");
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			usage();
		} else {
			String filename = args[0];
			new Mp3Details(filename);
		}
	}

	private static void usage() {
		System.out.println("mp3details [mp3agic " + Version.asString() + "]");
	}
}
