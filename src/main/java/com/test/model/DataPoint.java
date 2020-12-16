package com.test.model;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
public class DataPoint {

    private final double mean;
    private final double first;
    private final double median;
    private final double last;
    private final int count;
    private final DataPointType type;

    /**
     * private constructor, use the builder to create this object
     */
    private DataPoint() {
        type = null;
        count = 0;
        last = 0.0;
        median = 0.0;
        first = 0.0;
        mean = 0.0;
    }

    public DataPoint(double mean, double first, double median, double last, int count, DataPointType type) {
        this.mean = mean;
        this.first = first;
        this.median = median;
        this.last = last;
        this.count = count;
        this.type = type;
    }

    /**
     * the mean of the observations
     */
    public double getMean() {
        return mean;
    }

    /**
     * 1st quartile -- useful as a lower bound
     */
    public double getFirst() {
        return first;
    }

    /**
     * 2nd quartile -- median value
     */
    public double getMedian() {
        return median;
    }

    /**
     * 3rd quartile value -- less noisy upper value
     */
    public double getLast() {
        return last;
    }

    /**
     * the total number of measurements
     */
    public int getCount() {
        return count;
    }

    /**
     * type of DataPoint
     */
    public DataPointType getType() {
        return type;
    }

}
