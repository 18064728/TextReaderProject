package TextReader;

import java.io.*;

public class WordCounter implements Reader {

    /**
     * the word you want to search for.
     */
    private String word;

    /**
     * the amount of times the word occurs in a text file.
     */
    private int times;

    /**
     * whether the file is local (inside root folder) or not.
     */
    private boolean isLocalFile;

    WordCounter(boolean isLocalFile) {
        this.isLocalFile = isLocalFile;
        word = "";
        times = 0;
    }

    String getWord() {
        return word;
    }

    void setWord(String word) {
        this.word = word;
    }

    int getTimes() {
        return times;
    }

    @Override
    public void readFile(String resource, Boolean CaseSensitive) {
        if (resource == null)
            throw new IllegalArgumentException("resource can't be null");

        BufferedReader in;
        if (isLocalFile) {
            InputStream stream = getClass().getResourceAsStream(resource);
            in = new BufferedReader(new InputStreamReader(stream));
            count(in, CaseSensitive);

        } else {
            try {
                in = new BufferedReader(new FileReader(resource));
                count(in, CaseSensitive);
            } catch (FileNotFoundException e) {
                System.out.println("can't find the file");
                e.printStackTrace();
            }
        }
    }

    private void count(BufferedReader in, Boolean caseSensitive) {
        try {
            while (in.ready()) {
                String[] parts = in.readLine().trim().split("\\W+");

                for (String s : parts) {
                    if (caseSensitive) {
                        if (word.equals(s))
                            times++;
                    } else {
                        if (word.toLowerCase().equals(s.toLowerCase()))
                            times++;
                    }
                }
            }

            if (times == 1)
                System.out.println("the word " + word + " occurs 1 time in this file [case sensitive = " + caseSensitive + "]\n");
            else if (times > 1)
                System.out.println("the word " + word + " occurs " + times + " times in this file [case sensitive = " + caseSensitive + "]\n");
            if (times < 1)
                System.out.println("the word " + word + " doesn't occur in this file");
        } catch (IOException e) {
            System.out.println("an error occurred while reading the file.");
            e.printStackTrace();
        }
    }
}