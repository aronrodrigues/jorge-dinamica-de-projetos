

public class Logger {

	public static final void log(String message, Object... args) {
		
		System.out.println(String.format(message, args));
		
	} 
	
}
