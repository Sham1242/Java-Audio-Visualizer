import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;

public class AudioVisualizer extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static final int BAR_WIDTH = 5;

    private Complex[] fftData;
    private byte[] audioData;

    public AudioVisualizer() {
        super("Audio Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        JPanel panel = new JPanel();
        JTextField urlField = new JTextField(30);
        JButton downloadButton = new JButton("Download");
        panel.add(urlField);
        panel.add(downloadButton);
        getContentPane().add(panel, BorderLayout.NORTH);

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String youtubeURL = urlField.getText();
                downloadAudioFromYouTube(youtubeURL);
            }
        });

        setVisible(true);
    }

    public void visualize(String audioFilePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(audioFilePath));
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            int numBytesRead;
            audioData = new byte[line.getBufferSize() / 5];
            fftData = new Complex[line.getBufferSize() / 10];

            while ((numBytesRead = audioStream.read(audioData)) != -1) {
                updateFFTData();
                draw();
                line.write(audioData, 0, numBytesRead);
            }

            line.drain();
            line.stop();
            line.close();
            audioStream.close();

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private void updateFFTData() {
        int N = audioData.length;

        // Apply FFT to audioData
        Complex[] x = new Complex[N];
        for (int i = 0; i < N; i++) {
            x[i] = new Complex(audioData[i], 0);
        }

        fft(x);

        fftData = x;
    }

    private void fft(Complex[] x) {
        int N = x.length;

        // Base case
        if (N == 1) {
            return;
        }

        // Splitting the even and odd elements
        Complex[] even = new Complex[N / 2];
        Complex[] odd = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
            odd[k] = x[2 * k + 1];
        }

        // Recursive FFT on even and odd elements
        fft(even);
        fft(odd);

        // Combine the results
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex w = new Complex(Math.cos(kth), Math.sin(kth));
            Complex term = w.multiply(odd[k]);
            x[k] = even[k].add(term);
            x[k + N / 2] = even[k].subtract(term);
        }
    }

    private void draw() {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.GREEN);
        for (int i = 0; i < fftData.length; i++) {
            double amplitude = fftData[i].abs() / 1000.0; // Adjust the scaling factor for visualization
            int barHeight = (int) (amplitude * HEIGHT);
            int x = i * BAR_WIDTH;
            int y = HEIGHT - barHeight;
            g.fillRect(x, y, BAR_WIDTH, barHeight);
        }
    }

    private void downloadAudioFromYouTube(String youtubeURL) {
        try {
            URL url = new URL(youtubeURL);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream("audio.mp3");

            byte[] buffer = new byte[2048];
            int length;

            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }

            is.close();
            os.close();

            visualize("audio.mp3"); // Visualize the downloaded audio file

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AudioVisualizer visualizer = new AudioVisualizer();
        visualizer.visualize("path/to/audio/file.wav");
    }
}
