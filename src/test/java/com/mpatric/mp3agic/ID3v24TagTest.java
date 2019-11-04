package com.mpatric.mp3agic;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ID3v24TagTest {

    @Test
    public void shouldStoreAndRetrieveRecordingTime() throws Exception {
        final ID3v24Tag id3tag = new ID3v24Tag();
        final String recordingTime = "01/01/2011 00:00:00";
        id3tag.setRecordingTime(recordingTime);
        final byte[] bytes = id3tag.toBytes();
        final ID3v24Tag newId3tag = new ID3v24Tag(bytes);
        assertEquals(recordingTime, newId3tag.getRecordingTime());
    }

    @Test
    public void shouldSetGenreDescription() throws Exception {
        final ID3v24Tag id3tag = new ID3v24Tag();
        final String genreDescription = "?????";
        id3tag.setGenreDescription(genreDescription);
        final byte[] bytes = id3tag.toBytes();
        final ID3v24Tag newId3tag = new ID3v24Tag(bytes);
        assertTrue(genreDescription, newId3tag.getFrameSets().containsKey(ID3v24Tag.ID_GENRE));
        final List<ID3v2Frame> frames = newId3tag.getFrameSets().get(ID3v24Tag.ID_GENRE).getFrames();
        assertEquals(1, frames.size());
        final ID3v2TextFrameData frameData = new ID3v2TextFrameData(id3tag.hasUnsynchronisation(), frames.get(0).getData());
        assertEquals(genreDescription, frameData.getText().toString());
    }
}
