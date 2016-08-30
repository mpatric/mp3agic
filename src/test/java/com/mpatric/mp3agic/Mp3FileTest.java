package com.mpatric.mp3agic;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class Mp3FileTest {

    private static final String fs = File.separator;

    private static final String MP3_WITH_NO_TAGS = "src" + fs + "test" + fs + "resources" + fs + "notags.mp3";

    private static final String MP3_WITH_ID3V1_AND_ID3V23_TAGS = "src" + fs + "test" + fs + "resources" + fs + "v1andv23tags.mp3";

    private static final String MP3_WITH_DUMMY_START_AND_END_FRAMES = "src" + fs + "test" + fs + "resources" + fs + "dummyframes.mp3";

    private static final String MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS = "src" + fs + "test" + fs + "resources" + fs + "v1andv23andcustomtags.mp3";

    private static final String MP3_WITH_ID3V23_UNICODE_TAGS = "src" + fs + "test" + fs + "resources" + fs + "v23unicodetags.mp3";

    private static final String NOT_AN_MP3 = "src" + fs + "test" + fs + "resources" + fs + "notanmp3.mp3";

    private static final String MP3_WITH_INCOMPLETE_MPEG_FRAME = "src" + fs + "test" + fs + "resources" + fs + "incompletempegframe.mp3";

    private static final String MP3_TEMP = "target/junit.mp3";

    @Before
    public void initialize() throws IOException {
        // remove previously created test file
        Files.deleteIfExists(Paths.get(MP3_TEMP));
    }

    @Test
    public void shouldLoadMp3WithNoTags() throws IOException, UnsupportedTagException, InvalidDataException {
        loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 41);
        loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 256);
        loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 1024);
        loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 5000);
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_NO_TAGS));
        loadAndCheckTestMp3WithNoTags(mediaContent, 41);
        loadAndCheckTestMp3WithNoTags(mediaContent, 256);
        loadAndCheckTestMp3WithNoTags(mediaContent, 1024);
        loadAndCheckTestMp3WithNoTags(mediaContent, 5000);
    }

    @Test
    public void shouldLoadMp3WithId3Tags() throws IOException, UnsupportedTagException, InvalidDataException {
        loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 41);
        loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 256);
        loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 1024);
        loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 5000);
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_TAGS));
        loadAndCheckTestMp3WithTags(mediaContent, 41);
        loadAndCheckTestMp3WithTags(mediaContent, 256);
        loadAndCheckTestMp3WithTags(mediaContent, 1024);
        loadAndCheckTestMp3WithTags(mediaContent, 5000);
    }

    @Test
    public void shouldLoadMp3WithFakeStartAndEndFrames() throws IOException, UnsupportedTagException, InvalidDataException {
        loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 41);
        loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 256);
        loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 1024);
        loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 5000);
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_DUMMY_START_AND_END_FRAMES));
        loadAndCheckTestMp3WithTags(mediaContent, 41);
        loadAndCheckTestMp3WithTags(mediaContent, 256);
        loadAndCheckTestMp3WithTags(mediaContent, 1024);
        loadAndCheckTestMp3WithTags(mediaContent, 5000);
    }

    @Test
    public void shouldLoadMp3WithCustomTag() throws IOException, UnsupportedTagException, InvalidDataException {
        loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
        loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
        loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
        loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
        loadAndCheckTestMp3WithCustomTag(mediaContent, 41);
        loadAndCheckTestMp3WithCustomTag(mediaContent, 256);
        loadAndCheckTestMp3WithCustomTag(mediaContent, 1024);
        loadAndCheckTestMp3WithCustomTag(mediaContent, 5000);
    }

    @Test
    public void shouldThrowExceptionForFileThatIsNotAnMp3() throws Exception {
        try {
            new Mp3File(NOT_AN_MP3);
            fail("InvalidDataException expected but not thrown");
        } catch (InvalidDataException e) {
            assertEquals("No mpegs frames found", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionForFileThatIsNotAnMp3ForMediaContent() throws Exception {
        try {
            byte[] mediaContent = Files.readAllBytes(Paths.get(NOT_AN_MP3));
            new Mp3File(mediaContent);
            fail("InvalidDataException expected but not thrown");
        } catch (InvalidDataException e) {
            assertEquals("No mpegs frames found", e.getMessage());
        }
    }

    @Test
    public void shouldFindProbableStartOfMpegFramesWithPrescan() throws IOException {
        Mp3FileForTesting mp3File = new Mp3FileForTesting(MP3_WITH_ID3V1_AND_ID3V23_TAGS);
        testShouldFindProbableStartOfMpegFramesWithPrescan(mp3File);
    }

    @Test
    public void shouldFindProbableStartOfMpegFramesWithPrescanForMediaContent() throws IOException {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_TAGS));
        Mp3FileForTesting mp3File = new Mp3FileForTesting(mediaContent);
        testShouldFindProbableStartOfMpegFramesWithPrescan(mp3File);
    }

    private void testShouldFindProbableStartOfMpegFramesWithPrescan(Mp3FileForTesting mp3File) {
        assertEquals(0x44B, mp3File.preScanResult);
    }

    @Test
    public void shouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile() throws Exception {
        Mp3File mp3File = new Mp3File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
        testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(mp3File);
    }

    @Test
    public void shouldThrowExceptionIfSavingMp3WithSameNameAsSourceFileForMediaContent() throws Exception {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
        Mp3File mp3File = new Mp3File(mediaContent);
        mp3File.setSourceFileName(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
        // we dont want the arg exception when using a non-file based source
        //testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(mp3File);
    }

    private void testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(Mp3File mp3File) throws NotSupportedException, IOException {
        System.out.println(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
        try {
            mp3File.save(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
            fail("IllegalArgumentException expected but not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Save filename same as source filename", e.getMessage());
        }
    }

    @Test
    public void shouldSaveLoadedMp3WhichIsEquivalentToOriginal() throws Exception {
        copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
        copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
        copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
        copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
        Mp3File mp3File = new Mp3File(mediaContent);
        mp3File.setSourceFileName(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
        copyAndCheckTestMp3WithCustomTag(mediaContent, 41);
        copyAndCheckTestMp3WithCustomTag(mediaContent, 256);
        copyAndCheckTestMp3WithCustomTag(mediaContent, 1024);
        copyAndCheckTestMp3WithCustomTag(mediaContent, 5000);
    }

    @Test
    public void shouldLoadAndCheckMp3ContainingUnicodeFields() throws Exception {
        loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 41);
        loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 256);
        loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 1024);
        loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 5000);
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V23_UNICODE_TAGS));
        loadAndCheckTestMp3WithUnicodeFields(mediaContent, 41);
        loadAndCheckTestMp3WithUnicodeFields(mediaContent, 256);
        loadAndCheckTestMp3WithUnicodeFields(mediaContent, 1024);
        loadAndCheckTestMp3WithUnicodeFields(mediaContent, 5000);
    }

    @Test
    public void shouldSaveLoadedMp3WithUnicodeFieldsWhichIsEquivalentToOriginal() throws Exception {
        copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 41);
        copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 256);
        copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 1024);
        copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 5000);
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V23_UNICODE_TAGS));
        copyAndCheckTestMp3WithUnicodeFields(mediaContent, 41);
        copyAndCheckTestMp3WithUnicodeFields(mediaContent, 256);
        copyAndCheckTestMp3WithUnicodeFields(mediaContent, 1024);
        copyAndCheckTestMp3WithUnicodeFields(mediaContent, 5000);
    }

    @Test
    public void shouldIgnoreIncompleteMpegFrame() throws Exception {
        Mp3File mp3File = new Mp3File(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256);
        testShouldIgnoreIncompleteMpegFrame(mp3File);
    }

    @Test
    public void shouldIgnoreIncompleteMpegFrameForMediaContent() throws Exception {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_INCOMPLETE_MPEG_FRAME));
        Mp3File mp3File = new Mp3File(mediaContent, 256);
        testShouldIgnoreIncompleteMpegFrame(mp3File);
    }

    private void testShouldIgnoreIncompleteMpegFrame(Mp3File mp3File) throws Exception {
        assertEquals(0x44B, mp3File.getXingOffset());
        assertEquals(0x5EC, mp3File.getStartOffset());
        assertEquals(0xF17, mp3File.getEndOffset());
        assertTrue(mp3File.hasId3v1Tag());
        assertTrue(mp3File.hasId3v2Tag());
        assertEquals(5, mp3File.getFrameCount());
    }

    @Test
    public void shouldInitialiseProperlyWhenNotScanningFile() throws Exception {
        Mp3File mp3File = new Mp3File(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256, false);
        testShouldInitialiseProperlyWhenNotScanningFile(mp3File);
    }

    @Test
    public void shouldInitialiseProperlyWhenNotScanningMediaContent() throws Exception {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_INCOMPLETE_MPEG_FRAME));
        Mp3File mp3File = new Mp3File(mediaContent, 256, false);
        testShouldInitialiseProperlyWhenNotScanningFile(mp3File);
    }

    private void testShouldInitialiseProperlyWhenNotScanningFile(Mp3File mp3File) throws Exception {
        assertTrue(mp3File.hasId3v1Tag());
        assertTrue(mp3File.hasId3v2Tag());
    }

    @Test
    public void shouldRemoveId3v1Tag() throws Exception {
        String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
        testShouldRemoveId3v1Tag(new Mp3File(filename));
    }

    @Test
    public void shouldRemoveId3v1TagForMediaContent() throws Exception {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
        Mp3File mp3File = new Mp3File(mediaContent);
        mp3File.setSourceFileName(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
        testShouldRemoveId3v1Tag(mp3File);
    }

    private void testShouldRemoveId3v1Tag(Mp3File mp3File) throws Exception {
        String saveFilename = "unittest.copy";
        try {
            mp3File.removeId3v1Tag();
            mp3File.save(saveFilename);
            Mp3File newMp3File = new Mp3File(saveFilename);
            assertFalse(newMp3File.hasId3v1Tag());
            assertTrue(newMp3File.hasId3v2Tag());
            assertTrue(newMp3File.hasCustomTag());
        } finally {
            TestHelper.deleteFile(saveFilename);
        }
    }

    @Test
    public void shouldRemoveId3v2Tag() throws Exception {
        String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
        testShouldRemoveId3v2Tag(new Mp3File(filename));
    }

    @Test
    public void shouldRemoveId3v2TagForMediaContent() throws Exception {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
        Mp3File mp3File = new Mp3File(mediaContent);
        mp3File.setSourceFileName(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
        testShouldRemoveId3v2Tag(mp3File);
    }

    private void testShouldRemoveId3v2Tag(Mp3File mp3File) throws Exception {
        String saveFilename = "unittest.copy";
        try {
            mp3File.removeId3v2Tag();
            mp3File.save(saveFilename);
            Mp3File newMp3File = new Mp3File(saveFilename);
            assertTrue(newMp3File.hasId3v1Tag());
            assertFalse(newMp3File.hasId3v2Tag());
            assertTrue(newMp3File.hasCustomTag());
        } finally {
            TestHelper.deleteFile(saveFilename);
        }
    }

    @Test
    public void shouldRemoveCustomTag() throws Exception {
        String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
        testShouldRemoveCustomTag(new Mp3File(filename));
    }

    @Test
    public void shouldRemoveCustomTagForMediaContent() throws Exception {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
        testShouldRemoveCustomTag(new Mp3File(mediaContent));
    }

    private void testShouldRemoveCustomTag(Mp3File mp3File) throws Exception {
        String saveFilename = "unittest.copy";
        try {
            mp3File.removeCustomTag();
            mp3File.save(saveFilename);
            Mp3File newMp3File = new Mp3File(saveFilename);
            assertTrue(newMp3File.hasId3v1Tag());
            assertTrue(newMp3File.hasId3v2Tag());
            assertFalse(newMp3File.hasCustomTag());
        } finally {
            TestHelper.deleteFile(saveFilename);
        }
    }

    @Test
    public void shouldRemoveId3v1AndId3v2AndCustomTags() throws Exception {
        String filename = MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS;
        testShouldRemoveId3v1AndId3v2AndCustomTags(new Mp3File(filename));
    }

    @Test
    public void shouldRemoveId3v1AndId3v2AndCustomTagsForMediaContent() throws Exception {
        byte[] mediaContent = Files.readAllBytes(Paths.get(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
        testShouldRemoveId3v1AndId3v2AndCustomTags(new Mp3File(mediaContent));
    }

    //@Test
    public void shouldReadId3v1IfNoId3v2Present() throws Exception {
        // create id3 tag
        final ID3v1 id3v1 = new ID3v1Tag();
        id3v1.setAlbum("v1 album");
        id3v1.setArtist("v1 artist");
        id3v1.setComment("v1 comment");
        id3v1.setGenre(1);
        id3v1.setTitle("v1 title");
        id3v1.setTrack("v1 track");
        id3v1.setYear("v1 year");
        // new mp3
        Mp3File mp3File = new Mp3File();
        mp3File.setId3v1Tag(id3v1);
        mp3File.setId3v2Tag(null);
        // save the mp3
        mp3File.save(MP3_TEMP);
        mp3File = null;
        // open the new mp3
        mp3File = new Mp3File(MP3_TEMP);
        // get the tag
        ID3v1 id3 = mp3File.getId3v1Tag();

        assertEquals(id3v1.getAlbum(), id3.getAlbum());
        assertEquals(id3v1.getArtist(), id3.getArtist());
        assertEquals(id3v1.getComment(), id3.getComment());
        assertEquals(id3v1.getGenre(), id3.getGenre());
        assertEquals(id3v1.getGenreDescription(), id3.getGenreDescription());
        assertEquals(id3v1.getTitle(), id3.getTitle());
        assertEquals(id3v1.getTrack(), id3.getTrack());
        assertEquals(id3v1.getYear(), id3.getYear());
    }

    //@Test
    public void shouldReadId3v2IfPresent() throws Exception {
        // create id3 v1 tag
        final ID3v1 id3v1 = new ID3v1Tag();
        id3v1.setAlbum("v1 album");
        id3v1.setArtist("v1 artist");
        id3v1.setComment("v1 comment");
        id3v1.setGenre(1);
        id3v1.setTitle("v1 title");
        id3v1.setTrack("v1 track");
        id3v1.setYear("v1 year");
        // create id3 v2 tag
        final ID3v2 id3v2 = new ID3v22Tag();
        id3v2.setAlbum("v2 album");
        id3v2.setArtist("v2 artist");
        id3v2.setComment("v2 comment");
        id3v2.setGenre(2);
        id3v2.setTitle("v2 title");
        id3v2.setTrack("v2 track");
        id3v2.setYear("v2 year");
        // new mp3
        Mp3File mp3File = new Mp3File();
        mp3File.setId3v1Tag(id3v1);
        mp3File.setId3v2Tag(id3v2);
        // save the mp3
        mp3File.save(MP3_TEMP);
        mp3File = null;
        // open the new mp3
        mp3File = new Mp3File(MP3_TEMP);
        assertTrue(mp3File.hasId3v2Tag());
        // get the tag
        ID3v2 id3 = (ID3v2) mp3File.getId3Tag();
        assertEquals(id3v2.getAlbum(), id3.getAlbum());
        assertEquals(id3v2.getArtist(), id3.getArtist());
        assertEquals(id3v2.getComment(), id3.getComment());
        assertEquals(id3v2.getGenre(), id3.getGenre());
        assertEquals(id3v2.getGenreDescription(), id3.getGenreDescription());
        assertEquals(id3v2.getTitle(), id3.getTitle());
        assertEquals(id3v2.getTrack(), id3.getTrack());
        assertEquals(id3v2.getYear(), id3.getYear());
    }

    @Test
    public void shouldReturnEmptyValuesIfNoTagsPresent() throws Exception {
        final Mp3File mp3File = new Mp3File();
        mp3File.setId3v1Tag(null);
        mp3File.setId3v2Tag(null);
        assertNull(mp3File.getId3v1Tag());
        assertNull(mp3File.getId3v2Tag());
    }

    private void testShouldRemoveId3v1AndId3v2AndCustomTags(Mp3File mp3File) throws Exception {
        String saveFilename = "unittest.copy";
        try {
            mp3File.removeId3v1Tag();
            mp3File.removeId3v2Tag();
            mp3File.removeCustomTag();
            mp3File.save(saveFilename);
            Mp3File newMp3File = new Mp3File(saveFilename);
            assertFalse(newMp3File.hasId3v1Tag());
            assertFalse(newMp3File.hasId3v2Tag());
            assertFalse(newMp3File.hasCustomTag());
        } finally {
            TestHelper.deleteFile(saveFilename);
        }
    }

    private Mp3File copyAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
        Mp3File mp3File = loadAndCheckTestMp3WithCustomTag(filename, bufferLength);
        return copyAndCheckTestMp3WithCustomTag(mp3File);
    }

    private Mp3File copyAndCheckTestMp3WithCustomTag(byte[] mediaContent, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
        Mp3File mp3File = loadAndCheckTestMp3WithCustomTag(mediaContent, bufferLength);
        return copyAndCheckTestMp3WithCustomTag(mp3File);
    }

    private Mp3File copyAndCheckTestMp3WithCustomTag(Mp3File mp3File) throws NotSupportedException, IOException, UnsupportedTagException, InvalidDataException {
        String saveFilename = "unittest.copy";
        try {
            mp3File.save(saveFilename);
            Mp3File copyMp3file = loadAndCheckTestMp3WithCustomTag(saveFilename, 5000);
            assertEquals(mp3File.getId3v1Tag(), copyMp3file.getId3v1Tag());
            assertEquals(mp3File.getId3v2Tag(), copyMp3file.getId3v2Tag());
            assertArrayEquals(mp3File.getCustomTag(), copyMp3file.getCustomTag());
            return copyMp3file;
        } finally {
            TestHelper.deleteFile(saveFilename);
        }
    }

    private Mp3File copyAndCheckTestMp3WithUnicodeFields(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
        Mp3File mp3File = loadAndCheckTestMp3WithUnicodeFields(filename, bufferLength);
        return copyAndCheckTestMp3WithUnicodeFields(mp3File);
    }

    private Mp3File copyAndCheckTestMp3WithUnicodeFields(byte[] mediaContent, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
        Mp3File mp3File = loadAndCheckTestMp3WithUnicodeFields(mediaContent, bufferLength);
        return copyAndCheckTestMp3WithUnicodeFields(mp3File);
    }

    private Mp3File copyAndCheckTestMp3WithUnicodeFields(Mp3File mp3File) throws NotSupportedException, IOException, UnsupportedTagException, InvalidDataException {
        String saveFilename = "unittest.copy";
        try {
            mp3File.save(saveFilename);
            Mp3File copyMp3file = loadAndCheckTestMp3WithUnicodeFields(saveFilename, 5000);
            assertEquals(mp3File.getId3v2Tag(), copyMp3file.getId3v2Tag());
            return copyMp3file;
        } finally {
            TestHelper.deleteFile(saveFilename);
        }
    }

    private Mp3File loadAndCheckTestMp3WithNoTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
        return loadAndCheckTestMp3WithNoTags(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithNoTags(byte[] mediaContent, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(mediaContent, bufferLength);
        return loadAndCheckTestMp3WithNoTags(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithNoTags(Mp3File mp3File) {
        assertEquals(0x000, mp3File.getXingOffset());
        assertEquals(0x1A1, mp3File.getStartOffset());
        assertEquals(0xB34, mp3File.getEndOffset());
        assertFalse(mp3File.hasId3v1Tag());
        assertFalse(mp3File.hasId3v2Tag());
        assertFalse(mp3File.hasCustomTag());
        return mp3File;
    }

    private Mp3File loadAndCheckTestMp3WithTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
        return loadAndCheckTestMp3WithTags(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithTags(byte[] mediaContent, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(mediaContent, bufferLength);
        return loadAndCheckTestMp3WithTags(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithTags(Mp3File mp3File) {
        assertEquals(0x44B, mp3File.getXingOffset());
        assertEquals(0x5EC, mp3File.getStartOffset());
        assertEquals(0xF7F, mp3File.getEndOffset());
        assertTrue(mp3File.hasId3v1Tag());
        assertTrue(mp3File.hasId3v2Tag());
        assertFalse(mp3File.hasCustomTag());
        return mp3File;
    }

    private Mp3File loadAndCheckTestMp3WithUnicodeFields(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
        return loadAndCheckTestMp3WithUnicodeFields(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithUnicodeFields(byte[] mediaContent, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(mediaContent, bufferLength);
        return loadAndCheckTestMp3WithUnicodeFields(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithUnicodeFields(Mp3File mp3File) {
        assertEquals(0x0CA, mp3File.getXingOffset());
        assertEquals(0x26B, mp3File.getStartOffset());
        assertEquals(0xBFE, mp3File.getEndOffset());
        assertFalse(mp3File.hasId3v1Tag());
        assertTrue(mp3File.hasId3v2Tag());
        assertFalse(mp3File.hasCustomTag());
        return mp3File;
    }

    private Mp3File loadAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
        return loadAndCheckTestMp3WithCustomTag(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithCustomTag(byte[] mediaContent, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = loadAndCheckTestMp3(mediaContent, bufferLength);
        return loadAndCheckTestMp3WithCustomTag(mp3File);
    }

    private Mp3File loadAndCheckTestMp3WithCustomTag(Mp3File mp3File) {
        assertEquals(0x44B, mp3File.getXingOffset());
        assertEquals(0x5EC, mp3File.getStartOffset());
        assertEquals(0xF7F, mp3File.getEndOffset());
        assertTrue(mp3File.hasId3v1Tag());
        assertTrue(mp3File.hasId3v2Tag());
        assertTrue(mp3File.hasCustomTag());
        return mp3File;
    }

    private Mp3File loadAndCheckTestMp3(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = new Mp3File(filename, bufferLength);
        return loadAndCheckTestMp3(mp3File);
    }

    private Mp3File loadAndCheckTestMp3(byte[] mediaContent, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
        Mp3File mp3File = new Mp3File(mediaContent, bufferLength);
        return loadAndCheckTestMp3(mp3File);
    }

    private Mp3File loadAndCheckTestMp3(Mp3File mp3File) {
        assertTrue(mp3File.hasXingFrame());
        assertEquals(6, mp3File.getFrameCount());
        assertEquals(MpegFrame.MPEG_VERSION_1_0, mp3File.getVersion());
        assertEquals(MpegFrame.MPEG_LAYER_3, mp3File.getLayer());
        assertEquals(44100, mp3File.getSampleRate());
        assertEquals(MpegFrame.CHANNEL_MODE_JOINT_STEREO, mp3File.getChannelMode());
        assertEquals(MpegFrame.EMPHASIS_NONE, mp3File.getEmphasis());
        assertTrue(mp3File.isOriginal());
        assertFalse(mp3File.isCopyright());
        assertEquals(128, mp3File.getXingBitrate());
        assertEquals(125, mp3File.getBitrate());
        assertEquals(1, (mp3File.getBitrates().get(224)).getValue());
        assertEquals(1, (mp3File.getBitrates().get(112)).getValue());
        assertEquals(2, (mp3File.getBitrates().get(96)).getValue());
        assertEquals(1, (mp3File.getBitrates().get(192)).getValue());
        assertEquals(1, (mp3File.getBitrates().get(32)).getValue());
        assertEquals(156, mp3File.getLengthInMilliseconds());
        return mp3File;
    }

    private class Mp3FileForTesting extends Mp3File {

        int preScanResult;

        public Mp3FileForTesting(String filename) throws IOException {
            RandomAccessMediaSource mediaSource = new RandomAccessMediaFile(filename, "r");
            preScanResult = preScanFile(mediaSource);
        }

        public Mp3FileForTesting(byte[] mediaContent) throws IOException {
            RandomAccessMediaSource mediaSource = new RandomAccessMediaByteArray(mediaContent);
            preScanResult = preScanFile(mediaSource);
        }

    }
}
