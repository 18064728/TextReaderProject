package TextReader;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class UniqueCounter implements Reader {

    /**
     * a map that stores every word with its occurrences.
     */
    private Map<String, Integer> wordMap;

    /**
     * whether the file is local (inside root folder) or not.
     */
    private boolean isLocalFile;

    UniqueCounter(boolean isLocalFile) {
        this.isLocalFile = isLocalFile;
    }

    /**
     * get the amount of unique words.
     */
    Map<String, Integer> getUniqueWords() {
        return wordMap;
    }

    @Override
    public void readFile(String resource, Boolean CaseSensitive) {
        wordMap = new TreeMap<>();
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

    /**
     * Prints all the unique words in a text file.
     *
     * @param in            the BufferedReader.
     * @param caseSensitive whether the words are case sensitive or not.
     */
    private void count(BufferedReader in, Boolean caseSensitive) {
        int words = 0;
        try {
            while (in.ready()) {
                String[] parts = in.readLine().split("\\W+");
                //String[] parts = in.readLine().split("[[\\W+][a-z(?=')]]");

                for (String word : parts) {
                    if (caseSensitive) {
                        wordMap.merge(word, 1, Integer::sum);
                        words++;
                    } else {
                        wordMap.merge(word.toLowerCase(), 1, Integer::sum);
                        words++;
                    }
                }
            }
            System.out.println(wordMap + "\n" + wordMap.size() + " out of " + words + " words are unique [case sensitive = " + caseSensitive + "]\n");
        } catch (IOException e) {
            System.out.println("an error occurred while reading the file");
            e.printStackTrace();
        }
    }
}