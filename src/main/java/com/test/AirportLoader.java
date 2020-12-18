package com.test;

import com.test.model.DataPoint;
import com.test.model.DataPointType;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.*;

public class AirportLoader {

    private static final String BASE_URI = "http://localhost:8080/collect/airport";

    private final Client client = ClientBuilder.newClient();

    public AirportLoader() {
    }

    public void postData(String iata, Integer latitude, Integer longitude) {
        WebTarget path = client.target(BASE_URI+"?iata="+iata+"&latitude="+latitude+"&longitude="+longitude);
        Response response = path.request().post(null);
        System.out.print("post Airport: " + response.readEntity(String.class) + "\n");
    }

    public static void main(String[] args) {
        AirportLoader al= new AirportLoader();
        try(BufferedReader reader = new BufferedReader(new FileReader("airports.dat"))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] strs=line.split(",");
                if(strs[2]!=null&&!"".equals(strs[2])){
                    al.postData(strs[2],Integer.parseInt(strs[4]),Integer.parseInt(strs[5]));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("complete");
        System.exit(0);
    }
}
