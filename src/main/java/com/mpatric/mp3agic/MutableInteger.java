package com.mpatric.mp3agic;

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
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableInteger other = (MutableInteger) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
