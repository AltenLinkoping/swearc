package se.alten.swearc.webserver;

import org.junit.Assert;
import org.junit.Test;

import se.alten.swearc.logging.LogMessage;

public class LogMessageTest {

	@Test
	public void testIsEqual() throws Exception {
		String message = "Some Message 12345";

		LogMessage msg1 = LogMessage.create(message);
		LogMessage msg2 = LogMessage.create(message);

		Assert.assertEquals(msg1, msg2);
	}
}
