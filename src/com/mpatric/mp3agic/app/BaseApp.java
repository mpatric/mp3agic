package com.mpatric.mp3agic.app;

public class BaseApp {

	protected String extractFilename(String filename) {
		int bsPos = filename.lastIndexOf('\\');
		int fsPos = filename.lastIndexOf('/');
		if (bsPos >= 0 || fsPos >= 0) {
			if (fsPos == -1 || bsPos > fsPos) {
				return filename.substring(bsPos + 1);
			}
			return filename.substring(fsPos + 1);
		}
		return filename;
	}
	
	protected String extractPath(String filename) {
		int bsPos = filename.lastIndexOf('\\');
		int fsPos = filename.lastIndexOf('/');
		if (bsPos >= 0 || fsPos >= 0) {
			if (fsPos == -1 || bsPos > fsPos) {
				return filename.substring(0, bsPos + 1);
			}
			return filename.substring(0, fsPos + 1);
		}
		return "";
	}
	
	protected String extractExtension(String filename) {
		int dPos = filename.lastIndexOf('.');
		if (dPos >= 0) {
			return filename.substring(dPos);
		}
		return "";
	}
	
	protected String formatExceptionMessage(Exception e) {
		String message = "[" + e.getClass().getName();
		if (e.getMessage() != null) message += ": " + e.getMessage();
		message += "]";
		return message;
	}

	protected void printError(String message) {
		System.err.println(message);
	}

	protected void printOut(String message) {
		System.out.println(message);
	}
}
