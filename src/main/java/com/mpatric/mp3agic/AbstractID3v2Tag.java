package com.mpatric.mp3agic;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
	
	public static final Pattern GENRE_REGEX = Pattern.compile("\\(?(\\d+)\\)?(.*)?"); 

	private final Map<String, ID3v2FrameSet> frameSets = new TreeMap<String, ID3v2FrameSet>();

	public AbstractID3v2Tag() {
	}

	public AbstractID3v2Tag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		this(bytes, false);
	}
	
	public AbstractID3v2Tag(byte[] bytes, boolean obseleteFormat) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
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
		BufferTools.stringIntoByteBuffer(TAG, 0, TAG.length(), bytes, offset);
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
		BufferTools.stringIntoByteBuffer(FOOTER_TAG, 0, FOOTER_TAG.length(), bytes, offset);
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

	private String getTextFrameText(String frameId) {
		ID3v2TextFrameData frameData = extractTextFrameData(frameId);
		if (frameData != null && frameData.getText() != null) return frameData.getText().toString();
		return null;
	}
	
	private void setTextFrameText(String frameId, String text) {
		if (text != null && text.length() > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), text);
			addFrame(createFrame(frameId, frameData.toBytes()), true);
		}
	}

	public String getTrack() {
		return getTextFrameText(obseleteFormat ? ID_TRACK_OBSELETE : ID_TRACK);
	}

	public void setTrack(String track) {
		setTextFrameText(ID_TRACK, track);
	}

	public String getArtist() {
		return getTextFrameText(obseleteFormat ? ID_ARTIST_OBSELETE : ID_ARTIST);
	}

	public void setArtist(String artist) {
		setTextFrameText(ID_ARTIST, artist);
	}
	
	public String getAlbumArtist() {
		return getTextFrameText(obseleteFormat ? ID_ALBUM_ARTIST_OBSELETE : ID_ALBUM_ARTIST);
	}

	public void setAlbumArtist(String albumArtist) {
		setTextFrameText(ID_ALBUM_ARTIST, albumArtist);
	}

	public String getTitle() {
		return getTextFrameText(obseleteFormat ? ID_TITLE_OBSELETE : ID_TITLE);
	}

	public void setTitle(String title) {
		setTextFrameText(ID_TITLE, title);
	}

	public String getAlbum() {
		return getTextFrameText(obseleteFormat ? ID_ALBUM_OBSELETE : ID_ALBUM);
	}

	public void setAlbum(String album) {
		setTextFrameText(ID_ALBUM, album);
	}
	
	public String getYear() {
		return getTextFrameText(obseleteFormat ? ID_YEAR_OBSELETE : ID_YEAR);
	}

	public void setYear(String year) {
		setTextFrameText(ID_YEAR, year);
	}

	protected String getGenreText() {
		return getTextFrameText(obseleteFormat ? ID_GENRE_OBSELETE : ID_GENRE);
	}

	protected void setGenreText(String genreText) {
		setTextFrameText(ID_GENRE, genreText);
	}
	
	public int getGenre() {
		String text = getGenreText();
		if (text == null || text.length() == 0) return -1;

		Matcher m = GENRE_REGEX.matcher(text);
		if (!m.matches()) {
			return -1;
		}

		return Integer.parseInt(m.group(1));
	}

	public void setGenre(int genre) {
		if (genre >= 0) {
			String genreDescription = (ID3v1Genres.GENRES.length > genre) ? 
					ID3v1Genres.GENRES[genre] : ""; 
			setGenreText(String.format("(%d)%s", genre, genreDescription));
		}
	}
	
	public String getGenreDescription() {
		String genreText = getGenreText();

		Matcher m = GENRE_REGEX.matcher(genreText);
		if (!m.matches()) {
			return null;
		}

		int genreNum = Integer.parseInt(m.group(1));
		if (genreNum >= 0) {
			try {
				return ID3v1Genres.GENRES[genreNum];
			} catch (ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}
		
		return m.group(2);
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
			ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(useFrameUnsynchronisation(), "eng", null, comment);
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
			ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(useFrameUnsynchronisation(), ITUNES_COMMENT_DESCRIPTION, null, itunesComment);
			addFrame(createFrame(ID_COMMENT, frameData.toBytes()), true);
		}
	}


	public String getComposer() {
		return getTextFrameText(obseleteFormat ? ID_COMPOSER_OBSELETE : ID_COMPOSER);
	}

	public void setComposer(String composer) {
		setTextFrameText(ID_COMPOSER, composer);
	}
	
	public String getPublisher() {
		return getTextFrameText(obseleteFormat ? ID_PUBLISHER_OBSELETE : ID_PUBLISHER);
	}

	public void setPublisher(String publisher) {
		setTextFrameText(ID_PUBLISHER, publisher);
	}
	
	public String getOriginalArtist() {
		return getTextFrameText(obseleteFormat ? ID_ORIGINAL_ARTIST_OBSELETE : ID_ORIGINAL_ARTIST);
	}

	public void setOriginalArtist(String originalArtist) {
		setTextFrameText(ID_ORIGINAL_ARTIST, originalArtist);
	}

	public String getCopyright() {
		return getTextFrameText(obseleteFormat ? ID_COPYRIGHT_OBSELETE : ID_COPYRIGHT);
	}

	public void setCopyright(String copyright) {
		setTextFrameText(ID_COPYRIGHT, copyright);
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

	public String getEncoder() {
		return getTextFrameText(obseleteFormat ? ID_ENCODER_OBSELETE : ID_ENCODER);
	}

	public void setEncoder(String encoder) {
		setTextFrameText(ID_ENCODER, encoder);
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
			ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(useFrameUnsynchronisation(), 
					Encoding.getDefault(), mimeType, (byte)0, null, albumImage); 
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
