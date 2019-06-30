package adrenaline.server.model;

import adrenaline.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {

    private static ScoreBoard scoreBoard;

    @BeforeAll
    static void initScoreBoard(){
        scoreBoard = new ScoreBoard(new ArrayList<>());
        scoreBoard.initPlayerScore(Color.BLUE);
        scoreBoard.initPlayerScore(Color.YELLOW);
        scoreBoard.initPlayerScore(Color.GREEN);
        scoreBoard.initKillshotTrack(5);
    }

    @Test
    void initPlayerScoreBoardTest(){
        ScoreBoard testScoreBoard = new ScoreBoard(new ArrayList<>());
        testScoreBoard.initPlayerScore(Color.BLUE);
        testScoreBoard.initPlayerScore(Color.YELLOW);
        testScoreBoard.initPlayerScore(Color.GREEN);
        testScoreBoard.initKillshotTrack(5);
        assertEquals(0,testScoreBoard.getScoreMap().get(Color.BLUE));
        assertNotEquals(3,testScoreBoard.getScoreMap().get(Color.YELLOW));
        assertEquals(8,testScoreBoard.getDiminValues().get(Color.BLUE));
        assertNotEquals(0,testScoreBoard.getDiminValues().get(Color.YELLOW));
        assertEquals(5,testScoreBoard.getKillshotTrack().length);
        assertEquals(5,testScoreBoard.getOverkillFlags().length);
        assertEquals(0,testScoreBoard.getKillCount());
    }

    @Test
    void scoreKillTest(){
        HashMap<Color,Integer> scoreMap = new HashMap<>(scoreBoard.getScoreMap());
        scoreBoard.scoreKill(Color.BLUE, new ArrayList<>(Arrays.asList(Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.GREEN,Color.GREEN,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW)));
        //Test first blood and most damage
        int oldScore = scoreMap.get(Color.YELLOW);
        assertEquals(oldScore+9,scoreBoard.getScoreMap().get(Color.YELLOW));

        //Test second
        oldScore = scoreMap.get(Color.GREEN);
        assertEquals(oldScore+6,scoreBoard.getScoreMap().get(Color.GREEN));
    }

    @Test
    void scoreDoubleKillTest(){
        HashMap<Color,Integer> scoreMap = new HashMap<>(scoreBoard.getScoreMap());
        scoreBoard.scoreDoubleKill(Color.GREEN);
        int oldScore = scoreMap.get(Color.GREEN);
        assertEquals(oldScore+1,scoreBoard.getScoreMap().get(Color.GREEN));
    }

    @Test
    void gameEndedTest(){
        ScoreBoard testScoreBoard = new ScoreBoard(new ArrayList<>());
        testScoreBoard.initPlayerScore(Color.BLUE);
        testScoreBoard.initPlayerScore(Color.YELLOW);
        testScoreBoard.initPlayerScore(Color.GREEN);
        testScoreBoard.initKillshotTrack(5);
        assertFalse(testScoreBoard.gameEnded());
        while (!testScoreBoard.gameEnded()){
            testScoreBoard.scoreKill(Color.BLUE, new ArrayList<>(Arrays.asList(Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.GREEN,Color.GREEN,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW)));
        }
        assertEquals(5,testScoreBoard.getKillCount());
        assertEquals(5,testScoreBoard.getKillshotTrack().length);
    }
}
