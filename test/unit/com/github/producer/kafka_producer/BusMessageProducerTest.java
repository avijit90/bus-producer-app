package unit.com.github.producer.kafka_producer;

import com.github.producer.kafka_producer.BusMessageProducer;
import com.github.producer.model.Bus;
import com.github.producer.model.BusServiceResponse;
import com.github.producer.model.Service;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class BusMessageProducerTest {

    @Mock
    KafkaTemplate<String, Bus> kafkaTemplate;

    @InjectMocks
    BusMessageProducer unit;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void givenValidMessageThenShouldPublish(){
        BusServiceResponse messageToPublish = mock(BusServiceResponse.class);
        when(messageToPublish.getBusStopCode()).thenReturn("1000");
        Service service = mock(Service.class);
        when(service.getServiceNo()).thenReturn("1");
        Bus bus = mock(Bus.class);
        List<Bus> buses = new ArrayList<>();
        buses.add(bus);
        when(service.getBusList()).thenReturn(buses);
        List<Service> services = new ArrayList<>();
        services.add(service);
        when(messageToPublish.getServices()).thenReturn(services);

        unit.publish(messageToPublish);

    }
}
