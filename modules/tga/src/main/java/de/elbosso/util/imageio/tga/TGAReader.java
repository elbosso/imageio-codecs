package de.elbosso.util.imageio.tga;
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
public class TGAReader extends javax.imageio.ImageReader
{

	public static final int COLOR_MAPPED=1;
	public static final int TRUE_COLOR=2;
	public static final int BLACK_AND_WHITE=3;
	public static final int RLE_COLOR_MAPPED=9;
	public static final int RLE_TRUE_COLOR=10;
	public static final int RLE_BLACK_AND_WHITE=11;
	public static final int NO_TGA=0;
	private int height=-1;
	private int width=-1;
	private int depth=-1;
	private byte specs=-1;
	private int tgatype=NO_TGA;
	private int charmax=-1;
	private int colormaplength;
	private int colormapfirstindex;
	private int colormaplayout;
	private int idlength;

	
	public TGAReader(javax.imageio.spi.ImageReaderSpi originatingProvider) 
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
		if(tgatype>0)
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
			readExtent(is);
			int pixelcount=width*height;
			byte[] buf=null;
			int byteperpixel=depth/8;
			if(depth%8!=0)
				byteperpixel+=1;
			if (tgatype==de.elbosso.util.imageio.tga.TGAReader.TRUE_COLOR)
			{
				int rowbytes=width*byteperpixel;
				buf=new byte[rowbytes];
				if(depth>24)
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_ARGB);
				else
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);
				for (int y = 0; y < height; ++y)
				{         
					is.read(buf);
					int x=0;
					int uy=y;
					if((specs&0x20)==0)
						uy=height-1-y;
					while(x<width)
					{
						int color=0;
						if(depth>24)
							color=convert(buf[3+x*byteperpixel])*16777216+convert(buf[2+x*byteperpixel])*65536+convert(buf[1+x*byteperpixel])*256+convert(buf[0+x*byteperpixel]);
						else
							color=convert(buf[2+x*byteperpixel])*65536+convert(buf[1+x*byteperpixel])*256+convert(buf[0+x*byteperpixel]);
						int ux=x;
						if((specs&0x10)!=0)
							ux=width-1-x;
						bi.setRGB(ux,uy,color);
						++x;
					}
				}
			}
			else if (tgatype==de.elbosso.util.imageio.tga.TGAReader.BLACK_AND_WHITE)
			{
				int rowbytes=width*byteperpixel;
				buf=new byte[rowbytes];
				if(depth>24)
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_ARGB);
				else
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);
				for (int y = 0; y < height; ++y)
				{         
					is.read(buf);
					int x=0;
					int uy=y;
					if((specs&0x20)==0)
						uy=height-1-y;
					while(x<width)
					{
						int color=convert(buf[0+x*byteperpixel])*65536+convert(buf[0+x*byteperpixel])*256+convert(buf[0+x*byteperpixel]);
						int ux=x;
						if((specs&0x10)!=0)
							ux=width-1-x;
						bi.setRGB(ux,uy,color);
						++x;
					}
				}
			}
			else if (tgatype==de.elbosso.util.imageio.tga.TGAReader.RLE_TRUE_COLOR)
			{
				int allpixels=width*height;
				byte[] rle=new byte[1];
				buf=new byte[byteperpixel];
				if(depth>24)
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_ARGB);
				else
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);
				int y=0;
				int x=0;
				int uy=y;
				if((specs&0x20)==0)
					uy=height-1-y;
				int currentpixel=0;
				while( currentpixel<pixelcount)
				{         
					is.read(rle);
					//run length packet
					if((rle[0]&0x80)>0)
					{
						is.read(buf);
						int pc=(int)((rle[0]&0x7f)+1);
						int color=0;
						if(depth>24)
							color=convert(buf[3])*16777216+convert(buf[2])*65536+convert(buf[1])*256+convert(buf[0]);
						else
							color=convert(buf[2])*65536+convert(buf[1])*256+convert(buf[0]);
						for(int i=0;i<pc;++i)
						{
							int ux=x;
							if((specs&0x10)!=0)
								ux=width-1-x;
							bi.setRGB(ux,uy,color);
							++x;
							if(x==width)
							{
								x=0;
								++y;
								uy=y;
								if((specs&0x20)==0)
									uy=height-1-y;
							}
							++currentpixel;
						}
					}
					//raw packet
					else
					{
						int pc=(int)((rle[0])+1);
						byte[] raw=new byte[byteperpixel*pc];
						is.read(raw);
						for(int i=0;i<pc;++i)
						{
							int ux=x;
							if((specs&0x10)!=0)
								ux=width-1-x;
							int color=0;
							if(depth>24)
								color=convert(raw[3+i*byteperpixel])*16777216+convert(raw[2+i*byteperpixel])*65536+convert(raw[1+i*byteperpixel])*256+convert(raw[0+i*byteperpixel]);
							else
								color=convert(raw[2+i*byteperpixel])*65536+convert(raw[1+i*byteperpixel])*256+convert(raw[0+i*byteperpixel]);
							bi.setRGB(ux,uy,color);
							++x;
							if(x==width)
							{
								x=0;
								++y;
								uy=y;
								if((specs&0x20)==0)
									uy=height-1-y;
							}
							++currentpixel;
						}
					}
				}
			}
			else if (tgatype==de.elbosso.util.imageio.tga.TGAReader.RLE_BLACK_AND_WHITE)
			{
				int allpixels=width*height;
				byte[] rle=new byte[1];
				buf=new byte[byteperpixel];
				if(depth>24)
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_ARGB);
				else
					bi=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);
				int y=0;
				int x=0;
				int uy=y;
				if((specs&0x20)==0)
					uy=height-1-y;
				int currentpixel=0;
				while( currentpixel<pixelcount)
				{         
					is.read(rle);
					//run length packet
					if((rle[0]&0x80)>0)
					{
						is.read(buf);
						int pc=(int)((rle[0]&0x7f)+1);
						int color=0;
						color=convert(buf[0])*65536+convert(buf[0])*256+convert(buf[0]);
						for(int i=0;i<pc;++i)
						{
							int ux=x;
							if((specs&0x10)!=0)
								ux=width-1-x;
							bi.setRGB(ux,uy,color);
							++x;
							if(x==width)
							{
								x=0;
								++y;
								uy=y;
								if((specs&0x20)==0)
									uy=height-1-y;
							}
							++currentpixel;
						}
					}
					//raw packet
					else
					{
						int pc=(int)((rle[0])+1);
						byte[] raw=new byte[byteperpixel*pc];
						is.read(raw);
						for(int i=0;i<pc;++i)
						{
							int ux=x;
							if((specs&0x10)!=0)
								ux=width-1-x;
							int color=0;
							color=convert(raw[0+i*byteperpixel])*65536+convert(raw[0+i*byteperpixel])*256+convert(raw[0+i*byteperpixel]);
							bi.setRGB(ux,uy,color);
							++x;
							if(x==width)
							{
								x=0;
								++y;
								uy=y;
								if((specs&0x20)==0)
									uy=height-1-y;
							}
							++currentpixel;
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
	private int convert(byte arg)
	{
		int latch=arg;
		if(latch<0)
			latch+=256;
		return latch;
	}
	private void readExtent(javax.imageio.stream.ImageInputStream is)throws java.io.IOException
	{
		byte[] buf=new byte[colormaplength+idlength];
		is.read(buf);
	}
	private void readHeader(javax.imageio.stream.ImageInputStream is)throws java.io.IOException
	{
		byte[] buf=new byte[10];
		boolean advance=true;
		//length of image id field
		advance=is.read(buf,0,1)==1;
		if(advance)
		{
			idlength=convert(buf[0]);
		}
		//image type
		if(advance)
			advance=is.read(buf,0,2)==2;
		if(advance)
		{
			tgatype=buf[1];
			//For illustration purposes: we do only support a subset of possible tga image types here!
			if(((tgatype!=TRUE_COLOR)&&(tgatype!=BLACK_AND_WHITE))&&((tgatype!=RLE_TRUE_COLOR)&&(tgatype!=RLE_BLACK_AND_WHITE)))
				tgatype=NO_TGA;
		}
		//colormap specs
		if(advance)
			advance=is.read(buf,0,5)==5;
		if(advance)
		{
			colormaplength=convert(buf[3])*256+convert(buf[2]);
		}
		//image specs
		if(advance)
			advance=is.read(buf,0,10)==10;
		if(advance)
		{
			//origins here are omitted!
			//image width
			width=convert(buf[5])*256+convert(buf[4]);
			//image height
			height=convert(buf[7])*256+convert(buf[6]);
			//image depth
			depth=buf[8];
			//specs
			specs=buf[9];
		}
	}
}