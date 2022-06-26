
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.text.StyledEditorKit.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.LineBorder;
import javax.swing.undo.UndoManager;
import javax.swing.event.*;
import java.util.List;
import java.util.Vector;
import java.util.Arrays;
import java.util.Random;
import java.io.*;

public class Main {

    private JFrame frame;
    private JTextPane editor;
    private JComboBox<String> fontSizeComboBox;
    private JComboBox<String> textAlignComboBox;
    private JComboBox<String> fontFamilyComboBox;
    private UndoManager undoMgr;
    private String pictureButtonName;
    private File file;

    enum UndoActionType {
        UNDO, REDO};
    private static final List<String> FONT_LIST = Arrays
            .asList("Arial", "Bell MT", "Calibri", "Courier New","Georgia",
                    "Helevetica", "Lucida Sans", "MS Gothic", "Times New Roman", "Verdana");
    private static final String[] FONT_SIZES = { "Розмір шрифту", "12", "14", "16", "18", "20", "22", "24", "26", "28",
            "30", "36", "48", "72" };
    private static final String[] TEXT_ALIGNMENTS = { "вирівнювання", "вліво", "посередині", "вправо"};
    private static final String ELEM = AbstractDocument.ElementNameAttribute;
    private static final String COMP = StyleConstants.ComponentElementName;

    public static void main(String[] args) throws Exception {

        UIManager.put("TextPane.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                new Main().createAndShowGUI();
            }
        });
    }

    private void setButton(JButton button, String label, Color color, EditButtonActionListener e){
        button.setHideActionText(true);
        button.setText(label);
        button.setBackground(color);
        button.addActionListener(e);
    }
    private void createAndShowGUI() {

        frame = new JFrame();
        setFrameTitleWithExtn("новий файл");
        editor = new JTextPane();
        JScrollPane editorScrollPane = new JScrollPane(editor);
        editor.setDocument(getNewDocument());


        undoMgr = new UndoManager();
        EditButtonActionListener editButtonListener = new EditButtonActionListener();

        Color btnColor = new Color(235, 204, 113);

        JButton cutButton = new JButton(new CutAction());
        setButton(cutButton, "вирізати", btnColor, editButtonListener);

        JButton copyButton = new JButton(new CopyAction());
        setButton(copyButton, "копіювати", btnColor, editButtonListener);

        JButton pasteButton = new JButton(new PasteAction());
        setButton(pasteButton, "вставити", btnColor, editButtonListener);

        JButton boldButton = new JButton(new BoldAction());
        setButton(boldButton, "жирний", btnColor, editButtonListener);

        JButton italicButton = new JButton(new ItalicAction());
        setButton(italicButton, "курсив", btnColor, editButtonListener);

        JButton underlineButton = new JButton(new UnderlineAction());
        setButton(underlineButton, "підкреслення", btnColor, editButtonListener);

        JButton colorButton = new JButton("колір");
        colorButton.setBackground(btnColor);
        colorButton.addActionListener(new ColorActionListener());

        textAlignComboBox = new JComboBox<String>(TEXT_ALIGNMENTS);
        textAlignComboBox.setBackground(btnColor);
        textAlignComboBox.setEditable(false);
        textAlignComboBox.addItemListener(new TextAlignItemListener());
        textAlignComboBox.setBackground(Color.ORANGE);

        fontSizeComboBox = new JComboBox<String>(FONT_SIZES);
        fontSizeComboBox.setEditable(false);
        fontSizeComboBox.addItemListener(new FontSizeItemListener());
        fontSizeComboBox.setBackground(Color.ORANGE);

        Vector<String> editorFonts = getEditorFonts();
        editorFonts.add(0, "шрифт");
        fontFamilyComboBox = new JComboBox<String>(editorFonts);
        fontFamilyComboBox.setBackground(btnColor);
        fontFamilyComboBox.setEditable(false);
        fontFamilyComboBox.addItemListener(new FontFamilyItemListener());
        fontFamilyComboBox.setBackground(Color.ORANGE);

        JButton insertPictureButton = new JButton("вставити картинку");
        insertPictureButton.addActionListener(new PictureInsertActionListener());
        insertPictureButton.setBackground(btnColor);

        JButton deletePictureButton = new JButton("видалити картинку");
        deletePictureButton.setBackground(btnColor);
        deletePictureButton.addActionListener(new PictureDeleteActionListener());

        JButton undoButton = new JButton("назад");
        undoButton.addActionListener(new UndoActionListener(UndoActionType.UNDO));
        undoButton.setBackground(btnColor);

        JButton redoButton = new JButton("вперед");
        redoButton.addActionListener(new UndoActionListener(UndoActionType.REDO));
        redoButton.setBackground(btnColor);


        Color backgroundColor = new Color(250, 249, 212);
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.setBackground(backgroundColor);
        panel1.add(cutButton);
        panel1.add(copyButton);
        panel1.add(pasteButton);
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(boldButton);
        panel1.add(italicButton);
        panel1.add(underlineButton);
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(colorButton);
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(textAlignComboBox);
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(fontSizeComboBox);
        panel1.add(new JSeparator(SwingConstants.VERTICAL));
        panel1.add(fontFamilyComboBox);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2.setBackground(backgroundColor);
        panel2.add(insertPictureButton);
        panel2.add(deletePictureButton);
        panel2.add(new JSeparator(SwingConstants.VERTICAL));
        panel2.add(new JSeparator(SwingConstants.VERTICAL));
        panel2.add(new JSeparator(SwingConstants.VERTICAL));
        panel2.add(undoButton);
        panel2.add(redoButton);

        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
        toolBarPanel.add(panel1);
        toolBarPanel.add(panel2);

        frame.add(toolBarPanel, BorderLayout.NORTH);
        frame.add(editorScrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.ORANGE);
        JMenu fileMenu = new JMenu("МЕНЮ");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem newItem = new JMenuItem("новий файл");
        newItem.setMnemonic(KeyEvent.VK_N);
        newItem.addActionListener(new NewFileListener());
        JMenuItem openItem = new JMenuItem("відкрити");
        openItem.setMnemonic(KeyEvent.VK_O);
        openItem.addActionListener(new OpenFileListener());
        JMenuItem saveItem = new JMenuItem("зберегти");
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.addActionListener(new SaveFileListener());
        JMenuItem exitItem = new JMenuItem("закрити");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });

        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(1000, 500);
        frame.setLocation(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        editor.requestFocusInWindow();
    }

    private void setFrameTitleWithExtn(String titleExtn) {

        frame.setTitle("Текстовий редактор - " + titleExtn);
    }

    private StyledDocument getNewDocument() {

        StyledDocument doc = new DefaultStyledDocument();
        doc.addUndoableEditListener(new UndoEditListener());
        return doc;
    }

    private Vector<String> getEditorFonts() {

        String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Vector<String> returnList = new Vector<>();

        for (String font : availableFonts) {

            if (FONT_LIST.contains(font)) {

                returnList.add(font);
            }
        }

        return returnList;
    }

    private class EditButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            editor.requestFocusInWindow();
        }
    }

    private class ColorActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Color newColor = JColorChooser.showDialog(frame, "абракадабра", Color.RED);
            if (newColor == null) {

                editor.requestFocusInWindow();
                return;
            }

            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, newColor);
            editor.setCharacterAttributes(attr, false);
            editor.requestFocusInWindow();
        }
    }

    private class TextAlignItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() != ItemEvent.SELECTED) || (textAlignComboBox.getSelectedIndex() == 0)) {

                return;
            }

            String alignmentStr = (String) e.getItem();
            int newAlignment = textAlignComboBox.getSelectedIndex() - 1;

            textAlignComboBox.setAction(new AlignmentAction(alignmentStr, newAlignment));
            textAlignComboBox.setSelectedIndex(0);
            editor.requestFocusInWindow();
        }
    }

    private class FontSizeItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() != ItemEvent.SELECTED) || (fontSizeComboBox.getSelectedIndex() == 0)) {

                return;
            }

            String fontSizeStr = (String) e.getItem();
            int newFontSize = 0;

            try {
                newFontSize = Integer.parseInt(fontSizeStr);
            } catch (NumberFormatException ex) {

                return;
            }

            fontSizeComboBox.setAction(new FontSizeAction(fontSizeStr, newFontSize));
            fontSizeComboBox.setSelectedIndex(0);
            editor.requestFocusInWindow();
        }
    }

    private class FontFamilyItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() != ItemEvent.SELECTED) || (fontFamilyComboBox.getSelectedIndex() == 0)) {

                return;
            }

            String fontFamily = (String) e.getItem();
            fontFamilyComboBox.setAction(new FontFamilyAction(fontFamily, fontFamily));
            fontFamilyComboBox.setSelectedIndex(0);
            editor.requestFocusInWindow();
        }
    }

    private class UndoEditListener implements UndoableEditListener {

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {

            undoMgr.addEdit(e.getEdit());
        }
    }

    private class UndoActionListener implements ActionListener {

        private UndoActionType undoActionType;

        public UndoActionListener(UndoActionType type) {

            undoActionType = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (undoActionType) {

                case UNDO:
                    if (!undoMgr.canUndo()) {

                        editor.requestFocusInWindow();
                        return; // no edits to undo
                    }

                    undoMgr.undo();
                    break;

                case REDO:
                    if (!undoMgr.canRedo()) {

                        editor.requestFocusInWindow();
                        return; // no edits to redo
                    }

                    undoMgr.redo();
            }

            editor.requestFocusInWindow();
        }
    } // UndoActionListener

    private class PictureInsertActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            File pictureFile = choosePictureFile();

            if (pictureFile == null) {

                editor.requestFocusInWindow();
                return;
            }

            ImageIcon icon = new ImageIcon(pictureFile.toString());
            JButton picButton = new JButton(icon);
            picButton.setBorder(new LineBorder(Color.YELLOW));
            picButton.setMargin(new Insets(0, 0, 0, 0));
            picButton.setAlignmentY(.9f);
            picButton.setAlignmentX(.9f);
            picButton.addFocusListener(new PictureFocusListener());
            picButton.setName("PICTURE_ID_" + new Random().nextInt());
            editor.insertComponent(picButton);
            editor.requestFocusInWindow();
        }

        private File choosePictureFile() {

            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG & GIF Images", "png", "jpg", "gif");
            chooser.setFileFilter(filter);

            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            } else {
                return null;
            }
        }
    } // PictureInsertActionListener

    private class PictureFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {

            JButton button = (JButton) e.getComponent();
            button.setBorder(new LineBorder(Color.RED));
            pictureButtonName = button.getName();
        }

        @Override
        public void focusLost(FocusEvent e) {

            ((JButton) e.getComponent()).setBorder(new LineBorder(Color.RED));
        }
    }

    private class PictureDeleteActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            StyledDocument doc = getEditorDocument();
            ElementIterator iterator = new ElementIterator(doc);
            Element element;

            while ((element = iterator.next()) != null) {

                AttributeSet attrs = element.getAttributes();

                if (attrs.containsAttribute(ELEM, COMP)) {

                    JButton button = (JButton) StyleConstants.getComponent(attrs);

                    if (button.getName().equals(pictureButtonName)) {

                        try {
                            doc.remove(element.getStartOffset(), 1); // length = 1
                        } catch (BadLocationException ex_) {

                            throw new RuntimeException(ex_);
                        }
                    }
                }
            }

            editor.requestFocusInWindow();
            pictureButtonName = null;
        }
    } // PictureDeleteActionListener

    private StyledDocument getEditorDocument() {

        StyledDocument doc = (DefaultStyledDocument) editor.getDocument();
        return doc;
    }

    private class NewFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            initEditorAttributes();
            editor.setDocument(getNewDocument());
            file = null;
            setFrameTitleWithExtn("New file");
        }

        private void initEditorAttributes() {

            AttributeSet attrs1 = editor.getCharacterAttributes();
            SimpleAttributeSet attrs2 = new SimpleAttributeSet(attrs1);
            attrs2.removeAttributes(attrs1);
            editor.setCharacterAttributes(attrs2, true);
        }
    }

    private class OpenFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            file = chooseFile();

            if (file == null) {

                return;
            }

            readFile(file);
            setFrameTitleWithExtn(file.getName());
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            } else {
                return null;
            }
        }

        private void readFile(File file) {

            StyledDocument doc = null;

            try (InputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {

                doc = (DefaultStyledDocument) ois.readObject();
            } catch (FileNotFoundException ex) {

                JOptionPane.showMessageDialog(frame, "Input file was not found!");
                return;
            } catch (ClassNotFoundException | IOException ex) {

                throw new RuntimeException(ex);
            }

            editor.setDocument(doc);
            doc.addUndoableEditListener(new UndoEditListener());
            applyFocusListenerToPictures(doc);
        }

        private void applyFocusListenerToPictures(StyledDocument doc) {

            ElementIterator iterator = new ElementIterator(doc);
            Element element;

            while ((element = iterator.next()) != null) {

                AttributeSet attrs = element.getAttributes();

                if (attrs.containsAttribute(ELEM, COMP)) {

                    JButton picButton = (JButton) StyleConstants.getComponent(attrs);
                    picButton.addFocusListener(new PictureFocusListener());
                }
            }
        }
    }

    private class SaveFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (file == null) {

                file = chooseFile();

                if (file == null) {

                    return;
                }
            }

            DefaultStyledDocument doc = (DefaultStyledDocument) getEditorDocument();

            try (OutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(doc);
            } catch (IOException ex) {

                throw new RuntimeException(ex);
            }

            setFrameTitleWithExtn(file.getName());
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            } else {
                return null;
            }
        }
    }
}