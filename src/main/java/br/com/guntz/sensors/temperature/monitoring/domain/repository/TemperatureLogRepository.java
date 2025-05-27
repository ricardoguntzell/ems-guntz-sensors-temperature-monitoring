package br.com.guntz.sensors.temperature.monitoring.domain.repository;

import br.com.guntz.sensors.temperature.monitoring.domain.model.SensorId;
import br.com.guntz.sensors.temperature.monitoring.domain.model.TemperatureLog;
import br.com.guntz.sensors.temperature.monitoring.domain.model.TemperatureLogId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, TemperatureLogId> {

    Page<TemperatureLog> findAllBySensorId(SensorId sensorId, Pageable pageable);
}
