import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int k = 7; // Liczba klastrów
        String filePath = "train.txt";
        int it = programExit();

        KMeans kMeans = new KMeans(k);

        try {
            kMeans.loadData(filePath);
        } catch (IOException e) {
            System.out.println("error: " + e.getMessage());
            return;
        }

        kMeans.initializeClusters();
        kMeans.run(it);
    }

    public static int programExit() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("train.txt"));
        int lines = 0;
        while (reader.readLine() != null)
            lines++; reader.close();

        return Cluster.s;
    }
}


