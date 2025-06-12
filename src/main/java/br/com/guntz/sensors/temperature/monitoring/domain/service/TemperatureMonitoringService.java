package br.com.guntz.sensors.temperature.monitoring.domain.service;

import br.com.guntz.sensors.temperature.monitoring.api.model.TemperatureLogData;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorId;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorMonitoring;
import br.com.guntz.sensors.temperature.monitoring.domain.model.TemperatureLog;
import br.com.guntz.sensors.temperature.monitoring.domain.model.TemperatureLogId;
import br.com.guntz.sensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import br.com.guntz.sensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@AllArgsConstructor
@Service
public class TemperatureMonitoringService {

    private final SensorMonitoringRepository sensorMonitoringRepository;
    private TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {
        log.info("processTemperatureReading");

        if (temperatureLogData.getValue().equals(10.5)){
            throw new RuntimeException("Test error retry");
        }

        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(
                        sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () -> logIgnoredTemperature(temperatureLogData)
                );
    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLasTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = convertToTemperatureLog(temperatureLogData);
            temperatureLogRepository.save(temperatureLog);

            log.info("Temperature Updated: SensorId {} Temp {}",
                    temperatureLogData.getSensorId(),
                    temperatureLogData.getValue());
        } else {
            logIgnoredTemperature(temperatureLogData);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Temperature ignored: SensorId {} Temp {}",
                temperatureLogData.getSensorId(),
                temperatureLogData.getValue());
    }

    private TemperatureLog convertToTemperatureLog(TemperatureLogData temperatureLogData) {
        return TemperatureLog.builder()
                .id(new TemperatureLogId(temperatureLogData.getId()))
                .registeredAt(temperatureLogData.getRegisteredAt())
                .value(temperatureLogData.getValue())
                .sensorId(new SensorId(temperatureLogData.getSensorId()))
                .build();
    }

}
