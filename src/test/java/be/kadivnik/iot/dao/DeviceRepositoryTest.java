package be.kadivnik.iot.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import be.kadivnik.iot.BasePersistenceTest;
import be.kadivnik.iot.entity.Device;
import be.kadivnik.iot.persistence.annotation.IotTransactional;
import be.kadivnik.iot.persistence.dao.DeviceRepository;

public class DeviceRepositoryTest extends BasePersistenceTest {

	@Autowired
	private DeviceRepository dao;
	
	@Test
	@IotTransactional
	public void crudTest() {
		Device device = new Device();
		device.setName("testDevice");
		device.setLocation("myOffice");
		device.setAddress("outerSpace");

		dao.save(device);
		
		List<Device> devicesFound = dao.findByName("testDevice");
		Assert.assertTrue(devicesFound.size() == 1);

		Device deviceFound = devicesFound.get(0);
		Assert.assertEquals("testDevice", deviceFound.getName());
		Assert.assertEquals("myOffice", deviceFound.getLocation());
		Assert.assertEquals("outerSpace", deviceFound.getAddress());
		
		dao.delete(deviceFound);
		
		devicesFound = dao.findByName("testDevice");
		Assert.assertTrue(devicesFound.size() == 0);
	}
}
