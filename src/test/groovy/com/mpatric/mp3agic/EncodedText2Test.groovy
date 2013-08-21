package com.mpatric.mp3agic

import spock.lang.Specification
import spock.lang.Unroll

import static com.mpatric.mp3agic.EncodedText.CHARSET_UTF_8
import static com.mpatric.mp3agic.EncodedText.CHARSET_UTF_16
import static com.mpatric.mp3agic.EncodedText.CHARSET_UTF_16BE

import static com.mpatric.mp3agic.EncodedText.TEXT_ENCODING_ISO_8859_1
import static com.mpatric.mp3agic.EncodedText.TEXT_ENCODING_UTF_8
import static com.mpatric.mp3agic.EncodedText.TEXT_ENCODING_UTF_16
import static com.mpatric.mp3agic.EncodedText.TEXT_ENCODING_UTF_16BE

import java.nio.charset.CharacterCodingException

public class EncodedText2Test extends Specification {

    private static final TEST_STRING = 'This is a string!'
    // γειά σου
    private static final UNICODE_TEST_STRING = '\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5'
    // "This is a String!"
    private static final TEST_STRING_HEX_ISO8859_1 = '54 68 69 73 20 69 73 20 61 20 73 74 72 69 6e 67 21'
    // "γειά σου" (This can't be encoded with ISO-8859-1)
    private static final UNICODE_TEST_STRING_HEX_UTF8 = 'ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85'
    private static final UNICODE_TEST_STRING_HEX_UTF16LE = 'b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03'
    private static final UNICODE_TEST_STRING_HEX_UTF16BE = '03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5'

    private static final byte[] BUFFER_WITH_A_BACKTICK = [0x49, 0x60, 0x6D]

    @Unroll
    def "constructor works for String input with default encoding for string '#inputString"() {
        given:
        def encodedText = new EncodedText(inputString)
        when:
        def string = encodedText.toString()
        then:
        string == inputString
        where:
        inputString << [TEST_STRING, UNICODE_TEST_STRING]
    }

    @Unroll
    def "constructor works for String input '#inputString' with encoding '#encoding"() {
        when:
        def encodedText = new EncodedText(inputString)
        and:
        def string = encodedText.toString()
        then:
        string == inputString
        where:
        inputString         | encoding
        TEST_STRING         | TEXT_ENCODING_ISO_8859_1
        TEST_STRING         | TEXT_ENCODING_UTF_8
        TEST_STRING         | TEXT_ENCODING_UTF_16
        TEST_STRING         | TEXT_ENCODING_UTF_16BE
        UNICODE_TEST_STRING | TEXT_ENCODING_ISO_8859_1
        UNICODE_TEST_STRING | TEXT_ENCODING_UTF_8
        UNICODE_TEST_STRING | TEXT_ENCODING_UTF_16
        UNICODE_TEST_STRING | TEXT_ENCODING_UTF_16BE
    }

    @Unroll
    def "constructor works with byteArray input: '#inputArray and encoding '#encoding'"() {
        when:
        def encodedText = new EncodedText(encoding, inputArray)
        then:
        encodedText.toString() == expectedString
        where:
        encoding                 | inputArray                                                   | expectedString
        TEXT_ENCODING_ISO_8859_1 | TestHelper.hexStringToBytes(TEST_STRING_HEX_ISO8859_1)       | 'This is a string!'
        TEXT_ENCODING_UTF_8      | TestHelper.hexStringToBytes(UNICODE_TEST_STRING_HEX_UTF8)    | 'γειά σου'
        TEXT_ENCODING_UTF_16     | TestHelper.hexStringToBytes(UNICODE_TEST_STRING_HEX_UTF16LE) | 'γειά σου'
        TEXT_ENCODING_UTF_16BE   | TestHelper.hexStringToBytes(UNICODE_TEST_STRING_HEX_UTF16BE) | 'γειά σου'
    }

    @Unroll
    def "constructor works equally for String and byte array input with different encodings set"() {
        when:
        def encodedText = new EncodedText(encoding, inputString)
        def encodedText2 = new EncodedText(encoding, TestHelper.hexStringToBytes(inputArray))
        then:
        encodedText == encodedText2
        where:
        encoding                 | inputString         | inputArray
        TEXT_ENCODING_ISO_8859_1 | TEST_STRING         | TEST_STRING_HEX_ISO8859_1
        TEXT_ENCODING_UTF_8      | UNICODE_TEST_STRING | UNICODE_TEST_STRING_HEX_UTF8
        TEXT_ENCODING_UTF_16     | UNICODE_TEST_STRING | UNICODE_TEST_STRING_HEX_UTF16LE
        TEXT_ENCODING_UTF_16BE   | UNICODE_TEST_STRING | UNICODE_TEST_STRING_HEX_UTF16BE
    }

//    def "encoding and decoding of ISO8859-1 works properly"() {
//        given:
//        EncodedText encodedText = new EncodedText(TEXT_ENCODING_ISO_8859_1, TEST_STRING)
//        EncodedText encodedText2
//        def bytes
//        expect:
//        encodedText.getCharacterSet() == CHARSET_ISO_8859_1
//        encodedText.toString() == TEST_STRING
//
//        when:
//        // no bom & no terminator
//        bytes = encodedText.toBytes()
//        then:
//        TestHelper.bytesToHexString(bytes) == TEST_STRING_HEX_ISO8859_1
//        and:
//        encodedText2 = new EncodedText(TEXT_ENCODING_ISO_8859_1, bytes)
//        encodedText == encodedText2
//        // bom & no terminator
////        bytes = encodedText.toBytes(true)
////        assertEquals(TEST_STRING_HEX_ISO8859_1, TestHelper.bytesToHexString(bytes))
////        encodedText2 = new EncodedText(TEXT_ENCODING_ISO_8859_1, bytes)
////        assertEquals(encodedText, encodedText2)
////        // no bom & terminator
////        bytes = encodedText.toBytes(false, true)
////        assertEquals(TEST_STRING_HEX_ISO8859_1 + " 00", TestHelper.bytesToHexString(bytes))
////        encodedText2 = new EncodedText(TEXT_ENCODING_ISO_8859_1, bytes)
////        assertEquals(encodedText, encodedText2)
////        // bom & terminator
////        bytes = encodedText.toBytes(true, true)
////        assertEquals(TEST_STRING_HEX_ISO8859_1 + " 00", TestHelper.bytesToHexString(bytes))
////        encodedText2 = new EncodedText(TEXT_ENCODING_ISO_8859_1, bytes)
////        assertEquals(encodedText, encodedText2)
//    }

    def "testShouldEncodeAndDecodeUTF8Text"() {
        EncodedText encodedText = new EncodedText(TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING)
        assertEquals(CHARSET_UTF_8, encodedText.getCharacterSet())
        assertEquals(UNICODE_TEST_STRING, encodedText.toString())
        EncodedText encodedText2
        def bytes
        // no bom & no terminator
        bytes = encodedText.toBytes()
        String c = TestHelper.bytesToHexString(bytes)
        assertEquals(UNICODE_TEST_STRING_HEX_UTF8, c)
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_8, bytes)
        assertEquals(encodedText, encodedText2)
        // bom & no terminator
        bytes = encodedText.toBytes(true)
        assertEquals(UNICODE_TEST_STRING_HEX_UTF8, TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_8, bytes)
        assertEquals(encodedText, encodedText2)
        // no bom & terminator
        bytes = encodedText.toBytes(false, true)
        assertEquals(UNICODE_TEST_STRING_HEX_UTF8 + " 00", TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_8, bytes)
        assertEquals(encodedText, encodedText2)
        // bom & terminator
        bytes = encodedText.toBytes(true, true)
        assertEquals(UNICODE_TEST_STRING_HEX_UTF8 + " 00", TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_8, bytes)
        assertEquals(encodedText, encodedText2)
    }

    def "testShouldEncodeAndDecodeUTF16Text"() {
        EncodedText encodedText = new EncodedText(TEXT_ENCODING_UTF_16, UNICODE_TEST_STRING)
        assertEquals(CHARSET_UTF_16, encodedText.getCharacterSet())
        assertEquals(UNICODE_TEST_STRING, encodedText.toString())
        def bytes
        EncodedText encodedText2
        // no bom & no terminator
        bytes = encodedText.toBytes()
        assertEquals(UNICODE_TEST_STRING_HEX_UTF16LE, TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16, bytes)
        assertEquals(encodedText, encodedText2)
        // bom & no terminator
        bytes = encodedText.toBytes(true)
        assertEquals("ff fe " + UNICODE_TEST_STRING_HEX_UTF16LE, TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16, bytes)
        assertEquals(encodedText, encodedText2)
        // no bom & terminator
        bytes = encodedText.toBytes(false, true)
        assertEquals(UNICODE_TEST_STRING_HEX_UTF16LE + " 00 00", TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16, bytes)
        assertEquals(encodedText, encodedText2)
        // bom & terminator
        bytes = encodedText.toBytes(true, true)
        assertEquals("ff fe " + UNICODE_TEST_STRING_HEX_UTF16LE + " 00 00", TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16, bytes)
        assertEquals(encodedText, encodedText2)
    }

    def "testShouldEncodeAndDecodeUTF16BEText"() {
        EncodedText encodedText = new EncodedText(TEXT_ENCODING_UTF_16BE, UNICODE_TEST_STRING)
        assertEquals(CHARSET_UTF_16BE, encodedText.getCharacterSet())
        assertEquals(UNICODE_TEST_STRING, encodedText.toString())
        def bytes
        EncodedText encodedText2
        // no bom & no terminator
        bytes = encodedText.toBytes()
        assertEquals(UNICODE_TEST_STRING_HEX_UTF16BE, TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16BE, bytes)
        assertEquals(encodedText, encodedText2)
        // bom & no terminator
        bytes = encodedText.toBytes(true)
        assertEquals("fe ff " + UNICODE_TEST_STRING_HEX_UTF16BE, TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16BE, bytes)
        assertEquals(encodedText, encodedText2)
        // no bom & terminator
        bytes = encodedText.toBytes(false, true)
        assertEquals(UNICODE_TEST_STRING_HEX_UTF16BE + " 00 00", TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16BE, bytes)
        assertEquals(encodedText, encodedText2)
        // bom & terminator
        bytes = encodedText.toBytes(true, true)
        assertEquals("fe ff " + UNICODE_TEST_STRING_HEX_UTF16BE + " 00 00", TestHelper.bytesToHexString(bytes))
        encodedText2 = new EncodedText(TEXT_ENCODING_UTF_16BE, bytes)
        assertEquals(encodedText, encodedText2)
    }

    def "testShouldThrowExceptionWhenEncodingWithInvalidCharacterSet"() {
        try {
            new EncodedText((byte) 4, TEST_STRING)
            fail("IllegalArgumentException expected but not thrown")
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid text encoding 4", e.getMessage())
        }
    }

    def "testShouldInferISO8859_1EncodingFromBytesWithNoBOM"() {
        EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes(TEST_STRING_HEX_ISO8859_1))
        assertEquals(TEXT_ENCODING_ISO_8859_1, encodedText.getTextEncoding())
    }

    def "testShouldDetectUTF8EncodingFromBytesWithBOM"() {
        EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("ef bb bf " + UNICODE_TEST_STRING_HEX_UTF8))
        assertEquals(TEXT_ENCODING_UTF_8, encodedText.getTextEncoding())
    }

    def "testShouldDetectUTF16EncodingFromBytesWithBOM"() {
        EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("ff fe " + UNICODE_TEST_STRING_HEX_UTF16LE))
        assertEquals(TEXT_ENCODING_UTF_16, encodedText.getTextEncoding())
    }

    def "testShouldDetectUTF16BEEncodingFromBytesWithBOM"() {
        EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("fe ff " + UNICODE_TEST_STRING_HEX_UTF16BE))
        assertEquals(TEXT_ENCODING_UTF_16BE, encodedText.getTextEncoding())
    }

    def "testShouldTranscodeFromOneEncodingToAnother"() {
        EncodedText encodedText
        encodedText = new EncodedText(TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"))
        encodedText.setTextEncoding(TEXT_ENCODING_ISO_8859_1, true)
        assertEquals("43 61 66 e9 20 50 61 72 61 64 69 73 6f", TestHelper.bytesToHexString(encodedText.toBytes()))
        encodedText = new EncodedText(TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"))
        encodedText.setTextEncoding(TEXT_ENCODING_UTF_8, true)
        assertEquals("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f", TestHelper.bytesToHexString(encodedText.toBytes()))
        encodedText = new EncodedText(TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"))
        encodedText.setTextEncoding(TEXT_ENCODING_UTF_16, true)
        assertEquals("43 00 61 00 66 00 e9 00 20 00 50 00 61 00 72 00 61 00 64 00 69 00 73 00 6f 00", TestHelper.bytesToHexString(encodedText.toBytes()))
        encodedText = new EncodedText(TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"))
        encodedText.setTextEncoding(TEXT_ENCODING_UTF_16BE, true)
        assertEquals("00 43 00 61 00 66 00 e9 00 20 00 50 00 61 00 72 00 61 00 64 00 69 00 73 00 6f", TestHelper.bytesToHexString(encodedText.toBytes()))
    }

    def "testShouldThrowAnExceptionWhenAttemptingToTranscodeToACharacterSetWithUnmappableCharacters"() {
        EncodedText encodedText
        encodedText = new EncodedText(TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING)
        try {
            encodedText.setTextEncoding(TEXT_ENCODING_ISO_8859_1, true)
            fail("CharacterCodingException expected but not thrown")
        } catch (CharacterCodingException e) {
        }
    }

    def "testShouldThrowExceptionWhenTranscodingWithInvalidCharacterSet"() {
        EncodedText encodedText = new EncodedText(TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"))
        try {
            encodedText.setTextEncoding((byte) 4, true)
            fail("IllegalArgumentException expected but not thrown")
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid text encoding 4", e.getMessage())
        }
    }

    def "testShouldReturnNullWhenDecodingInvalidString"() {
        String s = "Not unicode"
        byte[] notUnicode = BufferTools.stringToByteBuffer(s, 0, s.length())
        EncodedText encodedText = new EncodedText(TEXT_ENCODING_UTF_16, notUnicode)
        assertNull(encodedText.toString())
    }

    def "testShouldHandleBacktickCharacterInString"() {
        EncodedText encodedText = new EncodedText((byte) 0, BUFFER_WITH_A_BACKTICK)
        assertEquals("I" + (char) (96) + "m", encodedText.toString())
    }

//	def "testShouldStillReturnBytesWhenStringIsEmpty"() {
//		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, "")
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(true, true)))
//	}
//
//	def "testShouldStillReturnBytesWhenUnicodeStringIsEmpty"() {
//		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, "")
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)))
//	}
//
//	def "testShouldStillReturnBytesWhenDataIsEmpty"() {
//		EncodedText encodedText
//		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, new byte[] {})
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(true, true)))
//		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, new byte[] {0})
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(true, true)))
//	}
//
//	def "testShouldStillReturnBytesWhenUnicodeDataIsEmpty"() {
//		EncodedText encodedText
//		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {})
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)))
//		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {0, 0})
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)))
//		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {(byte)0xff, (byte)0xfe})
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)))
//		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {(byte)0xff, (byte)0xfe, 0, 0})
//		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)))
//		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)))
//		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)))
//	}
}
