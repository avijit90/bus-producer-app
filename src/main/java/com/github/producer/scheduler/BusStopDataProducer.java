package com.github.producer.scheduler;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpMethod.GET;

@Service
public class BusStopDataProducer {

    final static Logger LOG = LoggerFactory.getLogger(BusStopDataProducer.class);

    public static final String BUS_ARRIVAL_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";
    public static final String BUS_STOP_CODE = "BusStopCode";
    public static final String ACCOUNT_KEY = "AccountKey";
    public static final String PARAMETERS = "parameters";

    @Autowired
    RestTemplate restTemplate;

    @Value("${dataMall.accountKey: getYourOwnKey}")
    private String accountKey;

    @Scheduled(fixedRate = 30000)
    public void run() throws URISyntaxException, MalformedURLException {

        final String endpoint = BUS_ARRIVAL_URL;

        LOG.info(format("Starting to query endpoint : {0}", endpoint));
        LOG.info(format("AccountKey resolved to : {0}", accountKey));

        URIBuilder uriBuilder = new URIBuilder(endpoint);
        uriBuilder.addParameter(BUS_STOP_CODE, "03011");
        URI uri = uriBuilder.build().toURL().toURI();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(asList(MediaType.APPLICATION_JSON));
        headers.set(ACCOUNT_KEY, accountKey);
        HttpEntity<String> entity = new HttpEntity<>(PARAMETERS, headers);

        try {
            ResponseEntity<String> result = restTemplate.exchange(uri, GET, entity, String.class);
            if (result.getStatusCodeValue() == 200) {
                LOG.info(format("Success response ={0}", result.getBody()));
            } else {
                LOG.info(format("Response without success ={0}", result));
            }
        } catch (Exception e) {
            LOG.error(format("Exception while calling endpoint={0}", endpoint), e);
        }

    }
}