import java.util.ArrayList;
import java.util.List;

abstract class Shape
{
	 String name;
    abstract double getArea();
    abstract double getPerimeter();
}
interface Drawable
{
    void draw();
}
class Rectangle extends Shape implements Drawable{
    double width;
    double height;
    Rectangle(double width, double height)
    {
        this.name="Rectangle";
        this.width = width;
        this.height = height;
    }
    @Override
    double getArea()
    {
        return width*height;
    }
    @Override
    double getPerimeter()
    {
        return 2*width+2*height;
    }
    @Override
    public void draw()
    {
        System.out.println("Drawing a rectangle");
    }
}
class Circle extends Shape implements Drawable {
    double radius;
    Circle(double radius)
    {
        this.name="Circle";
        this.radius = radius;
    }
    @Override
    double getArea()
    {
        return Math.PI*radius*radius;
    }
    @Override
    double getPerimeter()
    {
        return 2*Math.PI*radius;
    }
    @Override
    public void draw()
    {
        System.out.println("Drawing a circle");
    }
}
public class Ex1 {
    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<Shape>();
        shapes.add(new Rectangle(10,20));
        shapes.add(new Circle(20));
        for (int i=0;i<shapes.size();i++) {
            System.out.printf("Name: %s, Area: %.2f, Perimeter: %.2f ",shapes.get(i).name,shapes.get(i).getArea(),shapes.get(i).getPerimeter());
            Drawable drawable = (Drawable) shapes.get(i);
            drawable.draw();
        }
    }
}
