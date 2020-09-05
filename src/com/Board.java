package com;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Board extends JPanel{

	public static Cell[][] board;
	
	private ArrayList<Cell> completeBoard = new ArrayList<>();
	public final int dimension;
	private final int figureWidth, figureHeight;

	public Board(int dimension, BufferedImage puzzle){
		this.setPreferredSize(new Dimension(410, 0));
		this.setBackground(Color.BLACK);
		this.dimension = dimension;
		board = new Cell[dimension][dimension];
		int x = 0;
		int y = 0;
		figureWidth = puzzle.getWidth()/dimension;
		figureHeight = puzzle.getHeight()/dimension;

		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++){
				if(i == dimension - 1 && j == dimension -1){			
					continue;
				}
				completeBoard.add(new Cell(i, j, new Figure(i, j, new ImageIcon(puzzle.getSubimage(x, y, figureWidth, figureHeight)), dimension)));
				//completeBoard.add(new Cell(i, j, new Figure(i, j, new ImageIcon(ImageResizer.rotate( puzzle.getSubimage(x, y, figureWidth, figureHeight), 90)), dimension)));
				x += figureWidth;
			}
			x = 0;
			y += figureHeight;
		}
		messBoard();
		
		remover();
		
	}
	public void messBoard(){
		
		Random  random= new Random();
		ArrayList<Cell> cellStore = new ArrayList<>(completeBoard);
		
		for(int i = 0; i<dimension; i++){
			for(int j = 0; j<dimension; j++){
				if(i == dimension-1 && j == dimension-1){
					board[i][j] = new Cell(i, j);
					continue;
				}
				int randomDegree = random.nextInt(4) * 90;
				int randomIndex = random.nextInt(completeBoard.size());

				Figure figure = completeBoard.get(randomIndex).getFigure();
				figure.setPos(i,j);
				figure.setDegrees(randomDegree);
				BufferedImage bf = ImageResizer.rotate((BufferedImage) figure.getImage().getImage(), figure.getDegrees());
				figure.setIcon(new ImageIcon(bf));

				board[i][j] = new Cell(i, j, figure);
				completeBoard.remove(randomIndex);

			}
		}
		completeBoard = cellStore;
		remover();
	}
	public void updateBoard(){
		
		for(int i = 0; i<dimension; i++){
			for(int j = 0; j<dimension; j++){	
				if(board[i][j].getFigure() == null){
					JLabel label = new JLabel();
					label.setPreferredSize(new Dimension(figureWidth, figureHeight));
					this.add(label);
					continue;
				}
				this.add(board[i][j].getFigure());
			}
		}
		Puzzle.getContainer().validate();
	}
	public void remover(){
		this.removeAll();
		updateBoard();
	}
}
