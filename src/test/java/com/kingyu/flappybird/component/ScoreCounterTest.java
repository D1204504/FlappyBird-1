package com.kingyu.flappybird.component;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.*;
import java.io.*;

class ScoreCounterTest {

    private ScoreCounter scoreCounter;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("score", ".tmp");
        tempFile.deleteOnExit();
        scoreCounter = ScoreCounter.createInstanceForTesting(tempFile.getAbsolutePath());
        scoreCounter.reset();
    }

    @Test
    void testSingleton() {
        ScoreCounter instance1 = ScoreCounter.getInstance();
        ScoreCounter instance2 = ScoreCounter.getInstance();
        assertSame(instance1, instance2, "Singleton instances should be the same");
    }

    @Test
    void testLoadBestScore_FileExists() throws Exception {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile))) {
            dos.writeLong(100L);
        }

        scoreCounter = ScoreCounter.createInstanceForTesting(tempFile.getAbsolutePath());
        assertEquals(100L, scoreCounter.getBestScore(), "Best score should be loaded from file");
    }

    @Test
    void testLoadBestScore_FileNotExists() {
        File nonExistentFile = new File("non_existent_file.tmp");
        ScoreCounter scoreCounter = ScoreCounter.createInstanceForTesting(nonExistentFile.getAbsolutePath());
        assertEquals(-1L, scoreCounter.getBestScore(), "Best score should default to -1 if file does not exist");
    }

    @Test
    void testSaveScore() throws Exception {
        scoreCounter.score(mock(Bird.class)); // Increment score
        scoreCounter.saveScore();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(tempFile))) {
            assertEquals(scoreCounter.getBestScore(), dis.readLong(), "Saved score should match best score");
        }
    }

    @Test
    void testScore_BirdAlive() {
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);

        scoreCounter.score(bird);
        assertEquals(1L, scoreCounter.getCurrentScore(), "Score should increment by 1 if bird is not dead");
    }

    @Test
    void testScore_BirdDead() {
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(true);

        scoreCounter.score(bird);
        assertEquals(0L, scoreCounter.getCurrentScore(), "Score should not increment if bird is dead");
    }

    @Test
    void testReset() {
        scoreCounter.score(mock(Bird.class));
        scoreCounter.reset();
        assertEquals(0L, scoreCounter.getCurrentScore(), "Score should reset to 0");
    }

    @Test
    void testGetBestScore() {
        assertEquals(-1L, scoreCounter.getBestScore(), "Default best score should be -1");
    }

    @Test
    void testGetCurrentScore() {
        scoreCounter.score(mock(Bird.class));
        assertEquals(1L, scoreCounter.getCurrentScore(), "Current score should match expected value");
    }
    @Test
    void testSaveScore_Normal() throws Exception {
        scoreCounter.score(mock(Bird.class)); // Increment score
        scoreCounter.saveScore();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(tempFile))) {
            assertEquals(scoreCounter.getBestScore(), dis.readLong(), "Saved score should match best score");
        }
    }

    @Test
    void testSaveScore_Exception() throws Exception {
        ScoreCounter mockScoreCounter = spy(scoreCounter); // Spy on the real instance
        doThrow(new IOException("Simulated I/O error"))
                .when(mockScoreCounter)
                .writeBestScoreToFile(anyLong());

        // Test saveScore with the mocked exception
        mockScoreCounter.saveScore();

        // Verify the exception was caught and didn't propagate
        verify(mockScoreCounter).writeBestScoreToFile(anyLong());
    }


}
