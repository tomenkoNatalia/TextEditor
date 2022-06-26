import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

public class UserInterface {

    static void createUI(JFrame frame, JTextPane editor, StylingController stylingController, FileController fileController) {

        frame.setTitle("Text Editor ~ unsaved file");
        JScrollPane editorScrollPane = new JScrollPane(editor);
//        editor.setDocument(getNewDocument);
//        undoMgr = new UndoManager();

        Color backgroundColor = new Color(250, 249, 212);
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.setBackground(backgroundColor);

        setButton(StylingController.EditButtons.copy, panel1);
        setButton(StylingController.EditButtons.paste, panel1);
        setButton(StylingController.EditButtons.cut, panel1);
        setButton(StylingController.FormatButtons.bold, panel1);
        setButton(StylingController.FormatButtons.italic, panel1);
        setButton(StylingController.FormatButtons.underline, panel1);

        StylingController.TextColor.colorButton.setBackground(new Color(235, 204, 113));

        setComboBox(StylingController.TextAlign.textAlignComboBox, panel1);
        setComboBox(StylingController.FontSize.fontSizeComboBox, panel1);
        setComboBox(StylingController.FontFamily.fontFamilyComboBox, panel1);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        StylingController.InsertPicture.insertPictureButton.setBackground(new Color(235, 204, 113));
        panel2.add(StylingController.InsertPicture.insertPictureButton);

        StylingController.DeletePicture.deletePictureButton.setBackground(new Color(235, 204, 113));
        panel2.add(StylingController.DeletePicture.deletePictureButton);
        panel2.setBackground(backgroundColor);

//        undoButton.setBackground(new Color(235, 204, 113));
//
//        JButton redoButton = new JButton(">>");
//        redoButton.addActionListener(redoActionListener);
//        redoButton.setBackground(new Color(235, 204, 113));
//        panel2.add(undoButton);
//        panel2.add(redoButton);

        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
        toolBarPanel.add(panel1);
        toolBarPanel.add(panel2);

        frame.add(toolBarPanel, BorderLayout.NORTH);
        frame.add(editorScrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.ORANGE);
        menuBar.add(createMenu(FileController.NewFile.newFile, FileController.OpenFile.openFile, FileController.SaveFile.saveFile));
        frame.setJMenuBar(menuBar);

        frame.setSize(1000, 500);
        frame.setLocation(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        editor.requestFocusInWindow();
    }
    private static void setButton(JButton button, JPanel panel){
        button.setHideActionText(true);
        button.setBackground(new Color(235, 204, 113));
        panel.add(button);
    }

    private static void setComboBox(JComboBox<String> comboBox, JPanel panel){
        comboBox.setEditable(false);
        comboBox.setBackground(Color.ORANGE);
        panel.add(comboBox);
    }

    public static JMenu createMenu(JMenuItem newFile, JMenuItem openFile, JMenuItem saveFile){
        JMenu fileMenu = new JMenu("Menu");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        newFile.setMnemonic(KeyEvent.VK_N);
        openFile.setMnemonic(KeyEvent.VK_O);
        saveFile.setMnemonic(KeyEvent.VK_S);
//        exitItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                System.exit(0);
//            }
//        });

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);

        return fileMenu;
    }
}
