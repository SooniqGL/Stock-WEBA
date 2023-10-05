/*
 * Created on April 14, 2006
 *
 */
package com.greenfield.ui.graph;

//import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
//import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.greenfield.ui.graph.util.GifEncoder;
//import com.greenfield.ui.graph.util.ImageEncoder;
import com.greenfield.ui.graph.util.PngEncoder;

/**
 * @author UZHANQX
 *
 * This is the base class for all draws.
 * Only paint() needs to be implemented.
 * 
 * 1. initialize the size or other information;
 * 2. build();
 * 3. saveas() or getImage() to return.
 */
public abstract class BaseGraphics {
	
	/** used to draw, visible to children */
	protected Frame frame;
	
	/** used to draw, visible to children */
	protected Graphics2D pad;
	
	protected int imageWidth;
	
	protected int imageHeight;
	
	/** local variables */
	private Image image;


	/**
	 * Called first to lay out the image and frame.
	 * @param img_width
	 * @param img_height
	 */
	public void build(int img_width, int img_height) throws Exception {
		try {
			frame = new Frame();
			frame.addNotify();
			image = frame.createImage(img_width, img_height);
			pad = (Graphics2D) image.getGraphics();
			imageWidth = img_width;
			imageHeight = img_height;
			
			
			// add thickness ....
			//float strokeThickness = 1.0f;
			
			// A solid stroke
			//BasicStroke stroke = new BasicStroke(strokeThickness);
			//g.setStroke(stroke);
			// Draw shapes...; see e586 Drawing Simple Shapes

			// smooth out the jaggies
			pad.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

			// A dashed stroke
			/*
			float miterLimit = 10f;
			float[] dashPattern = {10f};
			float dashPhase = 5f;
			stroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
			g.setStroke(stroke); */
			
			paint();
		} catch(Exception exception) {
			System.out.println("--> Error 1 : ".concat(String.valueOf(String.valueOf(exception))));
			throw exception;
		}
	}

	public void clear() {
		if(frame != null) {
			frame.removeNotify();
		}
			
		if(pad != null) {
			pad.dispose();
		}	
	}

	/**
	 * Sub-class should implement this one.
	 * @throws Exception
	 */
	public abstract void paint() throws Exception;

	/**
	 * Called by getImage().
	 * @param outputstream
	 * @param i
	 * @return
	 */
	private boolean encode(OutputStream outputstream, int i) {
		try {
			if(i == 0) {
				GifEncoder gifencoder = new GifEncoder(image, outputstream);
				gifencoder.transparentRgbForced = (new Color(-1)).getRGB() & 0xffffff;
				gifencoder.encode();
			} else{
				PngEncoder pngencoder = new PngEncoder(image);
				pngencoder.setCompressionLevel(9);
				
				byte abyte0[] = pngencoder.pngEncode();
				if(abyte0 == null) {
					System.out.println("Null image");
				} else {
					outputstream.write(abyte0);
				}
					
				outputstream.flush();
			}
		} catch(Exception exception) {
			System.out.println("--> Error 3 : " + exception); // + "image=" + image + " out=" + outputstream);
			boolean flag = false;
			return flag;
		}
		return true;
	} 

	/**
	 * Save the image to a file
	 * @param fileName
	 */
	public void saveAs(String fileName) {
		try {
			FileOutputStream fileoutputstream = new FileOutputStream(fileName);
			if(fileName.toLowerCase().indexOf(".png") > 0) {
				fileoutputstream.write(getImage(1));
			} else {
				fileoutputstream.write(getImage(0));
			}
				
			fileoutputstream.flush();
			fileoutputstream.close();
		} catch(Exception exception) {
			System.out.println("--> Error 4 : ".concat(String.valueOf(String.valueOf(exception))));
		}
	}
	
	public void saveAsJpeg(String fileName) {
		try {
			FileOutputStream fileoutputstream = new FileOutputStream(fileName);
			fileoutputstream.write(getJPEGImage());
				
			fileoutputstream.flush();
			fileoutputstream.close();
		} catch(Exception exception) {
			System.out.println("--> Error 4 : ".concat(String.valueOf(String.valueOf(exception))));
		}
	}

	/**
	 * Get the image out directly.
	 * getImage(0) is for .gif file.
	 * Problem: This gif encode may error out when
	 * the color is more than 256.  So not a reliable source.
	 * @param i
	 * @return
	 */
	public byte[] getImage(int i) {
		byte abyte0[] = null;
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(0x186a0);
			if(!encode(bytearrayoutputstream, i)) {
				System.out.println("--> Error encode");
			}
				
			abyte0 = bytearrayoutputstream.toByteArray();
			bytearrayoutputstream.flush();
			bytearrayoutputstream.close();
		} catch(Exception exception) {
			System.out.println("--> Error 5 : ".concat(String.valueOf(String.valueOf(exception))));
		}
		
		return abyte0;
	}
	
	/**
	 * quality: 0 to 1, 1 is the best;
	 * default: 0.75
	 * @param quality
	 * @return
	 */
	public byte[] getJPEGImage() {
		float MY_QUALITY = 0.9f;
		byte abyte0[] = null;
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(0x186a0);
			
			EncodeJPEG.encode(byteStream, image, MY_QUALITY, imageWidth, imageHeight);
			abyte0 = byteStream.toByteArray();
			byteStream.flush();
			byteStream.close();
		} catch(Exception exception) {
			System.out.println("--> Error 5 : ".concat(String.valueOf(String.valueOf(exception))));
		}

		return abyte0;
	}

	/**
	 * Try to return Image.
	 */ 
	public Image getImage() {
		return image;
	}
	
    
}

