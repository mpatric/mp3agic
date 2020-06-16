package com.mpatric.mp3agic.mpeg;

import java.util.Map;

public class MPEGVersion {

	private final int versionNum;
	private final String version;
	private final Map<Integer, int[]> layerBitrates;
	private final int[] sampleRates;

	public MPEGVersion(String version, int versionNum, Map<Integer, int[]> layerBitrates, int[] sampleRates) {
		this.version = version;
		this.versionNum = versionNum;
		this.layerBitrates = layerBitrates;
		this.sampleRates = sampleRates;
	}

	public String getName() {
		return version;
	}

	public int getVersionNum() {
		return versionNum;
	}

	public int getBitrate(MpegFrame.Layer layer, int bitRateIndex) throws NullPointerException, ArrayIndexOutOfBoundsException {
		return this.layerBitrates.get(layer.getInt() - 1)[bitRateIndex - 1];
	}

	public int getSampleRate(int sampleRateIndex) throws ArrayIndexOutOfBoundsException {
		return this.sampleRates[sampleRateIndex];
	}
}

