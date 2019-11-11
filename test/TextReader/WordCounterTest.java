package TextReader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WordCounterTest {

    @Test
    public void setWord() {
        WordCounter wc = new WordCounter(true);
        wc.setWord("test");
        assertEquals("test", wc.getWord());
    }

    @Test
    public void readFile() {
        WordCounter wc = new WordCounter(true);
        wc.setWord("test");
        assertNotNull(wc.getWord());

        wc.readFile("/testCases/BeeMovieScript.txt", false);
        assertEquals(1, wc.getTimes());
    }

    @Test(expected = EmptyFileException.class)
    public void tbd() {
        //todo write this test
        // test with empty file ["/testCases/empty-file.txt"]
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotReadInvalidFile() {
        WordCounter wc = new WordCounter(false);
        wc.readFile(null, false);
    }

    @Test
    public void shouldCountWordsCorrectly() {
        WordCounter wc;
        String resource = "/testCases/CaseSensitiveTest.txt";
        int actual;

        wc = new WordCounter(true);
        wc.setWord("Foo");
        wc.readFile(resource, true);
        actual = wc.getTimes();
        assertEquals(3, actual);

        wc = new WordCounter(true);
        wc.setWord("foo");
        wc.readFile(resource, true);
        actual = wc.getTimes();
        assertEquals(2, actual);

        wc = new WordCounter(true);
        wc.setWord("Foo");
        wc.readFile(resource, false);
        actual = wc.getTimes();
        assertEquals(5, actual);

        wc = new WordCounter(true);
        wc.setWord("oof");
        wc.readFile(resource, false);
        actual = wc.getTimes();
        assertEquals(0, actual);
    }
}