package com.crimsonsky.remotainment.ui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.DefaultListModel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JLabel;

public class FoldersDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnDone;
	private JList list;
	private DefaultListModel listModel;
	
	/**
	 * Create the dialog.
	 */
	public FoldersDialog(ArrayList<String> entertainmentFoldersList) 
	{
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setTitle("Entertainment Folders");
		setBounds(100, 100, 450, 279);
		getContentPane().setLayout(null);
		
		listModel = new DefaultListModel();
		setEntertainmentFolders(entertainmentFoldersList);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addFolder();
			}
		});
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnAdd.setBounds(318, 36, 106, 23);
		getContentPane().add(btnAdd);
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFolder();
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRemove.setBounds(318, 78, 106, 23);
		getContentPane().add(btnRemove);
		
		btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listModel.getSize() > 0)
					setVisible(false);
			}
		});
		btnDone.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDone.setBounds(318, 192, 106, 23);
		getContentPane().add(btnDone);
		
		list = new JList(listModel);
		list.setFont(new Font("Tahoma", Font.PLAIN, 11));
		list.setBounds(10, 37, 297, 193);
		getContentPane().add(list);
		
		JLabel lblAddEntertainmentFolders = new JLabel("Add entertainment folders to share with your remote client");
		lblAddEntertainmentFolders.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblAddEntertainmentFolders.setBounds(10, 12, 414, 14);
		getContentPane().add(lblAddEntertainmentFolders);
	}
	
	public ArrayList<String> getEntertainmentFolders() 
	{
		ArrayList<String> entertainmentFoldersList = new ArrayList<String>();
		for (int i=0; i<listModel.getSize(); i++) {
			entertainmentFoldersList.add((String)listModel.getElementAt(i));
		}
		return entertainmentFoldersList;
	}
	
	public void setEntertainmentFolders(ArrayList<String> entertainmentFoldersList) 
	{
		for (int i=0; i<entertainmentFoldersList.size(); i++) {
			listModel.addElement(entertainmentFoldersList.get(i));
		}
	}
	
	private void addFolder()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retval = fileChooser.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            listModel.addElement(file.getAbsolutePath());
        }
	}
	
	private void removeFolder() 
	{
		if (list.getSelectedIndex() >= 0) {
			listModel.remove(list.getSelectedIndex());
		}
	}
}
