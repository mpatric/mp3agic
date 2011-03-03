package com.mpatric.mp3agic.app;

import com.mpatric.mp3agic.app.BaseApp;

import junit.framework.TestCase;

public class BaseAppTest extends TestCase {

	public void testShouldStripPathsFromFilenames() throws Exception {
		BaseApp app = new BaseApp();
		assertEquals("test.mp3", app.extractFilename("test.mp3"));
		assertEquals("test.mp3", app.extractFilename("/test.mp3"));
		assertEquals("test.mp3", app.extractFilename("\\test.mp3"));
		assertEquals("test.mp3", app.extractFilename("/something/else/test.mp3"));
		assertEquals("test.mp3", app.extractFilename("\\something\\else\\test.mp3"));
		assertEquals("test.mp3", app.extractFilename("C:/something/else/test.mp3"));
		assertEquals("test.mp3", app.extractFilename("C:\\something\\else\\test.mp3"));
		assertEquals("test.mp3", app.extractFilename("C:/something\\else/test.mp3"));
		assertEquals("test.mp3", app.extractFilename("C:\\something/else\\test.mp3"));
	}
	
	public void testShouldStripFilenamesFromPaths() throws Exception {
		BaseApp app = new BaseApp();
		assertEquals("", app.extractPath("test.mp3"));
		assertEquals("/", app.extractPath("/"));
		assertEquals("/", app.extractPath("/test.mp3"));
		assertEquals("\\", app.extractPath("\\test.mp3"));
		assertEquals("/something/else/", app.extractPath("/something/else/test.mp3"));
		assertEquals("\\something\\else\\", app.extractPath("\\something\\else\\test.mp3"));
		assertEquals("C:/something/else/", app.extractPath("C:/something/else/test.mp3"));
		assertEquals("C:\\something\\else\\", app.extractPath("C:\\something\\else\\test.mp3"));
		assertEquals("C:/something\\else/", app.extractPath("C:/something\\else/test.mp3"));
		assertEquals("C:\\something/else\\", app.extractPath("C:\\something/else\\test.mp3"));
	}
}
