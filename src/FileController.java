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
    JFrame frame;
     JTextPane editor;
     UndoManager undoMgr;
     File file;

    public FileController(JFrame frame, JTextPane editor, UndoManager undoMgr) {
        this.frame = frame;
        this.editor = editor;
        this.undoMgr = undoMgr;
    }

    class UndoEditListener implements UndoableEditListener {

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {

            undoMgr.addEdit(e.getEdit());
        }
    }

     class NewFile implements ActionListener {
        static JMenuItem newFile;

        public NewFile() {
            newFile = new JMenuItem("New file");
            newFile.addActionListener(this);
        }

        StyledDocument getNewDocument() {
        StyledDocument doc = new DefaultStyledDocument();
        doc.addUndoableEditListener(new UndoEditListener());
        return doc;
    }

        @Override
        public void actionPerformed(ActionEvent e) {

            initEditorAttributes();
            editor.setDocument(getNewDocument());
            file = null;
            frame.setTitle("Text Editor ~ new file");
        }

         void initEditorAttributes() {

            AttributeSet attrs1 = editor.getCharacterAttributes();
            SimpleAttributeSet attrs2 = new SimpleAttributeSet(attrs1);
            attrs2.removeAttributes(attrs1);
            editor.setCharacterAttributes(attrs2, true);
        }
    }

     class OpenFile implements ActionListener {
        static JMenuItem openFile;

        public OpenFile() {
            openFile = new JMenuItem("Open File");
            openFile.addActionListener(this);
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

         void applyFocusListenerToPictures(StyledDocument doc) {

            ElementIterator iterator = new ElementIterator(doc);
            Element element;

            while ((element = iterator.next()) != null) {

                AttributeSet attrs = element.getAttributes();

                if (attrs.containsAttribute(AbstractDocument.ElementNameAttribute, StyleConstants.ComponentElementName)) {

                    JButton picButton = (JButton) StyleConstants.getComponent(attrs);
                    picButton.addFocusListener(new StylingController.PictureFocusListener());
                }
            }
        }
    }

     class SaveFile implements ActionListener {
        static JMenuItem saveFile;

        public SaveFile() {
            saveFile = new JMenuItem("Save File");
            saveFile.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (file == null) {

                file = chooseFile();

                if (file == null) {

                    return;
                }
            }

            DefaultStyledDocument doc = (DefaultStyledDocument) editor.getDocument();

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
}
