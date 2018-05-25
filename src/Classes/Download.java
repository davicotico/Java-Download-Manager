package Classes;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 * @author Herbert Schildt
 * Contribuciones:
 * + David Ticona Saravia
 * + ...
 * Esta clase descarga un archivo desde una URL
 * @version 1.0.1
 */

public class Download extends Observable implements Runnable {
    // Tamaño Maximo del buffer de descarga
    private static final int MAX_BUFFER_SIZE = 1024;
    // Nombres de los estados
    public static final String STATUSES[] = {"Downloading", "Paused", "Complete", "Cancelled", "Error"};
    // Codigos de los estados
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    private URL url; // download URL
    private int size; // Tamaño de la descarga en bytes
    private int downloaded; // bytes descargados
    private int status; // Actual estado de la descarga
    private String folder; // Directorio destino para la descarga

    /**
     * Constructor
     * @param url URL del archivo
     * @param folder Directorio destino
     */
    public Download(URL url, String folder) {
        this.url = url;
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;
        this.folder = folder;
        download(); // Inicia el download.
    }

    /**
     * Retorna la URL de descarga
     * @return String
     */
    public String getUrl() {
        return url.toString();
    }

    /**
     * Retorna el tamaño de la descarga (bytes)
     * @return int
     */
    public int getSize() {
        return size;
    }

    /**
     * Progreso de la descarga (Porcentaje)
     */
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    /**
     * Retorna el Codigo de estado de la descarga
     */
    public int getStatus() {
        return status;
    }

    // Pausa la descarga
    public void pause() {
        status = PAUSED;
        stateChanged();
    }

    // Continua la descarga que estaba pausada
    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    /**
     * Cancela la descarga
     */
    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }

    /**
     * Marca la actual descarga como ERROR
     */
    private void error() {
        status = ERROR;
        stateChanged();
    }

    /**
     * Inicia o Continua una descarga
     */
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Retorna el nombre del archivo a partir de una URL
     * @param url
     * @return String
     */
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    /**
     * HILO que descarga el archivo
     */
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
            // Abre una conexión HTTP sobre la URL
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            // Se indica la porción del archivo a descargar
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

            // Conecta al servidor
            connection.connect();

            // Verifica que el codigo HTTP de respuesta este dentro del rango de 200
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            // Verifica si es valido el tamaño del contenido
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }

            /**
             * Asigna el tamaño de la descarga en caso que
             * aún no haya sido asignado.
             */
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }

            file = new RandomAccessFile(this.folder + getFileName(url), "rw");
            file.seek(downloaded);
            /**
             * Obtener una secuencia de datos (stream) de la conexión abierta.
             */
            stream = connection.getInputStream();
            while (status == DOWNLOADING) {
                /**
                 * Define el tamaño del buffer dependiendo de cuanto
                 * del archivo reste por descargar.
                 */
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }
                // Leer bytes del servidor y colocarlos en buffer
                // asigna a la variable read la cantidad de bytes leidos efectivamente.
                int read = stream.read(buffer);
                /**
                 * Si la cantidad de bytes leidos es -1 significa que la descarga
                 * fue completada y salimos del bucle.
                 */
                if (read == -1) {
                    break;
                }

                // Escribe el buffer sobre el archivo
                file.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }
            /**
            Cambia el estado a COMPLETE. Al llegar a este punto
            la descarga fue finalizada por completo.
            */
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            // Cierra el archivo
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                }
            }

            // Cierra la transmisión.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Notifica a los Observers que el estado de la descarga ha cambiado
     */
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}
