public class BoundingBox {
  private Point bottomLeftPoint;
  private Point topRightPoint;
  private double minX, minY, maxX, maxY;

  public BoundingBox(Point bottomLeftPoint, Point topRightPoint) {
    this.bottomLeftPoint = bottomLeftPoint;
    this.topRightPoint = topRightPoint;

    minX = bottomLeftPoint.getLng();
    minY = bottomLeftPoint.getLat();

    maxX = topRightPoint.getLng();
    maxY = topRightPoint.getLat();
  }

  public Point getBottomLeftPoint() {
    return bottomLeftPoint;
  }

  public Point getTopRightPoint() {
    return topRightPoint;
  }

  public boolean overlaps(BoundingBox other) {
    if (minX > other.getMaxX()) return false;

    if (maxX < other.getMinX()) return false;

    if (minY > other.getMaxY()) return false;

    if (maxY < other.getMinY()) return false;

    return true;
  }

  public boolean contains(BoundingBox other) {
    return minX <= other.getMinX() && maxX >= other.getMaxX() && minY <= other.getMinY()
        && maxY >= other.getMaxY();
  }

  public double calculateEnlargement(BoundingBox other) {
    double currentArea = getArea();
    // build new bounding box
    double newMinX = Math.min(minX, other.getMinX());
    double newMinY = Math.min(minY, other.getMinY());
    double newMaxX = Math.max(maxX, other.getMaxX());
    double newMaxY = Math.max(maxY, other.getMaxY());

    double newArea = (newMaxX - newMinX) * (newMaxY - newMinY);

    return newArea - currentArea;
  }

  public double getArea() {
    return (maxX - minX) * (maxY - minY);
  }

  public double getMinX() {
    return minX;
  }

  public double getMinY() {
    return minY;
  }

  public double getMaxX() {
    return maxX;
  }

  public double getMaxY() {
    return maxY;
  }
}
