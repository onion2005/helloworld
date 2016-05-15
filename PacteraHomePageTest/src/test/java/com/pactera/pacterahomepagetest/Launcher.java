package com.pactera.pacterahomepagetest;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.xml.DOMConfigurator;
import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;

public class Launcher {

	public static void main(String[] args) {
		// The launcher of the current tool
		if (0 == args.length) {
			// Try to use the testng.xml/log4j.xml in the same folder
			if (configLog4J(System.getProperty("user.dir") + File.separator + "log4j.xml")) {
				if (processAndInvokeTestNG(System.getProperty("user.dir") + File.separator + "testng.xml")) {
					System.out.println("Finish execution ... please check the result at test-output folder.");
				} else {
					errorMsgs();
				}
			} else {
				errorMsgs();
			}
		} else if (2 == args.length) {
			// The user has somehow specified a log4j.xml
			if (configLog4J(args[1])) {
				// The user has somehow specified a testng.xml
				if (processAndInvokeTestNG(args[0])) {
					System.out.println("Finish execution ... please check the result at test-output folder.");
				} else {
					errorMsgs();
				}				
			} else {
				errorMsgs();
			}
		} else {
			System.out.println("Invalid number of parameters !");
			errorMsgs();
		}
	}
	
	public static boolean configLog4J(String path) {
		System.out.print("Try to use " + path + " for log4j ... ");
		File log4jXML = new File(path);
		if (log4jXML.exists()) {
			System.out.println(" loaded.");
			try {
				DOMConfigurator.configure(path);
			} catch (Exception e) {
				// Something is wrong
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			System.out.println(" cannot find file " + path);
			return false;
		}
	}
	
	public static boolean processAndInvokeTestNG(String path) {
		System.out.print("Try to use " + path + " for testng ... ");
		File testNGXML = new File(path);
		if (testNGXML.exists()) {
			System.out.println(" loaded.");
			// Try to parse the file
			Parser parser = new Parser(path);
			ArrayList<XmlSuite> parsedSuite = null;
			// Parse it first
			System.out.print("Parse " + path + " ... ");
			try {
				parsedSuite = new ArrayList<XmlSuite>(parser.parse());
			} catch (Exception e) {
				// Something is wrong
				e.printStackTrace();
				return false;
			}
			System.out.println("OK.");
			System.out.print("Initialize testng .. ");
			TestNG tng = new TestNG();
			tng.setXmlSuites(parsedSuite);
			System.out.println("OK.");
			tng.run();
			return true;
		} else {
			System.out.println(" cannot find file " + path);
			return false;
		}
	}
	
	public static void errorMsgs() {
		System.out.println("Please re-run the tool and specify the full path of the valid testng.xml and the full path of the valid log4j.xml in the commandline. ");
		System.out.println("Or copy valid testng.xml and log4j.xml to the current folder.");
		System.out.println("java -jar PacteraHomePageTest-0.0.1-SNAPSHOT-tests.jar path_to_testng_config path_to_log4j_config");
		System.out.println("java -jar PacteraHomePageTest-0.0.1-SNAPSHOT-tests.jar");
		System.out.println("Good bye.");
	}
}