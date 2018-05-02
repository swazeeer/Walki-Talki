package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Receiver implements Runnable {
	private boolean running;
	private  SourceDataLine srcLine;
	private DatagramSocket datagramSocket;
	private int PORT;
	
	
	public  Receiver()  {
		// port to be used
		PORT = 23189;
		running = true;
		
		try {
			datagramSocket = new DatagramSocket(PORT);
		}catch(SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void terminate() {
		datagramSocket.close();
		running = false;
	}
	
	public void run() {
		
		try {
			byte[] receivedData = new byte[10000];
			AudioFormat format = new AudioFormat(8000.0f,16, 1, true, true);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format );
			
			srcLine = (SourceDataLine) AudioSystem.getLine(info);
			srcLine.open(format);
			srcLine.start();
			
			DatagramPacket datagramPacket = new DatagramPacket(receivedData, receivedData.length);
			while(running) {
				datagramSocket.receive(datagramPacket);
				srcLine.write(datagramPacket.getData(), 0, datagramPacket.getLength());	
			}
			
		} catch (IOException ex) {
           ex.printStackTrace();	
		} catch (LineUnavailableException ex) {
           ex.printStackTrace();
		}
		finally {
			srcLine.drain();
			srcLine.close();
			datagramSocket.close();
		}
	}
}









