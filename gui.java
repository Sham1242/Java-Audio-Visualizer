import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.mapper.VideoInfo;

public class AudioVisualizerApp extends JFrame {
    private JButton downloadButton;
    private JButton visualizeButton;
    private JButton editButton;
    private JTextField youtubeLinkField;
    private JComboBox<String> audioTypeComboBox;

    public AudioVisualizerApp() {
        setTitle("Audio Visualizer");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        youtubeLinkField = new JTextField();
        audioTypeComboBox = new JComboBox<>(new String[]{"M4A", "WAV", "MP3", "MP4"});
        downloadButton = new JButton("Download");
        visualizeButton = new JButton("Visualize");
        editButton = new JButton("Edit");

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String youtubeLink = youtubeLinkField.getText();
                Object selectedAudioTypeObject = audioTypeComboBox.getSelectedItem();
                String selectedAudioType = selectedAudioTypeObject != null ? selectedAudioTypeObject.toString() : "";

                
                // Define the directory where you want to save the downloaded audio
                String downloadDirectory = "download/path/";
        
                // Create a YoutubeDLRequest
                YoutubeDLRequest request = new YoutubeDLRequest(youtubeLink, downloadDirectory);
                
                // Set the preferred audio format (e.g., "best" for the best available quality)
                request.setOption("-f", selectedAudioType);
                
                try {
                    // Execute the request to download the audio
                    VideoInfo videoInfo = YoutubeDL.execute(request);
                    System.out.println("Downloaded: " + videoInfo.getTitle());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        });

        visualizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String audioFilePath = "path/to/your/audio/file.wav"; // Replace with the actual audio file path
                visualizeAudio(audioFilePath);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditDialog();
            }
        });

        add(new JLabel("YouTube Link:"));
        add(youtubeLinkField);
        add(new JLabel("Select Audio Type:"));
        add(audioTypeComboBox);
        add(downloadButton);
        add(visualizeButton);
        add(editButton);
    }

    private void showEditDialog() {
        JFrame editFrame = new JFrame("Edit Audio");
        editFrame.setSize(400, 200);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setLayout(new GridLayout(4, 2));
        editFrame.setLocationRelativeTo(null);

        JTextField inputField = new JTextField();
        JTextField outputField = new JTextField();
        JTextField startTimeField = new JTextField();
        JTextField endTimeField = new JTextField();

        JButton editSubmitButton = new JButton("Edit");

        editSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFilePath = inputField.getText();
                String outputFilePath = outputField.getText();
                String startTime = startTimeField.getText();
                String endTime = endTimeField.getText();

                // Execute FFmpeg command to edit the audio file
                String ffmpegCommand = "ffmpeg -i " + inputFilePath + " -ss " + startTime + " -to " + endTime + " " + outputFilePath;

                try {
                    Process process = Runtime.getRuntime().exec(ffmpegCommand);
                    process.waitFor();
                    JOptionPane.showMessageDialog(null, "Audio edited successfully!");
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error editing audio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editFrame.add(new JLabel("Input File:"));
        editFrame.add(inputField);
        editFrame.add(new JLabel("Output File:"));
        editFrame.add(outputField);
        editFrame.add(new JLabel("Start Time (hh:mm:ss):"));
        editFrame.add(startTimeField);
        editFrame.add(new JLabel("End Time (hh:mm:ss):"));
        editFrame.add(endTimeField);
        editFrame.add(editSubmitButton);

        editFrame.setVisible(true);
    }



    private void visualizeAudio(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);

            // Create a Clip to play the audio
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Create a new window for visualization
            JFrame visualizationFrame = new JFrame("Audio Visualization");
            visualizationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            visualizationFrame.setSize(800, 400);
            visualizationFrame.setLocationRelativeTo(null);

            // Create a custom JPanel for visualization
            JPanel visualizationPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawVisualization(g, clip);
                }
            };

            visualizationFrame.add(visualizationPanel);
            visualizationFrame.setVisible(true);

            // Start playing the audio
            clip.start();

            // Close the visualization window when audio playback ends
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        visualizationFrame.dispose();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
                System.out.println("Audio File Error");
        }
    }

    private void drawVisualization(Graphics g, Clip clip) {
        int width = getWidth();
        int height = getHeight();

        // Draw a simple visualization of audio volume over time
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.GREEN);
        int x = 0;
        int y = height / 2;
        
        long clipLength = clip.getMicrosecondLength();
        long currentPosition;

        while ((currentPosition = clip.getMicrosecondPosition()) < clipLength) {
            x = (int) ((double) currentPosition / clipLength * width);
            y = height / 2;
            g.drawLine(x, y, x, y - 100); // Adjust the scaling as needed
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AudioVisualizerApp().setVisible(true);
            }
        });
    }
}
