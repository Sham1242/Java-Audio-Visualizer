import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class YouTubeAudioDownloader extends JFrame {
    private JComboBox<String> qualityComboBox;
    private JTextField urlTextField;
    private JButton downloadButton;

    public YouTubeAudioDownloader() {
        setTitle("YouTube Audio Downloader");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        qualityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        urlTextField = new JTextField();
        downloadButton = new JButton("Download");
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String quality = (String) qualityComboBox.getSelectedItem();
                String url = urlTextField.getText();
                downloadAudio(url, quality);
            }
        });

        // Create layout
        setLayout(new FlowLayout());
        add(new JLabel("URL: "));
        add(urlTextField);
        add(new JLabel("Quality: "));
        add(qualityComboBox);
        add(downloadButton);
    }

    private void downloadAudio(String url, String quality) {
        // System save error
        System.out.println("Downloading audio from URL: " + url);
        System.out.println("Selected quality: " + quality);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new YouTubeAudioDownloader().setVisible(true);
            }
        });
    }
}
