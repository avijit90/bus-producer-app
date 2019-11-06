package com.github.producer.util;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class RequestHelper {

    public static final String ACCOUNT_KEY = "AccountKey";
    public static final String PARAMETERS = "parameters";

    @Value("${app.dataMall.accountKey: getYourOwnKey}")
    private String accountKey;

    final static Logger LOG = LoggerFactory.getLogger(RequestHelper.class);

    public HttpEntity buildEntityForRequest() {
        LOG.info(format("AccountKey resolved to : {0}", accountKey));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(asList(MediaType.APPLICATION_JSON));
        headers.set(ACCOUNT_KEY, accountKey);
        return new HttpEntity<>(PARAMETERS, headers);
    }

    public URI buildUriForRequest(String url, Map<String, String> requestParams) throws URISyntaxException, MalformedURLException {
        URIBuilder uriBuilder = new URIBuilder(url);

        if (!isEmpty(requestParams))
            requestParams.keySet().forEach(key -> uriBuilder.addParameter(key, requestParams.get(key)));

        URI uri = uriBuilder.build().toURL().toURI();
        return uri;
    }

}
