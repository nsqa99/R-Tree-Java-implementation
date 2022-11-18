public class Point extends BoundedObject {
  private double lat;
  private double lng;

  @Override
  public String toString() {
    return "Point{" +
        "lat=" + lat +
        ", lng=" + lng +
        '}';
  }

  public Point(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  @Override
  BoundingBox calculateBBox() {
    // bbox of point is itself
    return new BoundingBox(this, this);
  }
}
