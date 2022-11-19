import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
  public static void main(String[] args) {
    List<List<double[]>> entries = readDataFromFile();

    RTree tree = new RTree(false, 4, 2);

    for (int i = 0; i < entries.size(); i++) {
      tree.insert(prepareLinestring("e" + (i + 1), entries.get(i)));
    }

//    TreeVisualizer.display(tree);
    TreeVisualizer.displayInConsole(tree);
  }

  private static Entry prepareLinestring(String id, List<double[]> arg) {
    List<Point> points = new ArrayList<>();
    for (double[] coord : arg) {
      points.add(new Point(coord[1], coord[0]));
    }

    Linestring linestring = new Linestring(points);
    Entry e = new Entry(linestring);
    e.setId(id);

    return e;
  }

  private static List<List<double[]>> readDataFromFile() {
    List<List<double[]>> rs = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/data.txt")))) {
      while (true) {
        String lineData = br.readLine();
        if (lineData == null) break;

        List<String> coordinates = Arrays.stream(lineData.split(",")).collect(Collectors.toList());

        List<double[]> data = coordinates.stream()
            .map(coord -> Arrays.stream(coord.split("\\s+")).mapToDouble(Double::parseDouble).toArray())
            .filter(Objects::nonNull).collect(Collectors.toList());

        rs.add(data);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return rs;
  }
}