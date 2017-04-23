package com.mpatric.mp3agic;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class ID3v2ChapterFrameDataTest {

	@Test
	public void equalsItself() throws Exception {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertEquals(frameData, frameData);
	}

	@Test
	public void notEqualToNull() throws Exception {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertFalse(frameData.equals(null));
	}

	@Test
	public void notEqualToDifferentClass() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertFalse(frameData.equals("8"));
	}

	@Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData1 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData1.addSubframe("TIT2", subFrameData1);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfUnsynchronizationNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(true, "ch1", 1, 380, 3, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfIdNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch2", 1, 380, 3, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfIdIsNullOnOne() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, null, 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch2", 1, 380, 3, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfIdIsNullOnBoth() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, null, 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, null, 1, 380, 3, 400);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfStartTimeNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 2, 380, 3, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfEndTimeNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 280, 3, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfStartOffsetNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 2, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfEndOffsetNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 200);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfOneHasSubframes() throws Exception {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void hashCodeIsConsistent() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertEquals(frameData.hashCode(), frameData.hashCode());
	}

	@Test
	public void equalObjectsHaveSameHashCode() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameDataAgain = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertEquals(frameData.hashCode(), frameDataAgain.hashCode());
	}

	@Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData.addSubframe("TIT2", subFrameData);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {
				'c', 'h', '1', 0,
				0, 0, 0, 1,
				0, 0, 1, (byte) 0x7c,
				0, 0, 0, 3,
				0, 0, 1, (byte) 0x90,
				'T', 'I', 'T', '2',
				0, 0, 0, (byte) 0xc,
				0, 0,
				0,
				'H', 'e', 'l', 'l', 'o', ' ', 't', 'h', 'e', 'r', 'e'
		};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2ChapterFrameData frameDataCopy = new ID3v2ChapterFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void toStringOnMostlyEmptyFrameData() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		assertEquals(
				"ID3v2ChapterFrameData [id=null, startTime=0, endTime=0, startOffset=0, endOffset=0, subframes=[]]",
				frameData.toString());
	}

	@Test
	public void toStringOnFullFrameData() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertEquals(
				"ID3v2ChapterFrameData [id=ch1, startTime=1, endTime=380, startOffset=3, endOffset=400, subframes=[]]",
				frameData.toString());
	}

	@Test
	public void getsAndSetsId() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setId("My ID");
		assertEquals("My ID", frameData.getId());
	}

	@Test
	public void getsAndSetsStartTime() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setStartTime(9);
		assertEquals(9, frameData.getStartTime());
	}

	@Test
	public void getsAndSetsEndTime() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setEndTime(9);
		assertEquals(9, frameData.getEndTime());
	}

	@Test
	public void getsAndSetsStartOffset() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setStartOffset(9);
		assertEquals(9, frameData.getStartOffset());
	}

	@Test
	public void getsAndSetsEndOffset() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setEndOffset(9);
		assertEquals(9, frameData.getEndOffset());
	}

	@Test
	public void getsAndSetsSubframes() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		ArrayList<ID3v2Frame> subframes = new ArrayList<>(2);
		subframes.add(new ID3v2Frame("", new byte[]{'c', 'h', '1', 0}));
		subframes.add(new ID3v2Frame("", new byte[]{1, 0, 1, 0}));
		frameData.setSubframes(subframes);
		assertEquals(subframes, frameData.getSubframes());
	}
}
