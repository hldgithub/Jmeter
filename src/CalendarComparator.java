import java.util.Calendar;
import java.util.Comparator;

/**
 * 
 * calendar comparator
 *
 */
public class CalendarComparator implements Comparator<Calendar> {

    /**
     * comparator
     * 
     * @param arg0 Calendar
     * @param arg1 Calendar
     * @return int
     */
	@Override
	public int compare(Calendar arg0, Calendar arg1) {
	    int result = 0;
	    
		if (arg0.before(arg1)) {
		    result = -1;
	    }
	    
	    if (arg0.after(arg1)) {
	        result = 1; 
	    }
	    
	    return result;
	}
	
}
