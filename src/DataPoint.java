public class DataPoint {
    //reprezentuje punkt danych
    private double[] values;
    private String label;
    public DataPoint(double[] values) {
        this.values = values;
    }

    public double[] getValues() {
        return values;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}