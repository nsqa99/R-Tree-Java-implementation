package com.anhnsq.quadtree;

import com.anhnsq.BoundingBox;
import com.anhnsq.Entry;
import com.anhnsq.Point;

import java.util.List;

public class QuadTree {
  private final int cellCapacity;

  private final QuadTreeNode rootNode;

  public QuadTree(int cellCapacity) {
    this.cellCapacity = cellCapacity;

    rootNode = new QuadTreeNode();
    rootNode.setId("root");
    rootNode.setIsLeaf(true);

    double MIN_LNG_CONSTRAINT = 105.75085919292889;
    double MAX_LNG_CONSTRAINT = 105.75377270926128;
    double MIN_LAT_CONSTRAINT = 20.967417029088878;
    double MAX_LAT_CONSTRAINT = 20.96889456792961;

    BoundingBox rootBBox = new BoundingBox(new Point(MIN_LAT_CONSTRAINT, MIN_LNG_CONSTRAINT),
        new Point(MAX_LAT_CONSTRAINT, MAX_LNG_CONSTRAINT)); // WSG84 lat/lng constraints
    rootNode.setBbox(rootBBox);
  }

  public QuadTreeNode getRootNode() {
    return rootNode;
  }

  public void insert(Entry entry) {
    insertAndAdjust(rootNode, entry);
  }

  private void insertAndAdjust(QuadTreeNode node, Entry entry) {
    if (node.isLeafNode()) {
      if (node.getBBox().overlaps(entry.getBBox())) {
        node.getLeaveEntries().add(entry);

        if (node.getLeaveEntries().size() > cellCapacity) {
          quadSplit(node);
        }
      }
    } else {
      insertAndAdjust(node.getNw(), entry);

      insertAndAdjust(node.getNe(), entry);

      insertAndAdjust(node.getSw(), entry);

      insertAndAdjust(node.getSe(), entry);
    }
  }

  private void quadSplit(QuadTreeNode node) {
    List<Entry> currentEntries = node.getLeaveEntries();
    node.setIsLeaf(false);
    BoundingBox parentBBox = node.getBBox();

    BoundingBox nwBBox = new BoundingBox(new Point(node.getHalfOffsetY(), parentBBox.getMinX()), new Point(parentBBox.getMaxY(), node.getHalfOffsetX()));
    BoundingBox neBBox = new BoundingBox(new Point(node.getHalfOffsetY(), node.getHalfOffsetX()), new Point(parentBBox.getMaxY(), parentBBox.getMaxX()));
    BoundingBox swBBox = new BoundingBox(new Point(parentBBox.getMinY(), parentBBox.getMinX()), new Point(node.getHalfOffsetY(), node.getHalfOffsetX()));
    BoundingBox seBBox = new BoundingBox(new Point(parentBBox.getMinY(), node.getHalfOffsetX()), new Point(node.getHalfOffsetY(), parentBBox.getMaxX()));

    node.setNw(new QuadTreeNode(nwBBox, true));
    node.setNe(new QuadTreeNode(neBBox, true));
    node.setSw(new QuadTreeNode(swBBox, true));
    node.setSe(new QuadTreeNode(seBBox, true));

    for (Entry entry : currentEntries) {
      insertAndAdjust(node.getNw(), entry);
      insertAndAdjust(node.getNe(), entry);
      insertAndAdjust(node.getSw(), entry);
      insertAndAdjust(node.getSe(), entry);
    }

    node.setLeaveEntries(null);
  }
}
