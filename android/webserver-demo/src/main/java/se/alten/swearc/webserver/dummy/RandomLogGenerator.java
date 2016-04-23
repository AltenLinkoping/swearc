package se.alten.swearc.webserver.dummy;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class RandomLogGenerator {

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	private final int delaySecs;

	public RandomLogGenerator(int delaySeconds) {
		this.delaySecs = delaySeconds;
	}

	public void startLogging(Consumer<String> logger) {

		Runnable runnable = () -> {
			String words = RandomWords.get();
			System.out.println("Random log message: " + words);
			logger.accept(words);
		};

		scheduler.scheduleAtFixedRate(runnable, delaySecs, delaySecs, SECONDS);
	}

	public void stop() {
		scheduler.shutdown();
	}
}
