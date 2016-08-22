package com.mpatric.mp3agic;

import java.util.List;
import java.util.Map;

public interface ID3v2Source {

    String getAlbumArtist();

    byte[] getAlbumImage();

    String getAlbumImageMimeType();

    String getArtistUrl();

    String getAudioSourceUrl();

    String getAudiofileUrl();

    int getBPM();

    List<ID3v2ChapterTOCFrameData> getChapterTOC();

    List<ID3v2ChapterFrameData> getChapters();

    String getCommercialUrl();

    String getComposer();

    String getCopyright();

    String getCopyrightUrl();

    int getDataLength();

    String getDate();

    String getEncoder();

    Map<String, ID3v2FrameSet> getFrameSets();

    String getGrouping();

    String getItunesComment();

    String getKey();

    int getLength();

    boolean getObseleteFormat();

    String getOriginalArtist();

    boolean getPadding();

    String getPartOfSet();

    String getPaymentUrl();

    String getPublisher();

    String getPublisherUrl();

    String getRadiostationUrl();

    String getUrl();

    int getWmpRating();

    boolean isCompilation();

    boolean hasFooter();

    boolean hasUnsynchronisation();

}
