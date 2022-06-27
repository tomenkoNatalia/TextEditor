
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
import java.util.Random;
import java.io.*;

public class Main {

//    private UndoManager undoMgr;
//    private String pictureButtonName;
//    private File file;

    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame();
        JTextPane textPane = new JTextPane();
        UndoManager undoManager = new UndoManager();
        StylingController sc = new StylingController(frame, textPane, undoManager);
        FileController fc = new FileController(frame, textPane, undoManager);
        textPane.setDocument(fc.newFile());
        UserInterface.createUI(frame, textPane, sc, fc);
    }
}