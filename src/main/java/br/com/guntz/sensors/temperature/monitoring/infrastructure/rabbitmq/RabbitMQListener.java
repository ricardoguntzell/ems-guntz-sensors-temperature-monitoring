package br.com.guntz.sensors.temperature.monitoring.infrastructure.rabbitmq;

import br.com.guntz.sensors.temperature.monitoring.api.model.TemperatureLogData;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static br.com.guntz.sensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE;

@AllArgsConstructor
@Slf4j
@Component
public class RabbitMQListener {

    @RabbitListener(queues = QUEUE)
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData,
                       @Headers Map<String, Object> headers) {
        log.info("Temperature updated: SensorId {} Temp {}",
                temperatureLogData.getSensorId().toString(),
                temperatureLogData.getValue().toString());

        log.info("Headers: {}", headers.toString());

        Thread.sleep(Duration.ofSeconds(10000).toSeconds());
    }

}
