package com.jaypi4c;

import java.awt.geom.Line2D;

record Chunk(int minX, int minY, int maxX, int maxY) {
public boolean isWithin(Line2D.Float line){
    return line.intersectsLine(minX, minY, maxX, maxY);
}
}