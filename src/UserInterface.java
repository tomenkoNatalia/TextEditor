import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class UserInterface {
    static Color BACKGROUND_COLOR = new Color(217, 190, 250);
    static Color BUTTON_COLOR = new Color(168, 100, 250);
    static Color LABEL_COLOR = Color.white;

    static void createUI(JFrame frame, JTextPane textPane, StylingController sc, FileController fc) {
        textPane.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.setTitle("Text Editor ~ unsaved file");
        textPane.setBorder(new LineBorder(BUTTON_COLOR, 5));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topPanel.setBackground(BACKGROUND_COLOR);
        setButton(sc.formatButtons.bold, "B", 50, topPanel);
        setButton(sc.formatButtons.italic, "I", 50, topPanel);
        setButton(sc.formatButtons.underline, "U", 50, topPanel);
        setComboBox(sc.fontFamily.fontFamilyComboBox, topPanel);
        setComboBox(sc.fontSize.fontSizeComboBox, topPanel);
        setButton(sc.textColor.colorButton, "Text Color", 100, topPanel);
        topPanel.add(new JSeparator());
        topPanel.add(new JSeparator());
        setComboBox(sc.textAlign.textAlignComboBox, topPanel);
        topPanel.add(new JSeparator());
        topPanel.add(new JSeparator());
        setButton(sc.insertPicture.insertPictureButton, "Insert Picture", 120, topPanel);
        setButton(sc.deletePicture.deletePictureButton, "Delete Picture", 120, topPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        setButton(sc.editButtons.copy, "Copy", 70, bottomPanel);
        setButton(sc.editButtons.paste, "Paste", 70, bottomPanel);
        setButton(sc.editButtons.cut, "Cut", 70, bottomPanel);
        bottomPanel.add(new JSeparator());
        bottomPanel.add(new JSeparator());
        setButton(sc.undoRedo.undoButton, "<<", 60, bottomPanel);
        setButton(sc.undoRedo.redoButton, ">>", 60, bottomPanel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(textPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BUTTON_COLOR);
        menuBar.setForeground(LABEL_COLOR);
        menuBar.add(createMenu(fc.newFile.newFileButton, fc.openFile.openFileButton, fc.saveFile.saveFileButton, fc.closeFile.closeFileButton));
        frame.setJMenuBar(menuBar);

        frame.setSize(1000, 500);
        frame.setLocation(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textPane.requestFocusInWindow();
    }
    private static void setButton(JButton button, String label, int width, JPanel panel){
        button.setHideActionText(true);
        button.setPreferredSize(new Dimension(width, 20));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(LABEL_COLOR);
        button.setText(label);
        panel.add(button);
    }

    private static void setComboBox(JComboBox<String> comboBox, JPanel panel){
        comboBox.setEditable(false);
        comboBox.setPreferredSize(new Dimension(120, 20));
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
