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
public class PNMReader extends javax.imageio.ImageReader
{
	public static final int PBM=0x1;
	public static final int PGM=0x2;
	public static final int PPM=0x4;
	public static final int ASCII=0x10;
	public static final int BIN=0x20;
	public static final int NO_PNM=0x0;
	private int height=-1;
	private int width=-1;
	private int pnmtype=NO_PNM;
	private int charmax=-1;
	
	public PNMReader(javax.imageio.spi.ImageReaderSpi originatingProvider) 
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
		if((pnmtype&PPM)!=0)
		{
			colorModel=java.awt.image.ColorModel.getRGBdefault();
			sampleModel=colorModel.createCompatibleSampleModel(getWidth(0),getHeight(0));
		}
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
			if ((pnmtype & PBM)!=0)
			{
				int rowbytes=width/8;
				if(width%8!=0)
					rowbytes+=1;
				buf=new byte[rowbytes];
				bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);
				if ((pnmtype & ASCII)!=0)
				{
					java.lang.String line=is.readLine();
					int x=0;
					int y=0;
					int color=0;
					while(line!=null)
					{
						byte[] bytes=line.replace(" ","").replace("\t","").getBytes();
						for(int j=0;j<bytes.length;++j)
						{
							if((bytes[j]==0x31)||(bytes[j]==0x30))
							{
								if(bytes[j]==0x31)
									color=0x0;
								else 
									color=0xffffff;
								bi.setRGB(x,y,color);
								++x;
								if(x==width)
								{
									x=0;
									++y;
								}
							}
						}
						line=is.readLine();
					}
				}
				else
				{
					for (int y = 0; y < height; ++y)
					{         
						is.read(buf);
						int x=0;
						int bit=-1;
						int byteindex=0;
						byte thebyte=0;
						int color=0;
						while(x<width)
						{
							color=0;
							if(bit==-1)
							{
								bit=7;
								thebyte=buf[byteindex];
								++byteindex;
							}
							if((thebyte&(1<<bit))==0)
								color=0xffffff;
							bi.setRGB(x,y,color);
							++x;
							--bit;
						}
					}
				}
			}
			else if ((pnmtype & PGM)!=0)
			{
				buf=new byte[pixelcount];
				bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);
				if ((pnmtype & ASCII)!=0)
				{
					java.lang.String line=is.readLine();
					int x=0;
					int y=0;
					int color=0;
					while(line!=null)
					{
						java.util.StringTokenizer tokenizer=new java.util.StringTokenizer(line);
						while(tokenizer.hasMoreTokens())
						{
							int grey=(int)(((double)java.lang.Integer.parseInt(tokenizer.nextToken()))/((double)charmax)*255.0);
							color=(grey<<16)+(grey<<8)+grey;
							bi.setRGB(x,y,color);
							++x;
							if(x==width)
							{
								x=0;
								++y;
							}
						}
						line=is.readLine();
					}
				}
				else
				{
					is.read(buf);
					for (int y = 0; y < height; ++y)
					{                   
						for (int x = 0; x < width; ++x)
						{
							int p=y*width+x;
							int value=buf[p];
							if(value<0)
								value=256+value;
							int grey=(int)(((double)value)/((double)charmax)*255.0);
							int color=(grey<<16)+(grey<<8)+grey;
							bi.setRGB(x,y,color);
						}
					}
				}
			}
			else if ((pnmtype & PPM)!=0)
			{
				buf=new byte[pixelcount*3];
				bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);
				if ((pnmtype & ASCII)!=0)
				{
					java.lang.String line=is.readLine();
					int x=0;
					int y=0;
					int color=0;
					int[] tupel=new int[3];
					int counter=0;
					while(line!=null)
					{
						java.util.StringTokenizer tokenizer=new java.util.StringTokenizer(line);
						while(tokenizer.hasMoreTokens())
						{
							tupel[counter]=(int)(((double)java.lang.Integer.parseInt(tokenizer.nextToken()))/((double)charmax)*255.0);
							++counter;
							if(counter==tupel.length)
							{
								color=(tupel[0]<<16)+(tupel[1]<<8)+tupel[2];
								bi.setRGB(x,y,color);
								++x;
								if(x==width)
								{
									x=0;
									++y;
								}
								counter=0;
							}
						}
						line=is.readLine();
					}
				}
				else
				{
					is.read(buf);
					for (int y = 0; y < height; ++y)
					{                   
						for (int x = 0; x < width; ++x)
						{
							int p=y*width+x;
							int r=buf[p*3+0];
							if(r<0)
								r=256+r;
							r=(int)(((double)r)/((double)charmax)*255.0);
							int g=buf[p*3+1];
							if(g<0)
								g=256+g;
							g=(int)(((double)g)/((double)charmax)*255.0);
							int b=buf[p*3+2];
							if(b<0)
								b=256+b;
							b=(int)(((double)b)/((double)charmax)*255.0);
							int color=(r<<16)+(g<<8)+b;
							bi.setRGB(x,y,color);
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
			if(header[0]==0x50)//must be 'P'
			{
				if((header[1]>0x30)&&(header[1]<0x34))//must be between '0' and '4' if ASCII
				{
					pnmtype|=ASCII;
					type=header[1]-0x31;
				}
				else if((header[1]>0x33)&&(header[1]<0x37))//must be between '3' and '7' if BIN
				{
					pnmtype|=BIN;
					type=header[1]-0x34;
				}
				switch(type)
				{
					case 0:
					{
						pnmtype|=PBM;
						break;
					}
					case 1:
					{
						pnmtype|=PGM;
						break;
					}
					default:
					{
						pnmtype|=PPM;
						break;
					}
				}
				java.lang.String line=null;
				line=is.readLine();
				while ((line.indexOf('#')==0)||(line.length()==0))
				{
					line=is.readLine();
				}
				try{
				java.util.StringTokenizer tokenizer=new java.util.StringTokenizer(line);
				width=java.lang.Integer.parseInt(tokenizer.nextToken());
				height=java.lang.Integer.parseInt(tokenizer.nextToken());
				}catch(java.lang.Exception exp){exp.printStackTrace();}
				charmax=255;
				if((pnmtype&PBM)==0)
				{
					line=is.readLine();
					while ((line.indexOf('#')==0)||(line.length()==0))
					{
						line=is.readLine();
					}
					java.util.StringTokenizer tokenizer=new java.util.StringTokenizer(line);
					charmax=java.lang.Integer.parseInt(tokenizer.nextToken());
				}
			}
		}			
	}
}