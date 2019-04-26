package labeler;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;

public class PrintMsg extends JDialog{
	public JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	String Output;
	int max, seat;
	JLabel lblPrintOutput;
	public PrintMsg() {
		setBounds(100, 100, 374, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblPrintOutput = new JLabel("");
			lblPrintOutput.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblPrintOutput);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton Finished = new JButton("Cancel");
				Finished.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				{
					JButton btnOk = new JButton("ok");
					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							PrintRm();
						}
					});
					buttonPane.add(btnOk);
				}
				Finished.setActionCommand("Cancel");
				buttonPane.add(Finished);
			}
		}

	}
	public void updateLabel(String a) {
		lblPrintOutput.setText(a);
		
	}
	public void StartPrinting(String output, int start, int capacity) {
		Output = output;
		seat = start;
		max = capacity;
		PrintRm();
	}
	public void PrintRm() {
		DecimalFormat x = new DecimalFormat("-##");
		if (max >= seat) {
			updateLabel(Output + x.format(seat));
			System.out.println(Output + x.format(seat));
			seat++;
		}
		
	}
}
