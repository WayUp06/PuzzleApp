package com;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Figure extends JButton implements ActionListener {
	private int posX;
	private int posY;
	private final int solutionPosX;
	private final int solutionPosY;
	private int dimension;
	private ImageIcon icon;
	private int degrees;

	public Figure(int solutionPosX, int solutionPosY){
		this.solutionPosX = solutionPosX;
		this.solutionPosY = solutionPosY;
	}

	
	public Figure(int solPosX, int solPosY, ImageIcon icon, int dimension){
		this.dimension = dimension;
		this.solutionPosX = solPosX;
		this.solutionPosY = solPosY;
		this.posX = solPosX;
		this.posY = solPosY;
		this.icon = icon;
		
		this.setIcon(icon);
		this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		this.addActionListener(this);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)&&isEnabled()) {
					degrees = (degrees + 90) % 360;
					BufferedImage bf = ImageChanger.rotate((BufferedImage) getImage().getImage(), degrees);
					Board.board[posX][posY].setIcon(new ImageIcon(bf));
					Puzzle.getBoard().remover();

					CheckAnswer();
				}
			}
		});
		
	}

	public int getPosX() {
		return posX;
	}

	public void setPos(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public void setDegrees(int degrees) {
		this.degrees = degrees;
	}

	public ImageIcon getImage() {return icon;}

	public int getPosY() {
		return posY;
	}

	public int getDegrees() {return degrees;}

	public int getSolutionPosX() {
		return solutionPosX;
	}

	public int getSolutionPosY() {
		return solutionPosY;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Move();
	}



	private void Move() {
		Figure[][] board = Board.board;

			if (posY+1 < dimension && board[posX][posY + 1].getImage() == null) {
				Board.board[posX][posY + 1] = this;
				Board.board[posX][posY] = new Figure(-1, -1);
				posY++;
				Puzzle.getBoard().remover();
				CheckAnswer();

			} else if (posX + 1 < dimension && board[posX + 1][posY].getImage() == null) {
				Board.board[posX + 1][posY] = this;
				Board.board[posX][posY] = new Figure(-1, -1);
				posX++;
				Puzzle.getBoard().remover();
				CheckAnswer();

			} else if (posX > 0 && board[posX - 1][posY].getImage() == null) {
				Board.board[posX - 1][posY] = this;
				Board.board[posX][posY] = new Figure(-1, -1);
				posX--;
				Puzzle.getBoard().remover();
				CheckAnswer();

			} else if (posY > 0 && board[posX][posY - 1].getImage() == null) {
				Board.board[posX][posY - 1] = this;
				Board.board[posX][posY] = new Figure(-1, -1);
				posY--;
				Puzzle.getBoard().remover();
				CheckAnswer();
			}
	}
			


	private void CheckAnswer(){
		Figure figure;
		for(int i = 0; i<dimension; i++){
			for(int j = 0; j<dimension; j++){
				
				figure = Board.board[i][j];
				if(figure.getImage() == null)
					continue;
				
				if(figure.getPosX() != figure.getSolutionPosX() || figure.getPosY() != figure.getSolutionPosY() || figure.getDegrees() != 0){
					return;
				}
			}	
		}

		JOptionPane.showMessageDialog(new JPanel(), "Congratulations", "Puzzle completed", JOptionPane.INFORMATION_MESSAGE);
	}
}
