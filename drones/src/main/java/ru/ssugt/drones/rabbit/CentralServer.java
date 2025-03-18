package ru.ssugt.drones.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.ssugt.drones.api.dto.FlightMission;
import ru.ssugt.drones.rabbit.dto.DockStationCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Log4j2
@Component
@PropertySource("application.properties")
public class CentralServer {

    @Value("${rabbit.fanout.exchange.name}")
    private String FANOUT_EXCHANGE;
    @Value("${rabbit.direct.exchange.name}")
    private String DIRECT_EXCHANGE;
    @Value("${rabbit.request.dockstation.timeout}")
    private Integer requestDockStationStatusTimeout;
    @Value("${spring.rabbitmq.host:localhost}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.port:5672}")
    private int rabbitPort;
    @Value("${spring.rabbitmq.username:user}")
    private String rabbitUsername;
    @Value("${spring.rabbitmq.password:1}")
    private String rabbitPassword;
    private Connection connection;
    private Channel channel;
    private String callbackQueueName;
    private final ObjectMapper objectMapper;


    @Autowired
    public CentralServer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setPort(rabbitPort);
        factory.setUsername(rabbitUsername);
        factory.setPassword(rabbitPassword);
        connection = factory.newConnection();
        channel = connection.createChannel();

        // Объявляем fanout exchange (По сути маршрутизатор, который доставит сообщение всем док-станциям)
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);

        callbackQueueName = channel.queueDeclare().getQueue();
    }

    public List<DockStationCallback> requestStatus() throws IOException {
        List<DockStationCallback> responses = new ArrayList<>();
        String correlationId = UUID.randomUUID().toString();

        channel.basicPublish(FANOUT_EXCHANGE, "", new AMQP.BasicProperties.Builder()
                .correlationId(correlationId)
                .replyTo(callbackQueueName)
                .build(),
                "status_request".getBytes(StandardCharsets.UTF_8));

        log.info("Запрос статуса отправлен всем станциям...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                DockStationCallback response =  objectMapper.readValue(delivery.getBody(), DockStationCallback.class);
                responses.add(response);
                log.debug("Получен ответ: " + response);
            }
        };
        channel.basicConsume(callbackQueueName, true, deliverCallback, consumerTag -> {});


        try {
            Thread.sleep(requestDockStationStatusTimeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("Все ответы: " + responses);
        return responses;
    }

    public void sendTask(String stationId, FlightMission flightMission) throws IOException {
        channel.basicPublish(DIRECT_EXCHANGE, stationId, null, objectMapper.writeValueAsString(flightMission).getBytes(StandardCharsets.UTF_8));
        log.debug("Задание отправлено на станцию " + stationId + ": " + objectMapper.writeValueAsString(flightMission));
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

}