package com.mpatric.mp3agic;

public interface ID3v24 extends ID3v2 {
    void setTitles(Iterable<String> titles);

    void setArtists(Iterable<String> artists);

    void setAlbums(Iterable<String> albums);

    void setTracks(Iterable<String> tracks);

    void setYears(Iterable<String> years);

    void setGenreDescriptions(Iterable<String> genreDescriptions);

    void setComments(Iterable<String> comments);

    void setComposers(Iterable<String> composers);

    void setPublishers(Iterable<String> publishers);

    void setOriginalArtists(Iterable<String> originalArtists);

    void setAlbumArtists(Iterable<String> albumArtists);

    void setCopyrights(Iterable<String> copyrights);

    void setUrls(Iterable<String> urls);

    void setPartOfSets(Iterable<String> partOfSets);

    void setGroupings(Iterable<String> groupings);

    void setEncoders(Iterable<String> encoders);
}
