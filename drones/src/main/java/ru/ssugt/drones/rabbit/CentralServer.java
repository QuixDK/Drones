package ru.ssugt.drones.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ssugt.drones.api.dto.FlightMission;
import ru.ssugt.drones.rabbit.dto.DockStationCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class CentralServer {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String fanoutExchange;
    private final String directExchange;
    private final int requestDockStationStatusTimeout;

    private final ThreadLocal<List<DockStationCallback>> responses = new ThreadLocal<>();
    private final ThreadLocal<String> correlationId = new ThreadLocal<>();
    private final ThreadLocal<CountDownLatch> latch = new ThreadLocal<>();

    public CentralServer(RabbitTemplate rabbitTemplate,
                         ObjectMapper objectMapper,
                         @Value("${rabbit.fanout.exchange.name}") String fanoutExchange,
                         @Value("${rabbit.direct.exchange.name}") String directExchange,
                         @Value("${rabbit.request.dockstation.timeout}") int requestDockStationStatusTimeout) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.fanoutExchange = fanoutExchange;
        this.directExchange = directExchange;
        this.requestDockStationStatusTimeout = requestDockStationStatusTimeout;
    }

    public List<DockStationCallback> requestStatus() {
        responses.set(new ArrayList<>());
        correlationId.set(UUID.randomUUID().toString());
        latch.set(new CountDownLatch(1));

        // Отправка запроса статуса
        Message message = MessageBuilder.withBody("status_request".getBytes(StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setCorrelationId(correlationId.get())
                .setReplyTo("amq.rabbitmq.reply-to") // Используем встроенный reply-to
                .build();

        rabbitTemplate.send(fanoutExchange, "", message);
        log.info("Запрос статуса отправлен всем станциям...");

        try {
            // Ждем ответы в течение таймаута
            latch.get().await(requestDockStationStatusTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Ожидание ответов было прервано");
        }

        log.debug("Все ответы: " + responses.get());
        return responses.get();
    }

//    @RabbitListener(queues = "amq.rabbitmq.reply-to", ackMode = "AUTO") // Явно указываем AUTO
//    public void handleStatusResponse(Message message) {
//        try {
//            if (message.getMessageProperties().getCorrelationId().equals(correlationId.get())) {
//                DockStationCallback response = objectMapper.readValue(
//                        message.getBody(),
//                        DockStationCallback.class
//                );
//                responses.get().add(response);
//                log.debug("Received response: {}", response);
//            }
//        } catch (IOException e) {
//            log.error("Message processing error", e);
//        }
//    }

    public void sendTask(String stationId, FlightMission flightMission) {
        try {
            rabbitTemplate.convertAndSend(directExchange, stationId, flightMission);
            log.debug("Задание отправлено на станцию {}: {}", stationId, objectMapper.writeValueAsString(flightMission));
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализации задания", e);
        }
    }
}
