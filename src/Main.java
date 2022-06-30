
import javax.swing.*;
import javax.swing.undo.UndoManager;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        JTextPane textPane = new JTextPane();
        UndoManager undoManager = new UndoManager();
        StylingController sc = new StylingController(frame, textPane, undoManager);
        FileController fc = new FileController(frame, textPane, undoManager);
        textPane.setDocument(fc.newFile());
        UserInterface.createUI(frame, textPane, sc, fc);
    }
}