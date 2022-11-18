import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RTree {
  private Node rootNode;

  private final int maxEntry;
  private final int minEntry;

  public RTree(int maxEntry, int minEntry) {
    this.maxEntry = maxEntry;
    this.minEntry = minEntry;
  }

  public void insert(Entry entry, boolean isRootAlsoLeafNode) {
    if (rootNode == null) {
      rootNode = new Node(isRootAlsoLeafNode);
      Node childNode = new Node(true);
      childNode.setParent(rootNode);

      rootNode.getNodeEntries().add(childNode);
    }

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

  private void splitAndAdjust(Node node) {
    // add new node to parent
    Node parent = node.getParent();
    // if node is root
    if (parent == null) {
      // create new root
      parent = new Node();
      node.setParent(parent);
    }

    List<? extends BoundedObject> currentEntries = node.isLeafNode() ? node.getLeaveEntries() : node.getNodeEntries();

    Node newNode = new Node();
    newNode.setParent(parent);

    // reset entries
    if (node.isLeafNode()) {
      // new node should be a leaf node too
      newNode.setIsLeaf(true);
      node.getLeaveEntries().clear();
    } else {
      node.getNodeEntries().clear();
    }

    // distribute M + 1 entries to node & newNode
    quadraticSplit(node, newNode, currentEntries);
    adjustPath(node);

    parent.getNodeEntries().add(newNode);

    // split parent if needed
    if (parent.getNodeEntries().size() > maxEntry) {
      splitAndAdjust(parent);
    }
  }

  private void quadraticSplit(Node node1, Node node2, List<? extends BoundedObject> entries) {
    // Pick 2 seeds with maximal dead space
    double maxDeadSpace = Double.MIN_VALUE;
    List<BoundedObject> groupEntries1 = new ArrayList<>();
    List<BoundedObject> groupEntries2 = new ArrayList<>();
    BoundedObject seed1 = null, seed2 = null;

    for (BoundedObject entry : entries) {
      for (BoundedObject otherEntry : entries) {
        if (entry == otherEntry) continue;

        double deadSpace = BoundedObject.calculateBBox(List.of(entry, otherEntry)).getArea() -
            entry.getBBox().getArea() - otherEntry.getBBox().getArea();

        if (deadSpace > maxDeadSpace) {
          seed1 = entry;
          seed2 = otherEntry;
          maxDeadSpace = deadSpace;
        }
      }
    }

    assert seed1 != null;

    groupEntries1.add(seed1);
    groupEntries2.add(seed2);

    entries.remove(seed1);
    entries.remove(seed2);

    double expansion = 0;

    // loop through M-1 remaining entries
    // find the entry with min dead space if add to the two groups
    while(!entries.isEmpty()) {
      BoundedObject bestSeed = null;
      List<BoundedObject> bestGroup;

      for (BoundedObject entry : entries) {
        List<BoundedObject> mergedGroup1 = new ArrayList<>(groupEntries1);
        List<BoundedObject> mergedGroup2 = new ArrayList<>(groupEntries2);
        mergedGroup1.add(entry);
        mergedGroup2.add(entry);
        double areaMergeEntryWithGroup1 = BoundedObject.calculateBBox(mergedGroup1).getArea();
        double areaMergeEntryWithGroup2 = BoundedObject.calculateBBox(mergedGroup2).getArea();
        double deadSpace1 = areaMergeEntryWithGroup1 - entry.getBBox().getArea();
        double deadSpace2 = areaMergeEntryWithGroup2 - entry.getBBox().getArea();

        if (deadSpace1 - deadSpace2 > expansion) {
          bestSeed = entry;
          expansion = deadSpace1 - deadSpace2;
        }

        if (deadSpace2 - deadSpace1 > expansion) {
          bestSeed = entry;
          expansion = deadSpace2 - deadSpace1;
        }
      }

      assert bestSeed != null;

      List<BoundedObject> mergedGroup1WithBestSeed = new ArrayList<>(groupEntries1);
      List<BoundedObject> mergedGroup2WithBestSeed = new ArrayList<>(groupEntries2);
      mergedGroup1WithBestSeed.add(bestSeed);
      mergedGroup2WithBestSeed.add(bestSeed);
      double areaMergeEntryWithGroup1 = BoundedObject.calculateBBox(mergedGroup1WithBestSeed).getArea();
      double areaMergeEntryWithGroup2 = BoundedObject.calculateBBox(mergedGroup2WithBestSeed).getArea();

      // choose group closest to seed
      if (areaMergeEntryWithGroup1 < areaMergeEntryWithGroup2) {
        bestGroup = groupEntries1;
      } else if (areaMergeEntryWithGroup1 > areaMergeEntryWithGroup2) {
        bestGroup = groupEntries2;
      } else {
        // or has smaller area
        double area1 = BoundedObject.calculateBBox(groupEntries1).getArea();
        double area2 = BoundedObject.calculateBBox(groupEntries2).getArea();

        if (area1 > area2) {
          bestGroup = groupEntries2;
        } else if (area1 < area2) {
          bestGroup = groupEntries1;
        } else {
          // or has fewer entries
          bestGroup = groupEntries1.size() > groupEntries2.size() ? groupEntries2 : groupEntries1;
        }
      }

      bestGroup.add(bestSeed);
      entries.remove(bestSeed);

      if (groupEntries1.size() + entries.size() == minEntry) {
        groupEntries1.addAll(entries);
        entries.clear();
      }

      if (groupEntries2.size() + entries.size() == minEntry) {
        groupEntries2.addAll(entries);
        entries.clear();
      }
    }

    assert groupEntries1.size() + groupEntries2.size() == maxEntry + 1;

    setNodeEntries(node1, groupEntries1);

    setNodeEntries(node2, groupEntries2);
  }

  private void setNodeEntries(Node node, List<BoundedObject> entries) {
    if (node.isLeafNode()) {
      node.setLeaveEntries(entries.stream().filter(e -> e instanceof Entry).map(e -> (Entry) e).collect(Collectors.toList()));
    } else {
      node.setNodeEntries(entries.stream().filter(e -> e instanceof Node).map(e -> (Node) e).collect(Collectors.toList()));
    }
  }

  private void adjustPath(Node node) {
    if (node.getParent() == null) return;

    node.calculateBBox();
    adjustPath(node.getParent());
  }

  private Node chooseSubTree(Node node, Entry entry) {
    if (node.isLeafNode()) return node;

    List<Node> entries = node.getNodeEntries();
    double minArea = Double.MAX_VALUE;
    double minEnlargement = Double.MAX_VALUE;

    for (Node nodeEntry : entries) {
      double enlargementNeeded = nodeEntry.getBBox().calculateEnlargement(entry.getBBox());
      double currentArea = nodeEntry.getBBox().getArea();

      if (nodeEntry.getBBox().contains(entry.getBBox()) || enlargementNeeded < minEnlargement
          || currentArea < minArea) {
        node = nodeEntry;
        minArea = currentArea;
        minEnlargement = enlargementNeeded;
      }
    }

    return node;
  }
}
