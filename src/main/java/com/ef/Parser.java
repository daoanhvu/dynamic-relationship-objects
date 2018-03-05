package com.ef;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.ef.dao.LogDAO;
import com.ef.model.LogRecord;

/**
 * 
 * @author davu
 */
public class Parser {
	
	private static final byte ACCESS_FILE_AGR = 0x1;
	private static final byte START_DATE_AGR = 0x2;
	private static final byte DURATION_AGR = 0x4;
	private static final byte THRESHOLD_AGR = 0x8;
	
	static enum Duration {
		HOURLY,
		DAILY
	}
	
    public static void main( String[] args ) {
    	
    	Date startDate = new Date();
    	Duration duration = Duration.DAILY;
    	int threshold = 0;
    	String logFile = "";
    	
        if(args.length < 3) {
        	System.out.println("[ERROR] Argument insufficient!");
        	printHelp();
        	return;
        }
        
        byte command = 0;
        for(String arg: args) {
        	if(!arg.startsWith("--")) {
        		System.out.println("[ERROR] Argument must be started with -- ");
        		printHelp();
        		return;
        	}
        	
        	if( arg.startsWith("--accessFile=") ) {
        		logFile = getArgument(arg);
        		command = (byte) (command | ACCESS_FILE_AGR);
        	} else if( arg.startsWith("--startDate=") ) {
        		String str = getArgument(arg);
        		try {
        			startDate = Common.STARTDATE_FORMATTER.parse(str);
        			command = (byte) (command | START_DATE_AGR);
        		} catch(ParseException pex) {
        			System.out.println("[ERROR] startDate does not follow format 'yyyy-MM-dd HH:mm:ss' ");
        			return;
        		}
        	} else if( arg.startsWith("--duration=") ) {
        		String str = getArgument(arg);
        		command = (byte) (command | DURATION_AGR);
        		if( str.equalsIgnoreCase("hourly") ) {
        			duration = Duration.HOURLY;
        		} else if ( str.equalsIgnoreCase("daily") ) {
        			duration = Duration.DAILY;
        		} else {
        			System.out.println("[ERROR] duration just accepts 'hourly' or 'daily'.");
        			printHelp();
        			return;
        		}
        	} else if( arg.startsWith("--threshold=") ) {
        		try {
        			threshold = Integer.parseInt(getArgument(arg));
        			command = (byte) (command | THRESHOLD_AGR);
        		} catch(NumberFormatException ne) {
        			System.out.println("[ERROR] threshold must be an integer.");
        			printHelp();
        			return;
        		}
        	}
        }
        
//        System.out.println("[DEBUG] Command argument: " + command);
        if( command < 0xF ) {
        	if( (command & ACCESS_FILE_AGR) == 0x0) {
        		System.out.println("[ERROR] --accessFile is mandatory. ");
        	}
        	
        	if( (command & START_DATE_AGR) == 0x0) {
        		System.out.println("[ERROR] --startDate is mandatory. ");
        	}
        	
        	if( (command & DURATION_AGR) == 0x0) {
        		System.out.println("[ERROR] --duration is mandatory. ");
        	}
        	
        	if( (command & THRESHOLD_AGR) == 0x0) {
        		System.out.println("[ERROR] --threshold is mandatory. ");
        	}
        	
        	printHelp();
        	return;
        }
    	
    	System.out.println("Start parsing...");
    	long startRunningTime = System.nanoTime();
        
        //Calculate endDate (inclusive)
        Calendar endCal = Calendar.getInstance();
    	endCal.setTime(startDate);
    	
    	if(duration == Duration.DAILY) {
    		endCal.add(Calendar.DATE, 1);
    	} else {
    		//Hourly
    		endCal.add(Calendar.HOUR, 1);
    	}
    	
    	Date endDate = endCal.getTime();
    	LogReader.readData(logFile, startDate, endDate, threshold);
    	
    	long endRunningTime = System.nanoTime();
    	System.out.println("Parsing done! \nRunning time: " + TimeUnit.NANOSECONDS.toSeconds(endRunningTime - startRunningTime) + " seconds ");
    }
    
    private static void printHelp() {
    	System.out.println("Usage:");
    	System.out.println("java -jar parser.jar --accessFile=<log_file_path> --startDate=<startDate> --duration=<duration> --threshold=<threshold>\n");
    }
    
    private static String getArgument(String arg) {
    	int eqidx = arg.indexOf('=');
    	return arg.substring(eqidx+1, arg.length());
    }
}
