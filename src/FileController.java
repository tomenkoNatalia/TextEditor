import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FileController {

     NewFile newFile = new NewFile();
     SaveFile saveFile = new SaveFile();
     OpenFile openFile = new OpenFile();
     CloseFile closeFile = new CloseFile();
     JFrame frame;
     JTextPane textPane;
     UndoManager undoManager;
     File file;

     public StyledDocument newFile(){
         StyledDocument doc = new DefaultStyledDocument();
         doc.addUndoableEditListener(new UndoEditListener());
         return doc;
     }

    public FileController(JFrame frame, JTextPane textPane, UndoManager undoManager) {
        this.frame = frame;
        this.textPane = textPane;
        this.undoManager = undoManager;
    }

    class UndoEditListener implements UndoableEditListener {
        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            undoManager.addEdit(e.getEdit());
        }
    }

     class NewFile implements ActionListener {
         JMenuItem newFileButton;
        public NewFile() {
            newFileButton = new JMenuItem("New file");
            newFileButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            initEditorAttributes();
            textPane.setDocument(newFile());
            file = null;
            frame.setTitle("Text Editor ~ new file");
        }

         void initEditorAttributes() {
            AttributeSet characterAttributes = textPane.getCharacterAttributes();
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet(characterAttributes);
            simpleAttributeSet.removeAttributes(characterAttributes);
            textPane.setCharacterAttributes(simpleAttributeSet, true);
        }
    }

     class OpenFile implements ActionListener {
        JMenuItem openFileButton;
        public OpenFile() {
            openFileButton = new JMenuItem("Open File");
            openFileButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            file = chooseFile();
            if (file == null) {
                return;
            }
            readFile(file);
            frame.setTitle("Text Editor ~ " + file.getName());
        }

         File chooseFile() {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            } else {
                return null;
            }
        }

         void readFile(File file) {
            StyledDocument doc;
            try (InputStream stream = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(stream)) {
                doc = (DefaultStyledDocument) ois.readObject();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "Input file was not found!");
                return;
            } catch (ClassNotFoundException | IOException ex) {
                throw new RuntimeException(ex);
            }
            textPane.setDocument(doc);
            doc.addUndoableEditListener(new UndoEditListener());
        }
    }

     class SaveFile implements ActionListener {
        JMenuItem saveFileButton;
        public SaveFile() {
            saveFileButton = new JMenuItem("Save File");
            saveFileButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (file == null) {
                file = chooseFile();
                if (file == null) {
                    return;
                }
            }

            DefaultStyledDocument doc = (DefaultStyledDocument) textPane.getDocument();
            try (OutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(doc);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.setTitle("Text Editor ~ " + file.getName());
        }

         File chooseFile() {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            } else {
                return null;
            }
        }
    }

    static class CloseFile implements ActionListener{
         JMenuItem closeFileButton;
        public CloseFile() {
            closeFileButton = new JMenuItem("Close");
            closeFileButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
