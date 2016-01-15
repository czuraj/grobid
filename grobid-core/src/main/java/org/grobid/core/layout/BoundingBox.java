package org.grobid.core.layout;

/**
 * Created by zholudev on 18/08/15.
 * Represents a bounding box (e.g. for a reference marker in PDF)
 */
public class BoundingBox {
    private int page;
    private double x, y, width, height;

    private double x2, y2;

    private BoundingBox(int page, double x, double y, double width, double height) {
        this.page = page;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    public static BoundingBox fromTwoPoints(int page, double x1, double y1, double x2, double y2) {
        if (x1 > x2 || y1 > y2) {
            throw new IllegalArgumentException("Invalid points provided: (" + x1 + ";" + y1 + ")-(" + x2 + ";" + y2 + ")");
        }
        return new BoundingBox(page, x1, y1, x2 - x1, y2 - y1);
    }

    public static BoundingBox fromString(String coords) {
        String[] split = coords.split(",");

        Long pageNum = Long.valueOf(split[0], 10);

        float x = Float.parseFloat(split[1]);
        float y = Float.parseFloat(split[2]);
        float w = Float.parseFloat(split[3]);
        float h = Float.parseFloat(split[4]);

        return new BoundingBox(pageNum.intValue(), x, y, w, h);
    }

    public static BoundingBox fromPointAndDimensions(int page, double x, double y, double width, double height) {
        return new BoundingBox(page, x, y, width, height);
    }

    public static BoundingBox fromLayoutToken(LayoutToken tok) {
        return BoundingBox.fromPointAndDimensions(tok.getPage(), tok.getX(), tok.getY(), tok.getWidth(), tok.getHeight());
    }

    public boolean intersect(BoundingBox b) {
        double ax1 = this.x;
        double ax2 = this.x2;
        double ay1 = this.y;
        double ay2 = this.y2;

        double bx1 = b.x;
        double bx2 = b.x2;
        double by1 = b.y;
        double by2 = b.y2;


        if (ax2 < bx1) return false;
        else if (ax1 > bx2) return false;
        else if (ay2 < by1) return false;
        else if (ay1 > by2) return false;
        else
            return true;

    }

    public int getPage() {
        return page;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public BoundingBox boundBox(BoundingBox o) {
        if (this.page != o.page) {
            throw new IllegalStateException("Cannot compute a bounding box for different pages");
        }
        return fromTwoPoints(o.page, Math.min(this.x, o.x), Math.min(this.y, o.y), Math.max(this.x2, o.x2), Math.max(this.y2, o.y2));
    }

    @Override
    public String toString() {
        return String.format("%d,%.2f,%.2f,%.2f,%.2f", page, x, y, width, height);
    }
}
