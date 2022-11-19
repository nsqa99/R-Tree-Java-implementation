package com.anhnsq;

import java.util.ArrayList;
import java.util.List;

public class Node extends BoundedObject {
  private Node parent;
  private List<Node> nodeEntries;
  protected List<Entry> leafEntries;

  protected boolean isLeaf;

  public Node() {
    isLeaf = false;
    nodeEntries = new ArrayList<>();
    leafEntries = new ArrayList<>();
  }

  public Node(boolean isLeaf) {
    this.isLeaf = isLeaf;
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

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  @Override
  BoundingBox calculateBBox() {
    List<? extends BoundedObject> entries = isLeaf ? leafEntries : nodeEntries;
    bbox = calculateBBox(entries);
    bbox.calculateArea();

    return bbox;
  }
}
