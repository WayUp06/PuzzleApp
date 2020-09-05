package com;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class StartPuzzle extends JFrame implements ActionListener{
	private JButton open;
	private int dimension = 2;
	private BufferedImage image = null;

	public StartPuzzle(){
		setTitle("New puzzle");
		setSize(270, 350);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		JPanel main = new JPanel();
		main.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		open = new JButton(new ImageIcon(ImageLoader.loadImage("images\\question_1.jpg").getScaledInstance(250, 250, Image.SCALE_DEFAULT)));
		open.setName("Open");
		open.addActionListener(this);
		JButton start = new JButton("start");
		start.setName("Start");
		start.addActionListener(this);

		JLabel puzzleImage = new JLabel("Select an image");
		main.add(puzzleImage);
		main.add(open);
		main.add(start);
		add(main);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JButton button = (JButton)e.getSource();
		if(button.getName().equals("Open")){

			JFileChooser filechooser = new JFileChooser();
			int action = filechooser.showOpenDialog(null);
			if(action == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				try {
					image = ImageIO.read(file);

					
				} catch (IOException e1) {
					System.out.println("You must select an image");
				}
				if (image != null) {
					open.setIcon( new ImageIcon(image.getScaledInstance(250, 250, Image.SCALE_DEFAULT)));
				}
			}
		}else if(button.getName().equals("Start")){
			if(image == null)
				return;
			BufferedImage puzzelImage = ImageResizer.resizeImage(image, 400, 400);
			BufferedImage miniImage = ImageResizer.resizeImage(image, 200, 200);
			
			Puzzle.start(puzzelImage, dimension, miniImage);
			this.dispose();
		}
		
	}
}
