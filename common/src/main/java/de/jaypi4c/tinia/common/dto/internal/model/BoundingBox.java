package de.jaypi4c.tinia.common.dto.internal.model;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public record BoundingBox(double x, double y, double width, double height) implements Serializable {

    public static BoundingBox fromRectangle2D(Rectangle2D rectangle2D) {
        return new BoundingBox(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    public Rectangle2D toRectangle2D() {
        return new Rectangle2D.Double(x, y, width, height);
    }
}
