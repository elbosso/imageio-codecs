# imageio-codecs

<!---
[![start with why](https://img.shields.io/badge/start%20with-why%3F-brightgreen.svg?style=flat)](http://www.ted.com/talks/simon_sinek_how_great_leaders_inspire_action)
--->
[![GitHub release](https://img.shields.io/github/release/elbosso/imageio-codecs/all.svg?maxAge=1)](https://GitHub.com/elbosso/imageio-codecs/releases/)
[![GitHub tag](https://img.shields.io/github/tag/elbosso/imageio-codecs.svg)](https://GitHub.com/elbosso/imageio-codecs/tags/)
[![GitHub license](https://img.shields.io/github/license/elbosso/imageio-codecs.svg)](https://github.com/elbosso/imageio-codecs/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/elbosso/imageio-codecs.svg)](https://GitHub.com/elbosso/imageio-codecs/issues/)
[![GitHub issues-closed](https://img.shields.io/github/issues-closed/elbosso/imageio-codecs.svg)](https://GitHub.com/elbosso/imageio-codecs/issues?q=is%3Aissue+is%3Aclosed)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/elbosso/imageio-codecs/issues)
[![GitHub contributors](https://img.shields.io/github/contributors/elbosso/imageio-codecs.svg)](https://GitHub.com/elbosso/imageio-codecs/graphs/contributors/)
[![Github All Releases](https://img.shields.io/github/downloads/elbosso/imageio-codecs/total.svg)](https://github.com/elbosso/imageio-codecs)

This project contains some codecs for images not normally supported by java - this was my
first foray into java spi.

## PPM
The plugin allows to read the file formats popularized by the pnm tools: PPM, PGM and PBM. It writes both PGM and PPM.

## PAM
This plugin reads and writes a rather exotic format: PAM is used for example where one needs color images but does not have
access to true color rasterizing devices: It packs all the bits of one pixel into the eight bits of one byte - thus, one has
three bits per color except for blue

## TGA
This plugin reads Targa images.
