import java.util.ArrayList;
import java.util.List;

abstract class Shape {
    int x0, y0, x1, y1; // (x0, y0) lower left, (x1, y1) upper right
    boolean shapeInvariantOK() {
        assert x0 <= x1;
        assert y0 <= y1;
        assert x0 >= 0;
        assert y0 >= 0;
        return true;
    }
    Shape(int x0, int y0, int x1, int y1) {
        this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1= y1;
        assert shapeInvariantOK();
    }
    void move(int dx, int dy) {
    	assert shapeInvariantOK();
        x0 += dx; y0 += dy; x1 += dx; y1 += dy;
        assert shapeInvariantOK();
    }
    Rectangle boundingBox () {
        return new Rectangle(x0, y0, x1 - x0, y1 - y0);
    }
}
class Line extends Shape {
    int x, y, u, v; // from (x, y) to (u, v)
    boolean lineInvariantOK() {
        assert shapeInvariantOK();
        assert x != u;
        assert y != v;
        assert x0 == Math.min(x, u);
        assert y0 == Math.min(y, v);
        assert x1 == Math.max(x, u);
        assert y1 == Math.max(y, v);
        return true;
    }
    Line(int x, int y, int u, int v) {
        super(x < u ? x : u, y < v ? y : v,
              x < u ? u : x, y < v ? v : y);
        this.x = x; this.y = y; this.u = u; this.v = v;
        assert lineInvariantOK();
    }
    void move(int dx, int dy) {
    	assert lineInvariantOK();
        super.move(dx, dy);
        x += dx; y += dy; u += dx; v += dy;
        assert lineInvariantOK();
    }
}
class Rectangle extends Shape {
    int x, y, w, h;
    boolean rectangleInvariantOK() {
        assert shapeInvariantOK();
        assert 0 < w;
        assert 0 < h;
        assert x0 == x;
        assert y0 == y;
        assert x1 == x + w;
        assert y1 == y + h;
        return true;
    }
    Rectangle(int x, int y, int w, int h) {
        super(x, y, x + w, y + h);
        this.x = x; this.y = y; this.w = w; this.h = h;
        assert rectangleInvariantOK();
    }
    void move(int dx, int dy) {
    	assert rectangleInvariantOK();
        super.move(dx, dy); x += dx; y += dy;
        assert rectangleInvariantOK();
    }
}
class Group extends Shape {
    List<Shape> elts = new ArrayList<Shape>();
    boolean groupInvariantOK() {
    	Rectangle tbb = this.boundingBox();
    	assert elts.size() > 0;
        for (Shape s : elts) {
        	assert s != null;
        	assert s.shapeInvariantOK();
        	Rectangle bb = s.boundingBox();
        	assert bb.x0 >= tbb.x0;
        	assert bb.y0 >= tbb.y0;
        }
        return true;
    }
    Group(Shape s) {
        super(s.x0, s.y0, s.x1, s.y1);
        elts.add(s);
        assert groupInvariantOK();
    }
    void move(int dx, int dy) {
    	assert groupInvariantOK();
        super.move(dx, dy);
        for (Shape s: elts) s.move(dx, dy);
        assert groupInvariantOK();
    }
    void add(Shape s) {
    	assert groupInvariantOK();
        elts.add(s);
        if (s.x0 < x0) x0 = s.x0;
        if (s.y0 < y0) y0 = s.y0;
        if (x1 < s.x1) x1 = s.x1;
        if (y1 < s.y1) y1 = s.y1;
        assert groupInvariantOK();
    }
}
class TestShapes {
    public static void main(String[] args) {
        Shape r = new Rectangle(1, 1, 5, 5);
        Shape l = new Line(0, 0, 4, 4);
        Group g = new Group(r);
        g.add(l);
        Rectangle bb = g.boundingBox();
        System.out.println(g.x0);
        System.out.println(g.x1);
        System.out.println(g.y0);
        System.out.println(g.y1);
        g.move(10, 10);
        System.out.println(g.x0);
        System.out.println(g.x1);
        System.out.println(g.y0);
        System.out.println(g.y1);
        //r.move(3, 3);
        assert g.groupInvariantOK();
    }
}