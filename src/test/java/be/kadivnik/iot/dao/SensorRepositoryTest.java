package be.kadivnik.iot.dao;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.kadivnik.iot.BasePersistenceTest;
import be.kadivnik.iot.entity.Device;
import be.kadivnik.iot.entity.Sensor;
import be.kadivnik.iot.entity.enums.SensorType;
import be.kadivnik.iot.persistence.annotation.IotTransactional;
import be.kadivnik.iot.persistence.dao.SensorRepository;

public class SensorRepositoryTest extends BasePersistenceTest {

	@Autowired
	private SensorRepository dao;
	
	@Test
	@IotTransactional
	public void crudTest() {
		Device device = new Device();
		device.setName("testSensorDevice");
		device.setLocation("myOffice");
		device.setAddress("outerSpace");

		Sensor sensor = new Sensor();
		sensor.setDevice(device);
		sensor.setType(SensorType.HUMIDITY);
		sensor.setValue("getItOn");

		dao.save(sensor);
		
		List<Sensor> sensorsFound = dao.findAll();
		
		Optional<Sensor> sensorOptional = sensorsFound.stream()
			.filter(sensorFound -> sensorFound.getDevice().getName().equalsIgnoreCase("testSensorDevice"))
			.findFirst();
		Assert.assertTrue(sensorOptional.isPresent());
		
		Sensor sensorToTest = sensorOptional.get();
		Assert.assertEquals("testSensorDevice", sensorToTest.getDevice().getName());
		Assert.assertEquals("myOffice", sensorToTest.getDevice().getLocation());
		Assert.assertEquals("outerSpace", sensorToTest.getDevice().getAddress());
		Assert.assertEquals("getItOn", sensorToTest.getValue());
		Assert.assertEquals(SensorType.HUMIDITY, sensorToTest.getType());
		
		dao.delete(sensorToTest);
		
		sensorOptional = dao.findOne(sensorToTest.getId());
		Assert.assertFalse(sensorOptional.isPresent());
	}
}
