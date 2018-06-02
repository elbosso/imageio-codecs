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
			f.deleteOnExit();
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
	@Test
	public void writePGM()
	{
		try
		{
			java.io.File f=java.io.File.createTempFile("writePGM",".pgm");
			f.deleteOnExit();
			java.awt.image.BufferedImage bimg=javax.imageio.ImageIO.read(this.getClass().getClassLoader().getResource("240px-Computer.svg_gray.png"));
			javax.imageio.ImageIO.write(bimg,"pgm",f);
			java.awt.image.BufferedImage expected=javax.imageio.ImageIO.read(this.getClass().getClassLoader().getResource("240px-Computer.svg_raw.pgm"));
			java.awt.image.BufferedImage result=javax.imageio.ImageIO.read(f);
			Assert.assertTrue(imagesAreEqual(result,expected));
		} catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}
	}
	@Test
	public void writePAM()
	{
		try
		{
			java.io.File f=java.io.File.createTempFile("writePAM",".pam");
//			f.deleteOnExit();
			java.awt.image.BufferedImage bimg=javax.imageio.ImageIO.read(this.getClass().getClassLoader().getResource("240px-Computer.svg.png"));
			javax.imageio.ImageIO.write(bimg,"pam",f);
			java.awt.image.BufferedImage expected=javax.imageio.ImageIO.read(this.getClass().getClassLoader().getResource("240px-Computer.svg.pam"));
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
				int lrgb=image1.getRGB(x, y);
				int rrgb=image2.getRGB(x, y);
				int lr=((lrgb&0xff0000)>>16);
				int lg=((lrgb&0xff00)>>8);
				int lb=(lrgb&0xff);
				int rr=((rrgb&0xff0000)>>16);
				int rg=((rrgb&0xff00)>>8);
				int rb=(rrgb&0xff);
				int diff=java.lang.Math.abs( lr- rr);
				diff+=java.lang.Math.abs( lg- rg);
				diff+=java.lang.Math.abs( lb- rb);
				//I had to be a bit more lenient here because of the way gimp does ppm export (my test sample was produced with gimp)
				if (diff>100) {
					System.out.println(x+" "+y+" "+java.lang.Integer.toHexString(image1.getRGB(x, y))+" "+java.lang.Integer.toHexString(image2.getRGB(x, y)));
					return false;
				}
			}
		}
		return true;
	}
}
