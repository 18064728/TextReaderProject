package TextReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

class Client {

    private static final int width = 600;
    private static final int height = 500;
    private static final String title = "Text File Reader";

    private JPanel panel;

    private String resource = null;
    private java.awt.Color theme;
    private JCheckBox caseSensitiveCB;
    private Map<String, Integer> result;

    private Client() {
        addComponents();
    }

    /**
     * Creates a new Client.
     */
    static void DisplayGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Client();
    }

    /**
     * Checks of a file is local or not.
     * A file is considered local when located somewhere within the root folder [TextFileReaderProject].
     * <p>
     * note: not sure if this method is needed for the moment.
     * note 2: this method isn't working correctly yet.
     */
    /*public static boolean isLocalFile(File f) {
        //todo make this method prettier/safer

        Path path = f.toPath();
        String[] original = {"testCases", "test", "TextFileReaderProject", ""};
        String[] paths = new String[path.getNameCount()];
        for (int i = 0; i < path.getNameCount(); i++) {
            paths[i] = path.getName(i).toString();
            for (int j = 0; j < 3; j++) {
                if (paths[i].equals(original[j])) {
                    return true;
                }
            }
        }
        return false;
    }*/
    private void addComponents() {
        JFrame f = new JFrame();
        f.setSize(width, height);
        f.setTitle(title);

        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        panel.add(addReaderPanel());
        panel.add(addFilePanel());
        panel.add(addListPanel());
        panel.add(addThemePanel());

        f.add(panel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel addReaderPanel() {
        JPanel upperPanel = new JPanel();

        caseSensitiveCB = new JCheckBox("case sensitive");
        upperPanel.add(caseSensitiveCB);

        JButton uniqueBtn = new JButton(new AbstractAction("count unique words") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resource == null)
                    System.out.println("no file selected");
                else
                    search(new UniqueCounter(false));
            }
        });
        upperPanel.add(uniqueBtn);

        JButton wordBtn = new JButton(new AbstractAction("count specific word") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resource == null)
                    System.out.println("no file selected");
                else
                    search(new WordCounter(false));
            }
        });
        upperPanel.add(wordBtn);

        return upperPanel;
    }

    private JPanel addFilePanel() {
        JPanel filePanel = new JPanel();
        JLabel fileLbl = new JLabel("no file selected");


        JButton fileBtn = new JButton(new AbstractAction("choose a file") {
            /**
             * Displays a JFileChooser so the user can choose a text file
             *
             * @param e a button click.
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser jfc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt files", "txt");
                jfc.setFileFilter(filter);
                jfc.showOpenDialog(filePanel);
                File file = jfc.getSelectedFile();

                if (file != null) {
                    resource = file.toString();
                    if (isValidFIle(file)) {
                        System.out.println("chosen file: " + file.toString());
                        fileLbl.setText("file: " + jfc.getDescription(file) + " (" + getFileSize(file.length()) + ")");
                    } else System.out.println("invalid file");
                }
            }

            /**
             * Validates whether a file is valid or not.
             *
             * @param file the file to validate.
             * @return true if file is valid, false otherwise.
             */
            private boolean isValidFIle(File file) {
                String[] parts = file.getName().split("\\.");
                int EXTENSION = parts.length - 1; // last part always is the extension e.g aFile.txt

                if (parts[EXTENSION].equals("txt")) {
                    try {
                        BufferedReader in = new BufferedReader(new FileReader(resource));
                        return file.canRead() && in.ready();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                } else { // if it is not a .txt file
                    Object[] options = {"continue", "cancel"};
                    int response = JOptionPane.showOptionDialog(filePanel, String.format("%s has a .%s format and is not supported." +
                                    "\nIt is possible to continue, but results can be different than expected.", parts[0], parts[EXTENSION]),
                            "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            options, options[1]);
                    if (response == 0) {
                        try {
                            return file.canRead();
                        } catch (Exception e) {
                            System.out.println("can't find the file");
                            return false;
                        }
                    } else
                        return false;
                }
            }

            /**
             * Calculates the size of the file.
             *
             * @param length the length of the file.
             * @return the length of the file.
             */
            String getFileSize(long length) {
                if (length < 1024) {
                    return length + "b";
                } else if (length < 1024 * 1024) {
                    return length / 1024 + "kb";
                } else {
                    return (length / 1024) / 1024 + "mb";
                }
            }
        });

        filePanel.add(fileBtn);
        filePanel.add(fileLbl);

        return filePanel;
    }

    private JPanel addListPanel() {
        JPanel listPanel = new JPanel();
        DefaultListModel<Object> model = new DefaultListModel<>();


        JButton update = new JButton(new AbstractAction("update") {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.removeAllElements();
                Set keys = result.keySet();
                model.setSize(1000);
                for (int i : result.values()) {
                    //System.out.println(keys + " -> " + i);
                    //model.add(i,keys);
                }
                //model.add(0, result);
            }
        });

        listPanel.add(update);

        JList<Object> words = new JList<>(model);
        listPanel.add(words);
        JScrollPane sp = new JScrollPane(words);
        listPanel.add(sp);

        return listPanel;
    }

    /**
     * Some method which lets the user change the theme.
     * Currently in beta.
     */
    private JPanel addThemePanel() {
        JPanel themePanel = new JPanel();
        themePanel.setLayout(new BoxLayout(themePanel, BoxLayout.PAGE_AXIS));
        Component box = Box.createRigidArea(new Dimension(100, 50));
        themePanel.add(box);

        JLabel fontLbl = new JLabel();
        themePanel.add(fontLbl);

        JButton themeBtn = new JButton(new AbstractAction("change theme") {
            int counter = 1;

            @Override
            public void actionPerformed(ActionEvent e) {

                switch (counter) {
                    case 1:
                        theme = Color.CYAN;
                        fontLbl.setText("font 1");
                        for (Component c : panel.getComponents())
                            c.setBackground(theme);
                        break;
                    case 2:
                        theme = Color.GREEN;
                        fontLbl.setText("font 2");
                        for (Component c : panel.getComponents())
                            c.setBackground(theme);
                        break;
                    case 3:
                        theme = Color.YELLOW;
                        fontLbl.setText("font 3");
                        for (Component c : panel.getComponents())
                            c.setBackground(theme);
                        break;
                    case 4:
                        theme = Color.RED;
                        fontLbl.setText("font 4");
                        for (Component c : panel.getComponents())
                            c.setBackground(theme);
                        break;
                    case 5:
                        theme = UIManager.getColor("Panel.background");
                        for (Component c : panel.getComponents())
                            c.setBackground(theme);
                        fontLbl.setText("default font");
                        break;
                }
                counter++;
                if (counter > 5)
                    counter = 1;

                //upperPanel.setBackground(theme);
                // filePanel.setBackground(theme);
                //todo all panels should change color
                themePanel.setBackground(theme);
                caseSensitiveCB.setBackground(theme);
            }
        });
        themePanel.add(themeBtn);

        return themePanel;
    }

    private void search(Reader r) {
        boolean isCaseSensitive = caseSensitiveCB.isSelected();

        if (r.getClass().equals(UniqueCounter.class))
            newUniqueCounter(isCaseSensitive);
        else if (r.getClass().equals(WordCounter.class))
            newWordCounter(isCaseSensitive);
    }

    /**
     * Creates a new UniqueCounter.
     *
     * @param isCaseSensitive whether the words are case sensitive or not.
     */
    private void newUniqueCounter(boolean isCaseSensitive) {
        UniqueCounter uc = new UniqueCounter(false);
        if (resource != null) {
            uc.readFile(resource, isCaseSensitive);
            result = uc.getUniqueWords();
        }
    }

    /**
     * Creates a new WordCounter.
     *
     * @param isCaseSensitive whether the words are case sensitive or not.
     */
    private void newWordCounter(boolean isCaseSensitive) {
        WordCounter wc = new WordCounter(false);

        if (resource != null) {
            String word = getWord();
            if (word != null) {
                System.out.println("words: " + word);
                wc.setWord(word);
                wc.readFile(resource, isCaseSensitive);
            }
        }
    }

    /**
     * Displays a JOptionPane which asks for a word.
     *
     * @return the users input
     */
    private String getWord() {
        String word;
        JOptionPane pane = new JOptionPane(JOptionPane.showInputDialog("enter the word you want to search for"));
        if (pane.getMessage() != null) {
            word = (String) pane.getMessage();
            if (word.equals("")) {
                return getWord();
            } else
                return (String) pane.getMessage();
        }
        return null;
    }

    /**
     * Validates whether the word is valid or not.
     *
     * @return true if the word is valid, false otherwise.
     */
    private Boolean isValidWord() {
        //todo implement method
        return false;
    }
}