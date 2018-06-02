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
public class PAMReaderSpi extends javax.imageio.spi.ImageReaderSpi
{
	public PAMReaderSpi()
	{
		super("NetSys.IT",
			"0.0.1",
			new java.lang.String[]{"pam"},
			new java.lang.String[]{".pam"},
			new java.lang.String[]{"image/x-pbm", "image/x-portable-bitmap"},
			"de.elbosso.util.imageio.pam.PAMReader",
			new java.lang.Class[]{javax.imageio.stream.ImageInputStream.class},
			new java.lang.String[]{"de.elbosso.util.imageio.pam.PAMWriterSpi"},
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
		return "PAM";
	}
	public boolean canDecodeInput(java.lang.Object source)
	{

		boolean rv=false;
		if(source instanceof javax.imageio.stream.ImageInputStream)
		{
			javax.imageio.stream.ImageInputStream is=(javax.imageio.stream.ImageInputStream)source;
			is.mark();
			byte[] header=new byte[2];
			try{
			if(is.read(header)==2)
			{
				if((header[0]==0x50)&&(((header[1]==0x37)||(header[1]==0x38))||(header[1]==0x39)))//P7 it is
				{
						rv=true;
				}
			}			
			is.reset();
			}catch(java.io.IOException exp){}
		}

		return rv;
	}
	public javax.imageio.ImageReader createReaderInstance(java.lang.Object extension) 
	{
		return new de.elbosso.util.imageio.pam.PAMReader(this);
	}
}
