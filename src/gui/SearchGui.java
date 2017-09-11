package gui;

import indexbase.Indexer;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import search.QueryMatcher;

public class SearchGui extends JFrame {
	private DefaultListModel<String> model;
	private JCheckBox chckbxNewCheckBox;
	private JCheckBox chckbxStoplist;
	private JList<String> list;
	private JPanel contentPane;
	private JTextField textField;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					SearchGui frame = new SearchGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SearchGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(39, 21, 340, 31);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Buscar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!model.isEmpty())model.clear();
				
				Indexer i = new Indexer(chckbxNewCheckBox.isSelected(), chckbxStoplist.isSelected());
				QueryMatcher q = new QueryMatcher(chckbxNewCheckBox.isSelected(), chckbxStoplist.isSelected());
				
				i.createBase();
				TopDocs td = null;
				try {
					td = q.buildSearch(textField.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				int index = 0;
				System.out.println(td.totalHits);
				for (ScoreDoc sd : td.scoreDocs){
		            Document d = null;
					try {
						d = q.getSearcher().doc(sd.doc);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					model.add(index, "Path : "+  d.get("path") + ", Score : " + sd.score);
					index++;
		        }	
			}
		});
		btnNewButton.setBounds(64, 63, 112, 38);
		contentPane.add(btnNewButton);
		
		chckbxNewCheckBox = new JCheckBox("Stemming");
		chckbxNewCheckBox.setBounds(234, 59, 97, 16);
		contentPane.add(chckbxNewCheckBox);
		
		chckbxStoplist = new JCheckBox("Stopword");
		chckbxStoplist.setBounds(234, 85, 97, 16);
		contentPane.add(chckbxStoplist);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 120, 414, 130);
		contentPane.add(scrollPane);
		
		model = new DefaultListModel<String>();
		list = new JList<String>(model);
		scrollPane.setViewportView(list);
		
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {
		        	String filePath = list.getSelectedValue().toString().substring(7, list.getSelectedValue().toString().indexOf(",")), rootPath = System.getProperty("user.dir");
		        		if(filePath == null) return;
						try {
							Desktop.getDesktop().open(new File(rootPath+"/"+filePath));
						} catch (Exception e) {
							e.printStackTrace();
						}
		        	}
		        }
		});

	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
