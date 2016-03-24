package se.alten.swearc.webserver.dummy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RandomLogGenerator {

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	private final int delaySeconds;

	public RandomLogGenerator(int delaySeconds) {
		this.delaySeconds = delaySeconds;
	}

	public void startLogging(Consumer<String> logger) {

		Runnable runnable = () -> {
			String words = RandomWords.get();
			System.out.println("Random log message: " + words);
			logger.accept(words);
		};

		scheduler.scheduleAtFixedRate(runnable, delaySeconds, delaySeconds,
				TimeUnit.SECONDS);
	}
}
