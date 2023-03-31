package by.afinny.investments.config;

import by.afinny.investments.config.properties.KafkaConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaConfigProperties.class)
public class KafkaProducerConfig {

    private KafkaProperties kafkaProperties;
    private final KafkaConfigProperties config;
    private String BOOTSTRAP_SERVERS;

    @PostConstruct
    private void createKafkaProperties() {
        kafkaProperties = config.getKafkaProperties();
        BOOTSTRAP_SERVERS = config.getBootstrapServers();
    }

    @Bean
    public DefaultKafkaProducerFactory<String, Object> producerFactoryForWithdrawInsurance() {
        Map<String, Object> kafkaProducerProperties = getKafkaProducerProperties();
        return new DefaultKafkaProducerFactory<>(kafkaProducerProperties);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactoryForWithdrawInsurance) {
        return new KafkaTemplate<>(producerFactoryForWithdrawInsurance);
    }

    private Map<String, Object> getKafkaProducerProperties() {
        Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties();

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProperties.get("key.serializer"));
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerProperties.get("value.serializer"));
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);

        return props;
    }
}
