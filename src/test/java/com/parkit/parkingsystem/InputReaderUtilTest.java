package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.util.InputReaderUtil;

public class InputReaderUtilTest {
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
    void readSelection_givenCorrectNumberTest() throws Exception {
	//inject correct data to the scanner entry
	provideInput("1");
	assertThat(new InputReaderUtil().readSelection()).isEqualTo(1);
    }
    
    @Test
    void readSelection_givenUncorrectLetterTest() throws Exception {
	//inject wrong data to the scanner entry
	provideInput("m");
	assertThat(new InputReaderUtil().readSelection()).isEqualTo(-1);
    }
}