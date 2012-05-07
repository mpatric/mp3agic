package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ID3v1Tag implements ID3v1 {

	public static final int TAG_LENGTH = 128;

	private static final String VERSION_0 = "0";
	private static final String VERSION_1 = "1";
	private static final String TAG = "TAG";
	private static final int TITLE_OFFSET = 3;
	private static final int TITLE_LENGTH = 30;
	private static final int ARTIST_OFFSET = 33;
	private static final int ARTIST_LENGTH = 30;
	private static final int ALBUM_OFFSET = 63;
	private static final int ALBUM_LENGTH = 30;
	private static final int YEAR_OFFSET = 93;
	private static final int YEAR_LENGTH = 4;
	private static final int COMMENT_OFFSET = 97;
	private static final int COMMENT_LENGTH_V1_0 = 30;
	private static final int COMMENT_LENGTH_V1_1 = 28;
	private static final int TRACK_MARKER_OFFSET = 125;
	private static final int TRACK_OFFSET = 126;
	private static final int GENRE_OFFSET = 127;
	
	private String track = null;
	private String artist = null;
	private String title = null;
	private String album = null;
	private String year = null;
	private int genre = -1;
	private String comment = null;

	public ID3v1Tag() {
	}
	
	public ID3v1Tag(byte[] bytes) throws NoSuchTagException {
		unpackTag(bytes);
	}

	private void unpackTag(byte[] bytes) throws NoSuchTagException {
		sanityCheckTag(bytes);
		title = BufferTools.trimStringRight(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, TITLE_OFFSET, TITLE_LENGTH));
		artist = BufferTools.trimStringRight(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, ARTIST_OFFSET, ARTIST_LENGTH));
		album = BufferTools.trimStringRight(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, ALBUM_OFFSET, ALBUM_LENGTH));
		year = BufferTools.trimStringRight(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, YEAR_OFFSET, YEAR_LENGTH));
		genre = bytes[GENRE_OFFSET] & 0xFF;
		if (genre == 0xFF) {
			genre = -1;
		}
		if (bytes[TRACK_MARKER_OFFSET] != 0) {
			comment = BufferTools.trimStringRight(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, COMMENT_OFFSET, COMMENT_LENGTH_V1_0));
			track = null;
		} else {
			comment = BufferTools.trimStringRight(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, COMMENT_OFFSET, COMMENT_LENGTH_V1_1));
			int trackInt = bytes[TRACK_OFFSET];
			if (trackInt == 0) {
				track = "";
			} else {
				track = Integer.toString(trackInt);
			}
		}
	}
	
	private void sanityCheckTag(byte[] bytes) throws NoSuchTagException {
		if (bytes.length != TAG_LENGTH) {
			throw new NoSuchTagException("Buffer length wrong");
		}
		if (! TAG.equals(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, 0, TAG.length()))) {
			throw new NoSuchTagException();
		}
	}
	
	public byte[] toBytes() {
		byte[] bytes = new byte[TAG_LENGTH];
		packTag(bytes);
		return bytes;
	}
	
	public void toBytes(byte[] bytes) {
		packTag(bytes);
	}

	public void packTag(byte[] bytes) {
		Arrays.fill(bytes, (byte)0);
		try {
			BufferTools.stringIntoByteBuffer(TAG, 0, 3, bytes, 0);
		} catch (UnsupportedEncodingException e) {
		}
		packField(bytes, title, TITLE_LENGTH, TITLE_OFFSET);
		packField(bytes, artist, ARTIST_LENGTH, ARTIST_OFFSET);
		packField(bytes, album, ALBUM_LENGTH, ALBUM_OFFSET);
		packField(bytes, year, YEAR_LENGTH, YEAR_OFFSET);
		if (genre < 128) {
			bytes[GENRE_OFFSET] = (byte)genre;
		} else {
			bytes[GENRE_OFFSET] = (byte)(genre - 256);
		}
		if (track == null) {
			packField(bytes, comment, COMMENT_LENGTH_V1_0, COMMENT_OFFSET);
		} else {
			packField(bytes, comment, COMMENT_LENGTH_V1_1, COMMENT_OFFSET);
			String trackTemp = numericsOnly(track);
			if (trackTemp.length() > 0) {			
				int trackInt = Integer.parseInt(trackTemp.toString());
				if (trackInt < 128) {
					bytes[TRACK_OFFSET] = (byte)trackInt;
				} else {
					bytes[TRACK_OFFSET] = (byte)(trackInt - 256);
				}
			}
		}
	}

	private void packField(byte[] bytes, String value, int maxLength, int offset) {
		if (value != null) {
			try {
				BufferTools.stringIntoByteBuffer(value, 0, Math.min(value.length(), maxLength), bytes, offset);
			} catch (UnsupportedEncodingException e) {
			}
		}
	}
	
	private String numericsOnly(String s) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch >= '0' && ch <= '9') {
				stringBuffer.append(ch);
			} else {
				break;
			}
		}
		return stringBuffer.toString();
	}
	
	public String getVersion() {
		if (track == null) {
			return VERSION_0;
		} else {
			return VERSION_1;
		}
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public String getGenreDescription() {
		try {
			return ID3v1Genres.GENRES[genre];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Unknown";
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v1Tag)) return false;
		if (super.equals(obj)) return true;
		ID3v1Tag other = (ID3v1Tag) obj;
		if (genre != other.genre) return false;
		if (track == null) {
			if (other.track != null) return false;
		} else if (other.track == null) return false;
		else if (! track.equals(other.track)) return false;
		if (artist == null) {
			if (other.artist != null) return false;
		} else if (other.artist == null) return false;
		else if (! artist.equals(other.artist)) return false;
		if (title == null) {
			if (other.title != null) return false;
		} else if (other.title == null) return false;
		else if (! title.equals(other.title)) return false;
		if (album == null) {
			if (other.album != null) return false;
		} else if (other.album == null) return false;
		else if (! album.equals(other.album)) return false;
		if (year == null) {
			if (other.year != null) return false;
		} else if (other.year == null) return false;
		else if (! year.equals(other.year)) return false;
		if (comment == null) {
			if (other.comment != null) return false;
		} else if (other.comment == null) return false;
		else if (! comment.equals(other.comment)) return false;
		return true;
	}
}
