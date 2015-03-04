package logger;

public class SCLogger {
	public static final byte 
		LOG_DEBUG = 0,
		LOG_PARAMS = 1,
		LOG_CALLS = 2,
		LOG_INFO = 3,
		LOG_WARNING = 4,
		LOG_SEVERE = 5,
		LOG_PRODUCTION = 6;
		
	public static byte LOGGING_LEVEL = LOG_DEBUG;
	
	public static void log(String message, byte level){
		if (level >= LOGGING_LEVEL){
			System.out.println(level + ": " + message);
		}
	}
}
