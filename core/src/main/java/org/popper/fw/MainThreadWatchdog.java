package org.popper.fw;

import java.awt.Window;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

public class MainThreadWatchdog extends Thread {
	private final long maxStackTraceAge;
	
	private final long waitTime;
	
	private StackTraceElement[] oldStackTrace = null;
	
	private long stackTraceTimestamp;
	
	public MainThreadWatchdog(long maxStackTraceAge, long waitTime) {
		this.maxStackTraceAge = maxStackTraceAge;
		this.waitTime = waitTime;
	}
	
	public static void main(String[] args) throws Exception {
		new MainThreadWatchdog(100000, 10).start();
		Thread.sleep(1000);
	}
	
	@Override
	public void run() {
		boolean mainThreadExist = true;
		while (mainThreadExist) {
			try {
				mainThreadExist = checkMainThread();
				Thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean checkMainThread() {
		StackTraceElement[] currentStackTrace = stackTraceFromMainThread();
		if (currentStackTrace == null) {
			return false;
		}
		if (!equals(currentStackTrace, oldStackTrace)) {
			oldStackTrace = currentStackTrace;
			stackTraceTimestamp = System.currentTimeMillis();
		}
		
		if (System.currentTimeMillis() - maxStackTraceAge > stackTraceTimestamp) {
			lockDetected();
		}
		
		return true;
	}
	
	protected void lockDetected() {
		System.out.println("lock detected in main thread");
		System.out.println(stackTraceToString(oldStackTrace));
		
		for (Window window : Window.getWindows()) {
			System.out.println(window);
		}
		
		System.exit(-1);
	}
	
	private String stackTraceToString(StackTraceElement[] stackTrace) {
		StringBuilder builder = new StringBuilder();
		for (StackTraceElement ele : stackTrace) {
			builder.append(ele.toString()).append("\n");
		}
		
		return builder.toString();
	}
	
	private StackTraceElement[] stackTraceFromMainThread() {
		Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
		for (Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
			if (entry.getKey().getId() == 1L) {
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	private boolean equals(StackTraceElement[] stackTrace1, StackTraceElement[] stackTrace2) {
		if (stackTrace1 == null ^ stackTrace2 == null) {
			return false;
		}
		
		return Arrays.asList(stackTrace1).equals(Arrays.asList(stackTrace2));
	}
}
