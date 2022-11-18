import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BoundedObject {
  protected BoundingBox bbox;
  abstract BoundingBox calculateBBox();

  public BoundingBox getBBox() {
    if (this.bbox == null) {
      return calculateBBox();
    }

    return this.bbox;
  }

  public static BoundingBox calculateBBox(List<? extends BoundedObject> entries) {
    List<BoundingBox> entryBBoxes = entries.stream().map(BoundedObject::getBBox).collect(Collectors.toList());

    double xMin = Collections.min(entryBBoxes, Comparator.comparingDouble(BoundingBox::getMinX)).getMinX();
    double xMax = Collections.max(entryBBoxes, Comparator.comparingDouble(BoundingBox::getMaxX)).getMaxX();

    double yMin = Collections.min(entryBBoxes, Comparator.comparingDouble(BoundingBox::getMinY)).getMinY();
    double yMax = Collections.max(entryBBoxes, Comparator.comparingDouble(BoundingBox::getMaxY)).getMaxY();

    return new BoundingBox(new Point(yMin, xMin), new Point(yMax, xMax));
  }
}
