package com.mpatric.mp3agic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ID3v2PopmFrameDataTest {

	@Test
	public void shouldReturnAddress() throws Exception {
		byte[] bytes = {'A', 'd', 'd', 'r', 'e', 's', 's', 0, (byte) 0x00};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		assertEquals("Address", iD3v2PopmFrameData.getAddress());
	}

	@Test
	public void shouldReturn1StarRating() throws Exception {
		byte[] bytes = {'A', 'd', 'd', 'r', 'e', 's', 's', 0, (byte) 0x01};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		assertEquals(1, iD3v2PopmFrameData.getRating());
	}

	@Test
	public void shouldReturn2StarRating() throws Exception {
		byte[] bytes = {'A', 'd', 'd', 'r', 'e', 's', 's', 0, (byte) 0x40};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		assertEquals(2, iD3v2PopmFrameData.getRating());
	}

	@Test
	public void shouldReturn3StarRating() throws Exception {
		byte[] bytes = {'A', 'd', 'd', 'r', 'e', 's', 's', 0, (byte) 0x80};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		assertEquals(3, iD3v2PopmFrameData.getRating());
	}

	@Test
	public void shouldReturn4StarRating() throws Exception {
		byte[] bytes = {'A', 'd', 'd', 'r', 'e', 's', 's', 0, (byte) 0xC4};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		assertEquals(4, iD3v2PopmFrameData.getRating());
	}

	@Test
	public void shouldReturn5StarRating() throws Exception {
		byte[] bytes = {'A', 'd', 'd', 'r', 'e', 's', 's', 0, (byte) 0xFF};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		assertEquals(5, iD3v2PopmFrameData.getRating());
	}

	@Test
	public void shouldReturnMinus1ForInvalidRating() throws Exception {
		byte[] bytes = {'A', 'd', 'd', 'r', 'e', 's', 's', 0, (byte) 0x33};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		assertEquals(-1, iD3v2PopmFrameData.getRating());
	}

	@Test
	public void canSetAndGetRating() throws Exception {
		byte[] bytes = {0};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		iD3v2PopmFrameData.setRating(1);
		assertEquals(1, iD3v2PopmFrameData.getRating());
	}

	@Test
	public void canSetAndGetAddress() throws Exception {
		byte[] bytes = {0};
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, bytes);
		iD3v2PopmFrameData.setAddress("New Address");
		assertEquals("New Address", iD3v2PopmFrameData.getAddress());
	}

	@Test
	public void canGetLength() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, 0);
		iD3v2PopmFrameData.setAddress("Address");
		final int expectedLength = "Address".length() + 2;  // Length of address , plus 1 separator byte + 1 bye for rating
		assertEquals(expectedLength, iD3v2PopmFrameData.getLength());
	}

	@Test
	public void canPackFrameData() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, 5);
		byte[] expectedBytes = {'W', 'i', 'n', 'd', 'o', 'w', 's', ' ', 'M', 'e', 'd', 'i', 'a', ' ', 'P', 'l', 'a', 'y', 'e', 'r', ' ', '9', ' ', 'S', 'e', 'r', 'i', 'e', 's', 0, (byte) 0xFF};
		final byte[] result = iD3v2PopmFrameData.packFrameData();
		assertEquals(expectedBytes.length, result.length);
		int i = 0;
		for (byte expectedByte : expectedBytes) {
			assertEquals(expectedByte, result[i++]);
		}
	}

	@Test
	public void hashCodeOfTwoDifferentObjectsAreDifferent() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData1 = new ID3v2PopmFrameData(false, 0);
		ID3v2PopmFrameData iD3v2PopmFrameData2 = new ID3v2PopmFrameData(false, 1);
		assertFalse(iD3v2PopmFrameData1.hashCode() == iD3v2PopmFrameData2.hashCode());
	}


	@Test
	public void twoEquivalentObjectsAreEquals() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData1 = new ID3v2PopmFrameData(false, 0);
		ID3v2PopmFrameData iD3v2PopmFrameData2 = new ID3v2PopmFrameData(false, 0);
		assertEquals(iD3v2PopmFrameData1, iD3v2PopmFrameData2);
	}

	@Test
	public void sameObjectsAreEquals() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, 0);
		assertEquals(iD3v2PopmFrameData, iD3v2PopmFrameData);
	}

	@Test
	public void ID3v2PopmFrameDataIsNotEqualOtherType() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, 0);
		assertFalse(iD3v2PopmFrameData.equals("a String"));
	}

	@Test
	public void ID3v2PopmFrameDataIsNotEqualNull() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData = new ID3v2PopmFrameData(false, 0);
		assertFalse(iD3v2PopmFrameData.equals(null));
	}

	@Test
	public void ID3v2PopmFrameDataIsNotEqualOtherWithDifferentRating() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData1 = new ID3v2PopmFrameData(false, 1);
		ID3v2PopmFrameData iD3v2PopmFrameData2 = new ID3v2PopmFrameData(false, 2);
		assertFalse(iD3v2PopmFrameData1.equals(iD3v2PopmFrameData2));
	}

	@Test
	public void ID3v2PopmFrameDataIsNotEqualOtherWithDifferentAddress() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData1 = new ID3v2PopmFrameData(false, 1);
		iD3v2PopmFrameData1.setAddress("Address1");
		ID3v2PopmFrameData iD3v2PopmFrameData2 = new ID3v2PopmFrameData(false, 1);
		iD3v2PopmFrameData1.setAddress("Address2");
		assertFalse(iD3v2PopmFrameData1.equals(iD3v2PopmFrameData2));
	}

	@Test
	public void ID3v2PopmFrameDataIsNotEqualOtherWithNullAddress() throws Exception {
		ID3v2PopmFrameData iD3v2PopmFrameData1 = new ID3v2PopmFrameData(false, 1);
		iD3v2PopmFrameData1.setAddress("Address1");
		ID3v2PopmFrameData iD3v2PopmFrameData2 = new ID3v2PopmFrameData(false, 1);
		iD3v2PopmFrameData1.setAddress(null);
		assertFalse(iD3v2PopmFrameData1.equals(iD3v2PopmFrameData2));
	}


}
