package io.github.davicotico.downloadmanager.components;

import io.github.davicotico.downloadmanager.core.Download;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.table.AbstractTableModel;
import java.util.Observer;
import javax.swing.JProgressBar;

/**
 *
 * @author David Tomas Ticona Saravia (davicotico@yandex.com)
 */
public class DownloadsTableModel extends AbstractTableModel implements Observer {
    // Array con el nombre de las columnas
    private static final String[] columnNames = {"URL", "Size", "Progress", "Status"};
    // Para personalizar las columnas definimos un array de clases
    private static final Class[] columnClasses = {String.class,
        String.class, JProgressBar.class, String.class};
    // Un ArrayList para los objetos Download
    final private ArrayList<Download> downloadList = new ArrayList<>();

    public void addDownload(Download download) {
        // Adiciona este objeto (this) como observador
        // del objeto Download
        download.addObserver(this);
        // Adiciona el objeto a la lista de objetos Download
        downloadList.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    /**
     * @param row Indice de la fila
     * @return objeto Download de la fila seleccionada
     */
    public Download getDownload(int row) {
        return downloadList.get(row);
    }

    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
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

    @Override
    public int getRowCount() {
        return downloadList.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Download download = (Download) downloadList.get(row);
        switch (col) {
            case 0: // URL
                return download.getUrl();
            case 1: // Tamaño
                int size = download.getSize();
                return (size == -1) ? "" : Integer.toString(size);
            case 2: // Progreso
                return download.getProgress();
            case 3: // Estado
                return Download.STATUSES[download.getStatus()];
        }
        return "";
    }
    
    /**
     * Permite recibir notificaciones desde objectos de clase Download.Es llamado cuando un objeto Download notifica a sus Observers sobre algún cambio.
     * @param o 
     * @param arg 
     */
    @Override
    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }
}
