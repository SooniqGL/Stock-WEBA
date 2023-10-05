// Source File Name:   GifEncoder.java

package com.greenfield.ui.graph.util;


class GifEncoderHashitem
{

    public GifEncoderHashitem(int i, int j, int k, boolean flag)
    {
        rgb = i;
        count = j;
        index = k;
        isTransparent = flag;
    }

    public int rgb;
    public int count;
    public int index;
    public boolean isTransparent;
}
