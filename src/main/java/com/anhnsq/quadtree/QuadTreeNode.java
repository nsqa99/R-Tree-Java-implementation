package com.anhnsq.quadtree;

import com.anhnsq.BoundingBox;
import com.anhnsq.Node;

import java.util.UUID;

public class QuadTreeNode extends Node {
  /*
              N
              |
     W   <----+---->   E
              |
              S
  */
  private QuadTreeNode nw;
  private QuadTreeNode ne;
  private QuadTreeNode sw;
  private QuadTreeNode se;

  public QuadTreeNode(BoundingBox bbox, boolean isLeafNode) {
    super(isLeafNode);

    this.id = UUID.randomUUID().toString();
    this.bbox = bbox;
  }

  public QuadTreeNode() {
    this.id = UUID.randomUUID().toString();
  }
  public double getHalfOffsetX() {
    return bbox.getMinX() + (bbox.getMaxX() - bbox.getMinX()) / 2;
  }

  public double getHalfOffsetY() {
    return bbox.getMinY() + (bbox.getMaxY() - bbox.getMinY()) / 2;
  }

//  public BoundingBox calculateBBox() {
//    List<? extends BoundedObject> entries = isLeaf ? leafEntries : nodeEntries;
//    bbox = calculateBBox(entries);
//
//    return bbox;
//  }

  public QuadTreeNode getNw() {
    return nw;
  }

  public void setNw(QuadTreeNode nw) {
    this.nw = nw;
  }

  public QuadTreeNode getNe() {
    return ne;
  }

  public void setNe(QuadTreeNode ne) {
    this.ne = ne;
  }

  public QuadTreeNode getSw() {
    return sw;
  }

  public void setSw(QuadTreeNode sw) {
    this.sw = sw;
  }

  public QuadTreeNode getSe() {
    return se;
  }

  public void setSe(QuadTreeNode se) {
    this.se = se;
  }
}
