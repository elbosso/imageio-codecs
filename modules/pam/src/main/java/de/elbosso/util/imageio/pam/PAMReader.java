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
public class PAMReader extends javax.imageio.ImageReader
{
	private int height=-1;
	private int width=-1;
	private boolean compressed;
	private boolean hicolor;
	
	public PAMReader(javax.imageio.spi.ImageReaderSpi originatingProvider) 
	{
		super(originatingProvider);
	}	
	public int getHeight(int imageIndex)
	{

		readImageSpecs();
		return height;
	}
	public javax.imageio.metadata.IIOMetadata getImageMetadata(int imageIndex)
	{
		return null;
	}
	public java.util.Iterator getImageTypes(int imageIndex)
	{

		java.util.List types=new java.util.LinkedList();
		java.awt.image.ColorModel colorModel=null;
		java.awt.image.SampleModel sampleModel=null;
		colorModel=java.awt.image.ColorModel.getRGBdefault();
		sampleModel=colorModel.createCompatibleSampleModel(getWidth(0),getHeight(0));
		if((colorModel!=null)&&(sampleModel!=null))
		{
			types.add(new javax.imageio.ImageTypeSpecifier(colorModel,sampleModel));
		}
		return types.iterator();
	}
	public int getNumImages(boolean allowSearch)
	{
		return 1;
	}
	public javax.imageio.metadata.IIOMetadata getStreamMetadata()
	{
		return null;
	}
	public int getWidth(int imageIndex)
	{
		readImageSpecs();
		return width;
	}
	public java.awt.image.BufferedImage read(int imageIndex, javax.imageio.ImageReadParam param)
	{
		java.awt.image.BufferedImage bi=null;
		java.lang.Object inputref=getInput();
		javax.imageio.stream.ImageInputStream is=(javax.imageio.stream.ImageInputStream)inputref;
		try
		{
			readHeader(is);
			int pixelcount=width*height;
			byte[] buf=null;
			{
				buf=new byte[pixelcount];
				if(hicolor)
					buf=new byte[pixelcount*2];
				bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_ARGB);
				{
					is.read(buf);
					pixelcount=0;

					for (int p = 0; p < buf.length; ++p)
					{               
						if(compressed)
						{
							if(buf[p]==(byte)0xe0)
							{
								++p;
								int counter=buf[p];
								if(counter<0)
									counter+=256;
								counter+=3;
								++p;
								byte bufp=buf[p];
								int r=((bufp&0xE0)>>5)*32;
								int g=((bufp&0x1c)>>2)*32;
								int b=(bufp&0x03)*64;
								int color=(r<<16)+(g<<8)+b;
								for(int k=0;k<counter;++k)
								{
									try
									{
									bi.setRGB(pixelcount%width,pixelcount/width,color);
									++pixelcount;
									}
									catch(java.lang.IndexOutOfBoundsException exp)
									{
//										if(CLASS_LOGGER.isEnabledFor(org.apache.log4j.Level.ERROR))CLASS_LOGGER.error(xindex+" "+y+" "+counter+" "+xindex);
									}
								}
							}
							else
							{
								int r=((buf[p]&0xE0)>>5)*32;
								int g=((buf[p]&0x1c)>>2)*32;
								int b=(buf[p]&0x03)*64;
								int color=(r<<16)+(g<<8)+b;
								bi.setRGB(pixelcount%width,pixelcount/width,color);
								++pixelcount;
							}
						}
						else if(hicolor)
						{
							byte lefty=buf[p];
							++p;
							byte righty=buf[p];
							int r=((lefty&0x7c)<<1);
							int g=((lefty&0x03)<<6)|((righty&0xe0)>>2);
							int b=(righty&0x1f)<<3;
							int color=(r<<16)+(g<<8)+b;
							if((lefty&0x80)!=0)
								color|=0xff<<24;
							bi.setRGB(pixelcount%width,pixelcount/width,color);
							++pixelcount;
						}
						else
						{
							int r=((buf[p]&0xE0)>>5)*32;
							int g=((buf[p]&0x1c)>>2)*32;
							int b=(buf[p]&0x03)*64;
							int color=(r<<16)+(g<<8)+b;
							bi.setRGB(pixelcount%width,pixelcount/width,color);
							++pixelcount;
						}
						if(pixelcount==width*height)
						{
							p=buf.length;
						}
					}
				}
			}
		}
		catch(java.io.IOException exp)
		{
			exp.printStackTrace();
		}
		return bi;
	}
	private void readImageSpecs()
	{
		java.lang.Object inputref=getInput();
		if(inputref instanceof javax.imageio.stream.ImageInputStream)
		{
			javax.imageio.stream.ImageInputStream is=(javax.imageio.stream.ImageInputStream)inputref;
			is.mark();
			try{
				readHeader(is);
			is.reset();
			}catch(java.io.IOException exp){}
		}
	}
	private void readHeader(javax.imageio.stream.ImageInputStream is)throws java.io.IOException
	{
		int type=0;
		byte[] header=new byte[2];
		if(is.read(header)==2)
		{

			if((header[0]==0x50)&&(((header[1]==0x37)||(header[1]==0x38))||(header[1]==0x39)))//must be between '3' and '8' if BIN
			{
				compressed=(header[1]==0x38);
				hicolor=(header[1]==0x39);
				java.lang.String line=null;
				line=is.readLine();
				line=is.readLine();
				while ((line.indexOf('#')==0)||(line.length()==0))
				{
					line=is.readLine();
				}
				try{
				java.util.StringTokenizer tokenizer=new java.util.StringTokenizer(line);
				width=java.lang.Integer.parseInt(tokenizer.nextToken());
				height=java.lang.Integer.parseInt(tokenizer.nextToken());
//				if(tokenizer.hasMoreTokens())
//					int charmax=java.lang.Integer.parseInt(tokenizer.nextToken());
				}catch(java.lang.Exception exp){exp.printStackTrace();}
			}
		}			
	}
}