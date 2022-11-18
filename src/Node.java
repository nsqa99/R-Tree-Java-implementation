import java.util.ArrayList;
import java.util.List;

public class Node {
  private Node parent;
  // directory rectangle = bbox(E)
  private BoundingBox bbox;
  private List<Node> nodeEntries;
  private List<Entry> leafEntries;

  private boolean isLeaf;

  public Node() {
    isLeaf = false;
    nodeEntries = new ArrayList<>();
    leafEntries = new ArrayList<>();
  }

  public List<Node> getNodeEntries() {
    return nodeEntries;
  }

  public void setNodeEntries(List<Node> nodeEntries) {
    this.nodeEntries = nodeEntries;
  }

  public List<Entry> getLeaveEntries() {
    return leafEntries;
  }

  public void setLeaveEntries(List<Entry> leaveEntries) {
    this.leafEntries = leaveEntries;
  }

  public boolean isLeafNode() {
    return isLeaf;
  }

  public void setIsLeaf(boolean leaf) {
    isLeaf = leaf;
  }
}
