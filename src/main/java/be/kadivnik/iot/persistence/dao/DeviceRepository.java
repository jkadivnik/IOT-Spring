package be.kadivnik.iot.persistence.dao;

import java.util.List;

import be.kadivnik.iot.entity.Device;

public interface DeviceRepository extends BaseRepository<Device, Long> {

	List<Device> findByName(String name);
}
