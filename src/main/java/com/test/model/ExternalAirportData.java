package com.test.model;

public class ExternalAirportData {
    //здесь ссылочные типы можно сделать final чтобы их адреса не менялись, точно не помню, но помоему для того чтобы не
    //напрягать сборщик мусора. хотя для них сеттеров нет можно было и ничего не делать
    private final String city;
    private final String country;
    private final String iata;
    private final String icao;
    private double latitude;
    private double longitude;
    private int altitude;
    private double tzOffset;
    private final DST dst;

    public ExternalAirportData(String city, String country, String iata, String icao, double latitude, double longitude,
                               int altitude, double tzOffset, DST dst) {
        this.city = city;
        this.country = country;
        this.iata = iata;
        this.icao = icao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.tzOffset = tzOffset;
        this.dst = dst;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIata() {
        return iata;
    }

    public String getIcao() {
        return icao;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public double getTzOffset() {
        return tzOffset;
    }

    public DST getDst() {
        return dst;
    }
}
