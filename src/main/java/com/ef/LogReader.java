package com.ef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.ef.dao.LogDAO;
import com.ef.model.ClientLogger;
import com.ef.model.LogRecord;

public class LogReader {

	private static final String delimeter = "\\|";
	
	public static HashMap<String, ClientLogger> readData(final String logFile, 
			Date startDate, Date endDate, int threshold) {
		ClientLogger client;
		HashMap<String, ClientLogger> map = new HashMap<String, ClientLogger>();
		String line = "";
		LogRecord pr;
		
		Properties props = LogReader.getDBParams();
		LogDAO dao = new LogDAO(props);
		dao.openConnection();
		
		try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            while ((line = br.readLine()) != null) {
                
                String[] recordLine = line.split(delimeter);
                pr = new LogRecord();
                                
                String strStart = recordLine[0].trim();
                String ip = recordLine[1].trim();
                String method = recordLine[2].trim();
                int httpCode = Integer.parseInt(recordLine[3]);
                String clientDescription = recordLine[4].trim();
               
                pr.setIp(ip);
                Date logTime = Common.LOG_DATE_FORMATTER.parse(strStart);
                pr.setStartDate(logTime);
                pr.setRequestMethod(method);
                pr.setHttpCode(httpCode);
                pr.setClientDescription(clientDescription);
                
                //persit the request to DB
                dao.saveLogRecord(pr);
                
                client = map.get(ip);
                if(client == null) {
                	client = new ClientLogger();
                	client.getList().add(pr);
                	map.put(pr.getIp(), client);
                } else {
                	client.getList().add(pr);
                }
                
        		if( (logTime.compareTo(startDate) >= 0 ) &&
        				(logTime.compareTo(endDate) <= 0) ) {
        			client.addOneRequest();
        		}
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
		
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()) {
			String ip = iter.next();
			client = map.get(ip);
			
			if( client.getNumOfRequest() > threshold ) {
				dao.saveBlockRequest(startDate, ip, client.getNumOfRequest());
			}
		}
		
		dao.closeConnection();
		return map;
	}
	
	public static Properties getDBParams() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			File propFile = new File(getJarPath(), "database.properties");
			input = new FileInputStream(propFile);
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			prop.put("database.host", "localhost");
			prop.put("database.user", "root");
			prop.put("database.password", "");
			prop.put("database.port", "3306");
			prop.put("database.name", "requests");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return prop;

	}
	
	public static File getJarPath() throws URISyntaxException {
//		ClassLoader loader = Parser.class.getClassLoader();
//		return new File( loader.getResource(".").getPath());
		File jarPath = new File(Parser.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		return jarPath.getParentFile();
	}
}
