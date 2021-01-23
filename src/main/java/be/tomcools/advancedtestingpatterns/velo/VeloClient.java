package be.tomcools.advancedtestingpatterns.velo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class VeloClient {
    private final RestTemplate client;

    @Value("${app.velo.url}")
    private String url;

    @Autowired
    public VeloClient(final RestTemplate client) {
        this.client = client;
    }

    public List<VeloStation> retrieveStations() {
        ParameterizedTypeReference<List<VeloStation>> type = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<VeloStation>> stationList = client.exchange(url, HttpMethod.GET, null, type);
        return stationList.getBody();
    }
}
