package com.mpatric.mp3agic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ID3WrapperTest {
	//region getId3v1Tag
	@Test
	public void returnsV1Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals(id3v1Tag, wrapper.getId3v1Tag());
	}

	@Test
	public void returnsNullV1Tag() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		assertEquals(null, wrapper.getId3v1Tag());
	}
	//endregion

	//region getId3v2Tag
	@Test
	public void returnsV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals(id3v2Tag, wrapper.getId3v2Tag());
	}

	@Test
	public void returnsNullV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals(null, wrapper.getId3v2Tag());
	}
	//endregion

	//region getTrack
	@Test
	public void getTrackReturnsV2TagsTrackBeforeV1TagsTrack() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTrack("V2 Track");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Track", wrapper.getTrack());
	}

	@Test
	public void getTrackReturnsV1TagsTrackIfV2TagsTrackIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTrack("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Track", wrapper.getTrack());
	}

	@Test
	public void getTrackReturnsV1TagsTrackIfV2TagsTrackDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTrack(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Track", wrapper.getTrack());
	}

	@Test
	public void getTrackReturnsV1TagsTrackIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals("V1 Track", wrapper.getTrack());
	}

	@Test
	public void getTrackReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertNull(wrapper.getTrack());
	}
	//endregion

	//region setTrack
	@Test
	public void setsTrackOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setTrack("a track");
		assertEquals("a track", id3v1Tag.getTrack());
		assertEquals("a track", id3v2Tag.getTrack());
	}

	@Test
	public void setsTrackOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setTrack("a track");
		assertEquals("a track", id3v1Tag.getTrack());
	}

	@Test
	public void setsTrackOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setTrack("a track");
		assertEquals("a track", id3v2Tag.getTrack());
	}

	@Test
	public void setTrackDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setTrack("a track");
	}
	//endregion

	//region getArtist
	@Test
	public void getArtistReturnsV2TagsArtistBeforeV1TagsArtist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setArtist("V2 Artist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Artist", wrapper.getArtist());
	}

	@Test
	public void getArtistReturnsV1TagsArtistIfV2TagsArtistIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setArtist("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Artist", wrapper.getArtist());
	}

	@Test
	public void getArtistReturnsV1TagsArtistIfV2TagsArtistDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setArtist(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Artist", wrapper.getArtist());
	}

	@Test
	public void getArtistReturnsV1TagsArtistIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals("V1 Artist", wrapper.getArtist());
	}

	@Test
	public void getArtistReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertNull(wrapper.getArtist());
	}
	//endregion

	//region setArtist
	@Test
	public void setsArtistOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setArtist("an artist");
		assertEquals("an artist", id3v1Tag.getArtist());
		assertEquals("an artist", id3v2Tag.getArtist());
	}

	@Test
	public void setsArtistOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setArtist("an artist");
		assertEquals("an artist", id3v1Tag.getArtist());
	}

	@Test
	public void setsArtistOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setArtist("an artist");
		assertEquals("an artist", id3v2Tag.getArtist());
	}

	@Test
	public void setArtistDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setArtist("an artist");
	}
	//endregion

	//region getTitle
	@Test
	public void getTitleReturnsV2TagsTitleBeforeV1TagsTitle() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTitle("V2 Title");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Title", wrapper.getTitle());
	}

	@Test
	public void getTitleReturnsV1TagsTitleIfV2TagsTitleIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTitle("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Title", wrapper.getTitle());
	}

	@Test
	public void getTitleReturnsV1TagsTitleIfV2TagsTitleDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTitle(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Title", wrapper.getTitle());
	}

	@Test
	public void getTitleReturnsV1TagsTitleIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals("V1 Title", wrapper.getTitle());
	}

	@Test
	public void getTitleReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertNull(wrapper.getTitle());
	}
	//endregion

	//region setTitle
	@Test
	public void setsTitleOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setTitle("a title");
		assertEquals("a title", id3v1Tag.getTitle());
		assertEquals("a title", id3v2Tag.getTitle());
	}

	@Test
	public void setsTitleOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setTitle("a title");
		assertEquals("a title", id3v1Tag.getTitle());
	}

	@Test
	public void setsTitleOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setTitle("a title");
		assertEquals("a title", id3v2Tag.getTitle());
	}

	@Test
	public void setTitleDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setTitle("a title");
	}
	//endregion

	//region getAlbum
	@Test
	public void getAlbumReturnsV2TagsAlbumBeforeV1TagsAlbum() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbum("V2 Album");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Album", wrapper.getAlbum());
	}

	@Test
	public void getAlbumReturnsV1TagsAlbumIfV2TagsAlbumIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbum("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Album", wrapper.getAlbum());
	}

	@Test
	public void getAlbumReturnsV1TagsAlbumIfV2TagsAlbumDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbum(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Album", wrapper.getAlbum());
	}

	@Test
	public void getAlbumReturnsV1TagsAlbumIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals("V1 Album", wrapper.getAlbum());
	}

	@Test
	public void getAlbumReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertNull(wrapper.getAlbum());
	}
	//endregion

	//region setAlbum
	@Test
	public void setsAlbumOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setAlbum("an album");
		assertEquals("an album", id3v1Tag.getAlbum());
		assertEquals("an album", id3v2Tag.getAlbum());
	}

	@Test
	public void setsAlbumOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setAlbum("an album");
		assertEquals("an album", id3v1Tag.getAlbum());
	}

	@Test
	public void setsAlbumOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setAlbum("an album");
		assertEquals("an album", id3v2Tag.getAlbum());
	}

	@Test
	public void setAlbumDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setAlbum("an album");
	}
	//endregion

	//region getYear
	@Test
	public void getYearReturnsV2TagsYearBeforeV1TagsYear() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setYear("V2 Year");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Year", wrapper.getYear());
	}

	@Test
	public void getYearReturnsV1TagsYearIfV2TagsYearIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setYear("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Year", wrapper.getYear());
	}

	@Test
	public void getYearReturnsV1TagsYearIfV2TagsYearDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setYear(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Year", wrapper.getYear());
	}

	@Test
	public void getYearReturnsV1TagsYearIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals("V1 Year", wrapper.getYear());
	}

	@Test
	public void getYearReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertNull(wrapper.getYear());
	}
	//endregion

	//region setYear
	@Test
	public void setsYearOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setYear("a year");
		assertEquals("a year", id3v1Tag.getYear());
		assertEquals("a year", id3v2Tag.getYear());
	}

	@Test
	public void setsYearOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setYear("a year");
		assertEquals("a year", id3v1Tag.getYear());
	}

	@Test
	public void setsYearOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setYear("a year");
		assertEquals("a year", id3v2Tag.getYear());
	}

	@Test
	public void setYearDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setYear("a year");
	}
	//endregion

	//region getGenre
	@Test
	public void getGenreReturnsV2TagsGenreBeforeV1TagsGenre() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenre(10);
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setGenre(20);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals(20, wrapper.getGenre());
	}

	@Test
	public void getGenreReturnsV1TagsGenreIfV2TagsGenreIsNegativeOne() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenre(10);
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setGenre(-1);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals(10, wrapper.getGenre());
	}

	@Test
	public void getGenreReturnsV1TagsGenreIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenre(10);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals(10, wrapper.getGenre());
	}

	@Test
	public void getGenreReturnsNegativeOneIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertEquals(-1, wrapper.getGenre());
	}
	//endregion

	//region setGenre
	@Test
	public void setsGenreOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setGenre(22);
		assertEquals(22, id3v1Tag.getGenre());
		assertEquals(22, id3v2Tag.getGenre());
	}

	@Test
	public void setsGenreOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setGenre(22);
		assertEquals(22, id3v1Tag.getGenre());
	}

	@Test
	public void setsGenreOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setGenre(22);
		assertEquals(22, id3v2Tag.getGenre());
	}

	@Test
	public void setGenreDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setGenre(22);
	}
	//endregion

	//region getGenreDescription
	@Test
	public void getGenreDescriptionReturnsV2TagsGenreDescriptionBeforeV1TagsGenreDescription() {
		ID3v1TagForTesting id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenreDescription("V1 GenreDescription");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setGenreDescription("V2 GenreDescription");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 GenreDescription", wrapper.getGenreDescription());
	}

	@Test
	public void getGenreDescriptionReturnsV1TagsGenreDescriptionIfV2TagDoesNotExist() {
		ID3v1TagForTesting id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenreDescription("V1 GenreDescription");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals("V1 GenreDescription", wrapper.getGenreDescription());
	}

	@Test
	public void getGenreDescriptionReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertNull(wrapper.getGenreDescription());
	}
	//endregion

	//region getComment
	@Test
	public void getCommentReturnsV2TagsCommentBeforeV1TagsComment() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComment("V2 Comment");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Comment", wrapper.getComment());
	}

	@Test
	public void getCommentReturnsV1TagsCommentIfV2TagsCommentIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComment("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Comment", wrapper.getComment());
	}

	@Test
	public void getCommentReturnsV1TagsCommentIfV2TagsCommentDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComment(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V1 Comment", wrapper.getComment());
	}

	@Test
	public void getCommentReturnsV1TagsCommentIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertEquals("V1 Comment", wrapper.getComment());
	}

	@Test
	public void getCommentReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertNull(wrapper.getComment());
	}
	//endregion

	//region setComment
	@Test
	public void setsCommentOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setComment("a comment");
		assertEquals("a comment", id3v1Tag.getComment());
		assertEquals("a comment", id3v2Tag.getComment());
	}

	@Test
	public void setsCommentOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setComment("a comment");
		assertEquals("a comment", id3v1Tag.getComment());
	}

	@Test
	public void setsCommentOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setComment("a comment");
		assertEquals("a comment", id3v2Tag.getComment());
	}

	@Test
	public void setCommentDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setComment("a comment");
	}
	//endregion

	//region getComposer
	@Test
	public void getComposerReturnsV2TagsComposer() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComposer("V2 Composer");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Composer", wrapper.getComposer());
	}

	@Test
	public void getComposerReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getComposer());
	}
	//endregion

	//region setComposer
	@Test
	public void setsComposerOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setComposer("a composer");
		assertEquals("a composer", id3v2Tag.getComposer());
	}

	@Test
	public void setComposerDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setComposer("a composer");
	}
	//endregion

	//region getOriginalArtist
	@Test
	public void getOriginalArtistReturnsV2TagsOriginalArtist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setOriginalArtist("V2 OriginalArtist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 OriginalArtist", wrapper.getOriginalArtist());
	}

	@Test
	public void getOriginalArtistReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getOriginalArtist());
	}
	//endregion

	//region setOriginalArtist
	@Test
	public void setsOriginalArtistOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setOriginalArtist("an original artist");
		assertEquals("an original artist", id3v2Tag.getOriginalArtist());
	}

	@Test
	public void setOriginalArtistDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setOriginalArtist("an original artist");
	}
	//endregion

	//region getAlbumArtist
	@Test
	public void getAlbumArtistReturnsV2TagsAlbumArtist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbumArtist("V2 AlbumArtist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 AlbumArtist", wrapper.getAlbumArtist());
	}

	@Test
	public void getAlbumArtistReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getAlbumArtist());
	}
	//endregion

	//region setAlbumArtist
	@Test
	public void setsAlbumArtistOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setAlbumArtist("an album artist");
		assertEquals("an album artist", id3v2Tag.getAlbumArtist());
	}

	@Test
	public void setAlbumArtistDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setAlbumArtist("an album artist");
	}
	//endregion

	//region getCopyright
	@Test
	public void getCopyrightReturnsV2TagsCopyright() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setCopyright("V2 Copyright");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Copyright", wrapper.getCopyright());
	}

	@Test
	public void getCopyrightReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getCopyright());
	}
	//endregion

	//region setCopyright
	@Test
	public void setsCopyrightOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setCopyright("a copyright");
		assertEquals("a copyright", id3v2Tag.getCopyright());
	}

	@Test
	public void setCopyrightDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setCopyright("a copyright");
	}
	//endregion

	//region getUrl
	@Test
	public void getUrlReturnsV2TagsUrl() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setUrl("V2 Url");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Url", wrapper.getUrl());
	}

	@Test
	public void getUrlReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getUrl());
	}
	//endregion

	//region setUrl
	@Test
	public void setsUrlOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setUrl("a url");
		assertEquals("a url", id3v2Tag.getUrl());
	}

	@Test
	public void setUrlDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setUrl("a url");
	}
	//endregion

	//region getEncoder
	@Test
	public void getEncoderReturnsV2TagsEncoder() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setEncoder("V2 Encoder");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Encoder", wrapper.getEncoder());
	}

	@Test
	public void getEncoderReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getEncoder());
	}
	//endregion

	//region setEncoder
	@Test
	public void setsEncoderOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setEncoder("an encoder");
		assertEquals("an encoder", id3v2Tag.getEncoder());
	}

	@Test
	public void setEncoderDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setEncoder("an encoder");
	}
	//endregion

	//region getAlbumImage and getAlbumImageMimeType
	@Test
	public void getAlbumImageReturnsV2TagsAlbumImage() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbumImage(new byte[]{12, 4, 7}, "mime type");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertArrayEquals(new byte[]{12, 4, 7}, wrapper.getAlbumImage());
		assertEquals("mime type", wrapper.getAlbumImageMimeType());
	}

	@Test
	public void getAlbumImageReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getAlbumImage());
		assertNull(wrapper.getAlbumImageMimeType());
	}
	//endregion

	//region setAlbumImage
	@Test
	public void setsAlbumImageOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setAlbumImage(new byte[]{12, 4, 7}, "mime type");
		assertArrayEquals(new byte[]{12, 4, 7}, id3v2Tag.getAlbumImage());
		assertEquals("mime type", id3v2Tag.getAlbumImageMimeType());
	}

	@Test
	public void setAlbumImageDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setAlbumImage(new byte[]{12, 4, 7}, "mime type");
	}
	//endregion

	//region getLyrics
	@Test
	public void getLyricsReturnsV2TagsLyrics() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setLyrics("V2 Lyrics");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertEquals("V2 Lyrics", wrapper.getLyrics());
	}

	@Test
	public void getLyricsReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertNull(wrapper.getLyrics());
	}
	//endregion

	//region setLyrics
	@Test
	public void setsLyricsOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setLyrics("lyrics");
		assertEquals("lyrics", id3v2Tag.getLyrics());
	}

	@Test
	public void setLyricsDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setLyrics("lyrics");
	}
	//endregion

	//region clearComment
	@Test
	public void clearsCommentOnV1Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("a comment");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.clearComment();
		assertNull(id3v1Tag.getComment());
	}

	@Test
	public void clearsCommentFrameOnV2Tag() {
		ID3v2TagForTesting id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.addFrameSet(AbstractID3v2Tag.ID_COMMENT, new ID3v2FrameSet(AbstractID3v2Tag.ID_COMMENT));
		assertTrue(id3v2Tag.getFrameSets().containsKey(AbstractID3v2Tag.ID_COMMENT));
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.clearComment();
		assertFalse(id3v2Tag.getFrameSets().containsKey(AbstractID3v2Tag.ID_COMMENT));
	}
	//endregion

	//region clearCopyright
	@Test
	public void clearsCopyrightFrameOnV2Tag() {
		ID3v2TagForTesting id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.addFrameSet(AbstractID3v2Tag.ID_COPYRIGHT, new ID3v2FrameSet(AbstractID3v2Tag.ID_COPYRIGHT));
		assertTrue(id3v2Tag.getFrameSets().containsKey(AbstractID3v2Tag.ID_COPYRIGHT));
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.clearCopyright();
		assertFalse(id3v2Tag.getFrameSets().containsKey(AbstractID3v2Tag.ID_COPYRIGHT));
	}

	@Test
	public void clearCopyrightDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.clearCopyright();
	}
	//endregion

	//region clearCopyright
	@Test
	public void clearsEncoderFrameOnV2Tag() {
		ID3v2TagForTesting id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.addFrameSet(AbstractID3v2Tag.ID_ENCODER, new ID3v2FrameSet(AbstractID3v2Tag.ID_ENCODER));
		assertTrue(id3v2Tag.getFrameSets().containsKey(AbstractID3v2Tag.ID_ENCODER));
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.clearEncoder();
		assertFalse(id3v2Tag.getFrameSets().containsKey(AbstractID3v2Tag.ID_ENCODER));
	}

	@Test
	public void clearEncoderDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.clearEncoder();
	}
	//endregion

	//region ID3v1TagForTesting class
	private static class ID3v1TagForTesting implements ID3v1 {
		private String track;
		private String artist;
		private String title;
		private String album;
		private String year;
		private int genre;
		protected String genreDescription;
		private String comment;

		@Override
		public String getVersion() {
			return null;
		}

		@Override
		public String getTrack() {
			return track;
		}

		@Override
		public void setTrack(String track) {
			this.track = track;
		}

		@Override
		public String getArtist() {
			return artist;
		}

		@Override
		public void setArtist(String artist) {
			this.artist = artist;
		}

		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public String getAlbum() {
			return album;
		}

		@Override
		public void setAlbum(String album) {
			this.album = album;
		}

		@Override
		public String getYear() {
			return year;
		}

		@Override
		public void setYear(String year) {
			this.year = year;
		}

		@Override
		public int getGenre() {
			return genre;
		}

		@Override
		public void setGenre(int genre) {
			this.genre = genre;
		}

		@Override
		public String getGenreDescription() {
			return genreDescription;
		}

		public void setGenreDescription(String genreDescription) {
			this.genreDescription = genreDescription;
		}

		@Override
		public String getComment() {
			return comment;
		}

		@Override
		public void setComment(String comment) {
			this.comment = comment;
		}

		@Override
		public byte[] toBytes() throws NotSupportedException {
			return new byte[0];
		}
	}
	//endregion

	//region ID3v2TagForTesting class
	private static class ID3v2TagForTesting extends ID3v1TagForTesting implements ID3v2 {
		private String composer;
		private String originalArtist;
		private String albumArtist;
		private String copyright;
		private String url;
		private String encoder;
		private byte[] albumImage;
		private String albumImageMimeType;
		private String lyrics;
		private Map<String, ID3v2FrameSet> frameSets = new HashMap<>();

		@Override
		public boolean getPadding() {
			return false;
		}

		@Override
		public void setPadding(boolean padding) {

		}

		@Override
		public boolean hasFooter() {
			return false;
		}

		@Override
		public void setFooter(boolean footer) {

		}

		@Override
		public boolean hasUnsynchronisation() {
			return false;
		}

		@Override
		public void setUnsynchronisation(boolean unsynchronisation) {

		}

		@Override
		public int getBPM() {
			return 0;
		}

		@Override
		public void setBPM(int bpm) {

		}

		@Override
		public String getGrouping() {
			return null;
		}

		@Override
		public void setGrouping(String grouping) {

		}

		@Override
		public String getKey() {
			return null;
		}

		@Override
		public void setKey(String key) {

		}

		@Override
		public String getDate() {
			return null;
		}

		@Override
		public void setDate(String date) {

		}

		@Override
		public String getComposer() {
			return composer;
		}

		@Override
		public void setComposer(String composer) {
			this.composer = composer;
		}

		@Override
		public String getPublisher() {
			return null;
		}

		@Override
		public void setPublisher(String publisher) {

		}

		@Override
		public String getOriginalArtist() {
			return originalArtist;
		}

		@Override
		public void setOriginalArtist(String originalArtist) {
			this.originalArtist = originalArtist;
		}

		@Override
		public String getAlbumArtist() {
			return albumArtist;
		}

		@Override
		public void setAlbumArtist(String albumArtist) {
			this.albumArtist = albumArtist;
		}

		@Override
		public String getCopyright() {
			return copyright;
		}

		@Override
		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}

		@Override
		public String getArtistUrl() {
			return null;
		}

		@Override
		public void setArtistUrl(String url) {

		}

		@Override
		public String getCommercialUrl() {
			return null;
		}

		@Override
		public void setCommercialUrl(String url) {

		}

		@Override
		public String getCopyrightUrl() {
			return null;
		}

		@Override
		public void setCopyrightUrl(String url) {

		}

		@Override
		public String getAudiofileUrl() {
			return null;
		}

		@Override
		public void setAudiofileUrl(String url) {

		}

		@Override
		public String getAudioSourceUrl() {
			return null;
		}

		@Override
		public void setAudioSourceUrl(String url) {

		}

		@Override
		public String getRadiostationUrl() {
			return null;
		}

		@Override
		public void setRadiostationUrl(String url) {

		}

		@Override
		public String getPaymentUrl() {
			return null;
		}

		@Override
		public void setPaymentUrl(String url) {

		}

		@Override
		public String getPublisherUrl() {
			return null;
		}

		@Override
		public void setPublisherUrl(String url) {

		}

		@Override
		public String getUrl() {
			return url;
		}

		@Override
		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String getPartOfSet() {
			return null;
		}

		@Override
		public void setPartOfSet(String partOfSet) {

		}

		@Override
		public boolean isCompilation() {
			return false;
		}

		@Override
		public void setCompilation(boolean compilation) {

		}

		@Override
		public ArrayList<ID3v2ChapterFrameData> getChapters() {
			return null;
		}

		@Override
		public void setChapters(ArrayList<ID3v2ChapterFrameData> chapters) {

		}

		@Override
		public ArrayList<ID3v2ChapterTOCFrameData> getChapterTOC() {
			return null;
		}

		@Override
		public void setChapterTOC(ArrayList<ID3v2ChapterTOCFrameData> ctoc) {

		}

		@Override
		public String getEncoder() {
			return encoder;
		}

		@Override
		public void setEncoder(String encoder) {
			this.encoder = encoder;
		}

		@Override
		public byte[] getAlbumImage() {
			return albumImage;
		}

		@Override
		public void setAlbumImage(byte[] albumImage, String mimeType) {
			this.albumImage = albumImage;
			this.albumImageMimeType = mimeType;
		}

		@Override
		public void setAlbumImage(byte[] albumImage, String mimeType, byte imageType, String imageDescription) {

		}

		@Override
		public void clearAlbumImage() {

		}

		@Override
		public String getAlbumImageMimeType() {
			return albumImageMimeType;
		}

		@Override
		public int getWmpRating() {
			return 0;
		}

		@Override
		public void setWmpRating(int rating) {

		}

		@Override
		public String getItunesComment() {
			return null;
		}

		@Override
		public void setItunesComment(String itunesComment) {

		}

		@Override
		public String getLyrics() {
			return lyrics;
		}

		@Override
		public void setLyrics(String lyrics) {
			this.lyrics = lyrics;
		}

		@Override
		public void setGenreDescription(String text) {
			this.genreDescription = text;
		}

		@Override
		public int getDataLength() {
			return 0;
		}

		@Override
		public int getLength() {
			return 0;
		}

		@Override
		public boolean getObseleteFormat() {
			return false;
		}

		@Override
		public Map<String, ID3v2FrameSet> getFrameSets() {
			return frameSets;
		}

		public void addFrameSet(String id, ID3v2FrameSet frameSet) {
			frameSets.put(id, frameSet);
		}

		@Override
		public void clearFrameSet(String id) {
			frameSets.remove(id);
		}
	}
	//endregion
}
