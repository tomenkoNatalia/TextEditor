import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class UserInterface {
    static Color BACKGROUND_COLOR = new Color(223, 195, 250);
    static Color BUTTON_COLOR = new Color(178, 111, 242);
    static Color LABEL_COLOR = Color.white;

    static void createUI(JFrame frame, JTextPane editor, StylingController sc, FileController fc) {
        UIManager.put("TextPane.font", new Font("Arial", Font.PLAIN, 30));
        frame.setTitle("Text Editor ~ unsaved file");
        JScrollPane editorScrollPane = new JScrollPane(editor);
        editor.setBorder(new LineBorder(BUTTON_COLOR, 5));

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.setBackground(BACKGROUND_COLOR);
        setButton(sc.editButtons.copy, "Copy", panel1);
        setButton(sc.editButtons.paste, "Paste", panel1);
        setButton(sc.editButtons.cut, "Cut", panel1);
        setButton(sc.formatButtons.bold, "Bold", panel1);
        setButton(sc.formatButtons.italic, "Italic", panel1);
        setButton(sc.formatButtons.underline, "Underline", panel1);

        sc.textColor.colorButton.setBackground(BUTTON_COLOR);
        sc.textColor.colorButton.setForeground(LABEL_COLOR);

        setComboBox(sc.textAlign.textAlignComboBox, panel1);
        setComboBox(sc.fontSize.fontSizeComboBox, panel1);
        setComboBox(sc.fontFamily.fontFamilyComboBox, panel1);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sc.insertPicture.insertPictureButton.setBackground(BUTTON_COLOR);
        sc.insertPicture.insertPictureButton.setForeground(LABEL_COLOR);
        panel2.add(sc.insertPicture.insertPictureButton);

        sc.deletePicture.deletePictureButton.setBackground(BUTTON_COLOR);
        sc.deletePicture.deletePictureButton.setForeground(LABEL_COLOR);
        panel2.add(sc.deletePicture.deletePictureButton);
        panel2.setBackground(BACKGROUND_COLOR);

        sc.undoRedo.undoButton.setBackground(BUTTON_COLOR);
        sc.undoRedo.redoButton.setBackground(BUTTON_COLOR);
       sc.undoRedo.undoButton.setForeground(LABEL_COLOR);
        sc.undoRedo.redoButton.setForeground(LABEL_COLOR);
        panel2.add(sc.undoRedo.undoButton);
        panel2.add(sc.undoRedo.redoButton);


        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
        toolBarPanel.add(panel1);
        toolBarPanel.add(panel2);

        frame.add(toolBarPanel, BorderLayout.NORTH);
        frame.add(editorScrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BUTTON_COLOR);
        menuBar.setForeground(LABEL_COLOR);
        menuBar.add(createMenu(fc.newFile.newFileButton, fc.openFile.openFileButton, fc.saveFile.saveFileButton, fc.closeFile.closeFileButton));
        frame.setJMenuBar(menuBar);

        frame.setSize(1000, 500);
        frame.setLocation(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        editor.requestFocusInWindow();
    }
    private static void setButton(JButton button, String label, JPanel panel){
        button.setHideActionText(true);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(LABEL_COLOR);
        button.setText(label);
        panel.add(button);
    }

    private static void setComboBox(JComboBox<String> comboBox, JPanel panel){
        comboBox.setEditable(false);
        comboBox.setBackground(BUTTON_COLOR);
        comboBox.setForeground(LABEL_COLOR);
        panel.add(comboBox);
    }

    public static JMenu createMenu(JMenuItem newFile, JMenuItem openFile, JMenuItem saveFile, JMenuItem closeFile){
        JMenu fileMenu = new JMenu("Menu");
        fileMenu.setForeground(LABEL_COLOR);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        newFile.setMnemonic(KeyEvent.VK_N);
        openFile.setMnemonic(KeyEvent.VK_O);
        saveFile.setMnemonic(KeyEvent.VK_S);
        closeFile.setMnemonic(KeyEvent.VK_X);

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(closeFile);

        return fileMenu;
    }
}
