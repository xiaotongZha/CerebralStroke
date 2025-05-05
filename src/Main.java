

import ui.MainFrame;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import utils.ClearUtils;
public class Main {
    public static void main(String[] args) throws IOException {
        // 确保界面美观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        ClearUtils.clearAllGenerateDir();
                    }catch (IOException exception){
                        System.out.println(exception);
                    }
                }
            });
        });
    }

}
