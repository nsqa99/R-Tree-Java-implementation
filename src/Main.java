import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<Point> points = new ArrayList<>();
    Point p1 = new Point( 20.968605388096336, 105.75287734103347);
    Point p2 = new Point( 20.968709896123826, 105.75348357309747);
    Point p3 = new Point( 20.968640224114168,105.75348357309747);
    Point p4 = new Point( 20.968544425046574, 105.75360481950997);

    points.add(p1);
    points.add(p2);
    points.add(p3);
    points.add(p4);

    Linestring linestring = new Linestring(points);
    System.out.println(linestring.getBBox().getBottomLeftPoint());
    System.out.println(linestring.getBBox().getTopRightPoint());
  }
}