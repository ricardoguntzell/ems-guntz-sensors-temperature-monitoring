package br.com.guntz.sensors.temperature.monitoring.infrastructure.rabbitmq;

import br.com.guntz.sensors.temperature.monitoring.api.model.TemperatureLogData;
import br.com.guntz.sensors.temperature.monitoring.domain.service.SensorAlertService;
import br.com.guntz.sensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static br.com.guntz.sensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static br.com.guntz.sensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE;

@AllArgsConstructor
@Slf4j
@Component
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;
    private final SensorAlertService sensorAlertService;

    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    @SneakyThrows
    public void handleProcessTemperature(@Payload TemperatureLogData temperatureLogData) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
//        Thread.sleep(Duration.ofSeconds(5000).toSeconds());
    }

    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    @SneakyThrows
    public void handleAlerting(@Payload TemperatureLogData temperatureLogData) {
        sensorAlertService.processAlertReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5000).toSeconds());
    }


}
