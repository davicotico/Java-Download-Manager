package io.github.davicotico.downloadmanager;

import io.github.davicotico.downloadmanager.forms.DownloadManager;
import io.github.davicotico.downloadmanager.utils.ThemeManager;

/**
 *
 * @author David Tomas Ticona Saravia
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ThemeManager.initialize();
        ThemeManager.populateIcons();
        new DownloadManager().setVisible(true);
    }

}
