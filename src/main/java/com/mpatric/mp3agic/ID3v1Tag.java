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
		if (!TAG.equals(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, 0, TAG.length()))) {
			throw new NoSuchTagException();
		}
	}

	@Override
	public byte[] toBytes() {
		byte[] bytes = new byte[TAG_LENGTH];
		packTag(bytes);
		return bytes;
	}

	public void toBytes(byte[] bytes) {
		packTag(bytes);
	}

	public void packTag(byte[] bytes) {
		Arrays.fill(bytes, (byte) 0);
		try {
			BufferTools.stringIntoByteBuffer(TAG, 0, 3, bytes, 0);
		} catch (UnsupportedEncodingException e) {
		}
		packField(bytes, title, TITLE_LENGTH, TITLE_OFFSET);
		packField(bytes, artist, ARTIST_LENGTH, ARTIST_OFFSET);
		packField(bytes, album, ALBUM_LENGTH, ALBUM_OFFSET);
		packField(bytes, year, YEAR_LENGTH, YEAR_OFFSET);
		if (genre < 128) {
			bytes[GENRE_OFFSET] = (byte) genre;
		} else {
			bytes[GENRE_OFFSET] = (byte) (genre - 256);
		}
		if (track == null) {
			packField(bytes, comment, COMMENT_LENGTH_V1_0, COMMENT_OFFSET);
		} else {
			packField(bytes, comment, COMMENT_LENGTH_V1_1, COMMENT_OFFSET);
			String trackTemp = numericsOnly(track);
			if (trackTemp.length() > 0) {
				int trackInt = Integer.parseInt(trackTemp);
				if (trackInt < 128) {
					bytes[TRACK_OFFSET] = (byte) trackInt;
				} else {
					bytes[TRACK_OFFSET] = (byte) (trackInt - 256);
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
		StringBuilder stringBuffer = new StringBuilder();
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

	@Override
	public String getVersion() {
		if (track == null) {
			return VERSION_0;
		} else {
			return VERSION_1;
		}
	}

	@Override
	public String getTrack() {
		return track;
	}

	@Override
	public void setTrack(String track) {
		this.track = track;
	}

	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getAlbum() {
		return album;
	}

	@Override
	public void setAlbum(String album) {
		this.album = album;
	}

	@Override
	public String getYear() {
		return year;
	}

	@Override
	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public int getGenre() {
		return genre;
	}

	@Override
	public void setGenre(int genre) {
		this.genre = genre;
	}

	@Override
	public String getGenreDescription() {
		try {
			return ID3v1Genres.GENRES[genre];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Unknown";
		}
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((album == null) ? 0 : album.hashCode());
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + genre;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((track == null) ? 0 : track.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ID3v1Tag other = (ID3v1Tag) obj;
		if (album == null) {
			if (other.album != null)
				return false;
		} else if (!album.equals(other.album))
			return false;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (genre != other.genre)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (track == null) {
			if (other.track != null)
				return false;
		} else if (!track.equals(other.track))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
}
