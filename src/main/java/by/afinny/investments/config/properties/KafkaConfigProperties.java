package by.afinny.investments.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigProperties {
    @Autowired
    private KafkaProperties kafkaProperties;
    private String bootstrapServers;
    private Map<String, Topic> topics = new HashMap<>();

    @Getter
    @Setter
    public static class Topic {
        private String path;
        private boolean enabled;
    }
}
