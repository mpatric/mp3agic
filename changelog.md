# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]
### Added

### Changed
- ID3Wrapper getGenre() returns v2 tag before v1 tag (instead of v1 tag before v2 tag).
- ID3Wrapper getGenreDescription() returns v2 tag before v1 tag (instead of v1 tag before v2 tag).
- ID3v2CommentFrameData constructor requires description and comment to have the same text encoding.
- BufferTools, ByteBufferUtils, ID3v1Genres, ID3v2TagFactory, and Version have no public constructor.
