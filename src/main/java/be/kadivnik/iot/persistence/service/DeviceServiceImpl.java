package be.kadivnik.iot.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;

import be.kadivnik.iot.entity.Device;
import be.kadivnik.iot.persistence.dao.DeviceRepository;

public class DeviceServiceImpl {

	@Autowired
	private DeviceRepository deviceRepository;
	
	public void save(Device device) {
		deviceRepository.save(device);
	}
}
