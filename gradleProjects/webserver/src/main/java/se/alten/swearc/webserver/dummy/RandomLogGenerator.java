package se.alten.swearc.webserver.dummy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import se.alten.swearc.logging.Logger;

public class RandomLogGenerator {

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	private final int delaySeconds;

	public RandomLogGenerator(int delaySeconds) {
		this.delaySeconds = delaySeconds;
	}

	public void startLogging(Logger logger) {

		Runnable runnable = () -> {
			String words = RandomWords.get();
			System.out.println("Random log message: " + words);
			logger.message(words);
		};

		scheduler.scheduleAtFixedRate(runnable, delaySeconds, delaySeconds,
				TimeUnit.SECONDS);
	}
}
