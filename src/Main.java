import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    Point p1 = new Point( 20.968605388096336, 105.75287734103347);
    Point p2 = new Point( 20.968709896123826, 105.75348357309747);
    Point p3 = new Point( 20.968640224114168,105.75348357309747);
    Point p4 = new Point( 20.968544425046574, 105.75360481950997);

    Linestring linestring = new Linestring(List.of(p1, p2, p3, p4));
    System.out.println(linestring.getBBox().getBottomLeftPoint());
    System.out.println(linestring.getBBox().getTopRightPoint());
  }
}