package es.upm.grise.cruiseControl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.upm.grise.cruiseControl.exceptions.CannotSetSpeedLimitException;
import es.upm.grise.cruiseControl.exceptions.IncorrectSpeedLimitException;
import es.upm.grise.cruiseControl.exceptions.IncorrectSpeedSetException;
import es.upm.grise.cruiseControl.exceptions.SpeedSetAboveSpeedLimitException;

class CruiseControlTest {

	private RoadInformation roadInfo;
	private Speedometer speedometer;
	private CruiseControl cruiseControl;

	@BeforeEach
	void setUp() {
		roadInfo = new RoadInformation(120, 60);
		speedometer = new Speedometer(100);
		cruiseControl = new CruiseControl(roadInfo, speedometer);
	}

	@Test
	void testConstructorInitialState() {
		assertNull(cruiseControl.getSpeedLimit(), "Initial speedLimit should be null");
		assertNull(cruiseControl.getSpeedSet(), "Initial speedSet should be null");
		assertFalse(cruiseControl.isEnabled(), "CruiseControl should be disabled initially");
	}

	@Test
	void testSetSpeedSetSuccessWhenLimitIsNull() throws SpeedSetAboveSpeedLimitException, IncorrectSpeedSetException {
		cruiseControl.setSpeedSet(80);
		assertEquals(80, cruiseControl.getSpeedSet(), "speedSet should be updated to 80");
		assertTrue(cruiseControl.isEnabled(), "CruiseControl should be enabled");
	}

	@Test
	void testSetSpeedSetThrowsIncorrectSpeedSetExceptionWhenLimitIsNull() {
		assertThrows(IncorrectSpeedSetException.class, () -> cruiseControl.setSpeedSet(0),
				"Setting speedSet to 0 should throw IncorrectSpeedSetException");
		assertThrows(IncorrectSpeedSetException.class, () -> cruiseControl.setSpeedSet(-10),
				"Setting negative speedSet should throw IncorrectSpeedSetException");
	}

	@Test
	void testSetSpeedSetSuccessWhenLimitIsNonNull() throws CannotSetSpeedLimitException, IncorrectSpeedLimitException, SpeedSetAboveSpeedLimitException, IncorrectSpeedSetException {
		cruiseControl.setSpeedLimit(90);
		cruiseControl.setSpeedSet(80);
		assertEquals(80, cruiseControl.getSpeedSet(), "speedSet should be updated to 80");
		assertTrue(cruiseControl.isEnabled(), "CruiseControl should be enabled");
		
		// Set speedSet equal to speedLimit
		cruiseControl.setSpeedSet(90);
		assertEquals(90, cruiseControl.getSpeedSet(), "speedSet should be updated to 90");
	}

	@Test
	void testSetSpeedSetThrowsSpeedSetAboveSpeedLimitExceptionWhenLimitIsNonNull() throws CannotSetSpeedLimitException, IncorrectSpeedLimitException {
		cruiseControl.setSpeedLimit(90);
		assertThrows(SpeedSetAboveSpeedLimitException.class, () -> cruiseControl.setSpeedSet(95),
				"Setting speedSet above speedLimit should throw SpeedSetAboveSpeedLimitException");
	}

	@Test
	void testSetSpeedLimitSuccessWhenSpeedSetIsNull() throws CannotSetSpeedLimitException, IncorrectSpeedLimitException {
		cruiseControl.setSpeedLimit(110);
		assertEquals(110, cruiseControl.getSpeedLimit(), "speedLimit should be updated to 110");
	}

	@Test
	void testSetSpeedLimitThrowsIncorrectSpeedLimitExceptionWhenSpeedSetIsNull() {
		assertThrows(IncorrectSpeedLimitException.class, () -> cruiseControl.setSpeedLimit(0),
				"Setting speedLimit to 0 should throw IncorrectSpeedLimitException");
		assertThrows(IncorrectSpeedLimitException.class, () -> cruiseControl.setSpeedLimit(-5),
				"Setting negative speedLimit should throw IncorrectSpeedLimitException");
	}

	@Test
	void testSetSpeedLimitThrowsCannotSetSpeedLimitExceptionWhenSpeedSetIsNonNull() throws SpeedSetAboveSpeedLimitException, IncorrectSpeedSetException {
		cruiseControl.setSpeedSet(70);
		assertThrows(CannotSetSpeedLimitException.class, () -> cruiseControl.setSpeedLimit(100),
				"Setting speedLimit when speedSet is non-null should throw CannotSetSpeedLimitException");
	}

	@Test
	void testDisableResetsState() throws SpeedSetAboveSpeedLimitException, IncorrectSpeedSetException {
		cruiseControl.setSpeedSet(80);
		assertTrue(cruiseControl.isEnabled());
		assertEquals(80, cruiseControl.getSpeedSet());

		cruiseControl.disable();
		assertFalse(cruiseControl.isEnabled(), "CruiseControl should be disabled");
		assertNull(cruiseControl.getSpeedSet(), "speedSet should be reset to null");
	}
}
