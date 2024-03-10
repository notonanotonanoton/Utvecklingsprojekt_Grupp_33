package boundary;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class WordWrapRenderer extends JTextArea implements TableCellRenderer {
    WordWrapRenderer(Color background, Color foreground, Font font) {
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setBackground(background);
        setForeground(foreground);
        setFont(font);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));

        return this;
    }
}
