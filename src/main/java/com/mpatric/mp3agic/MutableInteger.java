package com.mpatric.mp3agic;

import java.util.Objects;

public class MutableInteger {

	private int value;

	public MutableInteger(int value) {
		this.value = value;
	}

	public void increment() {
		value++;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MutableInteger other = (MutableInteger) obj;
		return Objects.equals(value, other.value);
	}
}
