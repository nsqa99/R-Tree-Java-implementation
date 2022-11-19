import java.util.List;
import java.util.stream.Collectors;

public class Linestring extends BoundedObject {
  private List<Point> points;

  public Linestring(List<Point> points) {
    this.points = points;
  }

  public List<Point> getPoints() {
    return points;
  }

  @Override
  BoundingBox calculateBBox() {
    List<Double> xAxisValues = points.stream().sorted((p1, p2) -> {
      if (p1.getLng() == p2.getLng()) {
        return Double.compare(p1.getLat(), p2.getLat());
      }

      return Double.compare(p1.getLng(), p2.getLng());
    }).map(Point::getLng).collect(Collectors.toList());

    List<Double> yAxisValues = points.stream().sorted((p1, p2) -> {
          if (p1.getLat() == p2.getLat()) {
            return Double.compare(p1.getLng(), p2.getLng());
          }

          return Double.compare(p1.getLat(), p2.getLat());
        })
        .map(Point::getLat).collect(Collectors.toList());

    double xMin = xAxisValues.get(0);
    double xMax = xAxisValues.get(xAxisValues.size() - 1);

    double yMin = yAxisValues.get(0);
    double yMax = yAxisValues.get(yAxisValues.size() - 1);

    this.bbox = new BoundingBox(new Point(yMin, xMin), new Point(yMax, xMax));

    return this.bbox;
  }
}
