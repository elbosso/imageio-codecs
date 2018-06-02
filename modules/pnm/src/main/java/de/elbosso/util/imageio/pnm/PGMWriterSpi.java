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
import java.util.Locale;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;

/**
 *
 * @author elbosso
 */
public class PGMWriterSpi extends javax.imageio.spi.ImageWriterSpi
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(PGMWriterSpi.class);
	public PGMWriterSpi()
	{
		super("NetSys.IT",
			"0.1.0",
			new java.lang.String[]{"pgm"},
			new java.lang.String[]{".pgm"},
			new java.lang.String[]{"image/x-pbm", "image/x-portable-bitmap"},
			"de.elbosso.util.imageio.pnm.PGMWriter",
			new java.lang.Class[]{javax.imageio.stream.ImageOutputStream.class},
			new java.lang.String[]{"de.elbosso.util.imageio.pnm.PNMReaderSpi"},
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
	
	@Override
	public boolean canEncodeImage(ImageTypeSpecifier arg0)
	{
		boolean rv=(((arg0.getBufferedImageType()==java.awt.image.BufferedImage.TYPE_INT_ARGB)||(arg0.getBufferedImageType()==java.awt.image.BufferedImage.TYPE_INT_RGB))||
				((arg0.getBufferedImageType()==java.awt.image.BufferedImage.TYPE_3BYTE_BGR)||(arg0.getBufferedImageType()==java.awt.image.BufferedImage.TYPE_BYTE_INDEXED)));
		if(rv==false)
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(arg0.getBufferedImageType());
		return rv;
	}

	@Override
	public ImageWriter createWriterInstance(Object arg0) throws IOException
	{
		return new de.elbosso.util.imageio.pnm.PGMWriter(this);
	}

	@Override
	public String getDescription(Locale arg0)
	{
		return "PGM";
	}

}
