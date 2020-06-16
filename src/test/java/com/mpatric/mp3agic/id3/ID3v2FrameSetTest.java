package com.mpatric.mp3agic.id3;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class ID3v2FrameSetTest {

    @Test
    public void shouldCorrectlyImplementHashCodeAndEquals() throws Exception {
        EqualsVerifier.forClass(ID3v2FrameSet.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}
