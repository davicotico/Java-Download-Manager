package Classes;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

/**
 * @author Herbert Schildt
 * @version 1.0.0.
 */
public class DownloadsTableModel extends AbstractTableModel implements Observer {
    // Array con el nombre de las columnas
    private static final String[] columnNames = {"URL", "Size", "Progress", "Status"};
    // Para personalizar las columnas definimos un array de clases
    private static final Class[] columnClasses = {String.class,
        String.class, JProgressBar.class, String.class};
    // Un ArrayList para los objetos Download
    private ArrayList downloadList = new ArrayList();

    public void addDownload(Download download) {
        //Adiciona este objeto (this) como observador
        //del objeto Download
        download.addObserver(this);
        downloadList.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    /**
     * @param indice de la fila
     * @return objeto Download de la fila seleccionada
     */
    public Download getDownload(int row) {
        return (Download) downloadList.get(row);
    }

    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }


    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    public int getRowCount() {
        return downloadList.size();
    }

    public Object getValueAt(int row, int col) {
        Download download = (Download) downloadList.get(row);
        switch (col) {
            case 0: // URL
                return download.getUrl();
            case 1: // Tamaño
                int size = download.getSize();
                return (size == -1) ? "" : Integer.toString(size);
            case 2: // Progreso
                return new Float(download.getProgress());
            case 3: // Estado
                return Download.STATUSES[download.getStatus()];
        }
        return "";
    }
    /**
     * Permite recibir notificaciones desde objectos de clase Download.
     * Es llamado cuando un objeto Download notifica a sus Observers sobre algún cambio. 
     */
    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }
}
