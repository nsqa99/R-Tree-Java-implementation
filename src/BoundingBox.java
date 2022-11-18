public class BoundingBox {
  private Point bottomLeftPoint;
  private Point topRightPoint;

  public BoundingBox(Point bottomLeftPoint, Point topRightPoint) {
    this.bottomLeftPoint = bottomLeftPoint;
    this.topRightPoint = topRightPoint;
  }

  public Point getBottomLeftPoint() {
    return bottomLeftPoint;
  }

  public Point getTopRightPoint() {
    return topRightPoint;
  }

  public boolean overlaps(BoundingBox other) {
    double minX = bottomLeftPoint.getLng();
    double minY = bottomLeftPoint.getLat();

    double maxX = topRightPoint.getLng();
    double maxY = topRightPoint.getLat();

    double otherMinX = other.getBottomLeftPoint().getLng();
    double otherMinY = other.getBottomLeftPoint().getLat();

    double otherMaxX = other.getTopRightPoint().getLng();
    double otherMaxY = other.getTopRightPoint().getLat();

    if(minX > otherMaxX) return false;

    if(maxX < otherMinX) return false;

    if(minY > otherMaxY) return false;

    if(maxY < otherMinY) return false;

    return true;
  }

  public boolean contains(BoundingBox other) {
    double minX = bottomLeftPoint.getLng();
    double minY = bottomLeftPoint.getLat();

    double maxX = topRightPoint.getLng();
    double maxY = topRightPoint.getLat();

    double otherMinX = other.getBottomLeftPoint().getLng();
    double otherMinY = other.getBottomLeftPoint().getLat();

    double otherMaxX = other.getTopRightPoint().getLng();
    double otherMaxY = other.getTopRightPoint().getLat();

    return minX <= otherMinX && maxX >= otherMaxX && minY <= otherMinY && maxY >= otherMaxY;
  }
}
