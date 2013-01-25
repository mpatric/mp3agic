package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractID3v2Tag implements ID3v2 {

	public static final String ID_IMAGE = "APIC";
	public static final String ID_ENCODER = "TENC";
	public static final String ID_URL = "WXXX";
	public static final String ID_COPYRIGHT = "TCOP";
	public static final String ID_ORIGINAL_ARTIST = "TOPE";
	public static final String ID_COMPOSER = "TCOM";
	public static final String ID_PUBLISHER = "TPUB";
	public static final String ID_COMMENT = "COMM";
	public static final String ID_GENRE = "TCON";
	public static final String ID_YEAR = "TYER";
	public static final String ID_ALBUM = "TALB";
	public static final String ID_TITLE = "TIT2";
	public static final String ID_ARTIST = "TPE1";
	public static final String ID_ALBUM_ARTIST = "TPE2";
	public static final String ID_TRACK = "TRCK";
	public static final String ID_CHAPTER_TOC = "CTOC";
    public static final String ID_CHAPTER = "CHAP";
	public static final String ID_IMAGE_OBSELETE = "PIC";
	public static final String ID_ENCODER_OBSELETE = "TEN";
	public static final String ID_URL_OBSELETE = "WXX";
	public static final String ID_COPYRIGHT_OBSELETE = "TCR";
	public static final String ID_ORIGINAL_ARTIST_OBSELETE = "TOA";
	public static final String ID_COMPOSER_OBSELETE = "TCM";
	public static final String ID_PUBLISHER_OBSELETE = "TBP";
	public static final String ID_COMMENT_OBSELETE = "COM";
	public static final String ID_GENRE_OBSELETE = "TCO";
	public static final String ID_YEAR_OBSELETE = "TYE";
	public static final String ID_ALBUM_OBSELETE = "TAL";
	public static final String ID_TITLE_OBSELETE = "TT2";
	public static final String ID_ARTIST_OBSELETE = "TP1";
	public static final String ID_ALBUM_ARTIST_OBSELETE = "TP2";
	public static final String ID_TRACK_OBSELETE = "TRK";
	
	protected static final String TAG = "ID3";
	protected static final String FOOTER_TAG = "3DI";
	protected static final int HEADER_LENGTH = 10;
	protected static final int FOOTER_LENGTH = 10;
	protected static final int MAJOR_VERSION_OFFSET = 3;
	protected static final int MINOR_VERSION_OFFSET = 4;
	protected static final int FLAGS_OFFSET = 5;
	protected static final int DATA_LENGTH_OFFSET = 6;
	protected static final int FOOTER_BIT = 4;
	protected static final int EXPERIMENTAL_BIT = 5;
	protected static final int EXTENDED_HEADER_BIT = 6;
	protected static final int COMPRESSION_BIT = 6;
	protected static final int UNSYNCHRONISATION_BIT = 7;
	protected static final int PADDING_LENGTH = 256;
	private static final String ITUNES_COMMENT_DESCRIPTION = "iTunNORM";
	
	protected boolean unsynchronisation = false;
	protected boolean extendedHeader = false;
	protected boolean experimental = false;
	protected boolean footer = false;
	protected boolean compression = false;
	protected boolean padding = false;
	protected String version = null;
	private int dataLength = 0;
	private int extendedHeaderLength;
	private byte[] extendedHeaderData;
	private boolean obseleteFormat = false;

	private final Map<String, ID3v2FrameSet> frameSets;

	public AbstractID3v2Tag() {
		frameSets = new TreeMap<String, ID3v2FrameSet>();
	}

	public AbstractID3v2Tag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		this(bytes, false);
	}
	
	public AbstractID3v2Tag(byte[] bytes, boolean obseleteFormat) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		frameSets = new TreeMap<String, ID3v2FrameSet>();
		this.obseleteFormat = obseleteFormat;
		unpackTag(bytes);
	}

	private void unpackTag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		ID3v2TagFactory.sanityCheckTag(bytes);
		int offset = unpackHeader(bytes);
		try {
			if (extendedHeader) {
				offset = unpackExtendedHeader(bytes, offset);
			}
			int framesLength = dataLength;
			if (footer) framesLength -= 10;
			offset = unpackFrames(bytes, offset, framesLength);
			if (footer) {
				offset = unpackFooter(bytes, dataLength);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new InvalidDataException("Premature end of tag", e);
		}
	}

	private int unpackHeader(byte[] bytes) throws UnsupportedTagException, InvalidDataException {
		int majorVersion = bytes[MAJOR_VERSION_OFFSET];
		int minorVersion = bytes[MINOR_VERSION_OFFSET];
		version = majorVersion + "." + minorVersion;
		if (majorVersion != 2 && majorVersion != 3 && majorVersion != 4) {
			throw new UnsupportedTagException("Unsupported version " + version);
		}
		unpackFlags(bytes);
		if ((bytes[FLAGS_OFFSET] & 0x0F) != 0) throw new UnsupportedTagException("Unrecognised bits in header");
		dataLength = BufferTools.unpackSynchsafeInteger(bytes[DATA_LENGTH_OFFSET], bytes[DATA_LENGTH_OFFSET + 1], bytes[DATA_LENGTH_OFFSET + 2], bytes[DATA_LENGTH_OFFSET + 3]);
		if (dataLength < 1) throw new InvalidDataException("Zero size tag");
		return HEADER_LENGTH;
	}

	protected abstract void unpackFlags(byte[] bytes);
	
	private int unpackExtendedHeader(byte[] bytes, int offset) {
		extendedHeaderLength = BufferTools.unpackSynchsafeInteger(bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3]) + 4;
		extendedHeaderData = BufferTools.copyBuffer(bytes, offset + 4, extendedHeaderLength);
		return extendedHeaderLength;
	}

	protected int unpackFrames(byte[] bytes, int offset, int framesLength) {
		int currentOffset = offset;
		while (currentOffset <= framesLength) {
			ID3v2Frame frame;
			try {
				frame = createFrame(bytes, currentOffset);
				addFrame(frame, false);
				currentOffset += frame.getLength();
			} catch (InvalidDataException e) {
				break;
			}
		}
		return currentOffset;
	}

	private void addFrame(ID3v2Frame frame, boolean replace) {
		ID3v2FrameSet frameSet = frameSets.get(frame.getId());
		if (frameSet == null) {
			frameSet = new ID3v2FrameSet(frame.getId());
			frameSet.addFrame(frame);
			frameSets.put(frame.getId(), frameSet);
		} else if (replace) {
			frameSet.clear();
			frameSet.addFrame(frame);
		} else {
			frameSet.addFrame(frame);
		}
	}
	
	protected ID3v2Frame createFrame(byte[] bytes, int currentOffset) throws InvalidDataException {
		if (obseleteFormat) return new ID3v2ObseleteFrame(bytes, currentOffset);
		return new ID3v2Frame(bytes, currentOffset);
	}
	
	protected ID3v2Frame createFrame(String id, byte[] data) {
		if (obseleteFormat) return new ID3v2ObseleteFrame(id, data);
		else return new ID3v2Frame(id, data);
	}
	
	private int unpackFooter(byte[] bytes, int offset) throws InvalidDataException {
		if (! FOOTER_TAG.equals(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, offset, FOOTER_TAG.length()))) {
			throw new InvalidDataException("Invalid footer");
		}
		return FOOTER_LENGTH;
	}
	
	public byte[] toBytes() throws NotSupportedException {
		byte[] bytes = new byte[getLength()];
		packTag(bytes);
		return bytes;
	}

	public void packTag(byte[] bytes) throws NotSupportedException {
		int offset = packHeader(bytes, 0);
		if (extendedHeader) {
			offset = packExtendedHeader(bytes, offset);
		}
		offset = packFrames(bytes, offset);
		if (footer) {
			offset = packFooter(bytes, dataLength);
		}
	}
	
	private int packHeader(byte[] bytes, int offset) {
		try {
			BufferTools.stringIntoByteBuffer(TAG, 0, TAG.length(), bytes, offset);
		} catch (UnsupportedEncodingException e) {
		}
		String s[] = version.split("\\.");
		if (s.length > 0) {
			byte majorVersion = Byte.parseByte(s[0]);
			bytes[offset + MAJOR_VERSION_OFFSET] = majorVersion;
		}
		if (s.length > 1) {
			byte minorVersion = Byte.parseByte(s[1]);
			bytes[offset + MINOR_VERSION_OFFSET] = minorVersion;
		}
		packFlags(bytes, offset);
		BufferTools.packSynchsafeInteger(getDataLength(), bytes, offset + DATA_LENGTH_OFFSET);
		return offset + HEADER_LENGTH;
	}

	protected abstract void packFlags(byte[] bytes, int i);
	
	private int packExtendedHeader(byte[] bytes, int offset) {
		BufferTools.packSynchsafeInteger(extendedHeaderLength, bytes, offset);
		BufferTools.copyIntoByteBuffer(extendedHeaderData, 0, extendedHeaderData.length, bytes, offset + 4);
		return offset + 4 + extendedHeaderData.length;
	}

	public int packFrames(byte[] bytes, int offset) throws NotSupportedException {
		int newOffset = packSpecifiedFrames(bytes, offset, null, "APIC");
		newOffset = packSpecifiedFrames(bytes, newOffset, "APIC", null);
		return newOffset;
	}
	
	private int packSpecifiedFrames(byte[] bytes, int offset, String onlyId, String notId) throws NotSupportedException {
		Iterator<ID3v2FrameSet> setIterator = frameSets.values().iterator();		
		while (setIterator.hasNext()) {
			ID3v2FrameSet frameSet = setIterator.next();
			if ((onlyId == null || onlyId.equals(frameSet.getId())) && (notId == null || !notId.equals(frameSet.getId()))) { 			
				Iterator<ID3v2Frame> frameIterator = frameSet.getFrames().iterator();
				while (frameIterator.hasNext()) {
					ID3v2Frame frame = (ID3v2Frame) frameIterator.next();
					if (frame.getDataLength() > 0) {
						byte[] frameData = frame.toBytes();
						BufferTools.copyIntoByteBuffer(frameData, 0, frameData.length, bytes, offset);
						offset += frameData.length;
					}
				}
			}
		}	
		return offset;
	}
	
	private int packFooter(byte[] bytes, int offset) {
		try {
			BufferTools.stringIntoByteBuffer(FOOTER_TAG, 0, FOOTER_TAG.length(), bytes, offset);
		} catch (UnsupportedEncodingException e) {
		}
		String s[] = version.split(".");
		if (s.length > 0) {
			byte majorVersion = Byte.parseByte(s[0]);
			bytes[offset + MAJOR_VERSION_OFFSET] = majorVersion;
		}
		if (s.length > 1) {
			byte minorVersion = Byte.parseByte(s[0]);
			bytes[offset + MINOR_VERSION_OFFSET] = minorVersion;
		}
		packFlags(bytes, offset);
		BufferTools.packSynchsafeInteger(getDataLength(), bytes, offset + DATA_LENGTH_OFFSET);
		return offset + FOOTER_LENGTH;
	}

	private int calculateDataLength() {
		int length = 0;
		if (extendedHeader) length += extendedHeaderLength;  
		if (footer) length += FOOTER_LENGTH;
		else if (padding) length += PADDING_LENGTH;
		Iterator<ID3v2FrameSet> setIterator = frameSets.values().iterator();
		while (setIterator.hasNext()) {
			ID3v2FrameSet frameSet = setIterator.next();
			Iterator<ID3v2Frame> frameIterator = frameSet.getFrames().iterator();
			while (frameIterator.hasNext()) {
				ID3v2Frame frame = (ID3v2Frame) frameIterator.next();
				length += frame.getLength(); 
			}
		}
		return length;
	}
	
	protected boolean useFrameUnsynchronisation() {
		return false;
	}

	public String getVersion() {
		return version;
	}
		
	private void invalidateDataLength() {
		dataLength = 0;
	}

	public int getDataLength() {
		if (dataLength == 0) {
			dataLength = calculateDataLength();
		}
		return dataLength;
	}
	
	public int getLength() {
		return getDataLength() + HEADER_LENGTH;
	}
	
	public Map<String, ID3v2FrameSet> getFrameSets() {
		return frameSets;
	}
	
	public boolean getPadding() {
		return padding;
	}

	public void setPadding(boolean padding) {
		if (this.padding != padding) {
			invalidateDataLength();
			this.padding = padding;
		}
	}
	
	public boolean hasFooter() {
		return footer;
	}

	public void setFooter(boolean footer) {
		if (this.footer != footer) {
			invalidateDataLength();
			this.footer = footer;
		}
	}

	public boolean hasUnsynchronisation() {
		return unsynchronisation;
	}

	public void setUnsynchronisation(boolean unsynchronisation) {
		if (this.unsynchronisation != unsynchronisation) {
			invalidateDataLength();
			this.unsynchronisation = unsynchronisation;
		}
	}
	
	public boolean getObseleteFormat() {
		return obseleteFormat;
	}

	public String getTrack() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_TRACK_OBSELETE);
		else frameData = extractTextFrameData(ID_TRACK);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setTrack(String track) {
		if (track != null && track.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(track));
			addFrame(createFrame(ID_TRACK, frameData.toBytes()), true);
		}
	}

	public String getArtist() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_ARTIST_OBSELETE);
		else frameData = extractTextFrameData(ID_ARTIST);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setArtist(String artist) {
		if (artist != null && artist.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(artist));
			addFrame(createFrame(ID_ARTIST, frameData.toBytes()), true);
		}
	}
	
	public String getAlbumArtist() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_ALBUM_ARTIST_OBSELETE);
		else frameData = extractTextFrameData(ID_ALBUM_ARTIST);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setAlbumArtist(String albumArtist) {
	if (albumArtist != null && albumArtist.length() > 0) {
		invalidateDataLength();
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(albumArtist));
		addFrame(createFrame(ID_ALBUM_ARTIST, frameData.toBytes()), true);
		}
	}

	public String getTitle() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_TITLE_OBSELETE);
		else frameData = extractTextFrameData(ID_TITLE);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setTitle(String title) {
		if (title != null && title.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(title));
			addFrame(createFrame(ID_TITLE, frameData.toBytes()), true);
		}
	}

	public String getAlbum() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_ALBUM_OBSELETE);
		else frameData = extractTextFrameData(ID_ALBUM);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setAlbum(String album) {
		if (album != null && album.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(album));
			addFrame(createFrame(ID_ALBUM, frameData.toBytes()), true);
		}
	}
	
	public String getYear() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_YEAR_OBSELETE); 
		else frameData = extractTextFrameData(ID_YEAR);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setYear(String year) {
		if (year != null && year.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(year));
			addFrame(createFrame(ID_YEAR, frameData.toBytes()), true);
		}
	}

	public int getGenre() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_GENRE_OBSELETE);
		else frameData = extractTextFrameData(ID_GENRE);
		if (frameData == null || frameData.getText() == null) return -1;
		String text = frameData.getText().toString();
		if (text == null || text.length() == 0) return -1;
		try {
			return extractGenreNumber(text);
		} catch (NumberFormatException e) {
			String description = extractGenreDescription(text);
			if (description != null && description.length() > 0) {
				for (int i = 0; i < ID3v1Genres.GENRES.length; i++) {
					if (ID3v1Genres.GENRES[i].compareToIgnoreCase(description) == 0) return i;
				}
			}
		}
		return -1;
	}

	public void setGenre(int genre) {
		if (genre >= 0) {
			invalidateDataLength();
			String genreDescription;
			try {
				genreDescription = ID3v1Genres.GENRES[genre];
			} catch (ArrayIndexOutOfBoundsException e) {
				genreDescription = "";
			}
			String combinedGenre = "(" + Integer.toString(genre) + ")" + genreDescription;
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(combinedGenre));
			addFrame(createFrame(ID_GENRE, frameData.toBytes()), true);
		}
	}
	
	public String getGenreDescription() {
		int genreNum = getGenre();
		if (genreNum >= 0) {
			try {
				return ID3v1Genres.GENRES[genreNum];
			} catch (ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_GENRE_OBSELETE);
		else frameData = extractTextFrameData(ID_GENRE);
		if (frameData != null && frameData.getText() != null) {
			String text = frameData.getText().toString();
			if (text != null && text.length() > 0) {
				String description = extractGenreDescription(text);
				if (description != null & description.length() > 0) {
					return description;
				}
			}
		}
		return null;
	}
	
	protected int extractGenreNumber(String genreValue) throws NumberFormatException {
		String value = genreValue.trim(); 
		if (value.length() > 0) {
			if (value.charAt(0) == '(') {
				int pos = value.indexOf(')');
				if (pos > 0) {
					return Integer.parseInt(value.substring(1, pos));
				}
			}
		}
		return Integer.parseInt(value);
	}
	
	protected String extractGenreDescription(String genreValue) throws NumberFormatException {
		String value = genreValue.trim(); 
		if (value.length() > 0) {
			if (value.charAt(0) == '(') {
				int pos = value.indexOf(')');
				if (pos > 0) {
					return value.substring(pos + 1);
				}
			}
			return value;
		}
		return null;
	}

	
	public String getComment() {
		ID3v2CommentFrameData frameData;
		if (obseleteFormat) frameData = extractCommentFrameData(ID_COMMENT_OBSELETE, false);
		else frameData = extractCommentFrameData(ID_COMMENT, false);
		if (frameData != null && frameData.getComment() != null) return frameData.getComment().toString();
		return null;
	}

	public void setComment(String comment) {
		if (comment != null && comment.length() > 0) {
			invalidateDataLength();
			ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(useFrameUnsynchronisation(), "eng", null, new EncodedText(comment));
			addFrame(createFrame(ID_COMMENT, frameData.toBytes()), true);
		}
	}
	
	public String getItunesComment() {
		ID3v2CommentFrameData frameData;
		if (obseleteFormat) frameData = extractCommentFrameData(ID_COMMENT_OBSELETE, true);
		else frameData = extractCommentFrameData(ID_COMMENT, true);
		if (frameData != null && frameData.getComment() != null) return frameData.getComment().toString();
		return null;
	}
	
	public void setItunesComment(String itunesComment) {
		if (itunesComment != null && itunesComment.length() > 0) {
			invalidateDataLength();
			ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(useFrameUnsynchronisation(), ITUNES_COMMENT_DESCRIPTION, null, new EncodedText(itunesComment));
			addFrame(createFrame(ID_COMMENT, frameData.toBytes()), true);
		}
	}

	public String getComposer() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_COMPOSER_OBSELETE);
		else frameData = extractTextFrameData(ID_COMPOSER); 
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setComposer(String composer) {
		if (composer != null && composer.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(composer));
			addFrame(createFrame(ID_COMPOSER, frameData.toBytes()), true);
		}
	}
	
	public String getPublisher() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_PUBLISHER_OBSELETE);
		else frameData = extractTextFrameData(ID_PUBLISHER); 
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setPublisher(String publisher) {
		if (publisher != null && publisher.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(publisher));
			addFrame(createFrame(ID_PUBLISHER, frameData.toBytes()), true);
		}
	}
	
	public String getOriginalArtist() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_ORIGINAL_ARTIST_OBSELETE);
		else frameData = extractTextFrameData(ID_ORIGINAL_ARTIST);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setOriginalArtist(String originalArtist) {
		if (originalArtist != null && originalArtist.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(originalArtist));
			addFrame(createFrame(ID_ORIGINAL_ARTIST, frameData.toBytes()), true);
		}
	}

	public String getCopyright() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_COPYRIGHT_OBSELETE);
		else frameData = extractTextFrameData(ID_COPYRIGHT);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setCopyright(String copyright) {
		if (copyright != null && copyright.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(copyright));
			addFrame(createFrame(ID_COPYRIGHT, frameData.toBytes()), true);
		}
	}

	public String getUrl() {
		ID3v2UrlFrameData frameData;
		if (obseleteFormat) frameData = extractUrlFrameData(ID_URL_OBSELETE);
		else frameData = extractUrlFrameData(ID_URL); 
		if (frameData != null) return frameData.getUrl();
		return null;
	}

	public void setUrl(String url) {
		if (url != null && url.length() > 0) {
			invalidateDataLength();
			ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(useFrameUnsynchronisation(), null, url);
			addFrame(createFrame(ID_URL, frameData.toBytes()), true);
		}
	}
	
    public ArrayList<ID3v2ChapterFrameData> getChapters() {
        if (obseleteFormat) {
            return null;
        }

        return extractChapterFrameData(ID_CHAPTER);
    }
    
    public void setChapters(ArrayList<ID3v2ChapterFrameData> chapters) {
        if(chapters != null) {
            invalidateDataLength();
            boolean first = true;
            for(ID3v2ChapterFrameData chapter: chapters) {
                if(first) {
                    first = false;
                    addFrame(createFrame(ID_CHAPTER, chapter.toBytes()), true);
                } else {
                    addFrame(createFrame(ID_CHAPTER, chapter.toBytes()), false);
                }
            }
        }
    }

    public ArrayList<ID3v2ChapterTOCFrameData> getChapterTOC() {
        if (obseleteFormat) {
            return null;
        }

        return extractChapterTOCFrameData(ID_CHAPTER_TOC);
    }
    
    public void setChapterTOC(ArrayList<ID3v2ChapterTOCFrameData> toc) {
        if(toc != null) {
            invalidateDataLength();
            boolean first = true;
            for(ID3v2ChapterTOCFrameData ct: toc) {
                if(first) {
                    first = false;
                    addFrame(createFrame(ID_CHAPTER_TOC, ct.toBytes()), true);
                } else {
                    addFrame(createFrame(ID_CHAPTER_TOC, ct.toBytes()), false);
                }
            }
        }
    }

	public String getEncoder() {
		ID3v2TextFrameData frameData;
		if (obseleteFormat) frameData = extractTextFrameData(ID_ENCODER_OBSELETE);
		else frameData = extractTextFrameData(ID_ENCODER); 
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}

	public void setEncoder(String encoder) {
		if (encoder != null && encoder.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(encoder));
			addFrame(createFrame(ID_ENCODER, frameData.toBytes()), true);
		}
	}
	
	public byte[] getAlbumImage() {
		ID3v2PictureFrameData frameData;
		if (obseleteFormat) frameData = createPictureFrameData(ID_IMAGE_OBSELETE);
		else frameData = createPictureFrameData(ID_IMAGE); 
		if (frameData != null) return frameData.getImageData();
		return null;
	}

	public void setAlbumImage(byte[] albumImage, String mimeType) {
		if (albumImage != null && albumImage.length > 0 && mimeType != null && mimeType.length() > 0) { 
			invalidateDataLength();
			ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(useFrameUnsynchronisation(),  mimeType, (byte)0, null, albumImage); 
			addFrame(createFrame(ID_IMAGE, frameData.toBytes()), true);
		}
	}

	public String getAlbumImageMimeType() {
		ID3v2PictureFrameData frameData;
		if (obseleteFormat) frameData = createPictureFrameData(ID_IMAGE_OBSELETE);
		else frameData = createPictureFrameData(ID_IMAGE);
		if (frameData != null && frameData.getMimeType() != null) return frameData.getMimeType();
		return null;
	}
	
	public void clearFrameSet(String id) {
		if (frameSets.remove(id) != null) {
			invalidateDataLength();
		}
	}

    private ArrayList<ID3v2ChapterFrameData> extractChapterFrameData(String id) {
        ID3v2FrameSet frameSet = frameSets.get(id);
        if (frameSet != null) {
            ArrayList<ID3v2ChapterFrameData> chapterData = new ArrayList<ID3v2ChapterFrameData>();
            List<ID3v2Frame> frames = frameSet.getFrames();
            for (ID3v2Frame frame : frames) {
                ID3v2ChapterFrameData frameData;
                try {
                    frameData = new ID3v2ChapterFrameData(useFrameUnsynchronisation(),
                            frame.getData());
                    chapterData.add(frameData);
                } catch (InvalidDataException e) {
                    // do nothing
                }
            }
            return chapterData;
        }
        return null;
    }

    private ArrayList<ID3v2ChapterTOCFrameData> extractChapterTOCFrameData(String id) {
        ID3v2FrameSet frameSet = frameSets.get(id);
        if (frameSet != null) {
            ArrayList<ID3v2ChapterTOCFrameData> chapterData = new ArrayList<ID3v2ChapterTOCFrameData>();
            List<ID3v2Frame> frames = frameSet.getFrames();
            for (ID3v2Frame frame : frames) {
                ID3v2ChapterTOCFrameData frameData;
                try {
                    frameData = new ID3v2ChapterTOCFrameData(useFrameUnsynchronisation(),
                            frame.getData());
                    chapterData.add(frameData);
                } catch (InvalidDataException e) {
                    // do nothing
                }
            }
            return chapterData;
        }
        return null;
    }
	
	private ID3v2TextFrameData extractTextFrameData(String id) {
		ID3v2FrameSet frameSet = frameSets.get(id);
		if (frameSet != null) {
			ID3v2Frame frame = (ID3v2Frame) frameSet.getFrames().get(0);
			ID3v2TextFrameData frameData;
			try {
				frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), frame.getData());
				return frameData;
			} catch (InvalidDataException e) {
				// do nothing
			}
		}
		return null;
	}
	
	private ID3v2UrlFrameData extractUrlFrameData(String id) {
		ID3v2FrameSet frameSet = frameSets.get(id);
		if (frameSet != null) {
			ID3v2Frame frame = (ID3v2Frame) frameSet.getFrames().get(0);
			ID3v2UrlFrameData frameData;
			try {
				frameData = new ID3v2UrlFrameData(useFrameUnsynchronisation(), frame.getData());
				return frameData;
			} catch (InvalidDataException e) {
				// do nothing
			}
		}
		return null;
	}
	
	private ID3v2CommentFrameData extractCommentFrameData(String id, boolean itunes) {
		ID3v2FrameSet frameSet = frameSets.get(id);
		if (frameSet != null) {
			Iterator<ID3v2Frame> iterator = frameSet.getFrames().iterator();
			while (iterator.hasNext()) {
				ID3v2Frame frame = (ID3v2Frame) iterator.next();
				ID3v2CommentFrameData frameData;
				try {
					frameData = new ID3v2CommentFrameData(useFrameUnsynchronisation(), frame.getData());
					if (itunes && ITUNES_COMMENT_DESCRIPTION.equals(frameData.getDescription().toString())) {
						return frameData;
					} else if (! itunes) {
						return frameData;
					}
				} catch (InvalidDataException e) {
					// Do nothing
				}
			}
		}
		return null;
	}

	private ID3v2PictureFrameData createPictureFrameData(String id) {
		ID3v2FrameSet frameSet = frameSets.get(id);
		if (frameSet != null) {
			ID3v2Frame frame = (ID3v2Frame) frameSet.getFrames().get(0);
			ID3v2PictureFrameData frameData;
			try {
				if (obseleteFormat) frameData = new ID3v2ObseletePictureFrameData(useFrameUnsynchronisation(), frame.getData());
				else frameData = new ID3v2PictureFrameData(useFrameUnsynchronisation(), frame.getData());
				return frameData;
			} catch (InvalidDataException e) {
				// do nothing
			}
		}
		return null;
	}

	public boolean equals(Object obj) {
		if (! (obj instanceof AbstractID3v2Tag)) return false;
		if (super.equals(obj)) return true;
		AbstractID3v2Tag other = (AbstractID3v2Tag) obj;
		if (unsynchronisation != other.unsynchronisation) return false;
		if (extendedHeader != other.extendedHeader) return false;
		if (experimental != other.experimental) return false;
		if (footer != other.footer) return false;
		if (compression != other.compression) return false;
		if (dataLength != other.dataLength) return false;
		if (extendedHeaderLength != other.extendedHeaderLength) return false;
		if (version == null) {
			if (other.version != null) return false;
		} else if (other.version == null) return false;
		else if (! version.equals(other.version)) return false;
		if (frameSets == null) {
			if (other.frameSets != null) return false;
		} else if (other.frameSets == null) return false;
		else if (! frameSets.equals(other.frameSets)) return false;
		return true;
	}
}
