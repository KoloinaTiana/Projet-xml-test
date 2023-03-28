import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

public class MenuTableModel extends AbstractTableModel {

    private Object[][] data;
    private String[] columnNames = {"Nom", "Description", "Prix", "Catégorie", "Ingrédients", "Action"};

    public MenuTableModel(Object[][] data) {
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 5) {
            JButton button = new JButton("Supprimer");
            button.addActionListener(e -> {
                supprimerLigne(rowIndex);
            });
            return button;
        } else {
            return data[rowIndex][columnIndex];
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 5) {
            return JButton.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 5){
            return true;
        }else{
            return false;
        }
    }

    public void supprimerLigne(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.length) {
            Object[][] newData = new Object[data.length - 1][6];
            int j = 0;
            for (int i = 0; i < data.length; i++) {
                if (i != rowIndex) {
                    newData[j] = data[i];
                    j++;
                }
            }
            data = newData;
            fireTableDataChanged();
        }
    }
}
