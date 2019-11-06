package com.mpatric.mp3agic;

import java.util.ArrayList;
import java.util.List;

public interface ID3v2Improved extends ID3v2 {
	
	@Deprecated
	ArrayList<ID3v2ChapterFrameData> getChapters();
	@Deprecated
	ArrayList<ID3v2ChapterTOCFrameData> getChapterTOC();
	
	List<ID3v2ChapterFrameData> listOfChapters();
	List<ID3v2ChapterTOCFrameData> listOfChapterTOC();
}
