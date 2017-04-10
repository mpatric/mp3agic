package com.mpatric.mp3agic;

public class MpegFrame {

	public static final String MPEG_VERSION_1_0 = "1.0";
	public static final String MPEG_VERSION_2_0 = "2.0";
	public static final String MPEG_VERSION_2_5 = "2.5";
	public static final String MPEG_LAYER_1 = "I";
	public static final String MPEG_LAYER_2 = "II";
	public static final String MPEG_LAYER_3 = "III";
	public static final String[] MPEG_LAYERS = {null, MPEG_LAYER_1, MPEG_LAYER_2, MPEG_LAYER_3};
	public static final String CHANNEL_MODE_MONO = "Mono";
	public static final String CHANNEL_MODE_DUAL_MONO = "Dual mono";
	public static final String CHANNEL_MODE_JOINT_STEREO = "Joint stereo";
	public static final String CHANNEL_MODE_STEREO = "Stereo";
	public static final String MODE_EXTENSION_BANDS_4_31 = "Bands 4-31";
	public static final String MODE_EXTENSION_BANDS_8_31 = "Bands 8-31";
	public static final String MODE_EXTENSION_BANDS_12_31 = "Bands 12-31";
	public static final String MODE_EXTENSION_BANDS_16_31 = "Bands 16-31";
	public static final String MODE_EXTENSION_NONE = "None";
	public static final String MODE_EXTENSION_INTENSITY_STEREO = "Intensity stereo";
	public static final String MODE_EXTENSION_M_S_STEREO = "M/S stereo";
	public static final String MODE_EXTENSION_INTENSITY_M_S_STEREO = "Intensity & M/S stereo";
	public static final String MODE_EXTENSION_NA = "n/a";
	public static final String EMPHASIS_NONE = "None";
	public static final String EMPHASIS__50_15_MS = "50/15 ms";
	public static final String EMPHASIS_CCITT_J_17 = "CCITT J.17";

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

	private String version;
	private int layer;
	private boolean protection;
	private int bitrate;
	private int sampleRate;
	private boolean padding;
	private boolean privat;
	private String channelMode;
	private String modeExtension;
	private boolean copyright;
	private boolean original;
	private String emphasis;

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

	private void setFields(long frameHeader) throws InvalidDataException {
		long frameSync = extractField(frameHeader, BITMASK_FRAME_SYNC);
		if (frameSync != FRAME_SYNC) throw new InvalidDataException("Frame sync missing");
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

	protected int extractField(long frameHeader, long bitMask) {
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
		switch (version) {
			case 0:
				this.version = MPEG_VERSION_2_5;
				break;
			case 2:
				this.version = MPEG_VERSION_2_0;
				break;
			case 3:
				this.version = MPEG_VERSION_1_0;
				break;
			default:
				throw new InvalidDataException("Invalid mpeg audio version in frame header");
		}
	}

	private void setLayer(int layer) throws InvalidDataException {
		switch (layer) {
			case 1:
				this.layer = 3;
				break;
			case 2:
				this.layer = 2;
				break;
			case 3:
				this.layer = 1;
				break;
			default:
				throw new InvalidDataException("Invalid mpeg layer description in frame header");
		}
	}

	private void setProtection(int protectionBit) {
		this.protection = (protectionBit == 1);
	}

	private void setBitRate(int bitrate) throws InvalidDataException {
		if (MPEG_VERSION_1_0.equals(version)) {
			if (layer == 1) {
				switch (bitrate) {
					case 1:
						this.bitrate = 32;
						return;
					case 2:
						this.bitrate = 64;
						return;
					case 3:
						this.bitrate = 96;
						return;
					case 4:
						this.bitrate = 128;
						return;
					case 5:
						this.bitrate = 160;
						return;
					case 6:
						this.bitrate = 192;
						return;
					case 7:
						this.bitrate = 224;
						return;
					case 8:
						this.bitrate = 256;
						return;
					case 9:
						this.bitrate = 288;
						return;
					case 10:
						this.bitrate = 320;
						return;
					case 11:
						this.bitrate = 352;
						return;
					case 12:
						this.bitrate = 384;
						return;
					case 13:
						this.bitrate = 416;
						return;
					case 14:
						this.bitrate = 448;
						return;
				}
			} else if (layer == 2) {
				switch (bitrate) {
					case 1:
						this.bitrate = 32;
						return;
					case 2:
						this.bitrate = 48;
						return;
					case 3:
						this.bitrate = 56;
						return;
					case 4:
						this.bitrate = 64;
						return;
					case 5:
						this.bitrate = 80;
						return;
					case 6:
						this.bitrate = 96;
						return;
					case 7:
						this.bitrate = 112;
						return;
					case 8:
						this.bitrate = 128;
						return;
					case 9:
						this.bitrate = 160;
						return;
					case 10:
						this.bitrate = 192;
						return;
					case 11:
						this.bitrate = 224;
						return;
					case 12:
						this.bitrate = 256;
						return;
					case 13:
						this.bitrate = 320;
						return;
					case 14:
						this.bitrate = 384;
						return;
				}
			} else if (layer == 3) {
				switch (bitrate) {
					case 1:
						this.bitrate = 32;
						return;
					case 2:
						this.bitrate = 40;
						return;
					case 3:
						this.bitrate = 48;
						return;
					case 4:
						this.bitrate = 56;
						return;
					case 5:
						this.bitrate = 64;
						return;
					case 6:
						this.bitrate = 80;
						return;
					case 7:
						this.bitrate = 96;
						return;
					case 8:
						this.bitrate = 112;
						return;
					case 9:
						this.bitrate = 128;
						return;
					case 10:
						this.bitrate = 160;
						return;
					case 11:
						this.bitrate = 192;
						return;
					case 12:
						this.bitrate = 224;
						return;
					case 13:
						this.bitrate = 256;
						return;
					case 14:
						this.bitrate = 320;
						return;
				}
			}
		} else if (MPEG_VERSION_2_0.equals(version) || MPEG_VERSION_2_5.equals(version)) {
			if (layer == 1) {
				switch (bitrate) {
					case 1:
						this.bitrate = 32;
						return;
					case 2:
						this.bitrate = 48;
						return;
					case 3:
						this.bitrate = 56;
						return;
					case 4:
						this.bitrate = 64;
						return;
					case 5:
						this.bitrate = 80;
						return;
					case 6:
						this.bitrate = 96;
						return;
					case 7:
						this.bitrate = 112;
						return;
					case 8:
						this.bitrate = 128;
						return;
					case 9:
						this.bitrate = 144;
						return;
					case 10:
						this.bitrate = 160;
						return;
					case 11:
						this.bitrate = 176;
						return;
					case 12:
						this.bitrate = 192;
						return;
					case 13:
						this.bitrate = 224;
						return;
					case 14:
						this.bitrate = 256;
						return;
				}
			} else if (layer == 2 || layer == 3) {
				switch (bitrate) {
					case 1:
						this.bitrate = 8;
						return;
					case 2:
						this.bitrate = 16;
						return;
					case 3:
						this.bitrate = 24;
						return;
					case 4:
						this.bitrate = 32;
						return;
					case 5:
						this.bitrate = 40;
						return;
					case 6:
						this.bitrate = 48;
						return;
					case 7:
						this.bitrate = 56;
						return;
					case 8:
						this.bitrate = 64;
						return;
					case 9:
						this.bitrate = 80;
						return;
					case 10:
						this.bitrate = 96;
						return;
					case 11:
						this.bitrate = 112;
						return;
					case 12:
						this.bitrate = 128;
						return;
					case 13:
						this.bitrate = 144;
						return;
					case 14:
						this.bitrate = 160;
						return;
				}
			}
		}
		throw new InvalidDataException("Invalid bitrate in frame header");
	}

	private void setSampleRate(int sampleRate) throws InvalidDataException {
		if (MPEG_VERSION_1_0.equals(version)) {
			switch (sampleRate) {
				case 0:
					this.sampleRate = 44100;
					return;
				case 1:
					this.sampleRate = 48000;
					return;
				case 2:
					this.sampleRate = 32000;
					return;
			}
		} else if (MPEG_VERSION_2_0.equals(version)) {
			switch (sampleRate) {
				case 0:
					this.sampleRate = 22050;
					return;
				case 1:
					this.sampleRate = 24000;
					return;
				case 2:
					this.sampleRate = 16000;
					return;
			}
		} else if (MPEG_VERSION_2_5.equals(version)) {
			switch (sampleRate) {
				case 0:
					this.sampleRate = 11025;
					return;
				case 1:
					this.sampleRate = 12000;
					return;
				case 2:
					this.sampleRate = 8000;
					return;
			}
		}
		throw new InvalidDataException("Invalid sample rate in frame header");
	}

	private void setPadding(int paddingBit) {
		this.padding = (paddingBit == 1);
	}

	private void setPrivate(int privateBit) {
		this.privat = (privateBit == 1);
	}

	private void setChannelMode(int channelMode) throws InvalidDataException {
		switch (channelMode) {
			case 0:
				this.channelMode = CHANNEL_MODE_STEREO;
				break;
			case 1:
				this.channelMode = CHANNEL_MODE_JOINT_STEREO;
				break;
			case 2:
				this.channelMode = CHANNEL_MODE_DUAL_MONO;
				break;
			case 3:
				this.channelMode = CHANNEL_MODE_MONO;
				break;
			default:
				throw new InvalidDataException("Invalid channel mode in frame header");
		}
	}

	private void setModeExtension(int modeExtension) throws InvalidDataException {
		if (!CHANNEL_MODE_JOINT_STEREO.equals(channelMode)) {
			this.modeExtension = MODE_EXTENSION_NA;
		} else {
			if (layer == 1 || layer == 2) {
				switch (modeExtension) {
					case 0:
						this.modeExtension = MODE_EXTENSION_BANDS_4_31;
						return;
					case 1:
						this.modeExtension = MODE_EXTENSION_BANDS_8_31;
						return;
					case 2:
						this.modeExtension = MODE_EXTENSION_BANDS_12_31;
						return;
					case 3:
						this.modeExtension = MODE_EXTENSION_BANDS_16_31;
						return;
				}
			} else if (layer == 3) {
				switch (modeExtension) {
					case 0:
						this.modeExtension = MODE_EXTENSION_NONE;
						return;
					case 1:
						this.modeExtension = MODE_EXTENSION_INTENSITY_STEREO;
						return;
					case 2:
						this.modeExtension = MODE_EXTENSION_M_S_STEREO;
						return;
					case 3:
						this.modeExtension = MODE_EXTENSION_INTENSITY_M_S_STEREO;
						return;
				}
			}
			throw new InvalidDataException("Invalid mode extension in frame header");
		}
	}

	private void setCopyright(int copyrightBit) {
		this.copyright = (copyrightBit == 1);
	}

	private void setOriginal(int originalBit) {
		this.original = (originalBit == 1);
	}

	private void setEmphasis(int emphasis) throws InvalidDataException {
		switch (emphasis) {
			case 0:
				this.emphasis = EMPHASIS_NONE;
				break;
			case 1:
				this.emphasis = EMPHASIS__50_15_MS;
				break;
			case 3:
				this.emphasis = EMPHASIS_CCITT_J_17;
				break;
			default:
				throw new InvalidDataException("Invalid emphasis in frame header");
		}
	}

	public int getBitrate() {
		return bitrate;
	}

	public String getChannelMode() {
		return channelMode;
	}

	public boolean isCopyright() {
		return copyright;
	}

	public String getEmphasis() {
		return emphasis;
	}

	public String getLayer() {
		return MPEG_LAYERS[layer];
	}

	public String getModeExtension() {
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

	public String getVersion() {
		return version;
	}

	public int getLengthInBytes() {
		long length;
		int pad;
		if (padding) pad = 1;
		else pad = 0;
		if (layer == 1) {
			length = ((48000 * bitrate) / sampleRate) + (pad * 4);
		} else {
			length = ((144000 * bitrate) / sampleRate) + pad;
		}
		return (int) length;
	}
}
