package com.mpatric.mp3agic.app;

import java.io.UnsupportedEncodingException;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.Version;

public class Mp3Catalog extends BaseApp {
	
	private static final String CBR = "CBR";
	private static final String VBR = "VBR";
	private static final int MAX_CUSTOM_TAG_BYTES_TO_SHOW = 64;
	private Mp3File mp3file;
	
	protected Mp3Catalog() {
	}

	protected Mp3Catalog(String filename) {
		try {
			mp3file = new Mp3File(filename);
			catalog();
		} catch (BaseException e) {
			printError("ERROR processing " + extractFilename(filename) + " - " + e.getDetailedMessage());
		} catch (Exception e) {
			printError("ERROR processing " + extractFilename(filename) + " - " + formatExceptionMessage(e));
		}
	}
	
	protected void catalog() {
		StringBuffer cat = new StringBuffer();
		catalogMp3Fields(cat);
		catalogId3Fields(cat);
		catalogCustomTag(cat);
		printOut(cat.toString());
	}

	private void catalogMp3Fields(StringBuffer cat) {
		catalogField(cat, extractFilename(mp3file.getFilename()));
		catalogField(cat, Long.toString(mp3file.getLength()));
		catalogField(cat, Long.toString(mp3file.getLengthInSeconds()));
		catalogField(cat, mp3file.getVersion());
		catalogField(cat, mp3file.getLayer());
		catalogField(cat, Integer.toString(mp3file.getSampleRate()));
		catalogField(cat, Integer.toString(mp3file.getBitrate()));
		catalogField(cat, vbrString(mp3file.isVbr()));
		catalogField(cat, mp3file.getChannelMode());
	}
	
	private String vbrString(boolean vbr) {
		if (vbr) return VBR;
		return CBR;
	}
	
	private void catalogId3Fields(StringBuffer cat) {
		ID3v1 id3v1tag = mp3file.getId3v1Tag();
		ID3v2 id3v2tag = mp3file.getId3v2Tag();
		ID3Wrapper id3wrapper = new ID3Wrapper(id3v1tag, id3v2tag);
		if (id3v1tag != null) catalogField(cat, "1." + id3v1tag.getVersion());
		else catalogField(cat, null);
		if (id3v2tag != null) catalogField(cat, "2." + id3v2tag.getVersion());
		else catalogField(cat, null);
		catalogField(cat, id3wrapper.getTrack());
		catalogField(cat, id3wrapper.getArtist());
		catalogField(cat, id3wrapper.getAlbum());
		catalogField(cat, id3wrapper.getTitle());
		catalogField(cat, id3wrapper.getYear());
		catalogField(cat, id3wrapper.getGenreDescription());
		catalogField(cat, id3wrapper.getComment());
		catalogField(cat, id3wrapper.getComposer());
		catalogField(cat, id3wrapper.getOriginalArtist());
		catalogField(cat, id3wrapper.getCopyright());
		catalogField(cat, id3wrapper.getUrl());
		catalogField(cat, id3wrapper.getEncoder());
		catalogField(cat, id3wrapper.getAlbumImageMimeType());
	}
	
	private void catalogCustomTag(StringBuffer cat) {
		byte[] bytes = mp3file.getCustomTag();
		if (bytes == null) {
			catalogLastField(cat, "");
		} else {
			int length = Math.min(bytes.length, MAX_CUSTOM_TAG_BYTES_TO_SHOW);
			try {
				String s = BufferTools.byteBufferToString(bytes, 0, length);
				catalogLastField(cat, BufferTools.asciiOnly(s));
			} catch (UnsupportedEncodingException e) {
				catalogLastField(cat, "");
			}
		}
	}

	private void catalogField(StringBuffer cat, String field) {
		catalogField(cat, field, false);
	}
	
	private void catalogLastField(StringBuffer cat, String field) {
		catalogField(cat, field, true);
	}

	private void catalogField(StringBuffer cat, String field, boolean last) {
		cat.append("\"");
		if (field != null) {
			cat.append(escapeQuotes(field));
		}
		cat.append("\"");
		if (! last) {
			cat.append(",");
		}
	}

	protected String escapeQuotes(String s) {
		return BufferTools.substitute(s, "\"", "\"\"");
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			usage();
		} else {
			String filename = args[0];
			new Mp3Catalog(filename);
		}
	}

	private static void usage() {
		System.out.println("mp3cat [mp3agic " + Version.asString() + "]");
	}
}
