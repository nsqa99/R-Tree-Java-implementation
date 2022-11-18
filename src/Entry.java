// leaf node entry (mbb, object)
public class Entry {
  private BoundingBox mbb;
  private BoundedObject object;
  private Node parent;

  public Entry(BoundedObject object) {
    this.object = object;
    this.mbb = object.getBBox();
  }

  public Entry() {
  }

  public BoundingBox getMbb() {
    return mbb;
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
}
