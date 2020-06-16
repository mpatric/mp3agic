package com.mpatric.mp3agic.id3;

import com.mpatric.mp3agic.id3.ID3v2WWWFrameData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ID3v2WWWFrameDataTest {
	@Test
	public void getsAndSetsId() {
		ID3v2WWWFrameData frameData = new ID3v2WWWFrameData(false);
		frameData.setUrl("My URL");
		assertEquals("My URL", frameData.getUrl());
	}
}
