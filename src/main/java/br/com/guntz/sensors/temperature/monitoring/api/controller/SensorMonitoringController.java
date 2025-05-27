package br.com.guntz.sensors.temperature.monitoring.api.controller;

import br.com.guntz.sensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorId;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorMonitoring;
import br.com.guntz.sensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
public class SensorMonitoringController {

    private final SensorMonitoringRepository sensorMonitoringRepository;

    @GetMapping
    public ResponseEntity<SensorMonitoringOutput> getDetail(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);

        return ResponseEntity.ok(convertToOutputModel(sensorMonitoring));
    }

    @PutMapping("/enable")
    public ResponseEntity<Object> enable(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);

        sensorMonitoring.setEnabled(true);
        sensorMonitoringRepository.saveAndFlush(sensorMonitoring);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/enable")
    public ResponseEntity<Object> disable(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);

        sensorMonitoring.setEnabled(false);
        sensorMonitoringRepository.saveAndFlush(sensorMonitoring);

        return ResponseEntity.noContent().build();
    }

    private SensorMonitoring findByIdOrDefault(TSID sensorId) {
        return sensorMonitoringRepository.findById(new SensorId(sensorId))
                .orElse(SensorMonitoring.builder()
                        .id(new SensorId(sensorId))
                        .enabled(false)
                        .lasTemperature(null)
                        .updatedAt(null)
                        .build()
                );
    }

    private SensorMonitoringOutput convertToOutputModel(SensorMonitoring sensorMonitoring) {
        return SensorMonitoringOutput.builder()
                .id(sensorMonitoring.getId().getValue())
                .lasTemperature(sensorMonitoring.getLasTemperature())
                .updatedAt(sensorMonitoring.getUpdatedAt())
                .enabled(sensorMonitoring.getEnabled())
                .build();
    }

}
