package com.mpatric.mp3agic;

public class ID3Wrapper {

	private ID3v1 id3v1Tag;
	private ID3v2 id3v2Tag;

	public ID3Wrapper(ID3v1 id3v1Tag, ID3v2 id3v2Tag) {
		this.id3v1Tag = id3v1Tag;
		this.id3v2Tag = id3v2Tag;
	}
	
	public ID3v1 getId3v1Tag() {
		return id3v1Tag;
	}

	public ID3v2 getId3v2Tag() {
		return id3v2Tag;
	}

	public String getTrack() {
		if (id3v2Tag != null && id3v2Tag.getTrack() != null && id3v2Tag.getTrack().length() > 0) {
			return id3v2Tag.getTrack();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getTrack();
		} else {
			return null;
		}
	}

	public void setTrack(String track) {
		if (id3v2Tag != null) {
			id3v2Tag.setTrack(track);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setTrack(track);
		}
	}

	public String getArtist() {
		if (id3v2Tag != null && id3v2Tag.getArtist() != null && id3v2Tag.getArtist().length() > 0) {
			return id3v2Tag.getArtist();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getArtist();
		} else {
			return null;
		}
	}

	public void setArtist(String artist) {
		if (id3v2Tag != null) {
			id3v2Tag.setArtist(artist);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setArtist(artist);
		}
	}
	
	public String getTitle() {
		if (id3v2Tag != null && id3v2Tag.getTitle() != null && id3v2Tag.getTitle().length() > 0) {
			return id3v2Tag.getTitle();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getTitle();
		} else {
			return null;
		}
	}

	public void setTitle(String title) {
		if (id3v2Tag != null) {
			id3v2Tag.setTitle(title);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setTitle(title);
		}
	}

	public String getAlbum() {
		if (id3v2Tag != null && id3v2Tag.getAlbum() != null && id3v2Tag.getAlbum().length() > 0) {
			return id3v2Tag.getAlbum();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getAlbum();
		} else {
			return null;
		}
	}

	public void setAlbum(String album) {
		if (id3v2Tag != null) {
			id3v2Tag.setAlbum(album);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setAlbum(album);
		}
	}
	
	public String getYear() {
		if (id3v2Tag != null && id3v2Tag.getYear() != null && id3v2Tag.getYear().length() > 0) {
			return id3v2Tag.getYear();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getYear();
		} else {
			return null;
		}
	}

	public void setYear(String year) {
		if (id3v2Tag != null) {
			id3v2Tag.setYear(year);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setYear(year);
		}
	}

	public int getGenre() {
		if (id3v1Tag != null && id3v1Tag.getGenre() != -1) {
			return id3v1Tag.getGenre();
		} else if (id3v2Tag != null) {
			return id3v2Tag.getGenre();
		} else {
			return -1;
		}
	}

	public void setGenre(int genre) {
		if (id3v2Tag != null) {
			id3v2Tag.setGenre(genre);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setGenre(genre);
		}
	}
	
	public String getGenreDescription() {
		if (id3v1Tag != null) {
			return id3v1Tag.getGenreDescription();
		} else if (id3v2Tag != null) {
			return id3v2Tag.getGenreDescription();
		} else {
			return null;
		}
	}
	
	public String getComment() {
		if (id3v2Tag != null && id3v2Tag.getComment() != null && id3v2Tag.getComment().length() > 0) {
			return id3v2Tag.getComment();
		} else if (id3v1Tag != null) {
			return id3v1Tag.getComment();
		} else {
			return null;
		}
	}

	public void setComment(String comment) {
		if (id3v2Tag != null) {
			id3v2Tag.setComment(comment);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setComment(comment);
		}
	}

	public String getComposer() {
		if (id3v2Tag != null) {
			return id3v2Tag.getComposer();
		} else {
			return null;
		}
	}

	public void setComposer(String composer) {
		if (id3v2Tag != null) {
			id3v2Tag.setComposer(composer);
		}
	}
	
	public String getOriginalArtist() {
		if (id3v2Tag != null) {
			return id3v2Tag.getOriginalArtist();
		} else {
			return null;
		}
	}

	public void setOriginalArtist(String originalArtist) {
		if (id3v2Tag != null) {
			id3v2Tag.setOriginalArtist(originalArtist);
		}
	}

	public void setAlbumArtist(String albumArtist) {
		if (id3v2Tag != null) {
			id3v2Tag.setAlbumArtist(albumArtist);
		}
	}

	public String getAlbumArtist() {
		if (id3v2Tag != null) {
			return id3v2Tag.getAlbumArtist();
		} else {
			return null;
		}
	}

	public String getCopyright() {
		if (id3v2Tag != null) {
			return id3v2Tag.getCopyright();
		} else {
			return null;
		}
	}

	public void setCopyright(String copyright) {
		if (id3v2Tag != null) {
			id3v2Tag.setCopyright(copyright);
		}
	}

	public String getUrl() {
		if (id3v2Tag != null) {
			return id3v2Tag.getUrl();
		} else {
			return null;
		}
	}

	public void setUrl(String url) {
		if (id3v2Tag != null) {
			id3v2Tag.setUrl(url);
		}
	}

	public String getEncoder() {
		if (id3v2Tag != null) {
			return id3v2Tag.getEncoder();
		} else {
			return null;
		}
	}

	public void setEncoder(String encoder) {
		if (id3v2Tag != null) {
			id3v2Tag.setEncoder(encoder);
		}
	}
	
	public byte[] getAlbumImage() {
		if (id3v2Tag != null) {
			return id3v2Tag.getAlbumImage();
		} else {
			return null;
		}
	}

	public void setAlbumImage(byte[] albumImage, String mimeType) {
		if (id3v2Tag != null) {
			id3v2Tag.setAlbumImage(albumImage, mimeType);
		}
	}

	public String getAlbumImageMimeType() {
		if (id3v2Tag != null) {
			return id3v2Tag.getAlbumImageMimeType();
		} else {
			return null;
		}
	}

	public void clearComment() {
		if (id3v2Tag != null) {
			id3v2Tag.clearFrameSet(AbstractID3v2Tag.ID_COMMENT);
		}
		if (id3v1Tag != null) {
			id3v1Tag.setComment(null);
		}
	}

	public void clearCopyright() {
		if (id3v2Tag != null) {
			id3v2Tag.clearFrameSet(AbstractID3v2Tag.ID_COPYRIGHT);
		}
	}

	public void clearEncoder() {
		if (id3v2Tag != null) {
			id3v2Tag.clearFrameSet(AbstractID3v2Tag.ID_ENCODER);
		}
	}
}
