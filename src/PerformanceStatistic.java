import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Performance Statistics
 *
 */
public class PerformanceStatistic {

    /** transaction map */
	private static Map<String, Long> tpMap = new LinkedHashMap<String, Long>();
	/** concurrent users */
	private static Long concurrentUsers = new Long(0);
	/** number of successes */
	private static Long numSuccess = new Long(0);
    /** number of failures */
	private static Long numFatal = new Long(0);
    /** minimum miliseconds */
	private static Long minMs = new Long(0);
    /** maximum miliseconds */
	private static Long maxMs = new Long(0);
    /** total miliseconds */
	private static Long totalMs = new Long(0);
    /** total number of requests */
	private static Integer totalRequestNum = new Integer(0);
    /** name of run */
	private static String nameOfRun = null;
    /** run time */
	private static int runTime = 0;
    /** connection time out */
	private static int connectTimeout = 0;
    /** response time out */
	private static int responseTimeout = 0;
    /** server name */
	private static String servername;
    /** application name */
	private static String appName;
	/** Transactions under 0.3 seconds */
	private static Long sla1 = new Long(0);
	/** Transactions under 0.5 seconds */
	private static Long sla2 = new Long(0);
	/** Transactions under 2.0 seconds */
	private static Long sla3 = new Long(0);
    /** Transactions under 2.0 seconds */
    private static Long sla4 = new Long(0);
	
	/**
	 * set totalMs
	 * 
	 * @param totalMs totalMs
	 */
	public static void setTotalMs(Long totalMs) {
        PerformanceStatistic.totalMs = totalMs;
    }
	
    /**
     * set serverName
     * 
     * @param serverName serverName
     */
	public static void setServerName(String serverName) {
	    PerformanceStatistic.servername = serverName;
	}
	
    /**
     * set nameOfRun
     * 
     * @param nameOfRun nameOfRun
     */
	public static void setNameOfRun(String nameOfRun) {
		PerformanceStatistic.nameOfRun = nameOfRun;
	}

    /**
     * set runTime
     * 
     * @param runTime runTime
     */
	public static void setRunTime(int runTime) {
		PerformanceStatistic.runTime = runTime;
	}

    /**
     * set connectTimeout
     * 
     * @param connectTimeout connectTimeout
     */
	public static void setConnectTimeout(int connectTimeout) {
		PerformanceStatistic.connectTimeout = connectTimeout;
	}

    /**
     * set responseTimeout
     * 
     * @param responseTimeout responseTimeout
     */
	public static void setResponseTimeout(int responseTimeout) {
		PerformanceStatistic.responseTimeout = responseTimeout;
	}
	
    /**
     * set totalRequestNum
     * 
     * @param totalRequestNum totalRequestNum
     */
	public static void setTotalRequestNum(Integer totalRequestNum) {
		PerformanceStatistic.totalRequestNum = totalRequestNum;
	}
	
    /**
     * set numOfConcurrentUsers
     * 
     * @param numOfConcurrentUsers numOfConcurrentUsers
     */
	public static void setConcurrentUsers(long numOfConcurrentUsers) {
		PerformanceStatistic.concurrentUsers = numOfConcurrentUsers;
	}
	
    /**
     * set numSuccess
     * 
     * @param numSuccess numSuccess
     */
	public static void setNumSuccess(Long numSuccess) {
		PerformanceStatistic.numSuccess = numSuccess;
	}

    /**
     * set numFatal
     * 
     * @param numFatal numFatal
     */
	public static void setNumFatal(Long numFatal) {
		PerformanceStatistic.numFatal = numFatal;
	}

    /**
     * set minMs
     * 
     * @param minMs minMs
     */
	public static void setMinMs(Long minMs) {
		PerformanceStatistic.minMs = minMs;
	}

    /**
     * set maxMs
     * 
     * @param maxMs maxMs
     */
	public static void setMaxMs(Long maxMs) {
		PerformanceStatistic.maxMs = maxMs;
	}
		
    /**
     * set appName
     * 
     * @param appName appName
     */
    public static void setAppName(String appName) {
        PerformanceStatistic.appName = appName;
    }

    /**
     * addToSla1
     */
    public static void addToSla1() {
	    PerformanceStatistic.sla1 += 1;
	}
	
    /**
     * addToSla2
     */
	public static void addToSla2() {
        PerformanceStatistic.sla2 += 1;
    }
	
    /**
     * addToSla3
     */
	public static void addToSla3() {
        PerformanceStatistic.sla3 += 1;
    }
    
    /**
     * addToSla4
     */
    public static void addToSla4() {
        PerformanceStatistic.sla4 += 1;
    }
	
	/**
	 * add statistic
	 * @param statistic statistic
	 */
	public static void addStatistic(Calendar statistic) {
		int year = statistic.get(Calendar.YEAR);		
		int month = statistic.get(Calendar.MONTH);
		int date = statistic.get(Calendar.DATE);
		int hour = statistic.get(Calendar.HOUR);

		String strHour = null;
		if (hour == 0) {
			strHour = "00";
		} else if (hour > 0 && hour < 10) {
			strHour = "0" + hour;
		} else {
			strHour = String.valueOf(hour);
		}
		
		int min = statistic.get(Calendar.MINUTE);
		String strMin = null;
		if (min == 0) {
			strMin = "00";
		} else if (min > 0 && min < 10) {
			strMin = "0" + min;
		} else {
			strMin = String.valueOf(min);
		}
			
		String key = (month + 1) + "/" + date + "/" + year + " " + strHour + ":" + strMin;
		if (!tpMap.containsKey(key)) {
			tpMap.put(key, new Long(1));
		} else {
			Long tpCount = tpMap.get(key);
			tpMap.put(key, ++tpCount);
		}
	}	
	
	/**
	 * get statistics
	 * @return string
	 */
	public static String getStatistics() {
		StringBuffer result = new StringBuffer();
		
		Set<String> keySet = tpMap.keySet();
		for (String s : keySet) {
			result.append(s);
			result.append("\t");		
			result.append(tpMap.get(s));
			result.append("\n");
		}
		
		return result.toString();
	}
	
	/**
	 * get statistics as html
	 * @return string
	 */
	public static String getStatisticsAsHtml() {
		StringBuffer result = new StringBuffer("<html><head><META http-equiv=\"Content-Type\" content=\"text/html; "
		    + "charset=UTF-8\"><title>" + nameOfRun + "</title><style>h1, h2, h3, h4, h5, h6, table, tr, td "
		    + "{margin: 0;padding: 0;}h1 { font-size: 18px; margin: 10px 0px;  }h2 { font-size: 16px; "
		    + "margin: 5px 0px; }h3 { font-size: 14px; margin: 5px 0px; }h4 { font-size: 12px; } h1, h2, h3, h4, th "
		    + "{color: #121314;} table {} td { color: #334455; }body {font-size: 12px;}a { color: #0000FF; }a:hover "
		    + "{ font-weight: bolder; }th { font-size: 13px; }td { font-size: 12px; }#header { border-bottom: 1px "
            + "dotted #9BCE9B; margin-botton: 10px;}#container{ margin-bottom: 15px; }#footer { font-size: 10px; }"
            + "#wrapper{ margin-left: auto; margin-right: auto; width: 931px; }#sidebar { float: left; width: 180px; "
            + "padding-top: 15px; }#content { border-left: 1px dotted #9BCE9B; float: left; width: 720px; "
            + "padding-left: 30px; padding-top: 15px;margin-bottom: 45px;}#sidebar a, #sidebar a:visited { color: "
            + "#0000FF; }table { border: 1px solid gray; margin-bottom: 5px; width: 50%; }table td { border: 1px "
            + "solid gray; }table td, .result_table th { text-align: center; }</style>");				
		
		//Properties Used in Test
		result.append("</head><body>");
		result.append("<h1>JMeter Results Summary</h1><div>");		
		result.append(nameOfRun);
		result.append("</div>");		
		result.append("<ul><li>Server Name: ");
		result.append(servername);
		result.append("</li>");
		result.append("<li>Run Time (sec): ");
		result.append(runTime);
		result.append("</li>");
		result.append("<li>Connect Timeout (ms): ");
		result.append(connectTimeout);
		result.append("</li>");
		result.append("<li>Response Timeout (ms): ");
		result.append(responseTimeout);
        result.append("</li>");
        
        if (appName.equalsIgnoreCase("test")) {
            result.append("<br/><br/>");
            result.append("<li>Maximum Number of pairs: 25</li>");
            result.append("<li>Minimum Number of pairs: 2</li>");
            result.append("<li>Average Number of pairs: 10</li>");
        }
        
		result.append("</ul>");
		
		result.append("</div><br><div>");
		result.append("<h2>Summary</h2></div>");
		result.append("<div id=\"results\"><table>");
		
		//Headers
		result.append("<tr><th>Concurrent Users</th><th>Number of Requests</th><th>Minimum (ms)</th>");
		result.append("<th>Average (ms)</th><th>Maximum (ms)</th><th>Number Success</th><th>Number Fatal</th>");
		
        if (appName.equalsIgnoreCase("test")) {
            result.append("<th>Success Percentage<br/>(goal 99%)</th>");
        } else {
            result.append("<th>Success Percentage</th>");
        }
        
		result.append("<th>Avg Throughput (per sec)</th>");
		
        if (appName.equalsIgnoreCase("test")) {
            result.append("<th>% Completed Within 0.5 Seconds<br/>(goal 65%)</th>"
                + "<th>% Completed Within 2.0 Seconds<br/>(goal 90%)</th>"
                + "<th>% Completed Within 3.0 Seconds<br/>(goal 95%)</th></tr>");
        } else {
    		result.append("<th>% Completed Within 0.3 Seconds<br/>(goal 50%)</th>"
    		    + "<th>% Completed Within 0.5 Seconds<br/>(goal 95%)</th>"
    		    + "<th>% Completed Within 2.0 Seconds<br/>(goal 99%)</th></tr>");
        }
        
		//Content
		result.append("<tr>");
		result.append("<td>");
		result.append(concurrentUsers);
		result.append("</td>");
		result.append("<td>");
		result.append(totalRequestNum);			
		result.append("</td>");
		result.append("<td>");
		result.append(minMs);
		result.append("</td>");
		result.append("<td>");				
		BigDecimal avgMs = new BigDecimal(totalMs);        
		result.append(avgMs.divide(new BigDecimal(totalRequestNum), 2, RoundingMode.HALF_UP));
		result.append("</td>");
		result.append("<td>");
		result.append(maxMs);
		result.append("</td>");
		result.append("<td>");
		result.append(numSuccess);
		result.append("</td>");
		result.append("<td>");
		result.append(numFatal);
		result.append("</td>");
		result.append("<td>");
		BigDecimal ns = new BigDecimal(numSuccess);		
		BigDecimal nsTmp = ns.divide(new BigDecimal(totalRequestNum), 4, RoundingMode.HALF_UP);
		result.append(nsTmp.movePointRight(2));
		result.append("%</td>");
		result.append("<td>");
		
		BigDecimal avgTp = calcAvgTpPerSec();
		if (avgTp.compareTo(new BigDecimal(0)) == 0) {
		    result.append("n/a");
		} else {
		    result.append(avgTp);
		}
        result.append("</td>");
                
        //SLA stats 
        BigDecimal tmp = null;

        if (!appName.equalsIgnoreCase("test")) {
            result.append("<td>");
            BigDecimal sla1Value = new BigDecimal(sla1);
            tmp = sla1Value.divide(new BigDecimal(totalRequestNum), 4, RoundingMode.HALF_UP);        
            result.append(tmp.movePointRight(2));
            result.append("%</td>");
        }
		
        result.append("<td>");
        BigDecimal sla2Value = new BigDecimal(sla2);
        tmp = sla2Value.divide(new BigDecimal(totalRequestNum), 4, RoundingMode.HALF_UP);        
        result.append(tmp.movePointRight(2));
        result.append("%</td>");
        
        result.append("<td>");
        BigDecimal sla3Value = new BigDecimal(sla3);
        tmp = sla3Value.divide(new BigDecimal(totalRequestNum), 4, RoundingMode.HALF_UP);        
        result.append(tmp.movePointRight(2));
        result.append("%</td>");

        if (appName.equalsIgnoreCase("test")) {
            result.append("<td>");
            BigDecimal sla4Value = new BigDecimal(sla4);
            tmp = sla4Value.divide(new BigDecimal(totalRequestNum), 4, RoundingMode.HALF_UP);        
            result.append(tmp.movePointRight(2));
            result.append("%</td>");
        }
        
        result.append("</tr></table></div>");
				
		// Throughput (broken into one minute intervals)
		result.append("<br><br><div>");		
		result.append("<h2>Throughput (One Minute Intervals)</h2><div><p><h4>"
		    + "Note: The first and last entries in the table below are not used to calculate the average throughput"
		    + "as they may not represent a full minute of processing.</h4></p></div>");	
		result.append("<table>");
		result.append("<tr>");
		result.append("<th>Date/Time</th>");
		result.append("<th>Throughput</th>");
		result.append("</tr>");
				
		Set<String> keySet = tpMap.keySet();
		for (String s : keySet) {
			result.append("<tr>");
			result.append("<td>");
			result.append(s);
			result.append("</td>");
			result.append("<td>");			
			result.append(tpMap.get(s));
			result.append("</td>");
			result.append("</tr>");
		}
		
		result.append("</table></div></table></body></html>");
		
		return result.toString();			
	}
	
	/**
	 * calculate average transaction per second
	 * 
	 * @return big decimal
	 */
	private static BigDecimal calcAvgTpPerSec() {
	    BigDecimal result = null;
	    long totalTp = 0;
	    
	    //Chances are that the first and last minute of execution are not full
	    //minutes and can throw off the tp calculation.  So the first and last
	    //minutes are ignored to get a more accurate number
	    long i = 0;
	    Set<Map.Entry<String, Long>> entrySet = tpMap.entrySet();
	    for (Map.Entry<String, Long> entry : entrySet) {
	        //Ignore the first and last minute of the test for more accurate numbers
	        if (i == 0 || i == (tpMap.size() - 1)) {
	            if (tpMap.size() <= 2) {
	                // return 0
	                BigDecimal zero = new BigDecimal(0);
	                zero.setScale(2);
	                return zero;
	            }	            
	        } else {
	            totalTp = totalTp + entry.getValue();
	        }
	        i++;
	    }
	    
	    if (totalTp >= tpMap.size()) {
	        result = new BigDecimal(totalTp);
	        return result.divide(new BigDecimal(60*(tpMap.size() - 2)), 2, RoundingMode.HALF_UP);	        
	    } else {
	        result = new BigDecimal(totalTp);
	        result = result.multiply(new BigDecimal(100));
	        BigDecimal tmp = result.divide(new BigDecimal(60*(tpMap.size() - 2)), 2, RoundingMode.HALF_UP);	        
	        return tmp.movePointLeft(2);	        
	    }
	}
	
}
