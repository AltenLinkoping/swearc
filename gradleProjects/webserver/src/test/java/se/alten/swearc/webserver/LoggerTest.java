package se.alten.swearc.webserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import se.alten.swearc.logging.LogMessage;
import se.alten.swearc.logging.Logger;

public class LoggerTest {

	private Logger logger;
	private Consumer<LogMessage> consumer;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {

		consumer = mock(Consumer.class);

		logger = Logger.create(consumer);
	}

	@Test
	public void testCanCreateLogger() throws Exception {
		Assert.assertNotNull(logger);
	}

	@Test
	public void testSendLogMessage() throws Exception {

		logger.message("some message");

		verify(consumer).accept(LogMessage.create("some message"));
	}

}
