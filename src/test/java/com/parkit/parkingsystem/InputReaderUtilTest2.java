package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.util.InputReaderUtil;

public class InputReaderUtilTest2 {
    private final InputStream systemIn = System.in;
    private ByteArrayInputStream testIn;

    private void provideInput(String data) {
	testIn = new ByteArrayInputStream(data.getBytes());
	System.setIn(testIn);
    }

    @AfterEach
    public void restoreSystemInputOutput() {
	System.setIn(systemIn);
    }

    @Test
    void readVehicleRegistrationNumber_givenEntryTest() throws Exception {
	//inject correct data to the scanner entry
	provideInput("A");
	InputReaderUtil inputReaderUtil = new InputReaderUtil();
	String regNumber = inputReaderUtil.readVehicleRegistrationNumber();
	assertThat(regNumber).isEqualTo("A");
    }

    @Test
    void readVehicleRegistrationNumber_givenEmptyStringTest() {
	provideInput("");
	InputReaderUtil inputReaderUtil = new InputReaderUtil();
	assertThrows(NoSuchElementException.class, () -> {
	    inputReaderUtil.readVehicleRegistrationNumber();
	});
    }
}