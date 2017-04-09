/*
 * The MIT License
 *
 * Copyright 2017 Neeraj.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.mpatric.mp3agic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neeraj
 */
public class ID3v2TXXXFrameDataTest {

    public ID3v2TXXXFrameDataTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @org.junit.Test
    public void test() throws Exception {
        System.out.println("test for txxx frame");

        Map<String, ID3v2FrameSet> frameSets = new HashMap<>();

        // for input of new field
        ID3v2TXXXFrameData.createOrAddField(
                frameSets,
                true,
                "my_custom_text",
                "value",
                true);
        assertEquals(1, frameSets.size());

        // for extraction
        ID3v2TXXXFrameData frameData = ID3v2TXXXFrameData.extract(
                frameSets,
                true,
                "my_custom_text");
        assertEquals("my_custom_text", frameData.getDescription().toString());
        assertEquals("value", frameData.getValue().toString());

        // for input with replacement
        ID3v2TXXXFrameData.createOrAddField(
                frameSets,
                true,
                "my_custom_text",
                "value changed",
                true);
        frameData = ID3v2TXXXFrameData.extract(
                frameSets,
                true,
                "my_custom_text");
        assertEquals("my_custom_text", frameData.getDescription().toString());
        assertEquals("value changed", frameData.getValue().toString());

        // for input with-out replacement
        ID3v2TXXXFrameData.createOrAddField(
                frameSets,
                true,
                "my_custom_text",
                "value 2",
                false);
        ArrayList<ID3v2TXXXFrameData> frameDatas = ID3v2TXXXFrameData.extractAll(
                frameSets,
                true,
                "my_custom_text");
        assertEquals(2, frameDatas.size());

    }

}
