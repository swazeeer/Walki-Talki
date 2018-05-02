package application;

import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class Controller {
	@FXML 
	private Button talkButton;
	@FXML 
	private Button discButton;
	@FXML 
	private TextArea msgArea;
	
	private Thread sendThread;
	private Sender sendr;	
	private Thread rcvThread;
	private Receiver rcv;
	private String str;
	
	@FXML
	private void initialize() {
			rcv = new Receiver();
			rcvThread = new Thread(rcv);
			rcvThread.start();
			str = JOptionPane.showInputDialog(null, "Enter Partner's IP Address.",  "192.168.1." );
	}
	
	@FXML
	protected void startTalk(MouseEvent event) throws Exception{
		talkButton.setStyle("-fx-background-color:#00b300;");
		talkButton.setText("TALKING");
		sendr = new Sender(str);
		sendThread = new Thread(sendr);
		sendThread.start();
	}   
	
	@FXML
	protected void stopTalk(MouseEvent  event) throws Exception{
		talkButton.setStyle(null);
		talkButton.setText("TALK");
		sendr.terminate();
	}
	
	@FXML
	protected void toggleDisconnect(ActionEvent event) throws Exception{
		if(discButton.getStyle().equals("")) discButton.setStyle("-fx-background-color:#b30000;");
		else discButton.setStyle(null);	
		
		if(discButton.getText().equals("DISCONNECT")) {
			discButton.setText("DISCONNECTED");
			rcv.terminate();
		}
		else {
			discButton.setText("DISCONNECT");
			rcv = new Receiver();
			rcvThread = new Thread(rcv);
			rcvThread.start();
		}
	}
}
