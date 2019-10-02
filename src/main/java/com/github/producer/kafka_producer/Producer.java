package com.github.producer.kafka_producer;

import com.github.producer.model.Bus;
import com.github.producer.model.BusServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

@Component
public class Producer {

    final Logger LOG = LoggerFactory.getLogger(Producer.class);

    @Value("${app.kafka.topicName: first_topic}")
    private String topicName;

    @Autowired
    KafkaTemplate<String, Bus> kafkaTemplate;

    public void publish(BusServiceResponse messageToPublish) {

        try {

            String busStopCode = messageToPublish.getBusStopCode();

            messageToPublish.getServices().stream().forEach(service -> {

                String prefix = join(asList(busStopCode, service.getServiceNo(), "B"), "_");

                List<Bus> buses = asList(service.getNextBus(), service.getNextBus2(), service.getNextBus3());
                buses.stream().forEach(bus -> {

                    String key = prefix.concat(Integer.toString(buses.indexOf(bus)));

                   /* kafkaTemplate.send(record, (recordMetadata, e) -> {
                        if (e == null) {
                            LOG.info("Received new metadata");
                            LOG.info(format("Topic : {0}", recordMetadata.topic()));
                            LOG.info(format("Partition : {0}", recordMetadata.partition()));
                            LOG.info(format("Offset : {0}", recordMetadata.offset()));
                            LOG.info(format("Timestamp : {0}", recordMetadata.timestamp()));
                        } else
                            LOG.error(format("Record publishing exception : key={0}", key), e);
                    });*/

                    kafkaTemplate.send(topicName, key, bus);
                    LOG.info(format("Sent message with key={0}", key));

                });

            });

        } catch (Exception e) {
            LOG.error(format("Producer Exception for busStopCode={0}", messageToPublish.getBusStopCode()), e);
        }

    }

}
