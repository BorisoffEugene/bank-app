package ru.yandex.practicum.accounts.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import ru.yandex.practicum.accounts.dto.NotificationDto;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Интеграционное тестирование Kafka")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@EmbeddedKafka(topics = {KafkaTest.TEST_TOPIC_NAME})
public class KafkaTest {
    public static final String TEST_TOPIC_NAME = "notification";

    @Autowired
    private KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    @DisplayName("Тестируем Кафку")
    public void testKafka() {
        var consumerProps = KafkaTestUtils.consumerProps("bank-app", "true", embeddedKafkaBroker);

        try (var consumerForTest = new DefaultKafkaConsumerFactory<String, NotificationDto>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(NotificationDto.class)
        ).createConsumer()) {

            consumerForTest.subscribe(List.of(TEST_TOPIC_NAME));

            kafkaTemplate.send(TEST_TOPIC_NAME, "Test", new NotificationDto("Test Message"));

            ConsumerRecord<String, NotificationDto> inputMessage =
                    KafkaTestUtils.getSingleRecord(consumerForTest, TEST_TOPIC_NAME, Duration.ofSeconds(5));

            assertThat(inputMessage.key()).isEqualTo("Test");
            assertThat(inputMessage.value().message()).isEqualTo("Test Message");
        }
    }
}