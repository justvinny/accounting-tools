import java.io.*;

public class CSVUtilities {

    public static void writeToCSV(String csvData, File path) {
        String outputName = path.getName().split("\\.")[0] + ".csv";
        String newPath = path.getParent() + "\\" + outputName;
        File file = new File(newPath);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(csvData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
