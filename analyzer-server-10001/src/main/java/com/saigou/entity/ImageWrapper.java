package com.saigou.entity;

import com.google.protobuf.ByteString;
import lombok.Data;

@Data
public class ImageWrapper implements Comparable<ImageWrapper> {
    public final ByteString imageData;
    public final long timestamp;
    public final int imageWidth;
    public final int imageHeight;
    
    public ImageWrapper(ByteString imageData, long timestamp, int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageData = imageData;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(ImageWrapper o) {
        return Long.compare(this.timestamp, o.timestamp);
    }
}