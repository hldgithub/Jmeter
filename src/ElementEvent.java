import java.util.Calendar;

/**
 * 
 * Provides implementation for handling attributes
 * 
 */
public class ElementEvent extends Event {
	
    /**
     * get calendar attribute
     * 
     * @param index index
     * @return Calendar
     * @throws Exception exception
     */
	@Override
	public Calendar getCalendarAttribute(int index) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.valueOf(reader.getAttributeValue(index)));
		return c;
	}

    /**
     * get string attribute
     * 
     * @param index index
     * @return string
     * @throws Exception exception
     */
	@Override
	public String getStringAttribute(int index) throws Exception {
		String cuName = reader.getAttributeValue(index);
		return cuName;
	}

    /**
     * get characters
     * 
     * @return string
     * @throws Exception exception
     */
	@Override
	public String getCharacters() throws Exception {
		return reader.getText();
	}

}
