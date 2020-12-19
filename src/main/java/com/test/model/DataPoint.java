package com.test.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
@Getter
@RequiredArgsConstructor
public class DataPoint {
    /**
     * the mean of the observations
     */
    private final double mean;

    /**
     * 1st quartile -- useful as a lower bound
     */
    private final double first;

    /**
     * 2nd quartile -- median value
     */
    private final double median;

    /**
     * 3rd quartile value -- less noisy upper value
     */
    private final double last;

    /**
     * the total number of measurements
     */
    private final int count;

    /**
     * type of DataPoint
     */
    private final DataPointType type;
}
