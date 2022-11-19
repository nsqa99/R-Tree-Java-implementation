package com.anhnsq;

public class BoundingBox {
  private Point bottomLeftPoint;
  private Point topRightPoint;
  private double minX, minY, maxX, maxY;
  private Double area;
  private static final int EARTH_RADIUS = 6371; // Radius of the earth in km

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

    double newXAxisLength = getHaversineDistance(newMinY, newMinX, newMinY, newMaxX);
    double newYAxisLength = getHaversineDistance(newMinY, newMinX, newMaxY, newMinX);

    return newXAxisLength * newYAxisLength - currentArea;
  }

  public double getArea() {
    if (area == null) {
      calculateArea();
    }

    return area;
  }

  public void calculateArea() {
    double xAxisLength = getHaversineDistance(minY, minX, minY, maxX);
    double yAxisLength = getHaversineDistance(minY, minX, maxY, minX);

    area = xAxisLength * yAxisLength;
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

  private double getHaversineDistance(double lat1, double lng1, double lat2, double lng2) {
    double dLat = Math.toRadians(lat2 - lat1); // delta lat
    double dLng = Math.toRadians(lng2 - lng1); // delta lng
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2); // sin^2(deltaLat / 2) + cos(lat1) * cos(lat2) * sin^2(deltaLng / 2)

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS * c;
  }
}
