package com.crimsonsky.remotainment.ui;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class PasswordDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPasswordField txtPassword;
	private JButton btnOk;
	
	private String password;

	/**
	 * Create the dialog.
	 */
	public PasswordDialog() 
	{
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setTitle("Set Server Password");
		setBounds(100, 100, 279, 170);
		getContentPane().setLayout(null);
		{
			JLabel lblPassword = new JLabel("Password:");
			lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblPassword.setBounds(10, 63, 92, 14);
			getContentPane().add(lblPassword);
		}
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(68, 60, 178, 20);
		getContentPane().add(txtPassword);
		
		password = "";
		
		btnOk = new JButton("OK");
		btnOk.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password = new String(txtPassword.getPassword());
				if (password.length() > 0)
					setVisible(false);
			}
		});
		btnOk.setBounds(10, 98, 236, 23);
		getContentPane().add(btnOk);
		
		JLabel lblEncryptionKeyHelp = new JLabel("Set Text");
		lblEncryptionKeyHelp.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEncryptionKeyHelp.setText("<html>Enter password to allow only authenticated remote clients to communicate with your server</html>");
		lblEncryptionKeyHelp.setBounds(10, 4, 236, 48);
		getContentPane().add(lblEncryptionKeyHelp);
	}
	
	public String getPassword()
	{
		return password;
	}
}
