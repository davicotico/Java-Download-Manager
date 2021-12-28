package io.github.davicotico.downloadmanager.utils;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

/**
 *
 * @author David Tomas Ticona Saravia
 */
public class ThemeManager {
    
    public static HashMap<String, Icon> icons;
    
    public static void initialize() {
        ThemeManager.icons = new HashMap<>();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    public static void changeTheme(JFrame frame) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize LaF");
        }
        SwingUtilities.updateComponentTreeUI(frame);
        frame.pack();
    }
    
    public static void populateIcons() {
         IconFontSwing.register(FontAwesome.getIconFont());
         ThemeManager.icons.put("plus", IconFontSwing.buildIcon(FontAwesome.PLUS_SQUARE, 12, new Color(0, 150, 0)));
         ThemeManager.icons.put("pause", IconFontSwing.buildIcon(FontAwesome.PAUSE, 12, new Color(0, 150, 0)));
         ThemeManager.icons.put("play", IconFontSwing.buildIcon(FontAwesome.PLAY, 12, new Color(0, 150, 0)));
         ThemeManager.icons.put("stop", IconFontSwing.buildIcon(FontAwesome.STOP, 12, new Color(0, 150, 0)));
         ThemeManager.icons.put("folder-open", IconFontSwing.buildIcon(FontAwesome.FOLDER_OPEN, 12, new Color(0, 150, 0)));
         ThemeManager.icons.put("minus-square", IconFontSwing.buildIcon(FontAwesome.MINUS_SQUARE, 12, new Color(0, 150, 0)));
    }
    
}
