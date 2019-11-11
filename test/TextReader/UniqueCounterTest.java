package TextReader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UniqueCounterTest {

    @Test
    public void readFile() {
        UniqueCounter uc = new UniqueCounter(true);
        uc.readFile("/testCases/BeeMovieScript.txt", false);
    }

    @Test(expected = EmptyFileException.class)
    public void tbd() {
        //todo write this test
        // test with empty file ["/testCases/empty-file.txt"]
    }

    @Test
    public void shouldCountWordsCorrectly() {
        UniqueCounter uc = new UniqueCounter(true);
        String resource = "/testCases/CaseSensitiveTest.txt";
        uc.readFile(resource, true);

        int expected = 2;
        int actual = uc.getUniqueWords().size();
        assertEquals(expected, actual);

        uc.readFile(resource, false);
        expected = 1;
        actual = uc.getUniqueWords().size();
        assertEquals(expected, actual);
    }

}