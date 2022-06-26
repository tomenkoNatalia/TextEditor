import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.text.StyledEditorKit.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;

public class StylingController {
    FormatButtons formatButtons = new FormatButtons();
    EditButtons editButtons = new EditButtons();
    TextAlign textAlign = new TextAlign();
    TextColor textColor = new TextColor();
    FontFamily fontFamily = new FontFamily();
    FontSize fontSize = new FontSize();
    InsertPicture insertPicture = new InsertPicture();
    DeletePicture deletePicture = new DeletePicture();
    PictureFocusListener pictureFocusListener = new PictureFocusListener();
    
    private JFrame frame;
    private JTextPane editor;
    private UndoManager undoMgr;
    private static String pictureButtonName;

    enum UndoActionType {UNDO, REDO};

    public StylingController(JFrame frame, JTextPane editor, UndoManager undoMgr) {
        this.frame = frame;
        this.editor = editor;
        this.undoMgr = undoMgr;
    }

    class FormatButtons implements ActionListener {
        static JButton bold;
        static JButton italic;
        static JButton underline;

         public FormatButtons() {
             bold = new JButton(new BoldAction());
             bold.setText("Bold");
             bold.addActionListener(this);
             italic = new JButton(new ItalicAction());
             italic.setText("Italic");
             italic.addActionListener(this);
             underline = new JButton(new UnderlineAction());
             underline.setText("Underline");
             underline.addActionListener(this);
         }

         @Override
        public void actionPerformed(ActionEvent e) {
            editor.requestFocusInWindow();
        }
    }

    class EditButtons implements ActionListener {
        static JButton copy;
        static JButton paste;
        static JButton cut;

        public EditButtons() {
            copy = new JButton(new CopyAction());
            copy.setText("Copy");
            copy.addActionListener(this);
            paste = new JButton(new PasteAction());
            paste.setText("Paste");
            paste.addActionListener(this);
            cut = new JButton(new CutAction());
            cut.setText("Cut");
            cut.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            editor.requestFocusInWindow();
        }
    }

     class TextColor implements ActionListener {
         static JButton colorButton;

         public TextColor() {
             colorButton = new JButton("text color");
             colorButton.addActionListener(this);
         }

         @Override
        public void actionPerformed(ActionEvent e) {

            Color newColor = JColorChooser.showDialog(frame, "color picker", Color.RED);
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

     class TextAlign implements ItemListener {
         static JComboBox<String> textAlignComboBox;

         public TextAlign() {
             textAlignComboBox = new JComboBox<>(new String[]{"text alignment", "вліво", "посередині", "вправо"});
             textAlignComboBox.addItemListener(this);
         }

         @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() != ItemEvent.SELECTED) || (textAlignComboBox.getSelectedIndex() == 0)) {

                return;
            }

            String alignmentStr = (String) e.getItem();
            int newAlignment = textAlignComboBox.getSelectedIndex() - 1;

            textAlignComboBox.setAction(new StyledEditorKit.AlignmentAction(alignmentStr, newAlignment));
            textAlignComboBox.setSelectedIndex(0);
            editor.requestFocusInWindow();
        }
    }

     class FontSize implements ItemListener {
         static JComboBox<String> fontSizeComboBox;

         public FontSize() {
             fontSizeComboBox = new JComboBox<>(new String[]{"font size", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "36", "48", "72"});
             fontSizeComboBox.addItemListener(this);
         }

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

            fontSizeComboBox.setAction(new StyledEditorKit.FontSizeAction(fontSizeStr, newFontSize));
            fontSizeComboBox.setSelectedIndex(0);
            editor.requestFocusInWindow();
        }
    }

     class FontFamily implements ItemListener {

         static JComboBox<String> fontFamilyComboBox;

         public FontFamily() {
             fontFamilyComboBox = new JComboBox<>(new String[]{"font family", "Arial", "Bell MT", "Calibri", "Courier New","Georgia",
                     "Helevetica", "Lucida Sans", "MS Gothic", "Times New Roman", "Verdana"});
             fontFamilyComboBox.addItemListener(this);
         }

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() != ItemEvent.SELECTED) || (fontFamilyComboBox.getSelectedIndex() == 0)) {

                return;
            }

            String fontFamily = (String) e.getItem();
            fontFamilyComboBox.setAction(new StyledEditorKit.FontFamilyAction(fontFamily, fontFamily));
            fontFamilyComboBox.setSelectedIndex(0);
            editor.requestFocusInWindow();
        }
    }

//     class UndoEditListener implements UndoableEditListener {
//
//        @Override
//        public void undoableEditHappened(UndoableEditEvent e) {
//
//            undoMgr.addEdit(e.getEdit());
//        }
//    }
//
//     class UndoActionListener implements ActionListener {
//         UndoActionType undoActionType;
//
//        public UndoActionListener(UndoActionType type) {
//
//            undoActionType = type;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//
//            switch (undoActionType) {
//
//                case UNDO:
//                    if (!undoMgr.canUndo()) {
//
//                        editor.requestFocusInWindow();
//                        return; // no edits to undo
//                    }
//
//                    undoMgr.undo();
//                    break;
//
//                case REDO:
//                    if (!undoMgr.canRedo()) {
//
//                        editor.requestFocusInWindow();
//                        return; // no edits to redo
//                    }
//
//                    undoMgr.redo();
//            }
//
//            editor.requestFocusInWindow();
//        }
//    }

     class InsertPicture implements ActionListener {
         static JButton insertPictureButton;

         public InsertPicture() {
             insertPictureButton = new JButton("insert picture");
             insertPictureButton.addActionListener(this);
         }

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

         File choosePictureFile() {

            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG & GIF Images", "png", "jpg", "gif");
            chooser.setFileFilter(filter);

            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            } else {
                return null;
            }
        }
    }

     static class PictureFocusListener implements FocusListener {

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

     class DeletePicture implements ActionListener {
         static JButton deletePictureButton;

         public DeletePicture() {
             deletePictureButton = new JButton("delete picture");
             deletePictureButton.addActionListener(this);
         }

         @Override
        public void actionPerformed(ActionEvent e) {

            StyledDocument doc = (DefaultStyledDocument) editor.getDocument();
            ElementIterator iterator = new ElementIterator(doc);
            Element element;

            while ((element = iterator.next()) != null) {

                AttributeSet attrs = element.getAttributes();

                if (attrs.containsAttribute(AbstractDocument.ElementNameAttribute, StyleConstants.ComponentElementName)) {

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
    }
}
