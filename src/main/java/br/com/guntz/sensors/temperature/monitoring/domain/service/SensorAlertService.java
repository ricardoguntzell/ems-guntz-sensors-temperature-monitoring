package br.com.guntz.sensors.temperature.monitoring.domain.service;

import br.com.guntz.sensors.temperature.monitoring.api.model.TemperatureLogData;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorAlert;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorId;
import br.com.guntz.sensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void processAlertReading(TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> handleAlert(alert, temperatureLogData),
                        () -> logIgnoredAlert(temperatureLogData));
    }

    private void handleAlert(SensorAlert alert, TemperatureLogData temperatureLogData) {
        if (alert.getMaxTemperature() != null &&
                temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {

            logMaxTemperatureAlert(temperatureLogData);

        } else if (alert.getMinTemperature() != null &&
                temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {

            logMinTemperatureAlert(temperatureLogData);

        } else {
            logIgnoredAlert(temperatureLogData);
        }
    }

    private void logMaxTemperatureAlert(TemperatureLogData temperatureLogData) {
        log.info("Alert Max: SensorId {} Temp {}",
                temperatureLogData.getSensorId(),
                temperatureLogData.getValue());
    }

    private void logMinTemperatureAlert(TemperatureLogData temperatureLogData) {
        log.info("Alert Min: SensorId {} Temp {}",
                temperatureLogData.getSensorId(),
                temperatureLogData.getValue());
    }

    private void logIgnoredAlert(TemperatureLogData temperatureLogData) {
        log.info("Alert ignored: SensorId {} Temp {}",
                temperatureLogData.getSensorId(),
                temperatureLogData.getValue());
    }
}
