package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

public class EncodingTest {
	private static final byte[] BOM_BE = { -2, -1 }; 
	private static final byte[] BOM_LE = { -1, -2 }; 

	private static final byte[] TERMINATOR = { 0 }; 
	private static final byte[] TERMINATOR2 = { 0, 0 }; 

	private static final String TEST_STRING = "This is a string!";
	private static final String TEST_I18N_STRING = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5 Hello!";

	public static void testData(String original, byte[][] expectedByEncoding) {
		testData(original, expectedByEncoding, false);
	}
	
	public static void testData(String original, byte[][] expectedByEncoding, boolean lossy) {
		
		// Test each encoding thoroughly  
		for (int i = 0; i < Encoding.values().length; ++i) {
			Encoding encoding = Encoding.getEncoding(i);
			byte[] expected = expectedByEncoding[i];
			byte[] result = encoding.encode(original, true);
			
			assertArrayEquals(expected, result);

			// Convert the resulting bytes back into a string
			String strResult = encoding.parse(new ByteArrayInputStream(expected), true);
			if (lossy && encoding.charset == Charsets.ISO_8859_1) {
				assertEquals(original.length(), strResult.length());
			} else {
				assertEquals(original, strResult);
			}
			
			// Now remove the terminator from the expected result and make sure it still parses properly.
			byte[] trimmed = Arrays.copyOf(expected, expected.length - encoding.characterSize);
			strResult = encoding.parse(new ByteArrayInputStream(trimmed), false);
			if (lossy && encoding.charset == Charsets.ISO_8859_1) {
				assertEquals(original.length(), strResult.length());
			} else {
				assertEquals(original, strResult);
			}
		}
	}
	
	@Test
	public void testEmpty() {
		testData("", new byte[][] {
			TERMINATOR,
			TERMINATOR2,
			TERMINATOR2,
			TERMINATOR,
		});
	}

	@Test
	public void testBasic() {
		testData(TEST_STRING, new byte[][] {
			Bytes.concat(TEST_STRING.getBytes(Charsets.ISO_8859_1), TERMINATOR),
			Bytes.concat(BOM_BE, TEST_STRING.getBytes(Charsets.UTF_16BE), TERMINATOR2),
			Bytes.concat(TEST_STRING.getBytes(Charsets.UTF_16BE), TERMINATOR2),
			Bytes.concat(TEST_STRING.getBytes(Charsets.UTF_8), TERMINATOR),
		});
	}
	
	@Test
	public void testI18N() {
		testData(TEST_I18N_STRING, new byte[][] {
			Bytes.concat(TEST_I18N_STRING.getBytes(Charsets.ISO_8859_1), TERMINATOR),
			Bytes.concat(BOM_BE, TEST_I18N_STRING.getBytes(Charsets.UTF_16BE), TERMINATOR2),
			Bytes.concat(TEST_I18N_STRING.getBytes(Charsets.UTF_16BE), TERMINATOR2),
			Bytes.concat(TEST_I18N_STRING.getBytes(Charsets.UTF_8), TERMINATOR),
		}, true);
	}
	
	@Test
	public void testBomPresenceEmptyString() {
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(BOM_BE, TERMINATOR2), true));
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(BOM_LE, TERMINATOR2), true));
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(BOM_BE), false));
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(BOM_LE), false));
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(TERMINATOR2), true));
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(TERMINATOR2), true));
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(), false));
		assertEquals("", Encoding.ENCODING_UTF_16.parse(concatToStream(), false));
	}
	
	public static ByteArrayInputStream concatToStream(byte []... bytes) {
		byte[][] byteArrays = bytes;
		return new ByteArrayInputStream(Bytes.concat(byteArrays));
	}
}
