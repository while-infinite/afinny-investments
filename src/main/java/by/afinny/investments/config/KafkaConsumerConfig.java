package by.afinny.investments.config;

import by.afinny.investments.config.properties.KafkaConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaConfigProperties.class)
public class KafkaConsumerConfig {

    private final KafkaConfigProperties config;
    private KafkaProperties kafkaProperties;
    private String BOOTSTRAP_SERVERS;

    @PostConstruct
    private void createKafkaProperties() {
        kafkaProperties = config.getKafkaProperties();
        BOOTSTRAP_SERVERS = config.getBootstrapServers();
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> stubConsumerFactoryNewAccountAgree() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.investments.dto.kafka.ProducerNewAccountAgree");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "stubKafkaListenerNewAccountAgree")
    public ConcurrentKafkaListenerContainerFactory<String, Object> stubFactoryNewAccountAgree() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stubConsumerFactoryNewAccountAgree());

        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> consumerFactoryNewAccountAgree() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.investments.dto.kafka.ConsumerNewAccountAgree");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "kafkaListenerNewAccountAgree")
    public ConcurrentKafkaListenerContainerFactory<String, Object> factoryNewAccountAgree() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryNewAccountAgree());

        return factory;
    }

    private Map<String, Object> getKafkaConsumerProperties() {
        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerProperties.get("key.deserializer"));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerProperties.get("value.deserializer"));
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);

        return props;
    }
}
