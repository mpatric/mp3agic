package com.mpatric.mp3agic;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

public class EncodedTextTest {
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
		EncodedText encodedText = new EncodedText(original, true);
		String strResult = encodedText.toString();
		Assert.assertEquals(original, strResult);
		
		// Test each encoding thoroughly  
		for (int i = 0; i < Encoding.values().length; ++i) {
			Encoding encoding = Encoding.getEncoding(i);
			byte[] expected = expectedByEncoding[i];
			if (encoding == encodedText.encoding) {
				Assert.assertArrayEquals(expected, encodedText.toBytes());
			}
			ByteArrayInputStream in = new ByteArrayInputStream(Bytes.concat(new byte[] { (byte) i }, expected));
			EncodedText parsedFromBytes = new EncodedText(in);
			Assert.assertEquals(0, in.available());
			Assert.assertEquals(i, parsedFromBytes.encoding.ordinal());

			byte[] result = encoding.encode(original);
			Assert.assertArrayEquals(expected, result);
			strResult = encoding.parse(new ByteArrayInputStream(expected), true);

			// Convert the resulting bytes back into a string
			if (lossy && encoding.charset == Charsets.ISO_8859_1) {
				Assert.assertEquals(original.length(), strResult.length());
			} else {
				Assert.assertEquals(original, strResult);
			}
			
			// Now remove the terminator from the expected result and make sure it still parses properly.
			byte[] trimmed = Arrays.copyOf(expected, expected.length - encoding.characterSize);
			strResult = encoding.parse(new ByteArrayInputStream(trimmed), false);
			if (lossy && encoding.charset == Charsets.ISO_8859_1) {
				Assert.assertEquals(original.length(), strResult.length());
			} else {
				Assert.assertEquals(original, strResult);
			}
		}
	}
	
	@Test
	public void testEmpty() {
		testData("", new byte[][] {
			TERMINATOR,
			Bytes.concat(BOM_BE, TERMINATOR2),
			TERMINATOR2,
			TERMINATOR,
		});
	}

	@Test
	public void testBasic() {
		testData("This is a string!", new byte[][] {
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
	public void testUtf16Le() {
		EncodedText et = new EncodedText(Bytes.concat(
				new byte[] { 0x01 }, BOM_LE, TEST_I18N_STRING.getBytes(Charsets.UTF_16LE), TERMINATOR2
		));
		assertEquals(et.text, TEST_I18N_STRING);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testShouldThrowExceptionWhenEncodingWithInvalidCharacterSet() throws Exception {
		new EncodedText(Bytes.concat(
				new byte[] { 0x04 }, TEST_I18N_STRING.getBytes(Charsets.UTF_8), TERMINATOR2
		));
	}

}
