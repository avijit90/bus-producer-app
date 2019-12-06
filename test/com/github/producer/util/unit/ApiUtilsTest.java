package com.github.producer.util.unit;

import com.github.producer.util.ApiUtils;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.producer.util.ApplicationConstants.ACCOUNT_KEY;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ApiUtilsTest {

    @InjectMocks
    ApiUtils apiUtils;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldBuildEntityForRequestObjects() {
        HttpEntity httpEntity = apiUtils.buildEntityForRequest();

        assertNotNull(httpEntity);
        assertNotNull(httpEntity.getHeaders());
        assertNotNull(httpEntity.getHeaders().get(ACCOUNT_KEY));
    }

    @Test
    public void givenNoRequestParamsThenShouldBuildUriForRequestWithoutRequestParams() throws MalformedURLException, URISyntaxException {
        String url = "http:/test/url";
        URI uri = apiUtils.buildUriForRequest(url, null);

        assertNotNull(uri);
        assertEquals(uri.toString(), url);
    }

    @Test
    public void givenRequestParamsThenShouldBuildUriForRequestWithRequestParams() throws MalformedURLException, URISyntaxException {
        String url = "http:/test/url";
        String requestParamKey = "param1";
        String requestParamValue = "value1";
        ImmutableMap<String, String> requestParams = ImmutableMap.of(requestParamKey, requestParamValue);
        URI uri = apiUtils.buildUriForRequest(url, requestParams);

        assertNotNull(uri);
        assertTrue(startsWith(uri.toString(), url));
        assertTrue(contains(uri.toString(), requestParamKey));
        assertTrue(contains(uri.toString(), requestParamValue));
    }

    @Test(expected = URISyntaxException.class)
    public void givenUrlWithSpaceThenShouldThrowURISyntaxException() throws MalformedURLException, URISyntaxException {
        String url = "http://syntax /incorrect";
        apiUtils.buildUriForRequest(url, null);
    }

    @Test(expected = MalformedURLException.class)
    public void givenUrlWithIncorrectSyntaxThenShouldThrowURISyntaxException() throws MalformedURLException, URISyntaxException {
        String url = "htp://test";
        apiUtils.buildUriForRequest(url, null);
    }

}
