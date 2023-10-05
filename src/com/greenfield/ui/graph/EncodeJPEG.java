/*
 * Created on Jun 17, 2006
 *
 * Encode JPEG file
 */
package com.greenfield.ui.graph;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Element;


//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author qin Create JPEG from com.sun package
 */
public class EncodeJPEG {

	// Encodes the given image at the given
	// quality to the output stream.
	public static void encode_old(OutputStream outputStream, Image outputImage,
			float outputQuality, int outputWidth, int outputHeight)
			throws java.io.IOException {

		// Get a buffered image from the image.
		BufferedImage bi = new BufferedImage(outputWidth, outputHeight,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D biContext = bi.createGraphics();
		biContext.drawImage(outputImage, 0, 0, null);

		// Additional drawing code, such as
		// watermarks or logos can be placed here.

		// com.sun.image.codec.jpeg package
		// is included in Sun and IBM sdk 1.3.
		/*
		 * JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( outputStream
		 * );
		 * 
		 * // The default quality is 0.75. JPEGEncodeParam jep =
		 * JPEGCodec.getDefaultJPEGEncodeParam( bi );
		 * 
		 * jep.setQuality( outputQuality, true ); encoder.encode( bi, jep );
		 */
		outputStream.flush();
	}

	public static void encode(OutputStream stream, Image outputImage,
			float quality, int outputWidth, int outputHeight)
			throws IOException {
		if ((quality < 0) || (quality > 1)) {
			quality = 0.9f; // make it default
			// throw new IllegalArgumentException("Quality out of bounds!");
		}

		// Get a buffered image from the image.
		BufferedImage bi = new BufferedImage(outputWidth, outputHeight,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D biContext = bi.createGraphics();
		biContext.drawImage(outputImage, 0, 0, null);

		ImageWriter writer = null;
		Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
		if (iter.hasNext()) {
			writer = (ImageWriter) iter.next();
		}
		ImageOutputStream ios = ImageIO.createImageOutputStream(stream);
		writer.setOutput(ios);
		ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
		iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwparam.setCompressionQuality(quality);
		
		// metadata format names are: javax_imageio_jpeg_image_1.0 or javax_imageio_1.0
		// change default dpi 96 to 300
		// IIOMetadata data = writer.getDefaultStreamMetadata(iwparam); //.getDefaultImageMetadata(new ImageTypeSpecifier(image), writeParams);
		ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
		IIOMetadata data = writer.getDefaultImageMetadata(typeSpecifier, iwparam);
		Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");
		Element jfif = (Element)tree.getElementsByTagName("app0JFIF").item(0);
        jfif.setAttribute("Xdensity", "300");
        jfif.setAttribute("Ydensity", "300");
        jfif.setAttribute("resUnits", "1"); // density is dots per inch
        data.setFromTree("javax_imageio_jpeg_image_1.0", tree);

		writer.write(data, new IIOImage(bi, null, data), iwparam);
		ios.flush();
		writer.dispose();
		ios.close();
	}

	/*
	 * public static void saveAsJPEG(String jpgFlag, BufferedImage
	 * image_to_save, float JPEGcompression, FileOutputStream fos) throws
	 * IOException {
	 * 
	 * //useful documentation at
	 * http://docs.oracle.com/javase/7/docs/api/javax/imageio
	 * /metadata/doc-files/jpeg_metadata.html //useful example program at
	 * http://johnbokma.com/java/obtaining-image-metadata.html to output JPEG
	 * data
	 * 
	 * //old jpeg class //com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder
	 * = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
	 * //com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam =
	 * jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);
	 * 
	 * // Image writer JPEGImageWriter imageWriter = (JPEGImageWriter)
	 * ImageIO.getImageWritersBySuffix(“jpeg”).next(); ImageOutputStream ios =
	 * ImageIO.createImageOutputStream(fos); imageWriter.setOutput(ios);
	 * 
	 * //and metadata IIOMetadata imageMetaData =
	 * imageWriter.getDefaultImageMetadata(new
	 * ImageTypeSpecifier(image_to_save), null);
	 * 
	 * if (jpgFlag != null){
	 * 
	 * int dpi = 96;
	 * 
	 * try { dpi = Integer.parseInt(jpgFlag); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * //old metadata
	 * //jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam
	 * .DENSITY_UNIT_DOTS_INCH); //jpegEncodeParam.setXDensity(dpi);
	 * //jpegEncodeParam.setYDensity(dpi);
	 * 
	 * //new metadata Element tree = (Element)
	 * imageMetaData.getAsTree(“javax_imageio_jpeg_image_1.0?); Element jfif =
	 * (Element)tree.getElementsByTagName(“app0JFIF”).item(0);
	 * jfif.setAttribute(“Xdensity”, Integer.toString(dpi));
	 * jfif.setAttribute(“Ydensity”, Integer.toString(dpi));
	 * 
	 * }
	 * 
	 * if(JPEGcompression>=0 && JPEGcompression<=1f){
	 * 
	 * //old compression //jpegEncodeParam.setQuality(JPEGcompression,false);
	 * 
	 * // new Compression JPEGImageWriteParam jpegParams = (JPEGImageWriteParam)
	 * imageWriter.getDefaultWriteParam();
	 * jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
	 * jpegParams.setCompressionQuality(JPEGcompression);
	 * 
	 * }
	 * 
	 * //old write and clean //jpegEncoder.encode(image_to_save,
	 * jpegEncodeParam);
	 * 
	 * //new Write and clean up imageWriter.write(imageMetaData, new
	 * IIOImage(image_to_save, null, null), null); ios.close();
	 * imageWriter.dispose();
	 * 
	 * }
	 */
}

// encodeImage