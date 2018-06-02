package de.elbosso.util.imageio;

import org.junit.Test;

public class ReaderTest
{

	@Test
	public void alwaysSuccess()
	{
		java.lang.String[] formatNames=javax.imageio.ImageIO.getReaderFormatNames();
		for(java.lang.String formatName:formatNames)
		{
			java.util.Iterator<javax.imageio.ImageReader> readers=javax.imageio.ImageIO.getImageReadersByFormatName(formatName);
			while(readers.hasNext())
			{
				System.out.println(formatName+" "+readers.next());
			}
		}
	}
	@Test
	public void readPNM()
	{

	}
}