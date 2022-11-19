package com.anhnsq;

import com.anhnsq.quadtree.QuadTree;
import com.anhnsq.quadtree.QuadTreeNode;
import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.Viewer;

import java.util.List;
import java.util.UUID;

public class TreeVisualizer {
  private static final Graph graph = new SingleGraph("r3");

  public static void display(RTree tree) {
    System.setProperty("org.graphstream.ui", "swing");

    assert tree.getRootNode() != null;

    traverse(graph, tree.getRootNode());

    graph.setAttribute("ui.quality");
    graph.setAttribute("ui.antialias");

    Viewer viewer = graph.display();
    HierarchicalLayout hl = new HierarchicalLayout();
    viewer.enableAutoLayout(new HierarchicalLayout());
  }

  public static void display(QuadTree tree) {
    System.setProperty("org.graphstream.ui", "swing");

    QuadTreeNode rootNode = tree.getRootNode();

    graph.addNode(rootNode.getId());
    graph.getNode(rootNode.getId()).setAttribute("ui.label", rootNode.getId());

    traverse(graph, tree.getRootNode());

    graph.setAttribute("ui.quality");
    graph.setAttribute("ui.antialias");

    Viewer viewer = graph.display();
    HierarchicalLayout hl = new HierarchicalLayout();
    viewer.enableAutoLayout(new HierarchicalLayout());
  }

  public static void displayInConsole(RTree tree) {
    SimpleTreeNode rootNode = new SimpleTreeNode("Root");

    traverseV2(rootNode, tree.getRootNode());

    new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(rootNode));
  }

  public static void displayInConsole(QuadTree tree) {
    SimpleTreeNode rootNode = new SimpleTreeNode("Root");

    traverseQuadTree(rootNode, tree.getRootNode());

    new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(rootNode));
  }

  private static void traverse(Graph graph, Node node) {
    if (node.getParent() == null) {
      graph.addNode("root");
      graph.getNode("root").setAttribute("ui.label", "root");
    }

    if (node.isLeafNode()) {
      List<Entry> leafEntries = node.getLeaveEntries();

      for (Entry entry : leafEntries) {
        String entryId = entry.getId();
        graph.addNode(entryId);
        graph.getNode(entryId).setAttribute("ui.label", entryId);
        graph.addEdge(node.getId().concat(entry.getId()), node.getId(), entry.getId());
      }
    } else {
      List<Node> nodeEntries = node.getNodeEntries();

      for (Node entry : nodeEntries) {
        String entryId = entry.getId();
        graph.addNode(entryId);
        graph.getNode(entryId).setAttribute("ui.label", entryId);
        graph.addEdge(node.getId().concat(entryId), node.getId(), entryId);

        traverse(graph, entry);
      }
    }
  }

  private static void traverse(Graph graph, QuadTreeNode node) {
    if (node.isLeafNode()) {
      List<Entry> leafEntries = node.getLeaveEntries();

      for (Entry entry : leafEntries) {
        String entryId = entry.getId();
        String randomEntryId = UUID.randomUUID().toString();
        graph.addNode(randomEntryId);
        graph.getNode(randomEntryId).setAttribute("ui.label", entryId);
        graph.addEdge(node.getId().concat(entryId), node.getId(), randomEntryId);
      }
    } else {
      QuadTreeNode nw = node.getNw();
      QuadTreeNode ne = node.getNe();
      QuadTreeNode sw = node.getSw();
      QuadTreeNode se = node.getSe();

      if (nw != null) {
        String entryId = nw.getId();
        graph.addNode(entryId);
        graph.getNode(entryId).setAttribute("ui.label", entryId.split("-")[0]);
        graph.addEdge(node.getId().concat(entryId), node.getId(), entryId);

        traverse(graph, nw);
      }

      if (ne != null) {
        String entryId = ne.getId();
        graph.addNode(entryId);
        graph.getNode(entryId).setAttribute("ui.label", entryId.split("-")[0]);
        graph.addEdge(node.getId().concat(entryId), node.getId(), entryId);

        traverse(graph, ne);
      }

      if (sw != null) {
        String entryId = sw.getId();
        graph.addNode(entryId);
        graph.getNode(entryId).setAttribute("ui.label", entryId.split("-")[0]);
        graph.addEdge(node.getId().concat(entryId), node.getId(), entryId);

        traverse(graph, sw);
      }

      if (se != null) {
        String entryId = se.getId();
        graph.addNode(entryId);
        graph.getNode(entryId).setAttribute("ui.label", entryId.split("-")[0]);
        graph.addEdge(node.getId().concat(entryId), node.getId(), entryId);

        traverse(graph, se);
      }
    }
  }

  private static void traverseV2(SimpleTreeNode treeNode, Node node) {
    if (node.isLeafNode()) {
      List<Entry> leafEntries = node.getLeaveEntries();

      for (Entry entry : leafEntries) {
        String entryId = entry.getId();
        treeNode.addChild(new SimpleTreeNode(entryId));
      }
    } else {
      List<Node> nodeEntries = node.getNodeEntries();

      for (Node entry : nodeEntries) {
        String entryId = entry.getId();
        SimpleTreeNode childTreeNode = new SimpleTreeNode(entryId);
        treeNode.addChild(childTreeNode);

        traverseV2(childTreeNode, entry);
      }
    }
  }

  private static void traverseQuadTree(SimpleTreeNode treeNode, QuadTreeNode node) {
    if (node.isLeafNode()) {
      List<Entry> leafEntries = node.getLeaveEntries();

      for (Entry entry : leafEntries) {
        String entryId = entry.getId();
        treeNode.addChild(new SimpleTreeNode(entryId));
      }
    } else {
      QuadTreeNode nw = node.getNw();
      QuadTreeNode ne = node.getNe();
      QuadTreeNode sw = node.getSw();
      QuadTreeNode se = node.getSe();

      if (nw != null) {
        SimpleTreeNode childTreeNode = new SimpleTreeNode(nw.getId().split("-")[0]);
        treeNode.addChild(childTreeNode);

        traverseQuadTree(childTreeNode, nw);
      }

      if (ne != null) {
        SimpleTreeNode childTreeNode = new SimpleTreeNode(ne.getId().split("-")[0]);
        treeNode.addChild(childTreeNode);

        traverseQuadTree(childTreeNode, ne);
      }

      if (sw != null) {
        SimpleTreeNode childTreeNode = new SimpleTreeNode(sw.getId().split("-")[0]);
        treeNode.addChild(childTreeNode);

        traverseQuadTree(childTreeNode, sw);
      }

      if (se != null) {
        SimpleTreeNode childTreeNode = new SimpleTreeNode(se.getId().split("-")[0]);
        treeNode.addChild(childTreeNode);

        traverseQuadTree(childTreeNode, se);
      }
    }
  }
}
