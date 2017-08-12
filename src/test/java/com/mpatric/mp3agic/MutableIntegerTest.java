package com.mpatric.mp3agic;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class MutableIntegerTest {
	@Test
	public void initializesValue() {
		MutableInteger integer = new MutableInteger(8);
		assertEquals(8, integer.getValue());
	}

	@Test
	public void incrementsValue() {
		MutableInteger integer = new MutableInteger(8);
		integer.increment();
		assertEquals(9, integer.getValue());
	}

	@Test
	public void setsValue() {
		MutableInteger integer = new MutableInteger(8);
		integer.setValue(5);
		assertEquals(5, integer.getValue());
	}

	@Test
	public void shouldCorrectlyImplementHashCodeAndEquals() throws Exception {
		EqualsVerifier.forClass(MutableInteger.class)
				.usingGetClass()
				.suppress(Warning.NONFINAL_FIELDS)
				.verify();
	}
}
