package com.parkit.parkingsystem.util;

import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputReaderUtil {
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");
    Scanner scan = new Scanner(System.in);
    int input;
    String vehicleRegNumber;
    
    public int readSelection() {
	int input;
	try {
	    input = Integer.parseInt(scan.nextLine());
	    return input;
	    } catch (Exception e) {
	    logger.error("Error while reading user input from Shell", e);
	    System.out.println("Error reading input. Please enter valid number for proceeding further");
	    return -1;
	}
    }

    public String readVehicleRegistrationNumber() throws Exception {
	try  {
	    vehicleRegNumber = scan.nextLine();
	    if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
		throw new IllegalArgumentException("Invalid input provided");
	    }
	    return vehicleRegNumber;
	} catch (Exception e) {
	    logger.error("Error while reading user input from Shell", e);
	    System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
	    throw e;
	}
    }
}
