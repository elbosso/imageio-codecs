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

package de.elbosso.util.imageio.tga;

import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOMetadata;

/**
 *
 * @author elbosso
 */
public class TGAWriter extends javax.imageio.ImageWriter
{
	public TGAWriter(javax.imageio.spi.ImageWriterSpi originatingProvider)
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
		if(outputref!=null)
		{
			javax.imageio.stream.ImageOutputStream os=(javax.imageio.stream.ImageOutputStream)outputref;
			
		}
	}
}
