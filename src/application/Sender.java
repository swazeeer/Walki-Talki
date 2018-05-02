package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Sender implements Runnable{
	private DatagramSocket socket; 
	private DatagramPacket packet;
	private boolean running;
	private String str;
	private int numBytesRead;
	private int SIZE;
    private int PORT;
    private int count;
	
	public Sender(String ipaddress) {
		running = true;
		str = ipaddress;
		SIZE = 1024;
		numBytesRead = 0;
		PORT = 23189;
		count = 0;
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void terminate() {
		running = false;
	}
	
	public void run() {
		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        TargetDataLine microphone;
        AudioInputStream audioInputStream = null;
        
        try {
        	microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            try {
            	while(running) {
                    numBytesRead = microphone.read(data, 0, SIZE); 
                    out.write(data, 0, numBytesRead);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte audioData[] = out.toByteArray();
            // Get an input stream on the byte array
            // containing the data
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
            audioInputStream = new AudioInputStream(byteArrayInputStream,format, audioData.length / format.getFrameSize());
            

          byte Buffer[] = new byte[10000];
          try {
              while ((count = audioInputStream.read(Buffer, 0,Buffer.length)) != -1) {
                  if (count > 0) {
                      packet = new DatagramPacket(Buffer, Buffer.length, InetAddress.getByName(str), PORT);
                	  socket.send(packet);
                	 
                  }// end if
              }
          } catch (IOException e) {
        	  e.printStackTrace();
          }

          microphone.close();
          socket.close();
        } catch (LineUnavailableException e) {
        	e.printStackTrace();
        }
	}
}





