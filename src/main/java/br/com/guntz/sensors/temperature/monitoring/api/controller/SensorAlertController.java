package br.com.guntz.sensors.temperature.monitoring.api.controller;

import br.com.guntz.sensors.temperature.monitoring.api.model.SensorAlertInput;
import br.com.guntz.sensors.temperature.monitoring.api.model.SensorAlertOutput;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorAlert;
import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorId;
import br.com.guntz.sensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public ResponseEntity<SensorAlertOutput> getSensorById(@PathVariable TSID sensorId) {
        SensorAlertOutput sensorAlertOutput = convertToOutputModel(findByIdOrException(sensorId));

        return ResponseEntity.ok(sensorAlertOutput);
    }

    @PutMapping
    public ResponseEntity<SensorAlertOutput> saveOrUpdate(@PathVariable TSID sensorId, @RequestBody SensorAlertInput sensorAlertInput) {
        SensorAlert sansorAlertLocated = findByIdOrDefault(sensorId, sensorAlertInput);

        SensorAlert sensorSaved = sensorAlertRepository.saveAndFlush(sansorAlertLocated);

        return ResponseEntity.ok(convertToOutputModel(sensorSaved));
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@PathVariable TSID sensorId) {
        SensorAlert sansorAlertLocated = findByIdOrException(sensorId);

        sensorAlertRepository.delete(sansorAlertLocated);

        return ResponseEntity.noContent().build();
    }

    private SensorAlert findByIdOrException(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private SensorAlert findByIdOrDefault(TSID sensorId, SensorAlertInput sensorAlertInput) {
        return sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .maxTemperature(sensorAlertInput.getMaxTemperature())
                        .minTemperature(sensorAlertInput.getMinTemperature())
                        .build()
                );
    }

    private SensorAlertOutput convertToOutputModel(SensorAlert sensorAlert) {
        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .minTemperature(sensorAlert.getMinTemperature())
                .build();
    }
}
