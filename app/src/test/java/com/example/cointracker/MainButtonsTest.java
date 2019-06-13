package com.example.cointracker;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MainButtonsTest {
    @Test
    public void assertsMainButtons(){
        MainActivity main = new MainActivity();
        int buttonNumber = main.portfolioButtonWorks;
        assertEquals(1, buttonNumber);
    }

}
