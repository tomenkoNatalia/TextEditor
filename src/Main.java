
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

        UIManager.put("TextPane.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                JTextPane editor = new JTextPane();
                UndoManager undoMgr = new UndoManager();
                StylingController sc = new StylingController(frame, editor, undoMgr);
                FileController fc = new FileController(frame, editor, undoMgr);
                editor.setDocument(fc.newFile());
                UserInterface.createUI(frame, editor, sc, fc);
            }
        });
    }

//    private void setFrameTitleWithExtn(String titleExtn) {
//
//        frame.setTitle("Text Editor ~ " + titleExtn);
//    }
//
//    private StyledDocument getNewDocument() {
//
//        StyledDocument doc = new DefaultStyledDocument();
//        doc.addUndoableEditListener(new UndoEditListener());
//        return doc;
//    }
//
//    private StyledDocument getEditorDocument() {
//
//        StyledDocument doc = (DefaultStyledDocument) editor.getDocument();
//        return doc;
//    }
}