package com.example.cointracker;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;

public class MainButtonsTest {
    @Test
    public void assertsMainButtons(){
        try {
            MainActivity main = new MainActivity();
            int buttonNumber = main.portfolioButtonWorks;
            assertEquals(1, buttonNumber);
        } catch (IOException e) {
            System.out.println("Main Activity never spawned");
        }
    }

}
