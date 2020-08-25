package daa_project;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;


@SuppressWarnings("unused")
public class game implements MouseListener
{
	private JFrame f;
	private JButton b[][]=new JButton[100][100], submit;
	public JLabel l_ins;
	private int n_rows,n_cols,treasure_row,treasure_col,n_traps,trap_row,trap_col,temp;
	private String[] treasure_loc,n_rc,trap_loc;
	private ImageIcon treasureImg, buttonImg, trapImg, healingImg;
	private Timer inner_timer,outer_timer;
	public int i,j,count=1,hp,tempHealth = 0;
	public int visit[][];
	private int grid[][]=new int[100][100];
	public String s;
	private JPanel p,pp;

	public game()
	{
		initialize();

	}
	public ImageIcon create_scaleImage(String img_path)
	{
		ImageIcon imageIcon = new ImageIcon(img_path); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(1925/n_cols, 830/n_rows,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);  // transform it back
		return imageIcon;
	}
	private void initialize()
	{
		f = new JFrame();
		f.setTitle("Enter The Dungeon");
		f.setBounds(100, 100, 450, 300);
		p=new JPanel();
		pp = new JPanel();

		//taking grid dimensions...
		n_rc = JOptionPane.showInputDialog(f, "Enter number of rows and columns seperated by space: ").split(" ");	
		n_rows = Integer.parseInt(n_rc[0]);
		n_cols = Integer.parseInt(n_rc[1]);

		//instructions...
		JOptionPane.showMessageDialog(f,"Instructions for input: \nPositive Value means health spot (increases HP)\nNegative Value means to encounter demon (Decreases HP)\nZero means safe area!");
		JOptionPane.showMessageDialog(f,"\nEnter "+(n_rows*n_cols)+" values which are the values of spots in dungeon!");		
		visit=new int[n_rows][n_cols];
		//taking input... and storing...
		for(i=0;i<n_rows;i++)
		{
			for(j=0;j<n_cols;j++)
			{
				temp=Integer.parseInt(JOptionPane.showInputDialog(f,"Enter A Value: "));
				grid[i][j]=temp;
				visit[i][j]=0;
			}
		}
		p.setLayout(new GridLayout(n_rows, n_cols));
		// Using DP Algorithm to calculate HP
		calculateHP();
		
		//Images...
		buttonImg = create_scaleImage("Images/tile2_final.png");
		trapImg = create_scaleImage("Images/demon.jpg");
		treasureImg = create_scaleImage("Images/treasure_chest_final.jpg");
		healingImg = create_scaleImage("Images/fountain_of_youth.jpg");
		
		//Creating rooms ( buttons with img's )
		for(i=0;i<n_rows;i++)
		{
			for(j=0;j<n_cols;j++)
			{
				if(grid[i][j] < 0)
				{
					b[i][j] = new JButton(trapImg);
				}
				else if(grid[i][j] > 0)
				{
					b[i][j] = new JButton(healingImg);
				}
				else
				{
					b[i][j] = new JButton(buttonImg);
				}
				p.add(b[i][j]);
				b[i][j].addMouseListener(this);
			}
		}
		
		//Treasure room creation..
		treasure_row=n_rows;
		treasure_col=n_cols;
		b[treasure_row-1][treasure_col-1].setIcon(treasureImg);
		
		//instructions...
		s ="<html><br><br><br><span style=\"color : blue;\">      FIND THE WAY OUT OF DUNGEON WITH MIN HEALTH!</span><br><br><br><span style=\"color : orange;\">********************************INSTRUCTIONS***************************************<br><br><br></span>1. START WITH THE 1ST SQUARE AND TRAVEL THE ROOMS TO REACH THE TREASURE.<br><br><br>POINTS TO REMEBER : <br><br><br>1. HEALING FOUNTAIN INDICATE INCREASE IN HP<br><br><br>2. DEMON INDICATE DECREASE IN HP<br><br><br>3. NORMAL TILES INDICATE SAFE POSITION.<br><br><br>DIAGONAL PATH TRAVELLING IS NOT ALLOWED!, YOU CAN ONLY TRAVEL HORIZONTALLY OR VERTICALLY<br><br><br></body></html>";
		l_ins = new JLabel(s);
		
		//submit button to submit answer...
		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource() == submit)
				{
					if (tempHealth<0) {
						if((Math.abs(tempHealth)+1)==hp) 
						{
							JOptionPane.showMessageDialog(f, "Congrats!, You Got Correct Ans!");
						}
						else {
							JOptionPane.showMessageDialog(f, "Vari, You made it wrong!");
						}
					}
					else {
						if(tempHealth==hp)
						{
							JOptionPane.showMessageDialog(f, "Congrats!, You Got Correct Ans!");
						}
						else
						{
							JOptionPane.showMessageDialog(f, "Vari, You made it wrong!");
						}
					}
				}
			}
		});
		pp.add(l_ins);
		pp.add(submit);
		//initializing stuff...
		f.add(p);
		f.add(pp);
		f.setLayout(new GridLayout(2,1));
		f.isResizable();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void calculateHP()
	{
		int dungeon[][]= new int[n_rows][n_cols];
		for(int i=0;i<n_rows;i++) {
			for(int j=0;j<n_cols;j++) {
				dungeon[i][j]=grid[i][j];
			}
		}
		hp=minHp(dungeon);
	}
	public int minHp(int dungeon[][])
	{
		if(dungeon==null || dungeon.length==0 || dungeon[0]==null || dungeon[0].length==0) 
			return 0;

		int n = dungeon[0].length;
		int dp[] = new int[n + 1];
		Arrays.fill(dp, Integer.MAX_VALUE);
		dp[n-1] = 1;

		for(int row=dungeon.length-1; row>=0; row--) {
			for(int col=n-1; col>=0; col--) {
				int t = Math.min(dp[col], dp[col+1]);
				dp[col] = Math.max(1, t - dungeon[row][col]);
			}
		}
		return dp[0];
	}
//	public static void main(String[] args)
//	{
//		new ui();
//	}
	@Override
	public void mouseClicked(MouseEvent e){				//this method handles all the mouse click events like when a room is clicked what to do...
		// TODO Auto-generated method stub
		for(i=0;i<n_rows;i++)
		{
			for(j=0;j<n_cols;j++)
			{
				if(e.getSource() == b[i][j])
				{
					if(grid[i][j] < 0)
					{
						if(visit[i][j]==0)
						{
							b[i][j].setIcon(null);
							b[i][j].setBackground(Color.RED);
							tempHealth-=Math.abs(grid[i][j]);
							s ="<html><br><br><br><span style=\"color : blue;\">      FIND THE WAY OUT OF DUNGEON WITH MIN HEALTH!</span><br><br><br><span style=\"color : orange;\">********************************INSTRUCTIONS***************************************<br><br><br></span>1. START WITH THE 1ST SQUARE AND TRAVEL THE ROOMS TO REACH THE TREASURE.<br><br><br>POINTS TO REMEBER : <br><br><br>1. HEALING FOUNTAIN INDICATE INCREASE IN HP<br><br><br>2. DEMON INDICATE DECREASE IN HP<br><br><br>3. NORMAL TILES INDICATE SAFE POSITION.<br><br><br>DIAGONAL PATH TRAVELLING IS NOT ALLOWED!, YOU CAN ONLY TRAVEL HORIZONTALLY OR VERTICALLY<br><br><br>" + "Health : " + String.valueOf(tempHealth) +" </body></html>";
							visit[i][j]=1;
						}
						else 
						{
							if(i == n_rows-1 && j == n_cols-1)
							{

								b[i][j].setIcon(treasureImg);
							}
							else
							{
								b[i][j].setIcon(trapImg);
							}
							tempHealth+=Math.abs(grid[i][j]);

							s ="<html><br><br><br><span style=\"color : blue;\">      FIND THE WAY OUT OF DUNGEON WITH MIN HEALTH!</span><br><br><br><span style=\"color : orange;\">********************************INSTRUCTIONS***************************************<br><br><br></span>1. START WITH THE 1ST SQUARE AND TRAVEL THE ROOMS TO REACH THE TREASURE.<br><br><br>POINTS TO REMEBER : <br><br><br>1. HEALING FOUNTAIN INDICATE INCREASE IN HP<br><br><br>2. DEMON INDICATE DECREASE IN HP<br><br><br>3. NORMAL TILES INDICATE SAFE POSITION.<br><br><br>DIAGONAL PATH TRAVELLING IS NOT ALLOWED!, YOU CAN ONLY TRAVEL HORIZONTALLY OR VERTICALLY<br><br><br>" + "Health : " + String.valueOf(tempHealth) +" </body></html>";
							visit[i][j]=0;
						}
						l_ins.setText(s);
					}
					else if(grid[i][j] > 0)
					{
						if(visit[i][j]==0)
						{
							b[i][j].setIcon(null);
							b[i][j].setBackground(Color.GREEN);
							b[i][j].setForeground(Color.GRAY);
							tempHealth+=grid[i][j];
							s ="<html><br><br><br><span style=\"color : blue;\">      FIND THE WAY OUT OF DUNGEON WITH MIN HEALTH!</span><br><br><br><span style=\"color : orange;\">********************************INSTRUCTIONS***************************************<br><br><br></span>1. START WITH THE 1ST SQUARE AND TRAVEL THE ROOMS TO REACH THE TREASURE.<br><br><br>POINTS TO REMEBER : <br><br><br>1. HEALING FOUNTAIN INDICATE INCREASE IN HP<br><br><br>2. DEMON INDICATE DECREASE IN HP<br><br><br>3. NORMAL TILES INDICATE SAFE POSITION.<br><br><br>DIAGONAL PATH TRAVELLING IS NOT ALLOWED!, YOU CAN ONLY TRAVEL HORIZONTALLY OR VERTICALLY<br><br><br>" + "Health : " + String.valueOf(tempHealth) +" </body></html>";
							l_ins.setText(s);
							visit[i][j]=1;
						}
						else
						{
							if(i == n_rows-1 && j == n_cols-1)
							{
								b[i][j].setIcon(treasureImg);
							}
							else
							{
								tempHealth-=grid[i][j];
								b[i][j].setIcon(healingImg);
							}
							s ="<html><br><br><br><span style=\"color : blue;\">      FIND THE WAY OUT OF DUNGEON WITH MIN HEALTH!</span><br><br><br><span style=\"color : orange;\">********************************INSTRUCTIONS***************************************<br><br><br></span>1. START WITH THE 1ST SQUARE AND TRAVEL THE ROOMS TO REACH THE TREASURE.<br><br><br>POINTS TO REMEBER : <br><br><br>1. HEALING FOUNTAIN INDICATE INCREASE IN HP<br><br><br>2. DEMON INDICATE DECREASE IN HP<br><br><br>3. NORMAL TILES INDICATE SAFE POSITION.<br><br><br>DIAGONAL PATH TRAVELLING IS NOT ALLOWED!, YOU CAN ONLY TRAVEL HORIZONTALLY OR VERTICALLY<br><br><br>" + "Health : " + String.valueOf(tempHealth) +" </body></html>";
							l_ins.setText(s);
							visit[i][j]=0;
						}
					}
					else
					{
						if(visit[i][j]==0)
						{
							b[i][j].setIcon(null);
							b[i][j].setBackground(Color.BLUE);
							b[i][j].setForeground(Color.GRAY);
							s ="<html><br><br><br><span style=\"color : blue;\">      FIND THE WAY OUT OF DUNGEON WITH MIN HEALTH!</span><br><br><br><span style=\"color : orange;\">********************************INSTRUCTIONS***************************************<br><br><br></span>1. START WITH THE 1ST SQUARE AND TRAVEL THE ROOMS TO REACH THE TREASURE.<br><br><br>POINTS TO REMEBER : <br><br><br>1. HEALING FOUNTAIN INDICATE INCREASE IN HP<br><br><br>2. DEMON INDICATE DECREASE IN HP<br><br><br>3. NORMAL TILES INDICATE SAFE POSITION.<br><br><br>DIAGONAL PATH TRAVELLING IS NOT ALLOWED!, YOU CAN ONLY TRAVEL HORIZONTALLY OR VERTICALLY<br><br><br>" + "Health : " + String.valueOf(tempHealth) +" </body></html>";
							l_ins.setText(s);
							visit[i][j]=1;
						}
						else
						{
							if(i == n_rows-1 && j == n_cols-1)
							{
								b[i][j].setIcon(treasureImg);
							}
							else
							{
								b[i][j].setIcon(buttonImg);
							}
							s ="<html><br><br><br><span style=\"color : blue;\">      FIND THE WAY OUT OF DUNGEON WITH MIN HEALTH!</span><br><br><br><span style=\"color : orange;\">********************************INSTRUCTIONS***************************************<br><br><br></span>1. START WITH THE 1ST SQUARE AND TRAVEL THE ROOMS TO REACH THE TREASURE.<br><br><br>POINTS TO REMEBER : <br><br><br>1. HEALING FOUNTAIN INDICATE INCREASE IN HP<br><br><br>2. DEMON INDICATE DECREASE IN HP<br><br><br>3. NORMAL TILES INDICATE SAFE POSITION.<br><br><br>DIAGONAL PATH TRAVELLING IS NOT ALLOWED!, YOU CAN ONLY TRAVEL HORIZONTALLY OR VERTICALLY<br><br><br>" + "Health : " + String.valueOf(tempHealth) +" </body></html>";							
							l_ins.setText(s);
							visit[i][j]=0;
						}
					}
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
