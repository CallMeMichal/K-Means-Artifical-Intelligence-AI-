import java.util.ArrayList;
import java.util.List;

public class Cluster {
    static int s=3;
    private int id;
    private double[] centroid;
    private List<DataPoint> dataPoints;

    public Cluster(int id) {
        this.id = id;
        this.centroid = null;
        this.dataPoints = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void addDataPoint(DataPoint dataPoint) {
        dataPoints.add(dataPoint);
    }

    public void clearDataPoints() {
        dataPoints.clear();
    }

    //oblicza sume dlugosci miedzy punktami danych a centroidem klastra
    public double calculateSumOfDistances() {
        double sum = 0.0;

        for (DataPoint dataPoint : dataPoints) {
            sum += euclideanDistance(dataPoint.getValues(), centroid);
        }

        return sum;
    }

    private double euclideanDistance(double[] point1, double[] point2) {
        double sum = 0.0;

        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow((point1[i] - point2[i]), 2);
        }
        return Math.sqrt(sum);
    }
}