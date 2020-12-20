package com.test.model;

import lombok.*;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointData {
    /**
     * the mean of the observations
     */
    private double mean;

    /**
     * 1st quartile -- useful as a lower bound
     */
    private double first;

    /**
     * 2nd quartile -- median value
     */
    private double median;

    /**
     * 3rd quartile value -- less noisy upper value
     */
    private double last;

    /**
     * the total number of measurements
     */
    private int count;

    /**
     * type of DataPoint
     */
    private PointDataType type;
}
