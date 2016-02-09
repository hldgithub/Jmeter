import java.util.Calendar;

import javax.xml.stream.XMLStreamReader;

/**
 * 
 * Represents the data required for calculating the throughput
 * 
 */
public abstract class Event {
    
    /** reader */
	protected XMLStreamReader reader;
    
	/**
	 * get calendar attribute
	 * 
	 * @param index index
	 * @return Calendar
	 * @throws Exception exception
	 */
	public abstract Calendar getCalendarAttribute(int index) throws Exception;
	
	/**
	 * get string attribute
	 * 
	 * @param index index
	 * @return string
	 * @throws Exception exception
	 */
	public abstract String getStringAttribute(int index) throws Exception;
	
	/**
	 * get characters
	 * 
	 * @return string
	 * @throws Exception exception
	 */
	public abstract String getCharacters() throws Exception;
	
	/**
	 * set reader
	 * 
	 * @param reader xml stream reader
	 */
    public void setReader(XMLStreamReader reader) {
    	this.reader = reader;
    }
    
    /**
     * get reader
     * 
     * @return XMLStreamReader
     */
    public XMLStreamReader getReader() {
    	return reader;
    }
    
}
