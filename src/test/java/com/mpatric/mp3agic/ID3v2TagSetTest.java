package com.mpatric.mp3agic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ID3v2TagSetTest {
	@Test
	public void shouldClearTheFrameWhenSetTrackNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setTrack("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setTrack(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetTrackEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setTrack("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setTrack("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPartOfSetNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPartOfSet("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPartOfSet(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPartOfSetEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPartOfSet("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPartOfSet("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetGroupingNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setGrouping("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setGrouping(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetGroupingEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setGrouping("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setGrouping("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetArtistNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setArtist("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setArtist(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetArtistEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setArtist("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setArtist("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumArtistNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbumArtist("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbumArtist(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumArtistEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbumArtist("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbumArtist("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetTitleNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setTitle("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setTitle(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetTitleEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setTitle("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setTitle("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbum("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbum(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbum("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbum("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetYearNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setYear("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setYear(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetYearEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setYear("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setYear("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetDateNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setDate("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setDate(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetDateEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setDate("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setDate("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetKeyNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setKey("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setKey(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetKeyEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setKey("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setKey("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCommentNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setComment("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setComment(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCommentEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setComment("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setComment("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetItunesCommentNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setItunesComment("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setItunesComment(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetItunesCommentEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setItunesComment("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setItunesComment("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetLyricsNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setLyrics("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setLyrics(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetLyricsEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setLyrics("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setLyrics("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetComposerNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setComposer("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setComposer(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetComposerEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setComposer("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setComposer("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPublisherNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPublisher("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPublisher(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPublisherEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPublisher("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPublisher("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetOriginalArtistNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setOriginalArtist("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setOriginalArtist(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetOriginalArtistEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setOriginalArtist("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setOriginalArtist("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCopyrightNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setCopyright("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setCopyright(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCopyrightEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setCopyright("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setCopyright("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetArtistUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setArtistUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setArtistUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetArtistUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setArtistUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setArtistUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCommercialUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setCommercialUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setCommercialUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCommercialUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setCommercialUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setCommercialUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCopyrightUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setCopyrightUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setCopyrightUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetCopyrightUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setCopyrightUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setCopyrightUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAudiofileUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAudiofileUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAudiofileUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAudiofileUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAudiofileUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAudiofileUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAudioSourceUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAudioSourceUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAudioSourceUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAudioSourceUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAudioSourceUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAudioSourceUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetRadiostationUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setRadiostationUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setRadiostationUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetRadiostationUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setRadiostationUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setRadiostationUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPaymentUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPaymentUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPaymentUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPaymentUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPaymentUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPaymentUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPublisherUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPublisherUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPublisherUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetPublisherUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setPublisherUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setPublisherUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetUrlNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setUrl(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetUrlEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setUrl("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setUrl("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetChaptersNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		ArrayList<ID3v2ChapterFrameData> chapters = new ArrayList<>();
		ID3v2ChapterFrameData chapter = new ID3v2ChapterFrameData(false);
		chapter.setId("abc");
		chapters.add(chapter);
		id3Tag.setChapters(chapters);
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setChapters(null);
		assertEquals(length, id3Tag.getDataLength());
	}


	@Test
	public void shouldClearTheFrameWhenSetChapterTOCNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		ArrayList<ID3v2ChapterTOCFrameData> chapterTOCs = new ArrayList<>();
		ID3v2ChapterTOCFrameData chapterTOC = new ID3v2ChapterTOCFrameData(false);
		chapterTOC.setId("abc");
		chapterTOC.setChildren(new String[]{});
		chapterTOCs.add(chapterTOC);
		id3Tag.setChapterTOC(chapterTOCs);
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setChapterTOC(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetEncoderNull() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setEncoder("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setEncoder(null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetEncoderEmpty() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setEncoder("abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setEncoder("");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumImageNullAlbumImage() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbumImage(new byte[]{'a'}, "abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbumImage(null, "abc");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumImageNullMimeType() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbumImage(new byte[]{'a'}, "abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbumImage(new byte[]{'a'}, null);
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumImageEmptyAlbumImage() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbumImage(new byte[]{'a'}, "abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbumImage(new byte[]{}, "abc");
		assertEquals(length, id3Tag.getDataLength());
	}

	@Test
	public void shouldClearTheFrameWhenSetAlbumImageEmptyMimeType() {
		ID3v24Tag id3Tag = new ID3v24Tag();
		int length = id3Tag.getDataLength();
		id3Tag.setAlbumImage(new byte[]{'a'}, "abc");
		assertTrue(id3Tag.getDataLength() > length);
		id3Tag.setAlbumImage(new byte[]{'a'}, "");
		assertEquals(length, id3Tag.getDataLength());
	}
}
