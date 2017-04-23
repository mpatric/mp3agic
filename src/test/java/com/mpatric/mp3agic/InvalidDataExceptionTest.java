package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InvalidDataExceptionTest {
	@Test
	public void defaultConstructor() {
		InvalidDataException exception = new InvalidDataException();
		assertNull(exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	public void constructorWithMessage() {
		InvalidDataException exception = new InvalidDataException("A message");
		assertEquals("A message", exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	public void constructorWithMessageAndCause() {
		Throwable exceptionCause = new IllegalArgumentException("Bad argument");
		InvalidDataException exception = new InvalidDataException("A message", exceptionCause);
		assertEquals("A message", exception.getMessage());
		assertEquals(exceptionCause, exception.getCause());
	}
}
