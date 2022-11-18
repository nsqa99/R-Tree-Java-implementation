import java.util.List;

public class RTree {
  private Node rootNode;

  private int maxEntry;
  private int minEntry;

  public RTree(int maxEntry, int minEntry) {
    this.maxEntry = maxEntry;
    this.minEntry = minEntry;
  }

  public void insert(Entry entry) {
    Node node = rootNode;
    while (!node.isLeafNode()) {
      node = chooseSubTree(node, entry);
    }

    node.getLeaveEntries().add(entry);

    if (node.getLeaveEntries().size() > maxEntry) {
      splitAndAdjust(node);
    } else {
      adjustPath(node);
    }
  }

  private Node chooseSubTree(Node node, Entry entry) {
    
    return null;
  }
}
