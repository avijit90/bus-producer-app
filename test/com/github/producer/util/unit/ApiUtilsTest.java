package com.github.producer.util.unit;

import com.github.producer.util.ApiUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;

import static com.github.producer.util.ApplicationConstants.ACCOUNT_KEY;
import static org.junit.Assert.assertNotNull;
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
}
