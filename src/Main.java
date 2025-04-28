import ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 确保界面美观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
