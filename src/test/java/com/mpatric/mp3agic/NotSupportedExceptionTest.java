package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NotSupportedExceptionTest {
	@Test
	public void defaultConstructor() {
		NotSupportedException exception = new NotSupportedException();
		assertNull(exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	public void constructorWithMessage() {
		NotSupportedException exception = new NotSupportedException("A message");
		assertEquals("A message", exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	public void constructorWithMessageAndCause() {
		Throwable exceptionCause = new IllegalArgumentException("Bad argument");
		NotSupportedException exception = new NotSupportedException("A message", exceptionCause);
		assertEquals("A message", exception.getMessage());
		assertEquals(exceptionCause, exception.getCause());
	}
}
