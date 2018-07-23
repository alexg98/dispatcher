package com.test.dispatcher;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Class utilities
 * @author alexg98@gmail.com 
 */
public final class Utilities {

	private static final Logger logger = LoggerFactory.getLogger(Utilities.class);
	
	public static final void sleepSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        	logger.error(e.getMessage());
        }
    }
}
