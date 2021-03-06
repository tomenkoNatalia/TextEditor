import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UserInterface {
    static Color BACKGROUND_COLOR = new Color(217, 190, 250);
    static Color BUTTON_COLOR = new Color(151, 105, 207);
    static Color LABEL_COLOR = Color.white;

    static void createUI(JFrame frame, JTextPane textPane, StylingController sc, FileController fc) {
        //menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BUTTON_COLOR);
        menuBar.setForeground(LABEL_COLOR);
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(LABEL_COLOR);
        fileMenu.add(fc.newFile.newFileButton);
        fileMenu.add(fc.openFile.openFileButton);
        fileMenu.add(fc.saveFile.saveFileButton);
        fileMenu.add(fc.closeFile.closeFileButton);
        menuBar.add(fileMenu);
        JMenu pictureMenu = new JMenu("Pictures");
        pictureMenu.setForeground(LABEL_COLOR);
        pictureMenu.setForeground(LABEL_COLOR);
        pictureMenu.add(sc.addPicture.addPictureButton);
        pictureMenu.add(sc.deletePicture.deletePictureButton);
        menuBar.add(pictureMenu);

        //top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topPanel.setBackground(BACKGROUND_COLOR);

        //configuring buttons and adding them to panel
        setButton(sc.formatButtons.bold, "B", 50, topPanel);
        setButton(sc.formatButtons.italic, "I", 50, topPanel);
        setButton(sc.formatButtons.underline, "U", 50, topPanel);
        topPanel.add(new JSeparator());
        setComboBox(sc.fontFamily.fontFamilyComboBox, topPanel);
        topPanel.add(new JSeparator());
        setComboBox(sc.fontSize.fontSizeComboBox, topPanel);
        topPanel.add(new JSeparator());
        setComboBox(sc.textAlign.textAlignComboBox, topPanel);
        topPanel.add(new JSeparator());
        setButton(sc.textColor.colorButton, "Text Color", 100, topPanel);
        topPanel.add(new JSeparator());
        setButton(sc.backgroundColor.bgColor, "Page Color", 100, topPanel);

        //bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        //configuring buttons and adding them to panel
        setButton(sc.editButtons.copy, "Copy", 70, bottomPanel);
        setButton(sc.editButtons.paste, "Paste", 70, bottomPanel);
        setButton(sc.editButtons.cut, "Cut", 70, bottomPanel);
        bottomPanel.add(new JSeparator());
        bottomPanel.add(new JSeparator());
        setButton(sc.undoRedo.undoButton, "<<", 60, bottomPanel);
        setButton(sc.undoRedo.redoButton, ">>", 60, bottomPanel);

        //textPane
        textPane.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(new LineBorder(BUTTON_COLOR, 5));

        //configuring frame
        frame.setTitle("Text Editor ~ new file");
        frame.setSize(810, 500);
        frame.setLocation(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //adding panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

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
        comboBox.setPreferredSize(new Dimension(100, 20));
        comboBox.setBackground(BUTTON_COLOR);
        comboBox.setForeground(LABEL_COLOR);
        panel.add(comboBox);
    }
}
