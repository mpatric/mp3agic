package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.NoSuchTagException;

import junit.framework.TestCase;

public class ID3v1TagTest extends TestCase {

	private String VALID_TAG = "TAGTITLE1234567890123456789012345ARTIST123456789012345678901234ALBUM12345678901234567890123452001COMMENT123456789012345678901234";
	private String VALID_TAG_WITH_WHITESPACE = "TAGTITLE                         ARTIST                        ALBUM                         2001COMMENT                        ";

	public void testShouldThrowExceptionForTagBufferTooShort() {
		byte[] buffer = new byte[ID3v1Tag.TAG_LENGTH - 1]; 
		try {
			new ID3v1Tag(buffer);
			fail("NoSuchTagException expected but not thrown");
		} catch (NoSuchTagException e) {
			// expected
		}
	}
	
	public void testShouldThrowExceptionForTagBufferTooLong() {
		byte[] buffer = new byte[ID3v1Tag.TAG_LENGTH + 1]; 
		try {
			new ID3v1Tag(buffer);
			fail("NoSuchTagException expected but not thrown");
		} catch (NoSuchTagException e) {
			// expected
		}
	}
	
	public void testShouldExtractMaximumLengthFieldsFromValid10Tag() throws Exception {
		byte[] buffer = BufferTools.stringToByteBuffer(VALID_TAG, 0, VALID_TAG.length());
		buffer[buffer.length - 1] = -0x6D; // 0x93 as a signed byte
		ID3v1Tag id3v1tag = new ID3v1Tag(buffer);
		assertEquals("TITLE1234567890123456789012345", id3v1tag.getTitle());
		assertEquals("ARTIST123456789012345678901234", id3v1tag.getArtist());
		assertEquals("ALBUM1234567890123456789012345", id3v1tag.getAlbum());
		assertEquals("2001", id3v1tag.getYear());
		assertEquals("COMMENT12345678901234567890123", id3v1tag.getComment());
		assertEquals(null, id3v1tag.getTrack());
		assertEquals(0x93, id3v1tag.getGenre());
		assertEquals("Synthpop", id3v1tag.getGenreDescription());
	}
	
	public void testShouldExtractMaximumLengthFieldsFromValid11Tag() throws Exception {
		byte[] buffer = BufferTools.stringToByteBuffer(VALID_TAG, 0, VALID_TAG.length());
		buffer[buffer.length - 3] = 0x00;
		buffer[buffer.length - 2] = 0x01;
		buffer[buffer.length - 1] = 0x0D;
		ID3v1Tag id3v1tag = new ID3v1Tag(buffer);
		assertEquals("TITLE1234567890123456789012345", id3v1tag.getTitle());
		assertEquals("ARTIST123456789012345678901234", id3v1tag.getArtist());
		assertEquals("ALBUM1234567890123456789012345", id3v1tag.getAlbum());
		assertEquals("2001", id3v1tag.getYear());
		assertEquals("COMMENT123456789012345678901", id3v1tag.getComment());
		assertEquals("1", id3v1tag.getTrack());
		assertEquals(0x0d, id3v1tag.getGenre());
		assertEquals("Pop", id3v1tag.getGenreDescription());
	}
	
	public void testShouldExtractTrimmedFieldsFromValid11TagWithWhitespace() throws Exception {
		byte[] buffer = BufferTools.stringToByteBuffer(VALID_TAG_WITH_WHITESPACE, 0, VALID_TAG_WITH_WHITESPACE.length());
		buffer[buffer.length - 3] = 0x00;
		buffer[buffer.length - 2] = 0x01;
		buffer[buffer.length - 1] = 0x0D;
		ID3v1Tag id3v1tag = new ID3v1Tag(buffer);
		assertEquals("TITLE", id3v1tag.getTitle());
		assertEquals("ARTIST", id3v1tag.getArtist());
		assertEquals("ALBUM", id3v1tag.getAlbum());
		assertEquals("2001", id3v1tag.getYear());
		assertEquals("COMMENT", id3v1tag.getComment());
		assertEquals("1", id3v1tag.getTrack());
		assertEquals(0x0d, id3v1tag.getGenre());
		assertEquals("Pop", id3v1tag.getGenreDescription());
	}
	
	public void testShouldExtractTrimmedFieldsFromValid11TagWithNullspace() throws Exception {
		byte[] buffer = BufferTools.stringToByteBuffer(VALID_TAG_WITH_WHITESPACE, 0, VALID_TAG_WITH_WHITESPACE.length());
		TestHelper.replaceSpacesWithNulls(buffer);
		buffer[buffer.length - 3] = 0x00;
		buffer[buffer.length - 2] = 0x01;
		buffer[buffer.length - 1] = 0x0D;
		ID3v1Tag id3v1tag = new ID3v1Tag(buffer);
		assertEquals("TITLE", id3v1tag.getTitle());
		assertEquals("ARTIST", id3v1tag.getArtist());
		assertEquals("ALBUM", id3v1tag.getAlbum());
		assertEquals("2001", id3v1tag.getYear());
		assertEquals("COMMENT", id3v1tag.getComment());
		assertEquals("1", id3v1tag.getTrack());
		assertEquals(0x0d, id3v1tag.getGenre());
		assertEquals("Pop", id3v1tag.getGenreDescription());
	}
	
	public void testShouldGenerateValidTagBuffer() throws Exception {
		ID3v1Tag id3v1tag = new ID3v1Tag();
		id3v1tag.setTitle("TITLE");
		id3v1tag.setArtist("ARTIST");
		id3v1tag.setAlbum("ALBUM");
		id3v1tag.setYear("2001");
		id3v1tag.setComment("COMMENT");
		id3v1tag.setTrack("1");
		id3v1tag.setGenre(0x0d);
		byte[] expectedBuffer = BufferTools.stringToByteBuffer(VALID_TAG_WITH_WHITESPACE, 0, VALID_TAG_WITH_WHITESPACE.length());
		TestHelper.replaceSpacesWithNulls(expectedBuffer);
		expectedBuffer[expectedBuffer.length - 3] = 0x00;
		expectedBuffer[expectedBuffer.length - 2] = 0x01;
		expectedBuffer[expectedBuffer.length - 1] = 0x0D;
		assertTrue(Arrays.equals(expectedBuffer, id3v1tag.toBytes()));
	}
	
	public void testShouldGenerateValidTagBufferWithHighGenreAndTrackNumber() throws Exception {
		ID3v1Tag id3v1tag = new ID3v1Tag();
		id3v1tag.setTitle("TITLE");
		id3v1tag.setArtist("ARTIST");
		id3v1tag.setAlbum("ALBUM");
		id3v1tag.setYear("2001");
		id3v1tag.setComment("COMMENT");
		id3v1tag.setTrack("254");
		id3v1tag.setGenre(0x8d);
		byte[] expectedBuffer = BufferTools.stringToByteBuffer(VALID_TAG_WITH_WHITESPACE, 0, VALID_TAG_WITH_WHITESPACE.length());
		TestHelper.replaceSpacesWithNulls(expectedBuffer);
		expectedBuffer[expectedBuffer.length - 3] = 0x00;
		expectedBuffer[expectedBuffer.length - 2] = -0x02; // 254 as a signed byte
		expectedBuffer[expectedBuffer.length - 1] = -0x73; // 0x8D as a signed byte
		assertTrue(Arrays.equals(expectedBuffer, id3v1tag.toBytes()));
	}
	
	public void testShouldReadTagFieldsFromMp3() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tags.mp3");
		byte[] tagBuffer = BufferTools.copyBuffer(buffer, buffer.length - ID3v1Tag.TAG_LENGTH, ID3v1Tag.TAG_LENGTH);
		ID3v1 id3tag = new ID3v1Tag(tagBuffer);
		assertEquals("1", id3tag.getTrack());
		assertEquals("ARTIST123456789012345678901234", id3tag.getArtist());
		assertEquals("TITLE1234567890123456789012345", id3tag.getTitle());
		assertEquals("ALBUM1234567890123456789012345", id3tag.getAlbum());
		assertEquals("2001", id3tag.getYear());
		assertEquals(0x0d, id3tag.getGenre());
		assertEquals("Pop", id3tag.getGenreDescription());
		assertEquals("COMMENT123456789012345678901", id3tag.getComment());
	}
	
	public void testShouldConvertTagToBytesAndBackToEquivalentTag() throws Exception {
		ID3v1 id3tag = new ID3v1Tag();
		id3tag.setTrack("5");
		id3tag.setArtist("ARTIST");
		id3tag.setTitle("TITLE");
		id3tag.setAlbum("ALBUM");
		id3tag.setYear("1997");
		id3tag.setGenre(13);
		id3tag.setComment("COMMENT");
		byte[] data = id3tag.toBytes();
		ID3v1 id3tagCopy = new ID3v1Tag(data);
		assertEquals(id3tag, id3tagCopy);
	}
	
	public void testShouldReturnEmptyTrackIfNotSetOn11Tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1tagwithnotrack.mp3");
		byte[] tagBuffer = BufferTools.copyBuffer(buffer, buffer.length - ID3v1Tag.TAG_LENGTH, ID3v1Tag.TAG_LENGTH);
		ID3v1 id3tag = new ID3v1Tag(tagBuffer);
		assertEquals("", id3tag.getTrack());
	}
}
