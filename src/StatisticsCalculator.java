import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * 
 * Calculates the throughput of the performance test.  The calculation is based off
 * the data.xml file that is produced from Jmeter.
 * 
 */
public class StatisticsCalculator {
	
    /** element name */
	private static final String ELEMENT_NAME = "httpSample";
    /** Transactions under 0.3 seconds */
	private static final BigDecimal sla1Time = new BigDecimal(0.30);
    /** Transactions under 0.5 seconds */
	private static final BigDecimal sla2Time = new BigDecimal(0.50);
    /** Transactions under 2.0 seconds */
	private static final BigDecimal sla3Time = new BigDecimal(2.00);
    /** Transactions under 2.0 seconds */
    private static final BigDecimal sla4Time = new BigDecimal(3.00);
	
    /**
     * Main Controller
     * 
     * @param args args
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception {
        
        if (args[0] == null || args[0].trim().equals("")) {
            System.out.print("USAGE: StatisticsCalculator < file name | file path | Name Of Run | Run Up Time (sec) | "
                + "Connect Timeout | Response Timeout | server name>");
            System.exit(0);
        }
        
        PerformanceStatistic.setNameOfRun(args[2]);
        PerformanceStatistic.setRunTime(Integer.valueOf(args[3]));
        PerformanceStatistic.setConnectTimeout(Integer.valueOf(args[4]));
        PerformanceStatistic.setResponseTimeout(Integer.valueOf(args[5]));
        PerformanceStatistic.setServerName(args[6]);
        PerformanceStatistic.setAppName(args[7]);
        
        System.out.println("Reading Jmeter statistics...");
        
        XMLStreamReader reader = readInput(args[0], args[1]);
        buildThroughputStats(reader);
        reader.close();
      
        System.out.println("Finished reading Jmeter Statistics.");
        System.out.println("Adding statistics to html report...");
        
        String html = PerformanceStatistic.getStatisticsAsHtml();
        writeHtml(html, "results.html", args[1]);
        
        System.out.println("Report generated successfully.");
    }
    
    /**
     * Iterates the xml data stream to obtain the StAX events
     * 
     * @param reader reader
     * @throws Exception exception
     */
    private static void buildThroughputStats(XMLStreamReader reader) throws Exception {
        
    	List<Calendar> timeList = new ArrayList<Calendar>();
    	Set<String> cuSet = new HashSet<String>();
    	long numSuccess = 0;
    	long numFatal = 0;
    	long totalMs = 0;
    	List<Long> response = new ArrayList<Long>();
    	
    	while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
	            if (reader.getLocalName().equals(ELEMENT_NAME)) {
	            	Event tp = new ElementEvent();
		            tp.setReader(reader);
		            timeList.add(tp.getCalendarAttribute(2));
		            cuSet.add(tp.getStringAttribute(7));
		            if ("OK".equals(tp.getStringAttribute(6))) {
		            	numSuccess++;
		            } else {
		            	numFatal++;
		            }
		            
		            //time it took to complete this request (ms)
		            Long totalTxTimeMs = Long.valueOf(tp.getStringAttribute(0));
		            
		            BigDecimal slaTime = new BigDecimal(totalTxTimeMs);
		            slaTime = slaTime.divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP);
		            if (slaTime.compareTo(sla1Time) <= 0) {
		                PerformanceStatistic.addToSla1();
		                PerformanceStatistic.addToSla2();
		                PerformanceStatistic.addToSla3();
                        PerformanceStatistic.addToSla4();
		            } else if (slaTime.compareTo(sla2Time) <= 0) {
		                PerformanceStatistic.addToSla2();
                        PerformanceStatistic.addToSla3();
                        PerformanceStatistic.addToSla4();
		            } else if (slaTime.compareTo(sla3Time) <= 0) {
		                PerformanceStatistic.addToSla3();
                        PerformanceStatistic.addToSla4();
                    } else if (slaTime.compareTo(sla4Time) <= 0) {
                        PerformanceStatistic.addToSla4();
                    }
		            
		            totalMs = totalMs + totalTxTimeMs;
		            response.add(totalTxTimeMs);
	            }
            }
        }

    	PerformanceStatistic.setTotalMs(totalMs);

    	//Sort Asc
    	Collections.sort(timeList, new CalendarComparator());
    	Collections.sort(response);

    	for (Calendar c : timeList) {
    		PerformanceStatistic.addStatistic(c);
    	}
    	PerformanceStatistic.setConcurrentUsers(cuSet.size());
    	PerformanceStatistic.setTotalRequestNum(timeList.size());
    	PerformanceStatistic.setNumSuccess(numSuccess);
    	PerformanceStatistic.setNumFatal(numFatal);
    	PerformanceStatistic.setMinMs(Long.valueOf(response.get(0)));
    	PerformanceStatistic.setMaxMs(Long.valueOf(response.get(response.size()-1)));
    }

    /**
     * Accepts a valid url and returns an xml stream reader
     * 
     * @param fileName File name of xml document
     * @param path path
     * @return XmlStreamReader the xml document reader
     * @throws Exception exception
     */
    private static XMLStreamReader readInput(String fileName, String path) throws Exception {
    	path = path + fileName;

        FileInputStream dataStream = new FileInputStream(path);
        XMLStreamReader dataStreamReader = 
            XMLInputFactory.newInstance().createXMLStreamReader(dataStream);
        return dataStreamReader;
    }
    
    /**
     * write out the html file
     * 
     * @param html html string
     * @param fileName file name
     * @param path file path
     * @throws Exception exception
     */
    private static void writeHtml(String html, String fileName, String path) throws Exception {
    	path = path + fileName;
    	FileWriter fstream = new FileWriter(path);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(html);
        out.close();
    }

}
