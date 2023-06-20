# Java-Audio-Visualizer

This project is a Java-based Audio Visualizer application that incorporates real-time frequency analysis and visualization, providing users with an immersive experience. It utilizes Java, Swing, and FFmpeg technologies to achieve its functionality.

## Features

1. Real-time Frequency Analysis: The application implements the Fast Fourier Transform (FFT) algorithm for extracting frequency content from audio files in real time.

2. Audio Downloading: Users can download audio files from YouTube links using the application. The application allows the user to choose up to 4 different audio file types to download from.

3. Audio Visualization: The downloaded audio files can be visualized using the application's built-in visualization module. The visualization provides users with a graphical representation of the audio's frequency content.

4. Audio Editing: Users can also perform basic editing operations on the downloaded audio files, such as trimming, merging, or adding effects. The application provides a user-friendly interface for these editing functionalities.

## Technologies Used

The project is built using the following technologies:

- Java: The core programming language used for the application's development.
- Swing: The Java Swing framework is utilized for creating the interactive graphical user interface (GUI) of the application.
- FFmpeg: This open-source multimedia framework is integrated into the project to handle the audio processing and conversion tasks.

## Getting Started

To run the Audio Downloader and Visualizer application on your local machine, follow these steps:

1. Clone the project repository:

   ```bash
   git clone https://github.com/your-username/audio-downloader-visualizer.git
2. Ensure that you have Java Development Kit (JDK) installed on your system.

3. Install FFmpeg and make sure it is added to your system's PATH.

4. Build the application using your preferred Java development environment or by running the following command in the project directory:
   ```bash
   javac Main.java
   java Main

Usage
Once the Audio Downloader and Visualizer application is running, you can perform the following steps:

Enter a valid YouTube link in the provided input field to download audio files from that link.

Select up to 4 different audio file types to download.

After the audio files are downloaded, you can choose to visualize them using the built-in visualizer.

If you wish to edit the downloaded audio files, explore the editing options available in the application's GUI.
