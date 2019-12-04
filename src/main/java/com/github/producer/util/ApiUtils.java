package com.github.producer.util;

import com.github.producer.model.BusServiceResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.github.producer.util.ApplicationConstants.ACCOUNT_KEY;
import static com.github.producer.util.ApplicationConstants.PARAMETERS;
import static java.text.MessageFormat.format;
import static java.util.Collections.singletonList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class ApiUtils {

    @Value("${app.dataMall.accountKey: getYourOwnKey}")
    private String accountKey;

    final static Logger LOG = LoggerFactory.getLogger(ApiUtils.class);

    public HttpEntity buildEntityForRequest() {
        LOG.info(format("AccountKey resolved to : {0}", accountKey));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        headers.set(ACCOUNT_KEY, accountKey);
        return new HttpEntity<>(PARAMETERS, headers);
    }

    public URI buildUriForRequest(String url, Map<String, String> requestParams) throws URISyntaxException, MalformedURLException {
        URIBuilder uriBuilder = new URIBuilder(url);

        if (!isEmpty(requestParams))
            requestParams.keySet().forEach(key -> uriBuilder.addParameter(key, requestParams.get(key)));

        return uriBuilder.build().toURL().toURI();
    }

    public boolean validateResponse(String url, ResponseEntity<BusServiceResponse> result) {
        if (result == null) {
            LOG.info(format("No Response received from url={0}", url));
            return true;
        }

        if (result.getStatusCodeValue() != 200) {
            LOG.info(format("Response without success={0}", result.getStatusCode()));
            return true;
        }

        if (result.getBody() == null) {
            LOG.info(format("Response body is null while calling url={0}", url));
            return true;
        }

        return false;
    }

}
