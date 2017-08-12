package com.mpatric.mp3agic;

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
	public void equalsItself() {
		MutableInteger integer = new MutableInteger(8);
		assertEquals(integer, integer);
	}

	@Test
	public void equalIfValueEqual() {
		MutableInteger eight = new MutableInteger(8);
		MutableInteger eightAgain = new MutableInteger(8);
		assertEquals(eight, eightAgain);
	}

	@Test
	public void notEqualToNull() {
		MutableInteger integer = new MutableInteger(8);
		assertFalse(integer.equals(null));
	}

	@Test
	public void notEqualToDifferentClass() {
		MutableInteger integer = new MutableInteger(8);
		assertFalse(integer.equals("8"));
	}

	@Test
	public void notEqualIfValueNotEqual() {
		MutableInteger eight = new MutableInteger(8);
		MutableInteger nine = new MutableInteger(9);
		assertNotEquals(eight, nine);
	}

	@Test
	public void hashCodeIsConsistent() {
		MutableInteger integer = new MutableInteger(8);
		assertEquals(integer.hashCode(), integer.hashCode());
	}

	@Test
	public void equalObjectsHaveSameHashCode() {
		MutableInteger eight = new MutableInteger(8);
		MutableInteger eightAgain = new MutableInteger(8);
		assertEquals(eight.hashCode(), eightAgain.hashCode());
	}
}
