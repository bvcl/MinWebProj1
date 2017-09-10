package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

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

public class SearchGui extends JFrame {
	private DefaultListModel<String> model;
	private JCheckBox chckbxNewCheckBox;
	private JCheckBox chckbxStoplist;
	private JList<String> list;
	private JPanel contentPane;
	private JTextField textField;
	private LinkedList<String> nomeDocs;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public SearchGui() {
		nomeDocs = new LinkedList<String>();
		nomeDocs.add("Programação em JavaScript D3");
		nomeDocs.add("Programação em Java");
		nomeDocs.add("Seres Vivos");
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
				String consulta [] = textField.getText().toLowerCase().split(" ");
				for(int k =0;k<consulta.length;k++)System.out.println(consulta[k]);
				int indice=0;
				int i=0;
				int j=0;
				for(i=0;i<nomeDocs.size();i++){
					String nomeDocumento = nomeDocs.get(i).toLowerCase();
					for(j=0;j<consulta.length;j++){
						if(nomeDocumento.contains(consulta[j]) && !model.contains(nomeDocs.get(i))){
							model.add(indice, nomeDocs.get(i));
							indice++;
						}
					}
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
		        	System.out.println("Clicou "+list.getSelectedValue().toString());
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
