package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3v2ObseleteFrame;

import junit.framework.TestCase;

public class ID3v2ObseleteFrameTest extends TestCase {
	
	private static final String T_FRAME = "TP100\"0ARTISTABCDEFGHIJKLMNOPQRSTUVWXYZ0";
	private static final String LONG_T_FRAME = "TP10110Metamorphosis A a very long album B a very long album C a very long album D a very long album E a very long album F a very long album G a very long album H a very long album I a very long album J a very long album K a very long album L a very long album M0";
	
	public void testShouldReadValidLong32ObseleteTFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(LONG_T_FRAME, 0, LONG_T_FRAME.length());
		replaceNumbersWithBytes(bytes, 3);
		ID3v2ObseleteFrame frame = new ID3v2ObseleteFrame(bytes, 0);
		assertEquals(263, frame.getLength());
		assertEquals("TP1", frame.getId());
		String s = "0Metamorphosis A a very long album B a very long album C a very long album D a very long album E a very long album F a very long album G a very long album H a very long album I a very long album J a very long album K a very long album L a very long album M0";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		replaceNumbersWithBytes(expectedBytes, 0);
		assertTrue(Arrays.equals(expectedBytes, frame.getData()));
	}
	
	public void testShouldReadValid32ObseleteTFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer("xxxxx" + T_FRAME, 0, 5 + T_FRAME.length());
		replaceNumbersWithBytes(bytes, 8);
		ID3v2ObseleteFrame frame = new ID3v2ObseleteFrame(bytes, 5);
		assertEquals(40, frame.getLength());
		assertEquals("TP1", frame.getId());
		String s = "0ARTISTABCDEFGHIJKLMNOPQRSTUVWXYZ0";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		replaceNumbersWithBytes(expectedBytes, 0);
		assertTrue(Arrays.equals(expectedBytes, frame.getData()));
	}
		
	private void replaceNumbersWithBytes(byte[] bytes, int offset) {
		for (int i = offset; i < bytes.length; i++) {
			if (bytes[i] >= '0' && bytes[i] <= '9') {
				bytes[i] -= (byte)48;
			}
		}
	}
}
