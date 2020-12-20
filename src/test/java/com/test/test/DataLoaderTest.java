package com.test.test;

import com.test.loader.DataLoader;
import com.test.loader.dto.ExternalAirportData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class DataLoaderTest {

    @Test
    void testLoadData() {
        String name = this.getClass().getClassLoader().getResource("airports.dat").getFile();

        DataLoader loader = new DataLoader();
        Function<ExternalAirportData, Boolean> airportConsumer = (airport) -> true;
        long count = loader.loadDataFile( name, airportConsumer);

        Assertions.assertEquals(count, 1);
    }

    @Test
    void testLoadDataComments() {
        String name = this.getClass().getClassLoader().getResource("airports-comments.dat").getFile();

        DataLoader loader = new DataLoader();
        Function<ExternalAirportData, Boolean> airportConsumer = (airport) -> true;
        long count = loader.loadDataFile( name, airportConsumer);

        Assertions.assertEquals(count, 0);
    }

    @Test
    void testLoadDataEmpty() {
        String name = this.getClass().getClassLoader().getResource("airports-empty.dat").getFile();

        DataLoader loader = new DataLoader();
        Function<ExternalAirportData, Boolean> airportConsumer = (airport) -> true;
        long count = loader.loadDataFile( name, airportConsumer);

        Assertions.assertEquals(count, 0);
    }

    @Test
    void testLoadDataWithoutHeader() {
        String name = this.getClass().getClassLoader().getResource("airports-without-header.dat").getFile();

        DataLoader loader = new DataLoader();
        Function<ExternalAirportData, Boolean> airportConsumer = (airport) -> true;
        long count = loader.loadDataFile( name, airportConsumer);

        Assertions.assertEquals(count, 0);
    }

    @Test
    void testLoadDataWrongData() {
        String name = this.getClass().getClassLoader().getResource("airports-wrong-data.dat").getFile();

        DataLoader loader = new DataLoader();
        Function<ExternalAirportData, Boolean> airportConsumer = (airport) -> true;
        long count = loader.loadDataFile( name, airportConsumer);

        Assertions.assertEquals(count, 0);
    }

    @Test
    void testLoadDataWrongFileName() {
        String name = "airports-wrong-data-absent.dat";

        DataLoader loader = new DataLoader();
        Function<ExternalAirportData, Boolean> airportConsumer = (airport) -> true;
        long count = loader.loadDataFile( name, airportConsumer);

        Assertions.assertEquals(count, 0);
    }


}
