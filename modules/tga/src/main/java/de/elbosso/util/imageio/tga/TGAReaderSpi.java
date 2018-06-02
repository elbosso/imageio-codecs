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
public class TGAReaderSpi extends javax.imageio.spi.ImageReaderSpi
{
	public TGAReaderSpi()
	{
		super("NetSys.IT",
			"0.1.0",
			new java.lang.String[]{"tga","tpic"},
			new java.lang.String[]{".tga",".tpic"},
			new java.lang.String[]{"image/x-tga", "image/x-targa"},
			"de.elbosso.util.imageio.tga.TGAReader",
			new java.lang.Class[]{javax.imageio.stream.ImageInputStream.class},
			new java.lang.String[]{"de.elbosso.util.imageio.tga.TGAWriterSpi"},
			false,
			null,
			null,
			null,
			null,
			true,
			null,
			null,
			null,
			null);
	}
	public java.lang.String getDescription(java.util.Locale locale)
	{
		return "TGA";
	}
	public boolean canDecodeInput(java.lang.Object source)
	{
		boolean rv=false;
		if(source instanceof javax.imageio.stream.ImageInputStream)
		{
			javax.imageio.stream.ImageInputStream is=(javax.imageio.stream.ImageInputStream)source;
			is.mark();
			byte[] header=new byte[3];
			try{
			if(is.read(header)==3)
			{
				int tgatype=header[2];
				//For illustration purposes: we do only support a subset of possible tga image types here!
				rv=(((header[1]==0)||(header[1])==1)&&(((tgatype==de.elbosso.util.imageio.tga.TGAReader.TRUE_COLOR)||(tgatype==de.elbosso.util.imageio.tga.TGAReader.BLACK_AND_WHITE))||((tgatype==de.elbosso.util.imageio.tga.TGAReader.RLE_TRUE_COLOR)||(tgatype==de.elbosso.util.imageio.tga.TGAReader.RLE_BLACK_AND_WHITE))));
			}			
			is.reset();
			}catch(java.io.IOException exp){}
		}

		return rv;
	}
	public javax.imageio.ImageReader createReaderInstance(java.lang.Object extension) 
	{
		return new de.elbosso.util.imageio.tga.TGAReader(this);
	}
}
