package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mpatric.mp3agic.annotations.FrameMember;

public abstract class AbstractID3v2FrameData {
	
	private final boolean unsynchronisation;
	
	public AbstractID3v2FrameData(boolean unsynchronisation) {
		this.unsynchronisation = unsynchronisation;
	}
	
	protected void synchroniseAndUnpackFrameData(byte[] bytes) throws InvalidDataException {
		if (unsynchronisation && BufferTools.sizeSynchronisationWouldSubtract(bytes) > 0) {
			byte[] synchronisedBytes = BufferTools.synchroniseBuffer(bytes);
			unpackFrameData(synchronisedBytes);
		} else {
			unpackFrameData(bytes);
		}
	}
	
	protected byte[] packAndUnsynchroniseFrameData() {
		byte[] bytes = packFrameData();
		if (unsynchronisation && BufferTools.sizeUnsynchronisationWouldAdd(bytes) > 0) {
			return BufferTools.unsynchroniseBuffer(bytes);
		}
		return bytes;
	}
	
	protected byte[] toBytes() {
		return packAndUnsynchroniseFrameData();
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof AbstractID3v2FrameData)) return false;
		AbstractID3v2FrameData other = (AbstractID3v2FrameData) obj;
		if (unsynchronisation != other.unsynchronisation) return false;
		return true;
	}
	
	private static class FrameFieldData implements Comparable<FrameFieldData>{
		public Field field;
		public FrameMember frameMember;
		
		public FrameFieldData(Field field, FrameMember frameMember) {
			this.field = field; this.frameMember = frameMember;
		}
		
		public int compareTo(FrameFieldData arg0) {
			return Integer.valueOf(this.frameMember.ordinal()).
					compareTo(arg0.frameMember.ordinal());
		}
	}
	
	private static Map<Class<? extends AbstractID3v2FrameData>, List<FrameFieldData>> FIELD_MAP = 
			new HashMap<Class<? extends AbstractID3v2FrameData>, List<FrameFieldData>>();

	private static List<FrameFieldData> fetchMetadata(Class<? extends AbstractID3v2FrameData> c) {
		List<FrameFieldData> metadata = FIELD_MAP.get(c);
		if (null == metadata) {
			metadata = new ArrayList<FrameFieldData>(); 	
			Field [] fs = c.getDeclaredFields();
			for (Field f : fs) {
				FrameMember fm = f.getAnnotation(FrameMember.class);
				if (fm != null) {
					metadata.add(new FrameFieldData(f, fm));
				}
			}
			Collections.sort(metadata);
			metadata = Collections.unmodifiableList(metadata);
			FIELD_MAP.put(c, metadata);
		}
		return metadata;
	}

	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		List<FrameFieldData> fds = fetchMetadata(getClass());
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		Encoding encoding = null;
		for (FrameFieldData fd : fds) {
			Class<?> c = fd.field.getType();

			// Encoding
			if (c.equals(Encoding.class)) {
				encoding = Encoding.getEncoding(input.read());
				setFieldData(fd.field, this, encoding);
				continue;
			}
			
			// Strings
			if (c.equals(String.class)) {
				int width = fd.frameMember.width();
				if (-1 != width) {
					byte[] buffer = new byte[width];
					for (int i = 0; i < width; ++i) {
						buffer[i] = (byte) input.read();
					}
					setFieldData(fd.field, this, new String(buffer, Charsets.ISO_8859_1));
					continue;
				}
				
				// If the encoded flag is set, we should have encountered an encoding 
				if (fd.frameMember.encoded() && null == encoding) {
					throw new IllegalStateException("No encoding found");
				}
				
				Encoding readEncoding = fd.frameMember.encoded() ? 
						encoding : Encoding.ENCODING_ISO_8859_1;
				
				String value = readEncoding.parse(input, fd.frameMember.terminated());
				setFieldData(fd.field, this, value);
				continue;
			}
			
			// byte arrays
			if (c.equals(byte[].class)) {
				byte[] value = BufferTools.streamIntoByteBuffer(input);
				setFieldData(fd.field, this, value);
				continue;
			}

			// bytes & ints
			if (c.equals(int.class)) {
				Integer i = input.read();
				setFieldData(fd.field, this, i);
				continue;
			}

			if (c.equals(byte.class)) {
				Byte i = (byte) input.read();
				setFieldData(fd.field, this, i);
				continue;
			}

			throw new IllegalStateException("Unexpected type " + c);
		}
		if (input.available() > 0) {
			throw new IllegalStateException("Leftover data from frame parse");
		}
	}
	
	protected byte[] packFrameData() {
		List<FrameFieldData> fds = fetchMetadata(getClass());
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		Encoding encoding = null;
		for (FrameFieldData fd : fds) {
			Class<?> c = fd.field.getType();
			Object value = getFieldData(fd.field, this);

			// Encoding
			if (c.equals(Encoding.class)) {
				encoding = (Encoding) value;
				if (null == encoding) {
					encoding = Encoding.getDefault();
				}
				output.write(encoding.ordinal());
				continue;
			}
			
			// Strings
			if (c.equals(String.class)) {
				int width = fd.frameMember.width();
				String s = (String) value;
				if (-1 != width) {
					byte[] write = new byte[width];
					if (null != s) {
						byte [] source = s.getBytes();
						for (int i = 0; i < Math.min(source.length, width); ++i) {
							write[i] = source[i];
						}
					}
					output.write(write);
					continue;
				}

				if (fd.frameMember.encoded() && null == encoding) {
					throw new IllegalStateException("No encoding found");
				}
				Encoding writeEncoding = (fd.frameMember.encoded()) ?
						encoding : Encoding.ENCODING_ISO_8859_1;

				output.write(writeEncoding.encode(s, fd.frameMember.terminated()));
				continue;
			}
			
			// byte arrays
			if (c.equals(byte[].class)) {
				output.write((byte[]) value);
				continue;
			}

			// bytes
			if (c.equals(int.class) || c.equals(byte.class)) {
				output.write(((Number)value).intValue());
				continue;
			}

			throw new IllegalStateException();
		}
		return output.toByteArray();
	}

	private static Object getFieldData(Field f, Object obj) {
		try {
			f.setAccessible(true);
			return f.get(obj);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void setFieldData(Field f, Object parent, Object value) {
		try {
			f.setAccessible(true);
			f.set(parent, value);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
}
