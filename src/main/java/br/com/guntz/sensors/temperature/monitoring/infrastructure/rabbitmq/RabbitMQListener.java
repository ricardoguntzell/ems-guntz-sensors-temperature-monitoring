package br.com.guntz.sensors.temperature.monitoring.infrastructure.rabbitmq;

import br.com.guntz.sensors.temperature.monitoring.api.model.TemperatureLogData;
import br.com.guntz.sensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static br.com.guntz.sensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE;

@AllArgsConstructor
@Slf4j
@Component
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE, concurrency = "2-3")
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData) {

        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5000).toSeconds());
    }

}
