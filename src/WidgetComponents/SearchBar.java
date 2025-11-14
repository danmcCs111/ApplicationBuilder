package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import WidgetComponentInterfaces.SearchSubscriber;

public class SearchBar extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JTextField searchField;
	private JButton searchButton;
	private static int columnCharacterLength = 10;
	
	private ArrayList<SearchSubscriber> searchSubscribers = new ArrayList<SearchSubscriber>();

	public SearchBar()
	{
		buildWidgets();
	}
	
	public void setButtonBackground(Color c)
	{
		this.searchButton.setBackground(c);
	}
	
	public void setButtonForeground(Color c)
	{
		this.searchButton.setForeground(c);
	}
	
	public void addSearchSubscriber(SearchSubscriber subscriber)
	{
		searchSubscribers.add(subscriber);
	}
	
	public void setColumnCharacterLength(int length)
	{
		columnCharacterLength = length;
		searchField.setColumns(columnCharacterLength);
	}
	
	public void buildWidgets()
	{
		this.setLayout(new BorderLayout());
		searchField = new JTextField(columnCharacterLength);
		ActionListener searchActionListener = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				notifySearch();
			}
		};
		KeyAdapter searchKeyAdapter = new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					notifySearch();
				}
			}
		};
		searchField.addKeyListener(searchKeyAdapter);
		searchButton = new JButton("Search");
		searchButton.addActionListener(searchActionListener);
		this.add(searchField, BorderLayout.CENTER);
		this.add(searchButton, BorderLayout.EAST);
	}
	
	private void notifySearch()
	{
		for(SearchSubscriber ss : searchSubscribers)
		{
			ss.notifySearchText(searchField.getText());
		}
	}
}
