package com.mpatric.mp3agic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ID3v22TagTest {

    private static final byte ZERO = 0;
    private static final byte[] inputBytes = new byte[ID3v22Tag.FLAGS_OFFSET + 1];
    private static final byte[] outputBytes = new byte[ID3v22Tag.FLAGS_OFFSET + 1];

    @Before
    public void setUp() throws Exception {
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = 0;
        outputBytes[ID3v22Tag.FLAGS_OFFSET] = 0;
    }

    @Test
    public void shouldUnpackAndPackOffUnsynchronizationBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.UNSYNCHRONISATION_BIT, false);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertFalse(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.UNSYNCHRONISATION_BIT));
    }

    @Test
    public void shouldUnpackAndPackOnUnsynchronizationBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.UNSYNCHRONISATION_BIT, true);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertTrue(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.UNSYNCHRONISATION_BIT));
    }

    @Test
    public void shouldUnpackAndPackOffCompressionBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.COMPRESSION_BIT, false);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertFalse(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.COMPRESSION_BIT));
    }

    @Test
    public void shouldUnpackAndPackOnCompressionBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.COMPRESSION_BIT, true);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertTrue(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.COMPRESSION_BIT));
    }

    @Test
    public void shouldStoreAndRetrieveItunesComment() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String comment = "COMMENT";
        id3tag.setItunesComment(comment);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(comment, newId3tag.getItunesComment());
    }

    @Test
    public void shouldStoreAndRetrieveLyrics() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String lyrics = "La-la-la";
        id3tag.setLyrics(lyrics);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(lyrics, newId3tag.getLyrics());
    }

    @Test
    public void shouldStoreAndRetrievePublisher() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String publisher = "PUBLISHER";
        id3tag.setPublisher(publisher);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(publisher, newId3tag.getPublisher());
    }

    @Test
    public void shouldStoreAndRetrieveKey() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String key = "KEY";
        id3tag.setKey(key);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(key, newId3tag.getKey());
    }

    @Test
    public void shouldStoreAndRetrieveBPM() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final int bpm = 8 * 44100;
        id3tag.setBPM(bpm);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(bpm, newId3tag.getBPM());
    }

    @Test
    public void shouldStoreAndRetrieveDate() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String date = "DATE";
        id3tag.setDate(date);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(date, newId3tag.getDate());
    }

    @Test
    public void shouldStoreAndRetrieveAlbumArtist() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String albumArtist = "ALBUMARTIST";
        id3tag.setAlbumArtist(albumArtist);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(albumArtist, newId3tag.getAlbumArtist());
    }

    @Test
    public void shouldStoreAndRetrieveGrouping() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String grouping = "GROUPING";
        id3tag.setGrouping(grouping);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(grouping, newId3tag.getGrouping());
    }

    @Test
    public void shouldStoreAndRetrieveCompilation() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final boolean compilation = true;
        id3tag.setCompilation(compilation);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(compilation, newId3tag.isCompilation());
    }

    @Test
    public void shouldStoreAndRetrievePartOfSet() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String partOfSet = "PARTOFSET";
        id3tag.setPartOfSet(partOfSet);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertEquals(partOfSet, newId3tag.getPartOfSet());
    }

}
