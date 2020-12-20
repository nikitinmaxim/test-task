package com.test.rest.loader;

import com.test.rest.loader.dto.ExternalAirportData;
import com.test.rest.loader.dto.DST;
import com.test.rest.client.WeatherClient;
import com.test.util.Pair;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class DataLoader {

    private static final String CITY_HEADER_NAME = "City".toUpperCase();
    private static final String COUNTRY_HEADER_NAME = "Country".toUpperCase();
    private static final String IATA_HEADER_NAME = "IATA/FAA".toUpperCase();
    private static final String ICAO_HEADER_NAME = "ICAO".toUpperCase();
    private static final String LATITUDE_HEADER_NAME = "Latitude".toUpperCase();
    private static final String LONGITUDE_HEADER_NAME = "Longitude".toUpperCase();
    private static final String ALTITUDE_HEADER_NAME = "Altitude".toUpperCase();
    private static final String TIMEZONE_HEADER_NAME = "Timezone".toUpperCase();
    private static final String DST_HEADER_NAME = "DST".toUpperCase();

    private static final String CITY_DEFAULT = "";
    private static final String COUNTRY_DEFAULT = "";
    private static final String IATA_DEFAULT = "";
    private static final String ICAO_DEFAULT = "";
    private static final String LATITUDE_DEFAULT = "0";
    private static final String LONGITUDE_DEFAULT = "0";
    private static final String ALTITUDE_DEFAULT = "0";
    private static final String TIMEZONE_DEFAULT = "0";
    private static final String DST_DEFAULT = "U";

    public static void main(String[] args) {
        WeatherClient wc = new WeatherClient();
        DataLoader dataLoader  = new DataLoader();
        Function<ExternalAirportData, Boolean> airportConsumer = (airport) -> wc.addAirport(airport.getIata(), (int)Math.round(airport.getLatitude()), (int)Math.round(airport.getLongitude()));
        long count = dataLoader.getFileNames(args)
                        .map(files -> files.stream()
                                            .mapToLong(dataFile -> dataLoader.loadDataFile(dataFile, airportConsumer))
                                            .sum()
                        )
                        .orElseGet(() -> {
                            DataLoader.usage(args);
                            return 0L;
                        });
        log.info(String.format("load %d airports", count));
    }

    private Optional<List<String>> getFileNames(String[] args) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase("--file")) {
                result.add(args[i+1]);
            }
        }
        return result.size() > 0 ? Optional.of(result) : Optional.empty();
    }

    public long loadDataFile (String fileName, Function<ExternalAirportData, Boolean> consumer) {
        File file = Paths.get(fileName).toFile();
        if (file.exists() && file.isFile()) {
            Map<String, Integer> headers = loadHeader(fileName);
            if (checkHeaders(headers)) {
                return loadData(headers, fileName, consumer);
            }
        }
        return 0L;
    }

    private Map<String, Integer> loadHeader(String fileName) {
        try (Stream<String> lines =  Files.lines(Paths.get(fileName))) {
            return lines
                    .filter(line -> !line.startsWith("#"))
                    .findFirst()
                    .map(line -> parseHeader(line))
                    .orElseGet(() -> Collections.emptyMap());
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return Collections.emptyMap();
    }

    boolean checkHeaders(Map<String, Integer> headers) {
        return headers.entrySet().size() > 0;
    }

    private Map<String, Integer> parseHeader(String line) {
        String[] headers = line.split(";");
        return IntStream.range(0, headers.length)
                .mapToObj(index -> Pair.of(headers[index].toUpperCase(), index))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    private long loadData(Map<String, Integer> headers, String fileName, Function<ExternalAirportData, Boolean> consumer) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            return lines
                    .filter(line -> !line.startsWith("#"))
                    .skip(1)
                    .map(line -> parseLine(headers, line))
                    .filter(Objects::nonNull)
                    .map(airportData -> validate(validator, airportData))
                    .filter(Objects::nonNull)
                    .map(consumer::apply)
                    .count();
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return 0;
    }

    private ExternalAirportData validate(Validator validator, ExternalAirportData data) {
        Set<ConstraintViolation<ExternalAirportData>> violations = validator.validate(data);
        if (violations.size() > 0) {
            violations.forEach(violation -> log.error(violation.getMessage()));
            return null;
        }
        return data;
    }

    private ExternalAirportData parseLine(Map<String, Integer> headers, String line) {
        String[] items = line.split(";");
        Map<String, String> data = headers.entrySet().stream()
                .map(entry -> items.length > entry.getValue()
                                ? Pair.of(entry.getKey(), items[entry.getValue()].trim())
                                : null
                )
                .filter(pair -> Objects.nonNull(pair))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        return buildExternalAirportData(data);
    }

    private ExternalAirportData buildExternalAirportData(Map<String, String> data) {
        ExternalAirportData result = null;
        try {
            String city = data.getOrDefault(CITY_HEADER_NAME, CITY_DEFAULT);
            String country = data.getOrDefault(COUNTRY_HEADER_NAME, COUNTRY_DEFAULT);
            String iata = data.getOrDefault(IATA_HEADER_NAME, IATA_DEFAULT);
            String icao = data.getOrDefault(ICAO_HEADER_NAME, ICAO_DEFAULT);
            double latitude = Double.parseDouble(data.getOrDefault(LATITUDE_HEADER_NAME, LATITUDE_DEFAULT));
            double longitude = Double.parseDouble(data.getOrDefault(LONGITUDE_HEADER_NAME, LONGITUDE_DEFAULT));
            int altitude = Integer.parseInt(data.getOrDefault(ALTITUDE_HEADER_NAME, ALTITUDE_DEFAULT));
            double timezone = Double.parseDouble(data.getOrDefault(TIMEZONE_HEADER_NAME, TIMEZONE_DEFAULT));
            DST dst = DST.valueOf(data.getOrDefault(DST_HEADER_NAME, DST_DEFAULT));
            result = ExternalAirportData.of(city, country, iata, icao, latitude, longitude, altitude, timezone, dst);
        } catch (RuntimeException ex) {
            log.error(ex.toString(), ex);
        }
        return result;
    }

    public static void usage (String[] args) {
        log.info("Usage text"); // TODO: Write amazing usage text
    }

}
