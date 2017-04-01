package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.*;

public class ID3v1GenresTest {

	@Test
	public void returnsMinusOneForNonExistentGenre() throws Exception {
		assertEquals(-1, ID3v1Genres.matchGenreDescription("non existent"));
	}

	@Test
	public void returnsCorrectGenreIdForFirstExistentGenre() throws Exception {
		assertEquals(0, ID3v1Genres.matchGenreDescription("Blues"));
	}

	@Test
	public void returnsCorrectGenreIdForPolka() throws Exception {
		assertEquals(75, ID3v1Genres.matchGenreDescription("Polka"));
	}

	@Test
	public void returnsCorrectGenreIdForLastExistentGenre() throws Exception {
		assertEquals(147, ID3v1Genres.matchGenreDescription("Synthpop"));
	}

	@Test
	public void ignoresCase() throws Exception {
		assertEquals(137, ID3v1Genres.matchGenreDescription("heavy METAL"));
	}

}
