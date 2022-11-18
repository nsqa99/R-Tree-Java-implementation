public abstract class BoundedObject {
  protected BoundingBox bbox;
  abstract BoundingBox calculateBBox();

  public BoundingBox getBBox() {
    if (this.bbox == null) {
      return calculateBBox();
    }

    return this.bbox;
  }
}
