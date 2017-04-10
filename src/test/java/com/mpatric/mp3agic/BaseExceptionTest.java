package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseExceptionTest {

	@Test
	public void generatesCorrectDetailedMessageForSingleException() {
		BaseException e = new BaseException("ONE");
		assertEquals("ONE", e.getMessage());
		assertEquals("[com.mpatric.mp3agic.BaseException: ONE]", e.getDetailedMessage());
	}

	@Test
	public void generatesCorrectDetailedMessageForChainedBaseExceptions() {
		BaseException e1 = new BaseException("ONE");
		BaseException e2 = new UnsupportedTagException("TWO", e1);
		BaseException e3 = new NotSupportedException("THREE", e2);
		BaseException e4 = new NoSuchTagException("FOUR", e3);
		BaseException e5 = new InvalidDataException("FIVE", e4);
		assertEquals("FIVE", e5.getMessage());
		assertEquals("[com.mpatric.mp3agic.InvalidDataException: FIVE] caused by [com.mpatric.mp3agic.NoSuchTagException: FOUR] caused by [com.mpatric.mp3agic.NotSupportedException: THREE] caused by [com.mpatric.mp3agic.UnsupportedTagException: TWO] caused by [com.mpatric.mp3agic.BaseException: ONE]", e5.getDetailedMessage());
	}

	@Test
	public void generatesCorrectDetailedMessageForChainedExceptionsWithOtherExceptionInMix() {
		BaseException e1 = new BaseException("ONE");
		BaseException e2 = new UnsupportedTagException("TWO", e1);
		Exception e3 = new Exception("THREE", e2);
		BaseException e4 = new NoSuchTagException("FOUR", e3);
		BaseException e5 = new InvalidDataException("FIVE", e4);
		assertEquals("FIVE", e5.getMessage());
		assertEquals("[com.mpatric.mp3agic.InvalidDataException: FIVE] caused by [com.mpatric.mp3agic.NoSuchTagException: FOUR] caused by [java.lang.Exception: THREE] caused by [com.mpatric.mp3agic.UnsupportedTagException: TWO] caused by [com.mpatric.mp3agic.BaseException: ONE]", e5.getDetailedMessage());
	}
}
