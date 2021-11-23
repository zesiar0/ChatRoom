package RUN;

import java.awt.EventQueue;
import javax.swing.*;
import UILayer.ChatFrame;

public class RunChatClient {
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ChatFrame chatClientFrame = new ChatFrame();
				chatClientFrame.setVisible(true);
				chatClientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
