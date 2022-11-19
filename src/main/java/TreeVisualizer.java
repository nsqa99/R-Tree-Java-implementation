import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.view.Viewer;

import java.util.List;

public class TreeVisualizer {
  private static final Graph graph = new SingleGraph("r3");

  public static void display(RTree tree) {
    System.setProperty("org.graphstream.ui","swing");

    assert tree.getRootNode() != null;

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
}
