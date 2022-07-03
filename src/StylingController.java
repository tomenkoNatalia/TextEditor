import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.text.StyledEditorKit.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StylingController {
    FormatButtons formatButtons = new FormatButtons();
    EditButtons editButtons = new EditButtons();
    TextAlign textAlign = new TextAlign();
    FontFamily fontFamily = new FontFamily();
    FontSize fontSize = new FontSize();
    TextColor textColor = new TextColor();
    BackgroundColor backgroundColor = new BackgroundColor();
    AddPicture addPicture = new AddPicture();
    DeletePicture deletePicture = new DeletePicture();
    UndoRedo  undoRedo = new UndoRedo();
    
    private final JFrame frame;
    private final JTextPane textPane;
    private final UndoManager undoManager;
    private static String pictureButtonName;

    public StylingController(JFrame frame, JTextPane textPane, UndoManager undoManager) {
        this.frame = frame;
        this.textPane = textPane;
        this.undoManager = undoManager;
    }

    class FormatButtons implements ActionListener {
         JButton bold;
         JButton italic;
         JButton underline;

         public FormatButtons() {
             bold = new JButton(new BoldAction());
             bold.addActionListener(this);
             bold.setFont(new Font("Arial", Font.BOLD, 15));
             italic = new JButton(new ItalicAction());
             italic.addActionListener(this);
             italic.setFont(new Font("Verdana", Font.ITALIC, 14));
             underline = new JButton(new UnderlineAction());
             underline.addActionListener(this);
             Map<TextAttribute, Object> attributes = new HashMap<>();
             attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
             underline.setFont(underline.getFont().deriveFont(attributes));
         }

         @Override
        public void actionPerformed(ActionEvent e) {
            textPane.requestFocusInWindow();
        }
    }

    class EditButtons implements ActionListener {
         JButton copy;
         JButton paste;
         JButton cut;
        public EditButtons() {
            copy = new JButton(new CopyAction());
            copy.addActionListener(this);
            paste = new JButton(new PasteAction());
            paste.addActionListener(this);
            cut = new JButton(new CutAction());
            cut.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            textPane.requestFocusInWindow();
        }
    }

     class TextColor implements ActionListener {
          JButton colorButton;
         public TextColor() {
             colorButton = new JButton();
             colorButton.addActionListener(this);
         }

         @Override
        public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(frame, "Color Picker", Color.RED);
            if (newColor == null) {
                textPane.requestFocusInWindow();
                return;
            }
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, newColor);
            textPane.setCharacterAttributes(attr, false);
            textPane.requestFocusInWindow();
        }
    }

    class BackgroundColor implements ActionListener{
        JButton bgColor;

        public BackgroundColor() {
            this.bgColor = new JButton();
            bgColor.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e){
            Color newColor = JColorChooser.showDialog(frame, "Color Picker", Color.RED);
            if (newColor == null) {
                textPane.requestFocusInWindow();
                return;
            }
            textPane.setBackground(newColor);
        }
    }

     class TextAlign implements ItemListener {
          JComboBox<String> textAlignComboBox;
         public TextAlign() {
             textAlignComboBox = new JComboBox<>(new String[]{"Alignment", "left", "center", "right"});
             textAlignComboBox.addItemListener(this);
         }

         @Override
        public void itemStateChanged(ItemEvent e) {
            if ((e.getStateChange() != ItemEvent.SELECTED) || (textAlignComboBox.getSelectedIndex() == 0)) {
                return;
            }
            String alignmentS = (String) e.getItem();
            int newAlignment = textAlignComboBox.getSelectedIndex() - 1;
            textAlignComboBox.setAction(new StyledEditorKit.AlignmentAction(alignmentS, newAlignment));
            textAlignComboBox.setSelectedIndex(0);
            textPane.requestFocusInWindow();
        }
    }

     class FontSize implements ItemListener {
          JComboBox<String> fontSizeComboBox;
         public FontSize() {
             fontSizeComboBox = new JComboBox<>(new String[]{"12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "36", "48", "72"});
             fontSizeComboBox.setSelectedIndex(3);
             fontSizeComboBox.addItemListener(this);
         }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if ((e.getStateChange() != ItemEvent.SELECTED) || (fontSizeComboBox.getSelectedIndex() == 0)) {
                return;
            }
            String fontSizeStr = (String) e.getItem();
            int newFontSize;
            try {
                newFontSize = Integer.parseInt(fontSizeStr);
            } catch (NumberFormatException ex) {return;}

            fontSizeComboBox.setAction(new StyledEditorKit.FontSizeAction(fontSizeStr, newFontSize));
            textPane.requestFocusInWindow();
        }
    }

     class FontFamily implements ItemListener {
          JComboBox<String> fontFamilyComboBox;
         public FontFamily() {
             fontFamilyComboBox = new JComboBox<>(new String[]{"Arial", "Bell MT", "Calibri", "Courier New","Georgia",
                     "Helevetica", "Lucida Sans", "MS Gothic", "Times New Roman", "Verdana"});
             fontFamilyComboBox.setSelectedIndex(0);
             fontFamilyComboBox.addItemListener(this);
         }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if ((e.getStateChange() != ItemEvent.SELECTED) || (fontFamilyComboBox.getSelectedIndex() == 0)) {
                return;
            }
            String fontFamily = (String) e.getItem();
            fontFamilyComboBox.setAction(new StyledEditorKit.FontFamilyAction(fontFamily, fontFamily));
            textPane.requestFocusInWindow();
        }
    }
    class UndoRedo implements ActionListener {
        JButton undoButton;
        JButton redoButton;
        public UndoRedo() {
            undoButton = new JButton();
            undoButton.addActionListener(this);
            redoButton = new JButton();
            redoButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (undoButton.equals(source)) {
                if (!undoManager.canUndo()) {
                    textPane.requestFocusInWindow();
                    return;
                }
                undoManager.undo();
            } else if (redoButton.equals(source)) {
                if (!undoManager.canRedo()) {
                    textPane.requestFocusInWindow();
                    return;
                }
                undoManager.redo();
            }
            textPane.requestFocusInWindow();
        }
    }

     class AddPicture implements ActionListener {
         JMenuItem addPictureButton;
         public AddPicture() {
             addPictureButton = new JMenuItem("Add Picture");
             addPictureButton.addActionListener(this);
         }

         @Override
        public void actionPerformed(ActionEvent e) {
            File pictureFile = choosePictureFile();
            if (pictureFile == null) {
                textPane.requestFocusInWindow();
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
            textPane.insertComponent(picButton);
            textPane.requestFocusInWindow();
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
        JMenuItem deletePictureButton;
        public DeletePicture() {
            deletePictureButton = new JMenuItem("Delete Picture");
            deletePictureButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StyledDocument doc = (DefaultStyledDocument) textPane.getDocument();
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
            textPane.requestFocusInWindow();
            pictureButtonName = null;
        }
    }
}
