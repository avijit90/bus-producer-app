package com.github.producer.kafka_producer;

import com.github.producer.model.Bus;
import com.github.producer.model.BusSerializer;
import com.github.producer.model.BusServiceResponse;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

@Component
public class Producer {

    final Logger LOG = LoggerFactory.getLogger(Producer.class);

    @Value("${app.kafka.bootstrapServer: localhost:9092}")
    private String bootstrapServer;

    @Value("${app.kafka.topicName: first_topic}")
    private String topicName;

    private KafkaProducer<String, Bus> producer;

    @Autowired
    KafkaTemplate<String, Bus> kafkaTemplate;

    @PostConstruct
    private void buildProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BusSerializer.class.getName());
        producer = new KafkaProducer<>(properties);
    }

    public void publish(BusServiceResponse messageToPublish) {

        try {

            String busStopCode = messageToPublish.getBusStopCode();

            messageToPublish.getServices().stream().forEach(service -> {

                String key = join(asList(busStopCode, service.getServiceNo(), "B"), "_");

                List<Bus> buses = asList(service.getNextBus(), service.getNextBus2(), service.getNextBus3());
                buses.stream().forEach(bus -> {

                    ProducerRecord<String, Bus> record =
                            new ProducerRecord<>(topicName, key.concat(Integer.toString(buses.indexOf(bus))), bus);

                    producer.send(record, (recordMetadata, e) -> {
                        if (e == null) {
                            LOG.info("Received new metadata");
                            LOG.info(format("Topic : {0}", recordMetadata.topic()));
                            LOG.info(format("Partition : {0}", recordMetadata.partition()));
                            LOG.info(format("Offset : {0}", recordMetadata.offset()));
                            LOG.info(format("Timestamp : {0}", recordMetadata.timestamp()));
                        } else
                            LOG.error(format("Record publishing exception : key={0}", key), e);
                    });

                });


            });

        } catch (Exception e) {
            LOG.error(format("Producer Exception for busStopCode={0}", messageToPublish.getBusStopCode()), e);
        } finally {
            producer.flush();
            producer.close();
        }

    }

}
