import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import daa_project.*;

public class start {

	private JFrame mf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					start window = new start();
					window.mf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public start() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mf = new JFrame();
		mf.getContentPane().setBackground(new Color(244, 164, 96));
		mf.getContentPane().setLayout(null);
		
		JButton play = new JButton("PLAY");
		play.setFont(new Font("Tahoma", Font.BOLD, 13));
		play.setBackground(new Color(135, 206, 250));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mf.dispose();
//				new window_1player();
				new game();
			}
		});
		play.setBounds(313, 259, 209, 46);
		mf.getContentPane().add(play);
		
		JLabel mf_title = new JLabel("ENTER THE DUNGEON");
		mf_title.setForeground(new Color(255, 255, 0));
		mf_title.setFont(new Font("Tahoma", Font.BOLD, 26));
		mf_title.setBounds(271, 69, 302, 57);
		mf.getContentPane().add(mf_title);
		mf.setTitle("EnterTheDungeon");
		mf.setResizable(false);
		mf.setBackground(new Color(255, 165, 0));
		mf.setForeground(Color.ORANGE);
		mf.setBounds(100, 100, 828, 642);
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
