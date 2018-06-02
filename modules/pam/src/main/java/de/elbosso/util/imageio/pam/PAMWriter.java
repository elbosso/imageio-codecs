
package de.elbosso.util.imageio.pam;
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

public class PAMWriter  extends javax.imageio.ImageWriter
{private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(PAMWriter.class);
	public PAMWriter(javax.imageio.spi.ImageWriterSpi originatingProvider)
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
		return null;
	}

	@Override
	public IIOMetadata convertImageMetadata(IIOMetadata arg0, ImageTypeSpecifier arg1, ImageWriteParam arg2)
	{
		return null;
	}

	@Override
	public void write(IIOMetadata arg0, IIOImage arg1, ImageWriteParam arg2) throws IOException
	{
		java.lang.Object outputref=getOutput();

		if((outputref!=null)&&(outputref instanceof javax.imageio.stream.ImageOutputStream))
		{
			javax.imageio.stream.ImageOutputStream os=(javax.imageio.stream.ImageOutputStream)outputref;
			java.lang.String head="P7\n\n";
//			java.lang.String helper="P7\n\n";
			if(arg2!=null)
			{
				if(arg2.getCompressionMode()==ImageWriteParam.MODE_EXPLICIT)
				{
					if(arg2.getCompressionQuality()==0)
					{
						head="P8\n\n";
					}
					else if(arg2.getCompressionQuality() == 1)
					{
						head="P9\n\n";
					}
				}
//				helper=(((arg2.getCompressionMode()==ImageWriteParam.MODE_EXPLICIT)&&(arg2.getCompressionQuality()==0))?"P8":"P7")+"\n\n";
			}
//			if(CLASS_LOGGER.isEnabledFor(org.apache.log4j.Level.ERROR))CLASS_LOGGER.error(head);
			os.write(head.getBytes());
			int width=arg1.getRenderedImage().getWidth();
			int height=arg1.getRenderedImage().getHeight();
			java.lang.String helper=java.lang.Integer.toString(width)+" "+java.lang.Integer.toString(height)+"\n";
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
			byte[] scanline=new byte[width];
			if(head.startsWith("P9"))
				scanline=new byte[width*2];
			if(head.startsWith("P8"))
			{

				byte latch=0;
				int maxcounter=0;
				for (int y = 0; y < height; ++y)
				{
					int index=0;
					int counter=0;
					for (int x = 0; x < width; ++x)
					{
						int p=bimg.getRGB(x, y);
						int compressed=(((p&0xff0000)>>16)/32)<<5;
						compressed|=(((p&0xff00)>>8)/32<<2);
						compressed|=(p&0xff)/64;
	//					if(compressed>127)
	//						compressed-=128;
						if(compressed==0xe0)
							compressed=0xc0;
						byte current=(byte)compressed;
						if(x==0)
						{
							latch=current;
							counter=1;
						}
						else
						{
							if(latch!=current)
							{
								if(counter<4)
								{
									for(int k=0;k<counter;++k)
									{
										scanline[index]=latch;
										++index;
									}
								}
								else
								{
									scanline[index]=(byte)0xe0;
									++index;
									scanline[index]=(byte)(counter-3);
									if(counter>maxcounter)
										maxcounter=counter;
//									if(y==0)

									++index;
									scanline[index]=latch;
									++index;
								}
								latch=current;
								counter=1;
							}
							else
							{
								++counter;
							}
							if((counter==258)||(x+1==width))
							{
								if(counter<4)
								{
									for(int k=0;k<counter;++k)
									{
										scanline[index]=latch;
										++index;
									}
								}
								else
								{
									scanline[index]=(byte)0xe0;
									++index;
									scanline[index]=(byte)(counter-3);
									if(counter>maxcounter)
										maxcounter=counter;
//									if(y==0)

									++index;
									scanline[index]=latch;
									++index;
								}
								latch=current;
								counter=1;
							}
						}
					}
					os.write(scanline,0,index);
				}

			}
			else if (head.startsWith("P7"))
			{

				for (int y = 0; y < height; ++y)
				{
					for (int x = 0; x < width; ++x)
					{
						int p=bimg.getRGB(x, y);
						int compressed=(((p&0xff0000)>>16)/32)<<5;
						compressed|=(((p&0xff00)>>8)/32<<2);
						compressed|=(p&0xff)/64;
	//					if(compressed>127)
	//						compressed-=128;
						scanline[x]=(byte)compressed;
					}
					os.write(scanline);
				}
			}
			else if (head.startsWith("P9"))
			{

				for (int y = 0; y < height; ++y)
				{
					for (int x = 0; x < width; ++x)
					{
						int p=bimg.getRGB(x, y);
						int compressed=0x80+((p&0xf80000)>>17);
						compressed|=((p&0xc000)>>14);
						if((p&0xff000000)==0)
						{
							if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("seethrough");
							compressed&=0x7f;
						}
						scanline[2*x]=(byte)compressed;
						compressed=((p&0x3800)>>6);
						compressed|=((p&0xf8)>>3);
	//					if(compressed>127)
	//						compressed-=128;
						scanline[2*x+1]=(byte)compressed;
					}
					os.write(scanline);
				}
			}
		}
		else
			throw new java.io.IOException("illegal output given!");
	}

	@Override
	public ImageWriteParam getDefaultWriteParam()
	{
		ImageWriteParam rv=new ImageWriteParam(locale)
		{

			@Override
			public boolean canWriteCompressed()
			{
				return true;
			}

			@Override
			public boolean isCompressionLossless()
			{
				return true;
			}

		};

		return rv;
	}

}
