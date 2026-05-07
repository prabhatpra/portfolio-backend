package com.prabhat.portfolio.util;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

	private final Map<String, List<Long>> requestMap = new ConcurrentHashMap<>();
	
	private static final int LIMIT = 3;
	private static final long TIME_WINDOW = 10 * 60 * 1000;  // 10 minutes
	
	public boolean isAllowed(String email) {
		
		long now = Instant.now().toEpochMilli();
		
		requestMap.putIfAbsent(email, new CopyOnWriteArrayList<>());
		
		List<Long> timestamps = requestMap.get(email);
		
		timestamps.removeIf(time -> (now - time) > TIME_WINDOW);
		
		if (timestamps.size() >= LIMIT) {
			return false;
		}
		
		timestamps.add(now);
		return true;
	}
	
	@Scheduled(fixedRate = 60000)
	public void cleanUp() {
		long now = Instant.now().toEpochMilli();
		
		requestMap.values().forEach(list -> list.removeIf(time -> (now - time) > TIME_WINDOW));
	}
}
