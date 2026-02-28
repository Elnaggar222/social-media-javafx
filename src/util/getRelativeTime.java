package util;

import java.time.LocalDateTime;

public class getRelativeTime {
	
	public static String formatTimeAgo(LocalDateTime time) {

	    long minutes = java.time.Duration.between(time, LocalDateTime.now()).toMinutes();

	    if (minutes < 1)
	        return "Just now";

	    if (minutes < 60)
	        return minutes + " mins ago";

	    long hours = minutes / 60;

	    if (hours < 24)
	        return hours + " hrs ago";

	    long days = hours / 24;

	    return days + " days ago";
	}

}
