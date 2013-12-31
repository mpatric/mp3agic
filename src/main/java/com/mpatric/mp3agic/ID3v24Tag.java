package com.mpatric.mp3agic;

import java.util.ArrayList;

public class ID3v24Tag extends AbstractID3v2Tag implements ID3v24 {

	public static final String VERSION = "4.0";

	public ID3v24Tag() {
		super();
		version = VERSION;
	}

	public ID3v24Tag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		super(buffer);
	}
	
	public String[] getTitles() {
		return getTextFrameValues(ID_TITLE);
	}

	public void setTitles(String[] titles) {
		setTextFrameValues(ID_TITLE, titles);
	}

	public String[] getArtists() {
		return getTextFrameValues(ID_ARTIST);
	}

	public void setArtists(String[] artists) {
		setTextFrameValues(ID_ARTIST, artists);
	}

	public String[] getAlbums() {
		return getTextFrameValues(ID_ALBUM);
	}

	public void setAlbums(String[] albums) {
		setTextFrameValues(ID_ALBUM, albums);
	}

	public String[] getTracks() {
		return getTextFrameValues(ID_TRACK);
	}

	public void setTracks(String[] tracks) {
		setTextFrameValues(ID_TRACK, tracks);
	}

	public String[] getYears() {
		return getTextFrameValues(ID_YEAR);
	}

	public void setYears(String[] years) {
		setTextFrameValues(ID_YEAR, years);
	}

	public String[] getGenreDescriptions() {
		return getTextFrameValues(ID_GENRE);
	}

	public void setGenreDescriptions(String[] genres) {
		setTextFrameValues(ID_GENRE, genres);
	}

	public String[] getComments() {
		return getTextFrameValues(ID_COMMENT);
	}

	public void setComments(String[] comments) {
		setTextFrameValues(ID_COMMENT, comments);
	}

	public String[] getComposers() {
		return getTextFrameValues(ID_COMPOSER);
	}

	public void setComposers(String[] composers) {
		setTextFrameValues(ID_COMPOSER, composers);
	}

	public String[] getPublishers() {
		return getTextFrameValues(ID_PUBLISHER);
	}

	public void setPublishers(String[] publishers) {
		setTextFrameValues(ID_PUBLISHER, publishers);
	}

	public String[] getOriginalArtists() {
		return getTextFrameValues(ID_ORIGINAL_ARTIST);
	}

	public void setOriginalArtists(String[] originalArtists) {
		setTextFrameValues(ID_ORIGINAL_ARTIST, originalArtists);
	}

	public String[] getAlbumArtists() {
		return getTextFrameValues(ID_ALBUM_ARTIST);
	}

	public void setAlbumArtists(String[] albumArtists) {
		setTextFrameValues(ID_ALBUM_ARTIST, albumArtists);
	}

	public String[] getCopyrights() {
		return getTextFrameValues(ID_COPYRIGHT);
	}

	public void setCopyrights(String[] copyrights) {
		setTextFrameValues(ID_COPYRIGHT, copyrights);
	}

	public String[] getUrls() {
		return getTextFrameValues(ID_URL);
	}

	public void setUrls(String[] urls) {
		setTextFrameValues(ID_URL, urls);
	}

	public String[] getPartOfSets() {
		return getTextFrameValues(ID_PART_OF_SET);
	}

	public void setPartOfSets(String[] partOfSets) {
		setTextFrameValues(ID_PART_OF_SET, partOfSets);
	}

	public String[] getGroupings() {
		return getTextFrameValues(ID_GROUPING);
	}

	public void setGroupings(String[] groupings) {
		setTextFrameValues(ID_GROUPING, groupings);
	}

	public String[] getEncoders() {
		return getTextFrameValues(ID_ENCODER);
	}

	public void setEncoders(String[] encoders) {
		setTextFrameValues(ID_ENCODER, encoders);
	}

	protected void unpackFlags(byte[] buffer) {
		unsynchronisation = BufferTools.checkBit(buffer[FLAGS_OFFSET], UNSYNCHRONISATION_BIT);
		extendedHeader = BufferTools.checkBit(buffer[FLAGS_OFFSET], EXTENDED_HEADER_BIT);
		experimental = BufferTools.checkBit(buffer[FLAGS_OFFSET], EXPERIMENTAL_BIT);
		footer = BufferTools.checkBit(buffer[FLAGS_OFFSET], FOOTER_BIT);
	}
	
	protected void packFlags(byte[] bytes, int offset) {
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], UNSYNCHRONISATION_BIT, unsynchronisation);
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], EXTENDED_HEADER_BIT, extendedHeader);
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], EXPERIMENTAL_BIT, experimental);		
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], FOOTER_BIT, footer);
	}
	
	protected boolean useFrameUnsynchronisation() {
		return unsynchronisation;
	}
	
	protected ID3v2Frame createFrame(byte[] buffer, int currentOffset) throws InvalidDataException {
		return new ID3v24Frame(buffer, currentOffset);
	}
	
	protected ID3v2Frame createFrame(String id, byte[] data) {
		return new ID3v24Frame(id, data);
	}
	
	private String[] getTextFrameValues(String id) {
		ID3v2TextFrameData frameData = extractTextFrameData(id);
		return (frameData == null || frameData.getText() == null) ? null :
		frameData.getText().toStrings();
	}
	
	private void setTextFrameValues(String id, String[] values) {
		if (values != null && values.length > 0) {
			invalidateDataLength();
			ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(values));
			addFrame(createFrame(id, frameData.toBytes()), true);
		}
	}
	
	@Override
	public void setGenreDescription(String text) {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(useFrameUnsynchronisation(), new EncodedText(text));
		ID3v2FrameSet frameSet = getFrameSets().get(ID_GENRE);
		if (frameSet == null) {
			getFrameSets().put(ID_GENRE, frameSet = new ID3v2FrameSet(ID_GENRE));
		}
		frameSet.clear();
		frameSet.addFrame(createFrame(ID_GENRE, frameData.toBytes()));
	}
}
