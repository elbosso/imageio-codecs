# imageio-codecs

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