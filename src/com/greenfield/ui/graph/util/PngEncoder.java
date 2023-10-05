// Source File Name:   PngEncoder.java

package com.greenfield.ui.graph.util;

import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PngEncoder
{

    public PngEncoder()
    {
        this(null, false, 0, 0);
    }

    public PngEncoder(Image image1)
    {
        this(image1, false, 0, 0);
    }

    public PngEncoder(Image image1, boolean flag)
    {
        this(image1, flag, 0, 0);
    }

    public PngEncoder(Image image1, boolean flag, int i)
    {
        this(image1, flag, i, 0);
    }

    public PngEncoder(Image image1, boolean flag, int i, int j)
    {
        crc = new CRC32();
        image = image1;
        encodeAlpha = flag;
        setFilter(i);
        if(j >= 0 && j <= 9)
            compressionLevel = j;
    }

    public void setImage(Image image1)
    {
        image = image1;
        pngBytes = null;
    }

    public byte[] pngEncode(boolean flag)
    {
        byte abyte0[] = {
            -119, 80, 78, 71, 13, 10, 26, 10
        };
        if(image == null)
            return null;
        width = image.getWidth(null);
        height = image.getHeight(null);
        // image = image;
        pngBytes = new byte[(width + 1) * height * 3 + 200];
        maxPos = 0;
        bytePos = writeBytes(abyte0, 0);
        hdrPos = bytePos;
        writeHeader();
        dataPos = bytePos;
        if(writeImageData())
        {
            writeEnd();
            pngBytes = resizeByteArray(pngBytes, maxPos);
        } else
        {
            pngBytes = null;
        }
        return pngBytes;
    }

    public byte[] pngEncode()
    {
        return pngEncode(encodeAlpha);
    }

    public void setEncodeAlpha(boolean flag)
    {
        encodeAlpha = flag;
    }

    public boolean getEncodeAlpha()
    {
        return encodeAlpha;
    }

    public void setFilter(int i)
    {
        filter = 0;
        if(i <= 2)
            filter = i;
    }

    public int getFilter()
    {
        return filter;
    }

    public void setCompressionLevel(int i)
    {
        if(i >= 0 && i <= 9)
            compressionLevel = i;
    }

    public int getCompressionLevel()
    {
        return compressionLevel;
    }

    protected byte[] resizeByteArray(byte abyte0[], int i)
    {
        byte abyte1[] = new byte[i];
        int j = abyte0.length;
        System.arraycopy(abyte0, 0, abyte1, 0, Math.min(j, i));
        return abyte1;
    }

    protected int writeBytes(byte abyte0[], int i)
    {
        maxPos = Math.max(maxPos, i + abyte0.length);
        if(abyte0.length + i > pngBytes.length)
            pngBytes = resizeByteArray(pngBytes, pngBytes.length + Math.max(1000, abyte0.length));
        System.arraycopy(abyte0, 0, pngBytes, i, abyte0.length);
        return i + abyte0.length;
    }

    protected int writeBytes(byte abyte0[], int i, int j)
    {
        maxPos = Math.max(maxPos, j + i);
        if(i + j > pngBytes.length)
            pngBytes = resizeByteArray(pngBytes, pngBytes.length + Math.max(1000, i));
        System.arraycopy(abyte0, 0, pngBytes, j, i);
        return j + i;
    }

    protected int writeInt2(int i, int j)
    {
        byte abyte0[] = {
            (byte)(i >> 8 & 0xff), (byte)(i & 0xff)
        };
        return writeBytes(abyte0, j);
    }

    protected int writeInt4(int i, int j)
    {
        byte abyte0[] = {
            (byte)(i >> 24 & 0xff), (byte)(i >> 16 & 0xff), (byte)(i >> 8 & 0xff), (byte)(i & 0xff)
        };
        return writeBytes(abyte0, j);
    }

    protected int writeByte(int i, int j)
    {
        byte abyte0[] = {
            (byte)i
        };
        return writeBytes(abyte0, j);
    }

    protected int writeString(String s, int i)
    {
        return writeBytes(s.getBytes(), i);
    }

    protected void writeHeader()
    {
        int i = bytePos = writeInt4(13, bytePos);
        bytePos = writeString("IHDR", bytePos);
        width = image.getWidth(null);
        height = image.getHeight(null);
        bytePos = writeInt4(width, bytePos);
        bytePos = writeInt4(height, bytePos);
        bytePos = writeByte(8, bytePos);
        bytePos = writeByte(encodeAlpha ? 6 : 2, bytePos);
        bytePos = writeByte(0, bytePos);
        bytePos = writeByte(0, bytePos);
        bytePos = writeByte(0, bytePos);
        crc.reset();
        crc.update(pngBytes, i, bytePos - i);
        crcValue = crc.getValue();
        bytePos = writeInt4((int)crcValue, bytePos);
    }

    protected void filterSub(byte abyte0[], int i, int j)
    {
        int l = bytesPerPixel;
        int i1 = i + l;
        int j1 = j * bytesPerPixel;
        int k1 = l;
        int l1 = 0;
        for(int k = i1; k < i + j1; k++)
        {
            leftBytes[k1] = abyte0[k];
            abyte0[k] = (byte)((abyte0[k] - leftBytes[l1]) % 256);
            k1 = (k1 + 1) % 15;
            l1 = (l1 + 1) % 15;
        }

    }

    protected void filterUp(byte abyte0[], int i, int j)
    {
        int l = j * bytesPerPixel;
        for(int k = 0; k < l; k++)
        {
            byte byte0 = abyte0[i + k];
            abyte0[i + k] = (byte)((abyte0[i + k] - priorRow[k]) % 256);
            priorRow[k] = byte0;
        }

    }

    protected boolean writeImageData()
    {
        int i = height;
        int j = 0;
        bytesPerPixel = encodeAlpha ? 4 : 3;
        Deflater deflater = new Deflater(compressionLevel);
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1024);
        DeflaterOutputStream deflateroutputstream = new DeflaterOutputStream(bytearrayoutputstream, deflater);
        try
        {
            int k;
            for(; i > 0; i -= k)
            {
                k = Math.min(32767 / (width * (bytesPerPixel + 1)), i);
                int ai[] = new int[width * k];
                PixelGrabber pixelgrabber = new PixelGrabber(image, 0, j, width, k, ai, 0, width);
                try
                {
                    pixelgrabber.grabPixels();
                }
                catch(Exception exception)
                {
                    System.err.println("interrupted waiting for pixels!");
                    boolean flag3 = false;
                    return flag3;
                }
                if((pixelgrabber.getStatus() & 0x80) != 0)
                {
                    System.err.println("image fetch aborted or errored");
                    boolean flag1 = false;
                    return flag1;
                }
                byte abyte0[] = new byte[width * k * bytesPerPixel + k];
                if(filter == 1)
                    leftBytes = new byte[16];
                if(filter == 2)
                    priorRow = new byte[width * bytesPerPixel];
                int l = 0;
                int i1 = 1;
                for(int k1 = 0; k1 < width * k; k1++)
                {
                    if(k1 % width == 0)
                    {
                        abyte0[l++] = (byte)filter;
                        i1 = l;
                    }
                    abyte0[l++] = (byte)(ai[k1] >> 16 & 0xff);
                    abyte0[l++] = (byte)(ai[k1] >> 8 & 0xff);
                    abyte0[l++] = (byte)(ai[k1] & 0xff);
                    if(encodeAlpha)
                        abyte0[l++] = (byte)(ai[k1] >> 24 & 0xff);
                    if(k1 % width != width - 1 || filter == 0)
                        continue;
                    if(filter == 1)
                        filterSub(abyte0, i1, width);
                    if(filter == 2)
                        filterUp(abyte0, i1, width);
                }

                deflateroutputstream.write(abyte0, 0, l);
                j += k;
            }

            deflateroutputstream.close();
            byte abyte1[] = bytearrayoutputstream.toByteArray();
            int j1 = abyte1.length;
            crc.reset();
            bytePos = writeInt4(j1, bytePos);
            bytePos = writeString("IDAT", bytePos);
            crc.update("IDAT".getBytes());
            bytePos = writeBytes(abyte1, j1, bytePos);
            crc.update(abyte1, 0, j1);
            crcValue = crc.getValue();
            bytePos = writeInt4((int)crcValue, bytePos);
            deflater.finish();
            boolean flag = true;
            return flag;
        }
        catch(IOException ioexception)
        {
            System.err.println(ioexception.toString());
        }
        boolean flag2 = false;
        return flag2;
    }

    protected void writeEnd()
    {
        bytePos = writeInt4(0, bytePos);
        bytePos = writeString("IEND", bytePos);
        crc.reset();
        crc.update("IEND".getBytes());
        crcValue = crc.getValue();
        bytePos = writeInt4((int)crcValue, bytePos);
    }

    public static final boolean ENCODE_ALPHA = true;
    public static final boolean NO_ALPHA = false;
    public static final int FILTER_NONE = 0;
    public static final int FILTER_SUB = 1;
    public static final int FILTER_UP = 2;
    public static final int FILTER_LAST = 2;
    protected byte pngBytes[];
    protected byte priorRow[];
    protected byte leftBytes[];
    protected Image image;
    protected int width;
    protected int height;
    protected int bytePos;
    protected int maxPos;
    protected int hdrPos;
    protected int dataPos;
    protected int endPos;
    protected CRC32 crc;
    protected long crcValue;
    protected boolean encodeAlpha;
    protected int filter;
    protected int bytesPerPixel;
    protected int compressionLevel;
}
