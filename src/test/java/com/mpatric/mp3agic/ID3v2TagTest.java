package com.mpatric.mp3agic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class ID3v2TagTest {

	private static final byte BYTE_I = 0x49;
	private static final byte BYTE_D = 0x44;
	private static final byte BYTE_3 = 0x33;
	private static final byte[] ID3V2_HEADER = {BYTE_I, BYTE_D, BYTE_3, 4, 0, 0, 0, 0, 2, 1};

	@Test
	public void shouldInitialiseFromHeaderBlockWithValidHeaders() throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] header = BufferTools.copyBuffer(ID3V2_HEADER, 0, ID3V2_HEADER.length);
		header[3] = 2;
		header[4] = 0;
		ID3v2 id3v2tag;
		id3v2tag = createTag(header);
		assertEquals("2.0", id3v2tag.getVersion());
		header[3] = 3;
		id3v2tag = createTag(header);
		assertEquals("3.0", id3v2tag.getVersion());
		header[3] = 4;
		id3v2tag = createTag(header);
		assertEquals("4.0", id3v2tag.getVersion());
	}

	@Test
	public void shouldCalculateCorrectDataLengthsFromHeaderBlock() throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] header = BufferTools.copyBuffer(ID3V2_HEADER, 0, ID3V2_HEADER.length);
		ID3v2 id3v2tag = createTag(header);
		assertEquals(257, id3v2tag.getDataLength());
		header[8] = 0x09;
		header[9] = 0x41;
		id3v2tag = createTag(header);
		assertEquals(1217, id3v2tag.getDataLength());
	}

	@Test
	public void shouldThrowExceptionForNonSupportedVersionInId3v2HeaderBlock() throws NoSuchTagException, InvalidDataException {
		byte[] header = BufferTools.copyBuffer(ID3V2_HEADER, 0, ID3V2_HEADER.length);
		header[3] = 5;
		header[4] = 0;
		try {
			ID3v2TagFactory.createTag(header);
			fail("UnsupportedTagException expected but not thrown");
		} catch (UnsupportedTagException e) {
			// expected
		}
	}

	@Test
	public void shouldSortId3TagsAlphabetically() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tags.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		Map<String, ID3v2FrameSet> frameSets = id3v2tag.getFrameSets();
		Iterator<ID3v2FrameSet> frameSetIterator = frameSets.values().iterator();
		String lastKey = "";
		while (frameSetIterator.hasNext()) {
			ID3v2FrameSet frameSet = frameSetIterator.next();
			assertTrue(frameSet.getId().compareTo(lastKey) > 0);
			lastKey = frameSet.getId();
		}
	}

	@Test
	public void shouldReadFramesFromMp3With32Tag() throws IOException, NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tags.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("3.0", id3v2tag.getVersion());
		assertEquals(0x44B, id3v2tag.getLength());
		assertEquals(12, id3v2tag.getFrameSets().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TENC")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("WXXX")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TCOP")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TOPE")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TCOM")).getFrames().size());
		assertEquals(2, (id3v2tag.getFrameSets().get("COMM")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TPE1")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TALB")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TRCK")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TYER")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TCON")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TIT2")).getFrames().size());
	}

	@Test
	public void shouldReadId3v2WithFooter() throws IOException, NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv24tags.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("4.0", id3v2tag.getVersion());
		assertEquals(0x44B, id3v2tag.getLength());
	}

	@Test
	public void shouldReadTagFieldsFromMp3With24tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v24tagswithalbumimage.mp3");
		ID3v2 id3v24tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("4.0", id3v24tag.getVersion());
		assertEquals("1", id3v24tag.getTrack());
		assertEquals("ARTIST123456789012345678901234", id3v24tag.getArtist());
		assertEquals("TITLE1234567890123456789012345", id3v24tag.getTitle());
		assertEquals("ALBUM1234567890123456789012345", id3v24tag.getAlbum());
		assertEquals(0x0d, id3v24tag.getGenre());
		assertEquals("Pop", id3v24tag.getGenreDescription());
		assertEquals("COMMENT123456789012345678901", id3v24tag.getComment());
		assertEquals("LYRICS1234567890123456789012345", id3v24tag.getLyrics());
		assertEquals("COMPOSER23456789012345678901234", id3v24tag.getComposer());
		assertEquals("ORIGARTIST234567890123456789012", id3v24tag.getOriginalArtist());
		assertEquals("COPYRIGHT2345678901234567890123", id3v24tag.getCopyright());
		assertEquals("URL2345678901234567890123456789", id3v24tag.getUrl());
		assertEquals("COMMERCIALURL234567890123456789", id3v24tag.getCommercialUrl());
		assertEquals("COPYRIGHTURL2345678901234567890", id3v24tag.getCopyrightUrl());
		assertEquals("OFFICIALARTISTURL23456789012345", id3v24tag.getArtistUrl());
		assertEquals("OFFICIALAUDIOFILE23456789012345", id3v24tag.getAudiofileUrl());
		assertEquals("OFFICIALAUDIOSOURCE234567890123", id3v24tag.getAudioSourceUrl());
		assertEquals("INTERNETRADIOSTATIONURL23456783", id3v24tag.getRadiostationUrl());
		assertEquals("PAYMENTURL234567890123456789012", id3v24tag.getPaymentUrl());
		assertEquals("PUBLISHERURL2345678901234567890", id3v24tag.getPublisherUrl());
		assertEquals("ENCODER234567890123456789012345", id3v24tag.getEncoder());
		assertEquals(1885, id3v24tag.getAlbumImage().length);
		assertEquals("image/png", id3v24tag.getAlbumImageMimeType());
	}

	@Test
	public void shouldReadTagFieldsFromMp3With32tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tagswithalbumimage.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("1", id3tag.getTrack());
		assertEquals("ARTIST123456789012345678901234", id3tag.getArtist());
		assertEquals("TITLE1234567890123456789012345", id3tag.getTitle());
		assertEquals("ALBUM1234567890123456789012345", id3tag.getAlbum());
		assertEquals("2001", id3tag.getYear());
		assertEquals(0x0d, id3tag.getGenre());
		assertEquals("Pop", id3tag.getGenreDescription());
		assertEquals("COMMENT123456789012345678901", id3tag.getComment());
		assertEquals("LYRICS1234567890123456789012345", id3tag.getLyrics());
		assertEquals("COMPOSER23456789012345678901234", id3tag.getComposer());
		assertEquals("ORIGARTIST234567890123456789012", id3tag.getOriginalArtist());
		assertEquals("COPYRIGHT2345678901234567890123", id3tag.getCopyright());
		assertEquals("URL2345678901234567890123456789", id3tag.getUrl());
		assertEquals("ENCODER234567890123456789012345", id3tag.getEncoder());
		assertEquals(1885, id3tag.getAlbumImage().length);
		assertEquals("image/png", id3tag.getAlbumImageMimeType());
	}

	@Test
	public void shouldConvert23TagToBytesAndBackToEquivalentTag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		byte[] data = id3tag.toBytes();
		ID3v2 id3tagCopy = new ID3v23Tag(data);
		assertEquals(2340, data.length);
		assertEquals(id3tag, id3tagCopy);
	}

	@Test
	public void shouldConvert24TagWithFooterToBytesAndBackToEquivalentTag() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setFooter(true);
		byte[] data = id3tag.toBytes();
		ID3v2 id3tagCopy = new ID3v24Tag(data);
		assertEquals(2350, data.length);
		assertEquals(id3tag, id3tagCopy);
	}

	@Test
	public void shouldConvert24TagWithPaddingToBytesAndBackToEquivalentTag() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setPadding(true);
		byte[] data = id3tag.toBytes();
		ID3v2 id3tagCopy = new ID3v24Tag(data);
		assertEquals(2340 + AbstractID3v2Tag.PADDING_LENGTH, data.length);
		assertEquals(id3tag, id3tagCopy);
	}

	@Test
	public void shouldNotUsePaddingOnA24TagIfItHasAFooter() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setFooter(true);
		id3tag.setPadding(true);
		byte[] data = id3tag.toBytes();
		assertEquals(2350, data.length);
	}

	@Test
	public void shouldExtractGenreNumberFromCombinedGenreStringsCorrectly() throws Exception {
		ID3v23TagForTesting id3tag = new ID3v23TagForTesting();
		try {
			id3tag.extractGenreNumber("");
			fail("NumberFormatException expected but not thrown");
		} catch (NumberFormatException e) {
			// expected
		}
		assertEquals(13, id3tag.extractGenreNumber("13"));
		assertEquals(13, id3tag.extractGenreNumber("(13)"));
		assertEquals(13, id3tag.extractGenreNumber("(13)Pop"));
	}

	@Test
	public void shouldExtractGenreDescriptionFromCombinedGenreStringsCorrectly() throws Exception {
		ID3v23TagForTesting id3tag = new ID3v23TagForTesting();
		assertNull(id3tag.extractGenreDescription(""));
		assertEquals("", id3tag.extractGenreDescription("(13)"));
		assertEquals("Pop", id3tag.extractGenreDescription("(13)Pop"));
	}

	@Test
	public void shouldSetCombinedGenreOnTag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertEquals("(13)Pop", genre);
	}

	@Test
	public void testSetGenreDescriptionOn23Tag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		id3tag.setGenreDescription("Jazz");
		assertEquals("Jazz", id3tag.getGenreDescription());
		assertEquals(8, id3tag.getGenre());

		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertEquals("(8)Jazz", genre);
	}

	@Test
	public void testSetGenreDescriptionOn23TagWithUnknownGenre() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		try {
			id3tag.setGenreDescription("Bebop");
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// fine
		}
	}

	@Test
	public void testSetGenreDescriptionOn24Tag() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setGenreDescription("Jazz");
		assertEquals("Jazz", id3tag.getGenreDescription());
		assertEquals(8, id3tag.getGenre());

		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertEquals("Jazz", genre);
	}

	@Test
	public void testSetGenreDescriptionOn24TagWithUnknownGenre() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setGenreDescription("Bebop");
		assertEquals("Bebop", id3tag.getGenreDescription());
		assertEquals(-1, id3tag.getGenre());

		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertEquals("Bebop", genre);
	}

	@Test
	public void shouldReadCombinedGenreInTag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		byte[] bytes = id3tag.toBytes();
		ID3v2 id3tagFromData = new ID3v23Tag(bytes);
		assertEquals(13, id3tagFromData.getGenre());
		assertEquals("Pop", id3tagFromData.getGenreDescription());
	}

	@Test
	public void shouldGetCommentAndItunesComment() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/withitunescomment.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("COMMENT123456789012345678901", id3tag.getComment());
		assertEquals(" 00000A78 00000A74 00000C7C 00000C6C 00000000 00000000 000051F7 00005634 00000000 00000000", id3tag.getItunesComment());
	}

	@Test
	public void shouldReadFramesFromMp3WithObselete32Tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/obsolete.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("2.0", id3v2tag.getVersion());
		assertEquals(0x3c5a2, id3v2tag.getLength());
		assertEquals(10, id3v2tag.getFrameSets().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TCM")).getFrames().size());
		assertEquals(2, (id3v2tag.getFrameSets().get("COM")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TP1")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TAL")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TRK")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TPA")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TYE")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("PIC")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TCO")).getFrames().size());
		assertEquals(1, (id3v2tag.getFrameSets().get("TT2")).getFrames().size());
	}

	@Test
	public void shouldReadTagFieldsFromMp3WithObselete32tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/obsolete.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("2009", id3tag.getYear());
		assertEquals("4/15", id3tag.getTrack());
		assertEquals("image/png", id3tag.getAlbumImageMimeType());
		assertEquals(40, id3tag.getGenre());
		assertEquals("Alt Rock", id3tag.getGenreDescription());
		assertEquals("NAME1234567890123456789012345678901234567890", id3tag.getTitle());
		assertEquals("ARTIST1234567890123456789012345678901234567890", id3tag.getArtist());
		assertEquals("COMPOSER1234567890123456789012345678901234567890", id3tag.getComposer());
		assertEquals("ALBUM1234567890123456789012345678901234567890", id3tag.getAlbum());
		assertEquals("COMMENTS1234567890123456789012345678901234567890", id3tag.getComment());
	}

	@Test
	public void shouldReadTagFieldsWithUnicodeDataFromMp3() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23unicodetags.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5", id3tag.getArtist()); // greek
		assertEquals("\u4E2D\u6587", id3tag.getTitle()); // chinese
		assertEquals("\u3053\u3093\u306B\u3061\u306F", id3tag.getAlbum()); // japanese
		assertEquals("\u0AB9\u0AC7\u0AB2\u0ACD\u0AB2\u0ACB", id3tag.getComposer()); // gujarati
	}

	@Test
	public void shouldSetTagFieldsWithUnicodeDataAndSpecifiedEncodingCorrectly() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		id3tag.setArtist("\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5");
		id3tag.setTitle("\u4E2D\u6587");
		id3tag.setAlbum("\u3053\u3093\u306B\u3061\u306F");
		id3tag.setComment("\u03C3\u03BF\u03C5");
		id3tag.setComposer("\u0AB9\u0AC7\u0AB2\u0ACD\u0AB2\u0ACB");
		id3tag.setOriginalArtist("\u03B3\u03B5\u03B9\u03AC");
		id3tag.setCopyright("\u03B3\u03B5");
		id3tag.setUrl("URL");
		id3tag.setEncoder("\u03B9\u03AC");
		byte[] albumImage = TestHelper.loadFile("src/test/resources/image.png");
		id3tag.setAlbumImage(albumImage, "image/png", ID3v23Tag.PICTURETYPE_OTHER, "\u03B3\u03B5\u03B9\u03AC");
	}

	@Test
	public void shouldExtractChapterTOCFramesFromMp3() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithchapters.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);

		ArrayList<ID3v2ChapterTOCFrameData> chapterTOCs = id3tag.getChapterTOC();
		assertEquals(1, chapterTOCs.size());

		ID3v2ChapterTOCFrameData tocFrameData = chapterTOCs.get(0);
		assertEquals("toc1", tocFrameData.getId());
		String expectedChildren[] = {"ch1", "ch2", "ch3"};
		assertArrayEquals(expectedChildren, tocFrameData.getChildren());

		ArrayList<ID3v2Frame> subFrames = tocFrameData.getSubframes();
		assertEquals(0, subFrames.size());
	}

	@Test
	public void shouldExtractChapterTOCAndChapterFramesFromMp3() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithchapters.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);

		ArrayList<ID3v2ChapterFrameData> chapters = id3tag.getChapters();
		assertEquals(3, chapters.size());

		ID3v2ChapterFrameData chapter1 = chapters.get(0);
		assertEquals("ch1", chapter1.getId());
		assertEquals(0, chapter1.getStartTime());
		assertEquals(5000, chapter1.getEndTime());
		assertEquals(-1, chapter1.getStartOffset());
		assertEquals(-1, chapter1.getEndOffset());

		ArrayList<ID3v2Frame> subFrames1 = chapter1.getSubframes();
		assertEquals(1, subFrames1.size());
		ID3v2Frame subFrame1 = subFrames1.get(0);
		assertEquals("TIT2", subFrame1.getId());
		ID3v2TextFrameData frameData1 = new ID3v2TextFrameData(false, subFrame1.getData());
		assertEquals("start", frameData1.getText().toString());

		ID3v2ChapterFrameData chapter2 = chapters.get(1);
		assertEquals("ch2", chapter2.getId());
		assertEquals(5000, chapter2.getStartTime());
		assertEquals(10000, chapter2.getEndTime());
		assertEquals(-1, chapter2.getStartOffset());
		assertEquals(-1, chapter2.getEndOffset());

		ArrayList<ID3v2Frame> subFrames2 = chapter2.getSubframes();
		assertEquals(1, subFrames2.size());
		ID3v2Frame subFrame2 = subFrames2.get(0);
		assertEquals("TIT2", subFrame2.getId());
		ID3v2TextFrameData frameData2 = new ID3v2TextFrameData(false, subFrame2.getData());
		assertEquals("5 seconds", frameData2.getText().toString());

		ID3v2ChapterFrameData chapter3 = chapters.get(2);
		assertEquals("ch3", chapter3.getId());
		assertEquals(10000, chapter3.getStartTime());
		assertEquals(15000, chapter3.getEndTime());
		assertEquals(-1, chapter3.getStartOffset());
		assertEquals(-1, chapter3.getEndOffset());

		ArrayList<ID3v2Frame> subFrames3 = chapter3.getSubframes();
		assertEquals(1, subFrames3.size());
		ID3v2Frame subFrame3 = subFrames3.get(0);
		assertEquals("TIT2", subFrame3.getId());
		ID3v2TextFrameData frameData3 = new ID3v2TextFrameData(false, subFrame3.getData());
		assertEquals("10 seconds", frameData3.getText().toString());
	}

	@Test
	public void shouldReadTagFieldsFromMp3With32tagResavedByMp3tagWithUTF16Encoding() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tagswithalbumimage-utf16le.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals("1", id3tag.getTrack());
		assertEquals("ARTIST123456789012345678901234", id3tag.getArtist());
		assertEquals("TITLE1234567890123456789012345", id3tag.getTitle());
		assertEquals("ALBUM1234567890123456789012345", id3tag.getAlbum());
		assertEquals("2001", id3tag.getYear());
		assertEquals(0x01, id3tag.getGenre());
		assertEquals("Classic Rock", id3tag.getGenreDescription());
		assertEquals("COMMENT123456789012345678901", id3tag.getComment());
		assertEquals("COMPOSER23456789012345678901234", id3tag.getComposer());
		assertEquals("ORIGARTIST234567890123456789012", id3tag.getOriginalArtist());
		assertEquals("COPYRIGHT2345678901234567890123", id3tag.getCopyright());
		assertEquals("URL2345678901234567890123456789", id3tag.getUrl());
		assertEquals("ENCODER234567890123456789012345", id3tag.getEncoder());
		assertEquals(1885, id3tag.getAlbumImage().length);
		assertEquals("image/png", id3tag.getAlbumImageMimeType());
	}

	@Test
	public void shouldRemoveAlbumImageFrame() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tagswithalbumimage.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals(1885, id3tag.getAlbumImage().length);
		id3tag.clearAlbumImage();
		assertNull(id3tag.getAlbumImage());
	}

	@Test
	public void shouldReadBPM() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithbpm.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals(84, id3tag.getBPM());
	}

	@Test
	public void shouldReadFloatingPointBPM() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithbpmfloat.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals(84, id3tag.getBPM());
	}

	@Test
	public void shouldReadFloatingPointBPMWithCommaDelimiter() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithbpmfloatwithcomma.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals(84, id3tag.getBPM());
	}

	@Test
	public void shouldReadWmpRating() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithwmprating.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertEquals(3, id3tag.getWmpRating());
	}

	@Test
	public void shouldWriteWmpRating() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		final int expectedUnsetValue = -1;
		assertEquals(expectedUnsetValue, id3tag.getWmpRating());
		final int newValue = 4;
		id3tag.setWmpRating(newValue);
		assertEquals(newValue, id3tag.getWmpRating());
	}

	@Test
	public void shouldIgnoreInvalidWmpRatingOnWrite() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		final int originalValue = id3tag.getWmpRating();
		final int invalidValue = 6;
		id3tag.setWmpRating(invalidValue);
		assertEquals(originalValue, id3tag.getWmpRating());
	}


	private void setTagFields(ID3v2 id3tag) throws IOException {
		id3tag.setTrack("1");
		id3tag.setArtist("ARTIST");
		id3tag.setTitle("TITLE");
		id3tag.setAlbum("ALBUM");
		id3tag.setYear("1954");
		id3tag.setGenre(0x0d);
		id3tag.setComment("COMMENT");
		id3tag.setComposer("COMPOSER");
		id3tag.setOriginalArtist("ORIGINALARTIST");
		id3tag.setCopyright("COPYRIGHT");
		id3tag.setUrl("URL");
		id3tag.setCommercialUrl("COMMERCIALURL");
		id3tag.setCopyrightUrl("COPYRIGHTURL");
		id3tag.setArtistUrl("OFFICIALARTISTURL");
		id3tag.setAudiofileUrl("OFFICIALAUDIOFILEURL");
		id3tag.setAudioSourceUrl("OFFICIALAUDIOSOURCEURL");
		id3tag.setRadiostationUrl("INTERNETRADIOSTATIONURL");
		id3tag.setPaymentUrl("PAYMENTURL");
		id3tag.setPublisherUrl("PUBLISHERURL");
		id3tag.setEncoder("ENCODER");
		byte[] albumImage = TestHelper.loadFile("src/test/resources/image.png");
		id3tag.setAlbumImage(albumImage, "image/png");
	}

	private ID3v2 createTag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		ID3v2TagFactoryForTesting factory = new ID3v2TagFactoryForTesting();
		return factory.createTag(buffer);
	}

	class ID3v22TagForTesting extends ID3v22Tag {

		ID3v22TagForTesting(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			super(buffer);
		}

		@Override
		protected int unpackFrames(byte[] buffer, int offset, int framesLength) {
			return offset;
		}
	}

	class ID3v23TagForTesting extends ID3v23Tag {

		ID3v23TagForTesting() {
			super();
		}

		ID3v23TagForTesting(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			super(buffer);
		}

		@Override
		protected int unpackFrames(byte[] buffer, int offset, int framesLength) {
			return offset;
		}
	}

	class ID3v24TagForTesting extends ID3v24Tag {

		ID3v24TagForTesting(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			super(buffer);
		}

		@Override
		protected int unpackFrames(byte[] buffer, int offset, int framesLength) {
			return offset;
		}
	}

	class ID3v2TagFactoryForTesting {

		static final int MAJOR_VERSION_OFFSET = 3;

		ID3v2 createTag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			int majorVersion = buffer[MAJOR_VERSION_OFFSET];
			switch (majorVersion) {
				case 2:
					return new ID3v22TagForTesting(buffer);
				case 3:
					return new ID3v23TagForTesting(buffer);
				case 4:
					return new ID3v24TagForTesting(buffer);
			}
			throw new UnsupportedTagException("Tag version not supported");
		}
	}
}
