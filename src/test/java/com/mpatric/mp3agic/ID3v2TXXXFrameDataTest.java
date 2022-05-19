package com.mpatric.mp3agic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ID3v2TXXXFrameDataTest {

	@Test
	public void test_input() throws Exception {
		Map<String, ID3v2FrameSet> frameSets = new HashMap<>();

		// for input of new field
		ID3v2TXXXFrameData.createOrAddField(
				frameSets,
				true,
				"my_custom_text",
				"value",
				true);

		assertEquals(1, frameSets.size());
	}

	@Test
	public void test_extraction() throws Exception {
		Map<String, ID3v2FrameSet> frameSets = new HashMap<>();

		ID3v2TXXXFrameData.createOrAddField(
				frameSets,
				true,
				"my_custom_text",
				"value",
				true);

		// for extraction
		ID3v2TXXXFrameData frameData = ID3v2TXXXFrameData.extract(
				frameSets,
				true,
				"my_custom_text");

		assertEquals("my_custom_text", frameData.getDescription().toString());
		assertEquals("value", frameData.getValue().toString());
	}

	@Test
	public void test_input_replacement() throws Exception {
		Map<String, ID3v2FrameSet> frameSets = new HashMap<>();

		ID3v2TXXXFrameData.createOrAddField(
				frameSets,
				true,
				"my_custom_text",
				"value",
				true);
		assertEquals(1, frameSets.size());

		// for input with replacement
		ID3v2TXXXFrameData.createOrAddField(
				frameSets,
				true,
				"my_custom_text",
				"value changed",
				true);
		ID3v2TXXXFrameData frameData = ID3v2TXXXFrameData.extract(
				frameSets,
				true,
				"my_custom_text");

		assertEquals("my_custom_text", frameData.getDescription().toString());
		assertEquals("value changed", frameData.getValue().toString());
	}

	@Test
	public void test_input_no_replacement() throws Exception {
		Map<String, ID3v2FrameSet> frameSets = new HashMap<>();

		ID3v2TXXXFrameData.createOrAddField(
				frameSets,
				true,
				"my_custom_text",
				"value",
				true);
		assertEquals(1, frameSets.size());

		// for input with-out replacement
		ID3v2TXXXFrameData.createOrAddField(
				frameSets,
				true,
				"my_custom_text",
				"value 2",
				false);
		ArrayList<ID3v2TXXXFrameData> frameDatas = ID3v2TXXXFrameData.extractAll(
				frameSets,
				true,
				"my_custom_text");

		assertEquals(2, frameDatas.size());
	}

}
