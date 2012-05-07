package com.mpatric.mp3agic.app;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.Version;


public class Mp3Retag extends BaseApp {

	private static final String RETAG_EXTENSION = ".retag";
	private static final String BACKUP_EXTENSION = ".bak";
	private static final int CUSTOM_TAG_WARNING_THRESHOLD = 1024;

	protected static Map<String, String> imageFileTypes;
	protected static boolean attachImage = false;
	protected static boolean keepCustomTag = false;
	protected static String comment = null;
	protected static String encoder = null;
	protected static String customTag = null;
	protected static String filename = null;
	protected Mp3File mp3file;
	
	{
		imageFileTypes = new LinkedHashMap<String, String>();
		imageFileTypes.put("jpg", "image/jpeg");
		imageFileTypes.put("jpeg", "image/jpeg");
		imageFileTypes.put("png", "image/png");
	}
	
	protected Mp3Retag() {
	}

	protected Mp3Retag(String filename) {
		try {
			mp3file = new Mp3File(filename);
			boolean hasId3v1Tag = mp3file.hasId3v1Tag();
			boolean hasId3v2Tag = mp3file.hasId3v2Tag();
			boolean hasImage = false;
			if (hasId3v2Tag && mp3file.getId3v2Tag().getAlbumImage() != null) hasImage = true;
			if (! hasId3v1Tag && ! hasId3v2Tag) {
				printError("ERROR processing \"" + extractFilename(filename) + "\" - no ID3 tags found");
			} else {
				boolean hasCustomTag = mp3file.hasCustomTag();
				if (hasCustomTag && mp3file.getCustomTag().length > CUSTOM_TAG_WARNING_THRESHOLD) {
					printError("WARNING processing \"" + extractFilename(filename) + "\" - custom tag is " + mp3file.getCustomTag().length + " bytes, potential corrupt file");
				}
				retag();
				StringBuffer message = new StringBuffer();
				message.append("Retagged \"");
				message.append(extractFilename(mp3file.getFilename()));
				message.append("\"");
				if (! hasId3v1Tag) message.append(", added ID3v1 tag");
				if (! hasId3v2Tag) message.append(", added ID3v2 tag");
				if (! hasImage && mp3file.getId3v2Tag().getAlbumImage() != null) {
					message.append(", added album image");
				}
				if (! hasCustomTag) {
					if (mp3file.hasCustomTag()) message.append(", added custom tag");
				} else {
					if (! mp3file.hasCustomTag()) message.append(", removed custom tag");
					else if (keepCustomTag && customTag != null && customTag.length() > 0) message.append(", appended to custom tag");
					else message.append(", replaced custom tag"); 
				}
				printOut(message.toString());
			}
		} catch (BaseException e) {
			printError("ERROR processing \"" + extractFilename(filename) + "\" - " + e.getDetailedMessage());
			if (mp3file != null) deleteFile(mp3file.getFilename() + RETAG_EXTENSION);
		} catch (Exception e) {
			printError("ERROR processing \"" + extractFilename(filename) + "\" - " + formatExceptionMessage(e));
			if (mp3file != null) deleteFile(mp3file.getFilename() + RETAG_EXTENSION);
		}
	}

	private void retag() throws IOException, NotSupportedException {
		updateId3Tags();
		updateCustomTag();
		mp3file.save(mp3file.getFilename() + RETAG_EXTENSION);
		renameFiles();
	}

	private void updateId3Tags() {
		ID3Wrapper oldId3Wrapper = new ID3Wrapper(mp3file.getId3v1Tag(), mp3file.getId3v2Tag());
		ID3Wrapper newId3Wrapper = new ID3Wrapper(new ID3v1Tag(), new ID3v23Tag());
		preProcess(oldId3Wrapper);
		newId3Wrapper.setTrack(cleanTrack(oldId3Wrapper.getTrack()));
		newId3Wrapper.setArtist(trimField(oldId3Wrapper.getArtist()));
		newId3Wrapper.setTitle(trimField(oldId3Wrapper.getTitle()));
		newId3Wrapper.setArtist(trimField(oldId3Wrapper.getArtist()));
		newId3Wrapper.setAlbum(trimField(oldId3Wrapper.getAlbum()));
		newId3Wrapper.setYear(trimField(oldId3Wrapper.getYear()));
		newId3Wrapper.setGenre(oldId3Wrapper.getGenre());
		if (comment != null) {
			newId3Wrapper.setComment(trimField(comment));
		} else {
			newId3Wrapper.setComment(trimField(oldId3Wrapper.getComment()));
		}
		newId3Wrapper.setComposer(trimField(oldId3Wrapper.getComposer()));
		newId3Wrapper.setOriginalArtist(trimField(oldId3Wrapper.getOriginalArtist()));
		newId3Wrapper.setCopyright(trimField(oldId3Wrapper.getCopyright()));
		newId3Wrapper.setUrl(trimField(oldId3Wrapper.getUrl()));
		if (encoder != null) {
			newId3Wrapper.setEncoder(trimField(encoder));
		} else {
			newId3Wrapper.setEncoder(trimField(oldId3Wrapper.getEncoder()));
		}
		if (! attachImage || ! findAndSetAlbumImage(newId3Wrapper)) {
			newId3Wrapper.setAlbumImage(oldId3Wrapper.getAlbumImage(), cleanImageMimeType(oldId3Wrapper.getAlbumImageMimeType()));
		}
		newId3Wrapper.getId3v2Tag().setPadding(true);
		postProcess(newId3Wrapper);
		mp3file.setId3v1Tag(newId3Wrapper.getId3v1Tag());
		mp3file.setId3v2Tag(newId3Wrapper.getId3v2Tag());
	}
	
	private String cleanTrack(String track) {
		if (track == null) return track;
		int slashIndex = track.indexOf('/');
		if (slashIndex < 0) return trimField(track);
		return trimField(track.substring(0, slashIndex));
	}

	private String cleanImageMimeType(String mimeType) {
		if (mimeType == null) return mimeType;
		if (mimeType.indexOf('/') >= 0) return mimeType;
		return "image/" + mimeType.toLowerCase();
	}

	protected void preProcess(ID3Wrapper id3Wrapper) {
	}
	
	protected void postProcess(ID3Wrapper id3Wrapper) {
	}
	
	private String trimField(String value) {
		if (value == null) return null;
		return value.trim();
	}

	private boolean findAndSetAlbumImage(ID3Wrapper id3Wrapper) {
		String path = extractPath(mp3file.getFilename());
		if (trySetAlbumImage(id3Wrapper, path + id3Wrapper.getArtist() + " - " + id3Wrapper.getAlbum())) {
			return true;
		}
		if (trySetAlbumImage(id3Wrapper, path + toCompressedString(id3Wrapper.getArtist()) + "-" + toCompressedString(id3Wrapper.getAlbum()))) {
			return true;
		}
		if (trySetAlbumImage(id3Wrapper, path + id3Wrapper.getAlbum())) {
			return true;
		}
		if (trySetAlbumImage(id3Wrapper, path + toCompressedString(id3Wrapper.getAlbum()))) {
			return true;
		}
		if (trySetAlbumImage(id3Wrapper, path + "folder")) {
			return true;
		}
		int andPosInArtist = id3Wrapper.getArtist().indexOf(" & ");
		if (andPosInArtist > 0) {
			if (trySetAlbumImage(id3Wrapper, path + id3Wrapper.getArtist().substring(0, andPosInArtist) + " - " + id3Wrapper.getAlbum())) {
				return true;
			}
			if (trySetAlbumImage(id3Wrapper, path + toCompressedString(id3Wrapper.getArtist().substring(0, andPosInArtist)) + "-"  + toCompressedString(id3Wrapper.getAlbum()))) {
				return true;
			}
		}
		printError("WARNING processing \"" + extractFilename(mp3file.getFilename()) + "\" - no album image found");
		return false;
	}

	private String toCompressedString(String s) {
		StringBuffer compressed = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || (ch == '&') || (ch == '+') || (ch == '(') || (ch == ')')) {
				compressed.append(ch);
			}
		}
		return compressed.toString();
	}
	
	private boolean trySetAlbumImage(ID3Wrapper newId3Wrapper, String filenameWithoutExtension) {
		Iterator<String> iterator = imageFileTypes.keySet().iterator();
		while (iterator.hasNext()) {
			String fileExtension = iterator.next();
			String mimeType = imageFileTypes.get(fileExtension.toLowerCase());
			if (trySetAlbumImage(newId3Wrapper, filenameWithoutExtension + "." + fileExtension, mimeType)) {
				return true;
			}
		}
		return false;
	}

	private boolean trySetAlbumImage(ID3Wrapper id3Wrapper, String filename, String mimeType) {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(filename, "r");
			byte[] bytes = new byte[(int) file.length()];
			if (file.read(bytes) != file.length()) {
				return false;
			}
			id3Wrapper.setAlbumImage(bytes, mimeType);
			return true;
		} catch (IOException e) {
			// do nothing
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		return false;
	}
	
	private void updateCustomTag() {
		byte[] existingCustomTag = mp3file.getCustomTag();
		byte[] newCustomTag = null;
		if (keepCustomTag && existingCustomTag != null && existingCustomTag.length > 0) {
			if (customTag != null && customTag.length() > 0) {
				EncodedText customTagEncodedText = new EncodedText(customTag);
				byte bytes[] = customTagEncodedText.toBytes(true);
				int newLength = existingCustomTag.length + bytes.length;
				newCustomTag = new byte[newLength];
				BufferTools.copyIntoByteBuffer(existingCustomTag, 0, existingCustomTag.length, newCustomTag, 0);
				BufferTools.copyIntoByteBuffer(bytes, 0, bytes.length, newCustomTag, existingCustomTag.length);
			} else {
				newCustomTag = mp3file.getCustomTag();
			}
		} else if (customTag != null && customTag.length() > 0) {
			EncodedText customTagEncodedText = new EncodedText(customTag);
			newCustomTag = customTagEncodedText.toBytes(true);
		}
		mp3file.setCustomTag(newCustomTag);
	}
	
	protected void renameFiles() {
		File originalFile = new File(filename);
		File backupFile = new File(filename + BACKUP_EXTENSION);
		File retaggedFile = new File(filename + RETAG_EXTENSION);
		if (backupFile.exists()) {
			backupFile.delete();
		}
		originalFile.renameTo(backupFile);
		retaggedFile.renameTo(originalFile);
	}
	
	private void deleteFile(String filename) {
		File file = new File(filename);
		file.delete();
	}

	public static void main(String[] args) {
		if (! parseArgs(args)) {
			usage();
		} else {
			new Mp3Retag(filename);
		}
	}
	
	protected static boolean parseArgs(String args[]) {
		if (args.length < 1) {
			return false;
		} 
		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {				
				if ("-i".equals(args[i])) {
					attachImage = true;
				} else if ("-k".equals(args[i])) {
					keepCustomTag = true;
				} else if ("-c".equals(args[i])) {
					try {
						comment = args[i + 1];
						i++;
					} catch (ArrayIndexOutOfBoundsException e) {
						return false;
					}
				} else if ("-e".equals(args[i])) {
					try {
						encoder = args[i + 1];
						i++;
					} catch (ArrayIndexOutOfBoundsException e) {
						return false;
					}
				} else if ("-z".equals(args[i])) {
					try {
						customTag = args[i + 1];
						i++;
					} catch (ArrayIndexOutOfBoundsException e) {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (filename != null) {
					return false;
				}
				filename = args[i];
			}
		}
		if (filename == null) {
			return false;
		}
		return true;
	}

	private static void usage() {
		System.out.println("mp3retag [mp3agic " + Version.asString() + "]");
	}
}
