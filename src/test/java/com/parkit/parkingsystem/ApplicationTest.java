package com.parkit.parkingsystem;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ApplicationTest {

    // backup System.in to restore it later
    private final InputStream sysInBackup = System.in; 
    
    @AfterEach
    public void restoreSystemInputOutput() {
	System.setIn(sysInBackup);
    }
    
    @Test
    public void mainTest() {
	//simulate typing from keyboard for the menu selection
	ByteArrayInputStream in = new ByteArrayInputStream("3".getBytes());
	System.setIn(in);
	App.main(new String[]{});
    }
}
