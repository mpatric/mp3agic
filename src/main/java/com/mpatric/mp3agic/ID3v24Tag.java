package com.mpatric.mp3agic;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ID3v24Tag extends AbstractID3v2Tag implements ID3v24 {

	public static final String VERSION = "4.0";

	public ID3v24Tag() {
		super();
		version = VERSION;
	}

	public ID3v24Tag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		super(buffer);
	}
	
	@Override
	public List<String> splitTextFrameValues(String s) {
		return (s == null) ? new ArrayList<String>() : nsplit(s);
	}

	public void setTitles(Iterable<String> titles) {
		setTitle(njoin(titles));
	}

	public void setArtists(Iterable<String> artists) {
		setArtist(njoin(artists));
	}

	public void setAlbums(Iterable<String> albums) {
		setAlbum(njoin(albums));
	}

	public void setTracks(Iterable<String> tracks) {
		setTrack(njoin(tracks));
	}

	public void setYears(Iterable<String> years) {
		setYear(njoin(years));
	}

	public void setGenreDescriptions(Iterable<String> genres) {
		setGenreDescription(njoin(genres));
	}

	public void setComments(Iterable<String> comments) {
		setComment(njoin(comments));
	}

	public void setComposers(Iterable<String> composers) {
		setComposer(njoin(composers));
	}

	public void setPublishers(Iterable<String> publishers) {
		setPublisher(njoin(publishers));
	}

	public void setOriginalArtists(Iterable<String> originalArtists) {
		setOriginalArtist(njoin(originalArtists));
	}

	public void setAlbumArtists(Iterable<String> albumArtists) {
		setAlbumArtist(njoin(albumArtists));
	}

	public void setCopyrights(Iterable<String> copyrights) {
		setCopyright(njoin(copyrights));
	}

	public void setUrls(Iterable<String> urls) {
		setUrl(njoin(urls));
	}

	public void setPartOfSets(Iterable<String> partOfSets) {
		setPartOfSet(njoin(partOfSets));
	}

	public void setGroupings(Iterable<String> groupings) {
		setGrouping(njoin(groupings));
	}

	public void setEncoders(Iterable<String> encoders) {
		setEncoder(njoin(encoders));
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

	private List<String> nsplit(String s) {
		return (s == null) ? null : Arrays.asList(s.split("\0"));
	}

	private String njoin(Iterable<String> vals) {
		if (vals == null)
			return null;

		StringBuilder b = new StringBuilder("\0");
		for (String s : vals) {
			b.append(s);
			b.append("\0");
		}

		return b.substring(1);
	}
}
