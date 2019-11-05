package com.github.producer.kafka_producer;

import com.github.producer.model.Bus;
import com.github.producer.model.BusServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

@Component
public class BusMessageProducer {

    private final Logger LOG = LoggerFactory.getLogger(BusMessageProducer.class);

    @Value("${app.kafka.topicName: first_topic}")
    private String topicName;

    @Autowired
    private
    KafkaTemplate<String, Bus> kafkaTemplate;

    public void publish(BusServiceResponse messageToPublish) {

        String busStopCode = messageToPublish.getBusStopCode();
        messageToPublish.getServices().forEach(service -> {

            String key = join(asList(busStopCode, service.getServiceNo()), "_");
            List<Bus> buses = service.getBusList();
            buses.forEach(bus -> {

                ListenableFuture<SendResult<String, Bus>> listenableFuture = kafkaTemplate.send(topicName, key, bus);
                listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, Bus>>() {

                    @Override
                    public void onFailure(Throwable e) {
                        LOG.error(format("Record publishing exception : key={0}", key), e);
                    }

                    @Override
                    public void onSuccess(SendResult<String, Bus> result) {
                        LOG.info("-----------------------------------------------------------");
                        LOG.info(format("Message with key={0}", key));
                        LOG.info(format("Topic : {0}", result.getRecordMetadata().topic()));
                        LOG.info(format("Partition : {0}", result.getRecordMetadata().partition()));
                        LOG.info(format("Offset : {0}", result.getRecordMetadata().offset()));
                        LOG.info(format("Timestamp : {0}", result.getRecordMetadata().timestamp()));
                        LOG.info("-----------------------------------------------------------");
                    }
                });

            });

        });
    }

}
