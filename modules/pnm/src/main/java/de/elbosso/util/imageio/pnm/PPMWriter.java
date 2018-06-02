
package de.elbosso.util.imageio.pnm;
/*
Copyright (c) 2014.

Juergen Key. Alle Rechte vorbehalten.

Weiterverbreitung und Verwendung in nichtkompilierter oder kompilierter Form, 
mit oder ohne Veraenderung, sind unter den folgenden Bedingungen zulaessig:

   1. Weiterverbreitete nichtkompilierte Exemplare muessen das obige Copyright, 
die Liste der Bedingungen und den folgenden Haftungsausschluss im Quelltext 
enthalten.
   2. Weiterverbreitete kompilierte Exemplare muessen das obige Copyright, 
die Liste der Bedingungen und den folgenden Haftungsausschluss in der 
Dokumentation und/oder anderen Materialien, die mit dem Exemplar verbreitet 
werden, enthalten.
   3. Weder der Name des Autors noch die Namen der Beitragsleistenden 
duerfen zum Kennzeichnen oder Bewerben von Produkten, die von dieser Software 
abgeleitet wurden, ohne spezielle vorherige schriftliche Genehmigung verwendet 
werden.

DIESE SOFTWARE WIRD VOM AUTOR UND DEN BEITRAGSLEISTENDEN OHNE 
JEGLICHE SPEZIELLE ODER IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE 
UNTER ANDEREM EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER 
SOFTWARE FUER EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL IST DER AUTOR 
ODER DIE BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN, 
ZUFAELLIGEN, SPEZIELLEN, BEISPIELHAFTEN ODER FOLGENDEN SCHAEDEN (UNTER ANDEREM 
VERSCHAFFEN VON ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER 
NUTZUNGSFAEHIGKEIT; VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER 
GESCHAEFTSUNTERBRECHUNG), WIE AUCH IMMER VERURSACHT UND UNTER WELCHER 
VERPFLICHTUNG AUCH IMMER, OB IN VERTRAG, STRIKTER VERPFLICHTUNG ODER 
UNERLAUBTE HANDLUNG (INKLUSIVE FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM 
WEG SIE AUCH IMMER DURCH DIE BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR, 
WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.

*/

import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadata;

public class PPMWriter  extends javax.imageio.ImageWriter
{
	public PPMWriter(javax.imageio.spi.ImageWriterSpi originatingProvider)
	{
		super(originatingProvider);
	}
	@Override
	public IIOMetadata getDefaultStreamMetadata(ImageWriteParam arg0)
	{
		return null;
	}

	@Override
	public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier arg0, ImageWriteParam arg1)
	{
		return null;
	}

	@Override
	public IIOMetadata convertStreamMetadata(IIOMetadata arg0, ImageWriteParam arg1)
	{
		return arg0;
	}

	@Override
	public IIOMetadata convertImageMetadata(IIOMetadata arg0, ImageTypeSpecifier arg1, ImageWriteParam arg2)
	{
		return arg0;
	}

	@Override
	public void write(IIOMetadata arg0, IIOImage arg1, ImageWriteParam arg2) throws IOException
	{
		java.lang.Object outputref=getOutput();

		if((outputref!=null)&&(outputref instanceof javax.imageio.stream.ImageOutputStream))
		{
			javax.imageio.stream.ImageOutputStream os=(javax.imageio.stream.ImageOutputStream)outputref;
			java.lang.String helper="P6\n\n";
			os.write(helper.getBytes());
			int width=arg1.getRenderedImage().getWidth();
			int height=arg1.getRenderedImage().getHeight();
			helper=java.lang.Integer.toString(width)+" "+java.lang.Integer.toString(height)+"\n255\n";
			os.write(helper.getBytes());
			java.awt.image.BufferedImage bimg=null;
			if (arg1.getRenderedImage() instanceof java.awt.image.BufferedImage)
			{
				bimg= (java.awt.image.BufferedImage) arg1.getRenderedImage();
			}
			else
			{
				java.awt.image.ColorModel cm = arg1.getRenderedImage().getColorModel();
				java.awt.image.WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
				boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
				java.util.Hashtable properties = new java.util.Hashtable();
				String[] keys = arg1.getRenderedImage().getPropertyNames();
				if (keys != null)
				{
					for (int i = 0; i < keys.length; i++)
					{
						properties.put(keys[i], arg1.getRenderedImage().getProperty(keys[i]));
					}
				}
				bimg = new java.awt.image.BufferedImage(cm, raster, isAlphaPremultiplied, properties);
				arg1.getRenderedImage().copyData(raster);
			}
			byte[] scanline=new byte[width*3];
			int br=0xff;
			int bg=0xff;
			int bb=0xff;
			for (int y = 0; y < height; ++y)
			{
				int loop=0;
				for (int x = 0; x < width; ++x)
				{
					int p=bimg.getRGB(x, y);
					int a=((p>>24)&0xff);
					int r=((p&0xff0000)>>16);
					int g=((p&0xff00)>>8);
					int b=(p&0xff);
					r=((255-a)*br+a*r)/255;
					g=((255-a)*bg+a*g)/255;
					b=((255-a)*bb+a*b)/255;
					scanline[loop++]=(byte)r;
					scanline[loop++]=(byte)g;
					scanline[loop++]=(byte)b;
				}
				os.write(scanline);
			}

		}
		else
			throw new java.io.IOException("illegal output given!");
	}
}
