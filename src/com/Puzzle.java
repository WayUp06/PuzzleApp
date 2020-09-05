package com;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Puzzle extends JFrame{

	private static JPanel puzzleArea;

	private static BufferedImage def = ImageLoader.loadImage("images\\question_1.jpg");


	private final int width = 670;
	private final int height = 485;
	private final int iconSize = 30;
	private final int fontSize = 35;
	private final int delay = 1000;

	private static JButton miniImage = new JButton(new ImageIcon(def.getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
	private static Board board = null;
	private static Container container;
	
	public Puzzle(){

		this.setTitle("Puzzle");
		this.setSize(width, height);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setMinimumSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setResizable(false);
		container = this.getContentPane();


		BufferedImage retryIcon = ImageLoader.loadImage("images\\retryIcon.png");
		BufferedImage newGameIcon = ImageLoader.loadImage("images\\newIcon.png");


		JButton retryButton =
			new JButton(new ImageIcon(retryIcon.getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT)));
		retryButton.setName("retry");
		retryButton.addActionListener(new IconTimerLitener());

		JButton newGameButton =
			new JButton(new ImageIcon(newGameIcon.getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT)));
		newGameButton.setName("new");
		newGameButton.addActionListener(new IconTimerLitener());

		miniImage.setName("new");
		miniImage.addActionListener(new IconTimerLitener());

		puzzleArea = new JPanel();
		puzzleArea.setOpaque(true);
		puzzleArea.setBackground(Color.BLACK);


		JToolBar toolbar = new JToolBar();
		toolbar.add(newGameButton);
		toolbar.add(retryButton);
		

		puzzleArea.add(new JLabel(new ImageIcon(def)));
		

		
		container.add(toolbar, BorderLayout.NORTH);
		container.add(puzzleArea, BorderLayout.WEST);
		container.add(miniImage, BorderLayout.EAST);
	}
	class IconTimerLitener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object comp = e.getSource();
			if(comp instanceof JButton){
				JButton button = (JButton)comp;

				if(button.getName().equals("retry") && board != null){
					board.messBoard();
					
				}else if(button.getName().equals("new")){
					new StartPuzzle();
				}
			}
		}
		
	}

	public static void start(BufferedImage img, int dimension, BufferedImage mini){
		miniImage.setIcon(new ImageIcon(mini));
		if(board != null)
			container.remove(board);
		board = new Board(dimension, img);
		container.remove(puzzleArea);
		container.add(board, BorderLayout.WEST);
		container.validate();
	}
	public static Board getBoard() {
		return board;
	}

	public static Container getContainer(){
		return container;
	}
}