package UILayer;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.*;

import BLLayer.UsersBL;
import BLLayer.UsersEntity;

import java.awt.*;

public class ChatFrame extends JFrame {
	

	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();
	int screenHeight = screenSize.height;
	int screenWidth = screenSize.width;
	private static final long serialVersionUID = 1L;
	private static final int DEFAULTWIDTH = 800;
	private static final int DEFAULTHEIGHT = 400;

	private JLabel nameLable;
	private JComboBox<Object> statusCombo;
	private UsersEntity user = null;
	JTextArea chatTextArea;
	JTextField sendArea;
	JButton sendButton ;
	Thread  readerThread ;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public ChatFrame(){
		setTitle("This is a simple ChatClient_V2.0");
		setSize(DEFAULTWIDTH, DEFAULTHEIGHT);
		setLocation((screenWidth - DEFAULTWIDTH) / 2, (screenHeight - DEFAULTHEIGHT) / 2);
		setJMenuBar(initMenuBar());
		JPanel northPanel = initNorthPanel();
		JPanel mainPanel = initMainPanel();
		JPanel southPanel = initSouthPanel();

		getContentPane().add(BorderLayout.NORTH,northPanel );
		getContentPane().add(BorderLayout.CENTER,mainPanel);
		getContentPane().add(BorderLayout.SOUTH,southPanel);

		if(user == null){
			statusCombo.setEnabled(false);
			chatFrameLogin();
		}
	}


	private void chatFrameLogin(){
		if (user!= null) {
			JOptionPane.showMessageDialog(null, "Please logout first", "Login failed", JOptionPane.ERROR_MESSAGE);
		}else{
			LoginUI loginUI =new LoginUI();

			if(loginUI.showDialog(ChatFrame.this, "Login")){
				setUpNetworking();

				readerThread  =  new Thread(new ChatTextReader());
				readerThread.start();
				user = loginUI.getUser();
				nameLable.setText(user.getName());
				statusCombo.setEnabled(true);
				sendButton.setEnabled(true);
				user.setStatus(1);
				new UsersBL().setOnline(user.getID(), user.getName());
				statusCombo.setSelectedIndex(1);
				
			}
		}
	}

	private void chatFrameSignup(){
		SignupUI signupUI =new SignupUI();

		if(signupUI.showDialog(ChatFrame.this, "Sign Up")){
			 chatFrameLogin();
		}
	}

	@SuppressWarnings("deprecation")
	private void ChatFrameLogout()  {
		if(user == null){
			JOptionPane.showMessageDialog(null, "Please login first", "Logout failed", JOptionPane.ERROR_MESSAGE);
		}else{
			nameLable.setText("UnLogin");
			statusCombo.setEnabled(false);
			sendButton.setEnabled(false); 
			tellServerOffline();
			user.setStatus(0);
			new UsersBL().setOffline(user.getID(), user.getName());
			user = null;
			readerThread.stop();
			try {
				sock.close();
				reader.close();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void ChatFrameExit() {
		if (user != null) {
			ChatFrameLogout();	
		}
		System.exit(0);
	}

	private void setUpNetworking() {
		try {
			sock = new Socket("127.0.0.1",50000);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("Networking Established");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public class ChatTextReader implements Runnable {
		public void run(){
			String message;
			try {
				while((message = reader.readLine())!= null){
					System.out.println("read:"+message);
					chatTextArea.append(message+"\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}


	 private void tellServerOffline() {
		 if(user.getStatus() == 1){
            String message = user.getName() + "quit";
            writer.println(message);
            writer.flush();
		 }
    }

	 private void tellServerOnline() {
		 String message = "User "+user.getName()+" login";
		 writer.println(message);
		 writer.flush();
	 }

	private JMenuBar initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);
		JMenuItem loginItem = new JMenuItem("Login");
		JMenuItem signUpItem = new JMenuItem("Sign Up");
		JMenuItem logoutItem = new JMenuItem("Logout");
		JMenuItem exitItem = new JMenuItem("Exit");
		menu.add(loginItem);
		menu.add(logoutItem);
		menu.add(signUpItem);
		menu.add(exitItem);
		loginItem.addActionListener(e -> chatFrameLogin());
		signUpItem.addActionListener(e -> chatFrameSignup());
		logoutItem.addActionListener(e -> ChatFrameLogout());
		exitItem.addActionListener(e -> ChatFrameExit());
		return menuBar;
	}

	private JPanel initNorthPanel() {
		JPanel northPanel = new JPanel();
		JLabel userLable = new JLabel("User: ");
        nameLable = new JLabel("UnLogin");
    	JLabel statusLable = new JLabel("Status: ");
        statusCombo = new JComboBox<>();
        statusCombo.addItem("Offline");
        statusCombo.addItem("Online");
        statusCombo.addItem("Hide");

        statusCombo.addActionListener(e -> {
			int s = statusCombo.getSelectedIndex();
			if (s == 0) {
				tellServerOffline();
				user.setStatus(0);
				new UsersBL().setOffline(user.getID(), user.getName());
			} else if (s == 1) {
				new UsersBL().setOnline(user.getID(), user.getName());
				tellServerOnline();
				user.setStatus(1);
			} else {
				tellServerOffline();
				user.setStatus(2);
				new UsersBL().setHide(user.getID(), user.getName());

			}
		});
        northPanel.add(userLable);
        northPanel.add(nameLable);
        northPanel.add(statusLable);
        northPanel.add(statusCombo);
        return northPanel;
	}

	private JPanel initMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		chatTextArea = new JTextArea();
		chatTextArea.setLineWrap(true);
		chatTextArea.setEditable(false);

		JScrollPane qScroller = new JScrollPane(chatTextArea);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(qScroller,BorderLayout.CENTER);
		return mainPanel;
	}

	private JPanel initSouthPanel() {
		JPanel southPanel = new JPanel();
		sendArea = new JTextField(20);
		sendButton = new JButton("Send");
		sendButton.addActionListener(ev -> {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String IDandTime = user.getName()+ "    " + dateFormat.format(new Date());
				writer.println(IDandTime);
				writer.println(sendArea.getText());
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			sendArea.setText("");
			sendArea.requestFocus();
		});
		southPanel.add(sendArea);
		southPanel.add(sendButton);
		return southPanel;
	}
}
