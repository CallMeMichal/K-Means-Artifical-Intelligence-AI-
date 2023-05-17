import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KMeans {
    private List<DataPoint> dataPoints;
    private List<Cluster> clusters;
    private int k;

    public KMeans(int k) {
        this.k = k;
        this.dataPoints = new ArrayList<>();
        this.clusters = new ArrayList<>();
    }

    //ustawia etykiete jako ostatni element lini
    // obiekt point dodawany jest do list DataPoints
    public void loadData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] attributes = line.split(",");
            double[] values = new double[attributes.length - 1];  // Pomijanie ostatniej wartości (etykieta)

            for (int i = 0; i < attributes.length - 1; i++) {
                values[i] = Double.parseDouble(attributes[i]);
            }

            String label = attributes[attributes.length - 1];  // Odczytanie etykiety z ostatniego elementu

            DataPoint dataPoint = new DataPoint(values);
            dataPoint.setLabel(label);
            dataPoints.add(dataPoint);
        }
        reader.close();
    }

    //1.inicjalizacja klastrów , ustawiamy centroidy losowo
    public void initializeClusters() {
        for (int i = 0; i < k; i++) {
            Cluster cluster = new Cluster(i);
            cluster.setCentroid(dataPoints.get(i).getValues());
            clusters.add(cluster);
        }
    }


    //3.po przypisaniu punktow do klastra sa one czyszczone
    //potem dla kazdego punktu z datapoints znajdowany jest najblizszy klaster
    //po przypisaniu punktow do klastra obliczany jest nowy centroid na podstawie punktow przyaleznych do tego klastra
    // jezeli nowy centroid rozni sie od poprzedniego wyswietlane sa statystyki klastrow
    public void run(int it) {
        boolean centroidsUpdated = true;
        int iteration = 0;

        while (centroidsUpdated && iteration < it) {
            centroidsUpdated = false;
            clearClusters();

            for (DataPoint dataPoint : dataPoints) {
                Cluster closestCluster = null;
                double minDistance = Double.MAX_VALUE;

                for (Cluster cluster : clusters) {
                    double distance = euclideanDistance(dataPoint.getValues(), cluster.getCentroid());
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestCluster = cluster;
                    }
                }

                closestCluster.addDataPoint(dataPoint);
            }
            //aktualizacja centroidow
            for (Cluster cluster : clusters) {
                double[] newCentroid = calculateCentroid(cluster.getDataPoints());
                if (!arrayEquals(cluster.getCentroid(), newCentroid)) {
                    cluster.setCentroid(newCentroid);
                    centroidsUpdated = true;
                }
            }

            iteration++;
            System.out.println("Iteration: " + iteration);
            printClusterStatistics();
        }
    }

    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.clearDataPoints();
        }
    }

    private double euclideanDistance(double[] point1, double[] point2) {
        double sum = 0.0;
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow((point1[i] - point2[i]), 2);
        }
        return Math.sqrt(sum);
    }

    //2.obliczanie centroidu dla danej grupy punktów
    private double[] calculateCentroid(List<DataPoint> dataPoints) {
        int numAttributes = dataPoints.get(0).getValues().length;
        double[] centroid = new double[numAttributes];

        for (DataPoint dataPoint : dataPoints) {
            double[] values = dataPoint.getValues();
            //tutaj obliczana jest suma wartosci atrybutow dla kazdego indeksu i dodajemy ja do odpowiediego elementu w tablicy
            for (int i = 0; i < numAttributes; i++) {
                centroid[i] += values[i];
            }
        }
        //srednia wartosc atrybutu dla danego indeksu
        for (int i = 0; i < numAttributes; i++) {
            centroid[i] /= dataPoints.size();
        }

        return centroid;
    }

    //sluzy do sprawdzenia aktualizacji centroidow poprzez sprawdzenie nowej tablicy ze stara i jak sa takie same to daje false
    private boolean arrayEquals(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }

        for (int i = 0; i < arr1.length; i++){
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    //
    public void printClusterStatistics() {
        double totalDistance = 0.0;
        Map<Integer, Map<String, Integer>> clusterLabelCounts = new HashMap<>();

        for (Cluster cluster : clusters) {
            totalDistance += cluster.calculateSumOfDistances();

            Map<String, Integer> labelCounts = new HashMap<>();
            List<DataPoint> dataPoints = cluster.getDataPoints();

            for (DataPoint dataPoint : dataPoints) {
                String label = dataPoint.getLabel();
                labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
            }

            clusterLabelCounts.put(cluster.getId(), labelCounts);
        }

        System.out.println("Total Distance: " + totalDistance);
        System.out.println("Cluster Purity:");

        for (Map.Entry<Integer, Map<String, Integer>> entry : clusterLabelCounts.entrySet()) {
            int clusterId = entry.getKey();
            Map<String, Integer> labelCounts = entry.getValue();
            int totalDataPoints = clusters.get(clusterId).getDataPoints().size();

            for (Map.Entry<String, Integer> labelEntry : labelCounts.entrySet()) {
                String label = labelEntry.getKey();
                int count = labelEntry.getValue();
                double purity = (count / (double) totalDataPoints) * 100;
                System.out.println("Cluster " + clusterId + ", Label " + label + ": " + purity + "%");
            }
        }

        System.out.println();
    }


}



