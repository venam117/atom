package ru.example;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class GameTest {

    @Test
    public void getWurg0() throws Exception {
        assertEquals(null, Game.getWord("nodictionary.txt"));
    }

    @Test
    public void getWurg1() throws Exception {
        assertEquals("hallow", Game.getWord("testWord.txt"));
    }

    @Test
    public void cows0() throws Exception {
        String str = "halo";
        assertEquals(4, Game.cows(str.toCharArray(), "hallow"));
    }

    @Test
    public void bulls0() throws Exception {
        String str = "halo";
        String str1 = "hallow";
        assertEquals(3, Game.bulls(str.toCharArray(), str1.toCharArray()));
    }

}