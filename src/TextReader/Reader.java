package TextReader;

public interface Reader {

    /**
     * Reads a given file.
     *
     * @param resource      the path to the txt file that needs to be read.
     * @param caseSensitive whether the words should be case sensitive or not.
     */
    void readFile(String resource, Boolean caseSensitive);

}
