package com.saigou.entity;

import com.google.protobuf.ByteString;

public class ImageWrapper implements Comparable<ImageWrapper> {
    public final ByteString imageData;
    public final long timestamp;
    
    public ImageWrapper(ByteString imageData, long timestamp) {
        this.imageData = imageData;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(ImageWrapper o) {
        return Long.compare(this.timestamp, o.timestamp);
    }
}