package com.mpatric.mp3agic.mpeg;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.exception.InvalidDataException;
import com.mpatric.mp3agic.mpeg.MPEGVersion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MpegFrame {

	public static final MPEGVersion MPEG_VERSION_1_0 = new MPEGVersion("1.0", 3, createLayerMap(new int[][]{
			{32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448},
			{32, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384},
			{32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320}
	}), new int[]{44100, 48000, 32000});

	private static final Map<Integer, int[]> MPEG_2_2_5_LAYER_BITRATES = createLayerMap(new int[][]{
			{32, 48, 56, 64, 80, 96, 112, 128, 144, 160, 176, 192, 224, 256},
			{8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160},
			{8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160}
	});

	public static final MPEGVersion MPEG_VERSION_2_0 = new MPEGVersion("2.0", 2, MPEG_2_2_5_LAYER_BITRATES, new int[]{
			22050, 24000, 16000
	});
	private static final MPEGVersion MPEG_VERSION_2_5 = new MPEGVersion("2.0", 0, MPEG_2_2_5_LAYER_BITRATES, new int[]{
			11025, 12000, 8000
	});

	private static final MPEGVersion[] MPEG_VERSIONS = {MPEG_VERSION_1_0, MPEG_VERSION_2_0, MPEG_VERSION_2_5};

	public enum Layer {
		LAYER_1(1, "I"), LAYER_2(2, "II"), LAYER_3(3, "III");

		private final int layerInt;
		private final String layer;

		Layer(int layerInt, String layer) {
			this.layerInt = layerInt;
			this.layer = layer;
		}

		public String getLayer() {
			return layer;
		}

		public Integer getInt() {
			return this.layerInt;
		}
	}

	public enum ChannelMode {
		STEREO, JOINT_STEREO, DUAL_MONO, MONO
	}

	private static final int FRAME_DATA_LENGTH = 4;
	private static final int FRAME_SYNC = 0x7FF;
	private static final long BITMASK_FRAME_SYNC = 0xFFE00000L;
	private static final long BITMASK_VERSION = 0x180000L;
	private static final long BITMASK_LAYER = 0x60000L;
	private static final long BITMASK_PROTECTION = 0x10000L;
	private static final long BITMASK_BITRATE = 0xF000L;
	private static final long BITMASK_SAMPLE_RATE = 0xC00L;
	private static final long BITMASK_PADDING = 0x200L;
	private static final long BITMASK_PRIVATE = 0x100L;
	private static final long BITMASK_CHANNEL_MODE = 0xC0L;
	private static final long BITMASK_MODE_EXTENSION = 0x30L;
	private static final long BITMASK_COPYRIGHT = 0x8L;
	private static final long BITMASK_ORIGINAL = 0x4L;
	private static final long BITMASK_EMPHASIS = 0x3L;

	private MPEGVersion version;
	private Layer layer;
	private boolean protection;
	private int bitrate;
	private int sampleRate;
	private boolean padding;
	private boolean privat;
	private ChannelMode channelMode;
	private ExtensionMode modeExtension;
	private boolean copyright;
	private boolean original;
	private Emphasis emphasis;

	public MpegFrame(byte[] frameData) throws InvalidDataException {
		if (frameData.length < FRAME_DATA_LENGTH) throw new InvalidDataException("Mpeg frame too short");
		long frameHeader = BufferTools.unpackInteger(frameData[0], frameData[1], frameData[2], frameData[3]);
		setFields(frameHeader);
	}

	public MpegFrame(byte frameData1, byte frameData2, byte frameData3, byte frameData4) throws InvalidDataException {
		long frameHeader = BufferTools.unpackInteger(frameData1, frameData2, frameData3, frameData4);
		setFields(frameHeader);
	}

	protected MpegFrame() {
	}

	public static Map<Integer, int[]> createLayerMap(int[][] bitrates) {
		Map<Integer, int[]> map = new HashMap<>();
		for (int i = 0; i < bitrates.length; i++) {
			map.put(i, bitrates[i]);
		}

		return map;
	}

	private void setFields(long frameHeader) throws InvalidDataException {
		long frameSync = extractField(frameHeader, BITMASK_FRAME_SYNC);
		if (frameSync != FRAME_SYNC) {
			throw new InvalidDataException("Frame sync missing");
		}
		setVersion(extractField(frameHeader, BITMASK_VERSION));
		setLayer(extractField(frameHeader, BITMASK_LAYER));
		setProtection(extractField(frameHeader, BITMASK_PROTECTION));
		setBitRate(extractField(frameHeader, BITMASK_BITRATE));
		setSampleRate(extractField(frameHeader, BITMASK_SAMPLE_RATE));
		setPadding(extractField(frameHeader, BITMASK_PADDING));
		setPrivate(extractField(frameHeader, BITMASK_PRIVATE));
		setChannelMode(extractField(frameHeader, BITMASK_CHANNEL_MODE));
		setModeExtension(extractField(frameHeader, BITMASK_MODE_EXTENSION));
		setCopyright(extractField(frameHeader, BITMASK_COPYRIGHT));
		setOriginal(extractField(frameHeader, BITMASK_ORIGINAL));
		setEmphasis(extractField(frameHeader, BITMASK_EMPHASIS));
	}

	public int extractField(long frameHeader, long bitMask) {
		int shiftBy = 0;
		for (int i = 0; i <= 31; i++) {
			if (((bitMask >> i) & 1) != 0) {
				shiftBy = i;
				break;
			}
		}
		return (int) ((frameHeader >> shiftBy) & (bitMask >> shiftBy));
	}

	private void setVersion(int version) throws InvalidDataException {
		for (MPEGVersion mpegVersion : MPEG_VERSIONS) {
			if (mpegVersion.getVersionNum() == version) {
				this.version = mpegVersion;
				return;
			}
		}

		throw new InvalidDataException("Invalid mpeg audio version in frame header");
	}

	private void setLayer(int layerIdx) throws InvalidDataException {
		if (layerIdx < 1 || layerIdx > 3) {
			throw new InvalidDataException("Invalid mpeg layer description in frame header");
		}

		this.layer = Layer.values()[3 - layerIdx];
	}

	private void setProtection(int protectionBit) {
		this.protection = (protectionBit == 1);
	}

	private void setBitRate(int bitrate) throws InvalidDataException {
		try {
			this.bitrate = version.getBitrate(this.layer, bitrate);
		} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
			throw new InvalidDataException("Invalid bitrate in frame header");
		}
	}


	private void setSampleRate(int sampleRate) throws InvalidDataException {
		try {
			this.sampleRate = version.getSampleRate(sampleRate);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new InvalidDataException("Invalid sample rate in frame header");
		}
	}

	private void setPadding(int paddingBit) {
		this.padding = (paddingBit == 1);
	}

	private void setPrivate(int privateBit) {
		this.privat = (privateBit == 1);
	}


	private void setChannelMode(int channelMode) throws InvalidDataException {
		try {
			this.channelMode = ChannelMode.values()[channelMode];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new InvalidDataException("Invalid channel mode in frame header");
		}
	}

	public enum ExtensionMode {
		BANDS_4_31(0, Layer.LAYER_1, Layer.LAYER_2),
		BANDS_8_310(1, Layer.LAYER_1, Layer.LAYER_2),
		BANDS_12_31(2, Layer.LAYER_1, Layer.LAYER_2),
		BANDS_16_31(3, Layer.LAYER_1, Layer.LAYER_2),
		NONE(0, Layer.LAYER_3),
		INTENSITY_STEREO(1, Layer.LAYER_3),
		M_S_STEREO(2, Layer.LAYER_3),
		INTENSITY_M_S_STEREO(3, Layer.LAYER_3),
		NA(-1, Layer.LAYER_1, Layer.LAYER_2, Layer.LAYER_3);

		private final Layer[] layers;
		private final int extensionIdx;

		ExtensionMode(int extensionIdx, Layer... layers) {
			this.extensionIdx = extensionIdx;
			this.layers = layers;
		}

		public static ExtensionMode findModeIdxLayer(int idx, Layer layer) {
			for (ExtensionMode em : ExtensionMode.values()) {
				if (em.extensionIdx == idx && Arrays.asList(em.layers).contains(layer)) {
					return em;
				}
			}

			return null;
		}
	}

	private void setModeExtension(int modeExtension) throws InvalidDataException {
		if (channelMode != ChannelMode.JOINT_STEREO) {
			this.modeExtension = ExtensionMode.NA;
		} else {
			ExtensionMode em = ExtensionMode.findModeIdxLayer(modeExtension, this.layer);
			if (em == null) {
				throw new InvalidDataException("Invalid mode extension in frame header");
			}

			this.modeExtension = em;
		}
	}

	private void setCopyright(int copyrightBit) {
		this.copyright = (copyrightBit == 1);
	}

	private void setOriginal(int originalBit) {
		this.original = (originalBit == 1);
	}

	public enum Emphasis {
		NONE, _50_15_MS, CCITT_J_17
	}

	private void setEmphasis(int emphasis) throws InvalidDataException {
		try {
			this.emphasis = Emphasis.values()[emphasis];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new InvalidDataException("Invalid emphasis in frame header");
		}
	}

	public int getBitrate() {
		return bitrate;
	}

	public ChannelMode getChannelMode() {
		return channelMode;
	}

	public boolean isCopyright() {
		return copyright;
	}

	public Emphasis getEmphasis() {
		return emphasis;
	}

	public Layer getLayer() {
		return layer;
	}

	public ExtensionMode getModeExtension() {
		return modeExtension;
	}

	public boolean isOriginal() {
		return original;
	}

	public boolean hasPadding() {
		return padding;
	}

	public boolean isPrivate() {
		return privat;
	}

	public boolean isProtection() {
		return protection;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public MPEGVersion getVersion() {
		return version;
	}

	public int getLengthInBytes() {
		long length;
		int pad;
		if (padding) pad = 1;
		else pad = 0;
		if (layer == Layer.LAYER_1) {
			length = ((48000 * bitrate) / sampleRate) + (pad * 4);
		} else {
			length = ((144000 * bitrate) / sampleRate) + pad;
		}
		return (int) length;
	}
}
