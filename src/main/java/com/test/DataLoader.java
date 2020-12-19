package com.test;

import com.test.util.Pair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    private static final String CODE_HEADER_NAME = "IATA/FAA".toUpperCase();
    private static final String LATITUDE_HEADER_NAME = "Latitude".toUpperCase();
    private static final String LONGITUDE_HEADER_NAME = "Longitude".toUpperCase();

    public static void main(String[] args) {
        WeatherClient wc = new WeatherClient();
        DataLoader dataLoader  = new DataLoader();
        Function<DataLoader.Airport, Boolean> airportConsumer = (airport) -> wc.addAirPort(airport.getCode(), airport.getLatitude(), airport.getLongitude());
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

    public long loadDataFile (String fileName, Function<DataLoader.Airport, Boolean> consumer) {
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
        return 3 == headers.entrySet().size();
    }

    private Map<String, Integer> parseHeader(String line) {
        String[] headers = line.split(";");
        return IntStream.range(0, headers.length)
                .mapToObj(index -> Pair.of(headers[index].toUpperCase(), index))
                .filter(pair -> pair.getFirst().equalsIgnoreCase(CODE_HEADER_NAME)
                        || pair.getFirst().equalsIgnoreCase(LATITUDE_HEADER_NAME)
                        || pair.getFirst().equalsIgnoreCase(LONGITUDE_HEADER_NAME))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    private long loadData(Map<String, Integer> headers, String fileName, Function<DataLoader.Airport, Boolean> consumer) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            return lines
                    .filter(line -> !line.startsWith("#"))
                    .skip(1)
                    .map(line -> parseLine(headers, line))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(airport -> consumer.apply(airport))
                    .count();
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return 0;
    }

    private Optional<Airport> parseLine(Map<String, Integer> headers, String line) {
        Optional<Airport> result = Optional.empty();
        String[] items = line.split(";");

        Map<String, String> data = headers.entrySet().stream()
                .map(entry -> items.length > entry.getValue()
                                ? Pair.of(entry.getKey(), items[entry.getValue()].trim())
                                : null
                )
                .filter(pair -> Objects.nonNull(pair))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        if (data.size() == headers.size()) {
            try {
                String code = data.get(CODE_HEADER_NAME);
                int latitude = Integer.parseInt(data.get(LATITUDE_HEADER_NAME));
                int longitude = Integer.parseInt(data.get(LONGITUDE_HEADER_NAME));
                result = Optional.of(Airport.of(code, latitude, longitude));
            } catch (RuntimeException ex) {
                log.error(ex.toString(), ex);
            }
        }
        return result;
    }

    public static void usage (String[] args) {
        log.info("Usage text"); // TODO: Write amazing usage text
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class Airport {
        private final String code;
        private final int latitude;
        private final int longitude;
    }
}
