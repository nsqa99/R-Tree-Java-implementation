// leaf node entry (mbb, object)
public class Entry extends BoundedObject {
  private BoundedObject object;
  private Node parent;

  public Entry(BoundedObject object) {
    this.object = object;
  }

  public Entry() {
  }

  public BoundedObject getObject() {
    return object;
  }

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  @Override
  BoundingBox calculateBBox() {
    bbox = object.getBBox();

    return bbox;
  }
}
