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
	
	public Figure(int solPosX, int solPosY, ImageIcon figure, int dimension){
		this.dimension = dimension;
		this.solutionPosX = solPosX;
		this.solutionPosY = solPosY;
		this.posX = solPosX;
		this.posY = solPosY;

		this.icon = figure;
		
		this.setIcon(figure);
		this.setPreferredSize(new Dimension(figure.getIconWidth(), figure.getIconHeight()));
		this.addActionListener(this);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)&&isEnabled()) {
					degrees = (degrees + 90) % 360;
					BufferedImage bf = ImageResizer.rotate((BufferedImage) getImage().getImage(), degrees);
					Board.board[posX][posY].getFigure().setIcon(new ImageIcon(bf));
					Puzzle.getBoard().remover();
					long l = Solution.check((BufferedImage) getImage().getImage(),
						(BufferedImage) Board.board[posX + 1][posY].getFigure().getImage().getImage());

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
		Cell[][] board = Board.board;

			if (posY+1 < dimension && board[posX][posY + 1].getFigure() == null) {
				Board.board[posX][posY + 1].setFigure(this);
				Board.board[posX][posY].setFigure(null);
				posY++;
				Puzzle.getBoard().remover();
				CheckAnswer();

			} else if (posX + 1 < dimension && board[posX + 1][posY].getFigure() == null) {
				Board.board[posX + 1][posY].setFigure(this);
				Board.board[posX][posY].setFigure(null);
				posX++;
				Puzzle.getBoard().remover();
				CheckAnswer();

			} else if (posX > 0 && board[posX - 1][posY].getFigure() == null) {
				Board.board[posX - 1][posY].setFigure(this);
				Board.board[posX][posY].setFigure(null);
				posX--;
				Puzzle.getBoard().remover();
				CheckAnswer();

			} else if (posY > 0 && board[posX][posY - 1].getFigure() == null) {
				Board.board[posX][posY - 1].setFigure(this);
				Board.board[posX][posY].setFigure(null);
				posY--;
				Puzzle.getBoard().remover();
				CheckAnswer();
			}
	}
			


	private void CheckAnswer(){
		Figure figure;
		for(int i = 0; i<dimension; i++){
			for(int j = 0; j<dimension; j++){
				
				figure = Board.board[i][j].getFigure();
				if(figure == null)
					continue;
				
				if(figure.getPosX() != figure.getSolutionPosX() || figure.getPosY() != figure.getSolutionPosY() || figure.getDegrees() != 0){
					return;
				}
			}	
		}

		JOptionPane.showMessageDialog(new JPanel(), "Congratulations", "Puzzle completed", JOptionPane.INFORMATION_MESSAGE);
	}
}
