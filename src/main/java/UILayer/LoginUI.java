package UILayer;

import BLLayer.UsersBL;
import BLLayer.UsersEntity;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginUI extends JPanel{
	private static final long serialVersionUID = 6329954484003010244L;
	private static final int DEFAULTWIDTH = 240;
	private static final int DEFAULTHEIGHT = 150;
	private JTextField username;
	private JPasswordField password;
	private JDialog dialog;
	private JButton loginButton;
	
	private UsersEntity user;
	private boolean ok;

	
	public LoginUI(){

		JLabel welcome = new JLabel("Welcome to ChatRoom!");
		Font font= new Font("Welcome", Font.HANGING_BASELINE, 20);
		welcome.setFont(font);

		JPanel panel = new JPanel();
		panel.setSize(200,150);
		LayoutManager panelMgr = new GridLayout(2, 2);
		panel.setLayout(panelMgr);
		panel.add(new JLabel("      User name:"));
		panel.add(username = new JTextField());
		panel.add(new JLabel("       Password:"));
		panel.add(password = new JPasswordField());
		panel.setOpaque(false);

		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String tname = username.getText();
				String tpassword = new String(password.getPassword());

				user = null;
				try {
					user = new UsersBL().loginCheckOut(tname,tpassword);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unknown error!Please try again" ,"Login failed", JOptionPane.ERROR_MESSAGE);
				}	
				if(user!=null){
					ok = true;
					dialog.setVisible(false);
				}else{
					JOptionPane.showMessageDialog(null,"Invalid username/password!" ,"Login failed", JOptionPane.ERROR_MESSAGE);
					ok = false;
					dialog.setVisible(true);
				}
			}
		});

		JButton signUpButton = new JButton("SignUp");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				 SignupUI signupUI = new SignupUI();
				 signupUI.showDialog(LoginUI.this, "Sign Up");
				
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(loginButton);
		buttonPanel.add(signUpButton);
		

		LayoutManager loginMgr = new BorderLayout();
		setLayout(loginMgr);
		add(welcome, BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);
	}
		
	public UsersEntity getUser() {
		return user;		
	}

	public boolean showDialog(Component parent,String title){
		ok = false;

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		Frame owner = null;
		if(parent instanceof Frame) 
			owner = (Frame) parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);

		if(dialog == null || dialog.getOwner() != owner){
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(loginButton);
			dialog.setSize(DEFAULTWIDTH, DEFAULTHEIGHT);
			dialog.setLocation((screenWidth - DEFAULTWIDTH )/2,(screenHeight - DEFAULTHEIGHT)/2 );
			dialog.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent arg0) {
					setVisible(false);
				}	
			});
		}
		dialog.setTitle(title);
		dialog.setVisible(true);
		return ok;
	}
	
}
