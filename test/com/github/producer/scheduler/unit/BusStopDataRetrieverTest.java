package com.github.producer.scheduler.unit;

import com.github.producer.kafka_producer.BusMessageProducer;
import com.github.producer.model.BusServiceResponse;
import com.github.producer.scheduler.BusStopDataRetriever;
import com.github.producer.util.ApiUtils;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.producer.util.ApplicationConstants.BUS_ARRIVAL_URL;
import static com.github.producer.util.ApplicationConstants.BUS_STOP_CODE;
import static com.google.common.collect.ImmutableMap.of;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpMethod.GET;

@RunWith(MockitoJUnitRunner.class)
public class BusStopDataRetrieverTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    BusMessageProducer busMessageProducer;

    @Mock
    ApiUtils apiUtils;

    @InjectMocks
    BusStopDataRetriever unit;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void givenValidResponseThenShouldPublishData() throws URISyntaxException, MalformedURLException {
        URIBuilder uriBuilder = new URIBuilder("http://test/Url");
        URI uri = uriBuilder.build().toURL().toURI();
        HttpEntity entity = mock(HttpEntity.class);
        ResponseEntity<BusServiceResponse> result = mock(ResponseEntity.class);

        when(apiUtils.buildUriForRequest(BUS_ARRIVAL_URL, of(BUS_STOP_CODE, "03011"))).thenReturn(uri);
        when(apiUtils.buildEntityForRequest()).thenReturn(entity);
        when(apiUtils.isResponseInvalid(uri.toURL().toString(), result)).thenReturn(false);
        when(restTemplate.exchange(uri, GET, entity, BusServiceResponse.class)).thenReturn(result);

        unit.run();

        verify(apiUtils).buildUriForRequest(eq(BUS_ARRIVAL_URL), eq(of(BUS_STOP_CODE, "03011")));
        verify(apiUtils).buildEntityForRequest();
        verify(restTemplate).exchange(eq(uri), eq(GET), eq(entity), eq(BusServiceResponse.class));
        verify(apiUtils).isResponseInvalid(eq(uri.toURL().toString()), eq(result));
        verify(busMessageProducer).publish(result.getBody());
    }

    @Test
    public void givenInvalidResponseThenShouldNotPublishData() throws URISyntaxException, MalformedURLException {
        URIBuilder uriBuilder = new URIBuilder("http://test/Url");
        URI uri = uriBuilder.build().toURL().toURI();
        HttpEntity entity = mock(HttpEntity.class);
        ResponseEntity<BusServiceResponse> result = mock(ResponseEntity.class);

        when(apiUtils.buildUriForRequest(BUS_ARRIVAL_URL, of(BUS_STOP_CODE, "03011"))).thenReturn(uri);
        when(apiUtils.buildEntityForRequest()).thenReturn(entity);
        when(apiUtils.isResponseInvalid(uri.toURL().toString(), result)).thenReturn(true);
        when(restTemplate.exchange(uri, GET, entity, BusServiceResponse.class)).thenReturn(result);

        unit.run();

        verify(apiUtils).buildUriForRequest(eq(BUS_ARRIVAL_URL), eq(of(BUS_STOP_CODE, "03011")));
        verify(apiUtils).buildEntityForRequest();
        verify(restTemplate).exchange(eq(uri), eq(GET), eq(entity), eq(BusServiceResponse.class));
        verify(apiUtils).isResponseInvalid(eq(uri.toURL().toString()), eq(result));
        verifyNoMoreInteractions(busMessageProducer);
    }

}
