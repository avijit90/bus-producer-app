package com.github.producer.scheduler;

import com.github.producer.kafka_producer.BusMessageProducer;
import com.github.producer.model.BusServiceResponse;
import com.github.producer.util.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.producer.util.ApplicationConstants.BUS_ARRIVAL_URL;
import static com.github.producer.util.ApplicationConstants.BUS_STOP_CODE;
import static com.google.common.collect.ImmutableMap.of;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpMethod.GET;

@Service
public class BusStopDataRetriever {

    final static Logger LOG = LoggerFactory.getLogger(BusStopDataRetriever.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    BusMessageProducer busMessageProducer;

    @Autowired
    RequestHelper requestHelper;

    @Scheduled(fixedRate = 30000)
    public void run() throws URISyntaxException, MalformedURLException {

        LOG.info(format("Starting to query endpoint : {0}", BUS_ARRIVAL_URL));
        URI uri = requestHelper.buildUriForRequest(BUS_ARRIVAL_URL, of(BUS_STOP_CODE, "03011"));
        HttpEntity entity = requestHelper.buildEntityForRequest();

        try {
            ResponseEntity<BusServiceResponse> result = restTemplate.exchange(uri, GET, entity, BusServiceResponse.class);
            if (result.getStatusCodeValue() == 200) {
                BusServiceResponse response = result.getBody();
                LOG.info(format("Success response={0}", response));
                busMessageProducer.publish(response);
            } else {
                LOG.info(format("Response without success={0}", result.getStatusCode()));
            }
        } catch (Exception e) {
            LOG.error(format("Exception while calling endpoint={0}", BUS_ARRIVAL_URL), e);
        }

    }
}
