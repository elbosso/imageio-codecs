package de.elbosso.util.imageio;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class WriterTest
{
	@Test
	public void alwaysSuccess()
	{
		java.lang.String[] formatNames=javax.imageio.ImageIO.getWriterFormatNames();
		for(java.lang.String formatName:formatNames)
		{
			java.util.Iterator<javax.imageio.ImageWriter> writers=javax.imageio.ImageIO.getImageWritersByFormatName(formatName);
			while(writers.hasNext())
			{
				System.out.println(formatName+" "+writers.next());
			}
		}
	}
	@Test
	public void writePPM()
	{
		try
		{
			java.io.File f=java.io.File.createTempFile("writePPM",".ppm");
//			f.deleteOnExit();
			java.awt.image.BufferedImage bimg=javax.imageio.ImageIO.read(this.getClass().getClassLoader().getResource("240px-Computer.svg.png"));
			javax.imageio.ImageIO.write(bimg,"ppm",f);
			java.awt.image.BufferedImage expected=javax.imageio.ImageIO.read(this.getClass().getClassLoader().getResource("240px-Computer.svg_raw.ppm"));
			java.awt.image.BufferedImage result=javax.imageio.ImageIO.read(f);
			Assert.assertTrue(imagesAreEqual(result,expected));
		} catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	boolean imagesAreEqual(java.awt.image.BufferedImage image1, java.awt.image.BufferedImage image2) {
		if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
			return false;
		}
		for (int x = 0; x < image2.getWidth(); x++) {
			for (int y = 0; y < image2.getHeight(); y++) {
				if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
					System.out.println(x+" "+y+" "+java.lang.Integer.toHexString(image1.getRGB(x, y))+" "+java.lang.Integer.toHexString(image2.getRGB(x, y)));
					return false;
				}
			}
		}
		return true;
	}
}
