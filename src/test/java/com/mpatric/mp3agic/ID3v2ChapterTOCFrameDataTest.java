package com.mpatric.mp3agic;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class ID3v2ChapterTOCFrameDataTest {

	@Test
	public void equalsItself() throws Exception {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertEquals(frameData, frameData);
	}

	@Test
	public void notEqualToNull() throws Exception {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertFalse(frameData.equals(null));
	}

	@Test
	public void notEqualToDifferentClass() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertFalse(frameData.equals("8"));
	}

	@Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		String children[] = {"ch1", "ch2"};
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", children);
		ID3v2TextFrameData subFrameData1 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData1.addSubframe("TIT2", subFrameData1);
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", children);

		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfUnsynchronizationNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(true, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfIsRootNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, false, false, "toc1", new String[]{"ch1", "ch2"});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfIsOrderedNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, true, "toc1", new String[]{"ch1", "ch2"});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfIdNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc2", new String[]{"ch1", "ch2"});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfIdIsNullOnOne() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, null, new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfIdIsNullOnBoth() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, null, new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, null, new String[]{"ch1", "ch2"});
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfChildrenNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc`", new String[]{"ch3", "ch2"});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfOneDoeNotHaveChildren() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc`", new String[]{});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfChildrenNullOnOne() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", null);
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc`", new String[]{"ch3", "ch2"});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfOneHasSubframes() throws Exception {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void hashCodeIsConsistent() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertEquals(frameData.hashCode(), frameData.hashCode());
	}

	@Test
	public void equalObjectsHaveSameHashCode() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameDataAgain = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertEquals(frameData.hashCode(), frameDataAgain.hashCode());
	}

	@Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		String children[] = {"ch1", "ch2"};
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, true, "toc1", children);
		ID3v2TextFrameData subFrameData = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData.addSubframe("TIT2", subFrameData);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {
				't', 'o', 'c', '1', 0,
				3, 2,
				'c', 'h', '1', 0,
				'c', 'h', '2', 0,
				'T', 'I', 'T', '2',
				0, 0, 0, (byte) 0xc,
				0, 0,
				0,
				'H', 'e', 'l', 'l', 'o', ' ', 't', 'h', 'e', 'r', 'e'
		};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2ChapterTOCFrameData frameDataCopy = new ID3v2ChapterTOCFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void toStringOnMostlyEmptyFrameData() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		assertEquals(
				"ID3v2ChapterTOCFrameData [isRoot=false, isOrdered=false, id=null, children=null, subframes=[]]",
				frameData.toString());
	}

	@Test
	public void toStringOnFullFrameData() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, true, "toc1", new String[]{"ch1", "ch2"});
		assertEquals(
				"ID3v2ChapterTOCFrameData [isRoot=true, isOrdered=true, id=toc1, children=[ch1, ch2], subframes=[]]",
				frameData.toString());
	}

	@Test
	public void getsAndSetsIsRoot() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setRoot(true);
		assertTrue(frameData.isRoot());
	}

	@Test
	public void getsAndSetsIsOrdered() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setOrdered(true);
		assertTrue(frameData.isOrdered());
	}

	@Test
	public void getsAndSetsId() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setId("My ID");
		assertEquals("My ID", frameData.getId());
	}

	@Test
	public void getsAndSetsChildren() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setChildren(new String[]{"ch1", "ch2"});
		assertArrayEquals(new String[]{"ch1", "ch2"}, frameData.getChildren());
	}

	@Test
	public void getsAndSetsSubframes() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		ArrayList<ID3v2Frame> subframes = new ArrayList<>(2);
		subframes.add(new ID3v2Frame("", new byte[]{'c', 'h', '1', 0}));
		subframes.add(new ID3v2Frame("", new byte[]{1, 0, 1, 0}));
		frameData.setSubframes(subframes);
		assertEquals(subframes, frameData.getSubframes());
	}
}
