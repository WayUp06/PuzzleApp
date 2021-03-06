package com;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Board extends JPanel{

	public static Figure[][] board;
	
	private ArrayList<Figure> completeBoard = new ArrayList<>();
	public final int dimension;
	private final int figureWidth, figureHeight;



	public static BufferedImage[] getPuzzleForSol(int dimension, BufferedImage puzzle){
		int x = 0;
		int y = 0;
		int figWidth = puzzle.getWidth()/dimension;//тут добавити так щоб різало норм скіпаючи частину щоб ширина виходила цілим числом
		int figHeight = puzzle.getHeight()/dimension;
		int count = dimension*dimension;
		BufferedImage [] compBoard = new BufferedImage[count];

		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++){
				compBoard[i*dimension + j] = puzzle.getSubimage(x, y, figWidth, figHeight);
				x += figWidth;
			}
			x = 0;
			y += figHeight;
		}

		Random rnd = new Random();
		for (int i = compBoard.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			BufferedImage a = compBoard[index];
			compBoard[index] = compBoard[i];
			compBoard[i] = a;
		}

		return compBoard;

	}

	public Board(int dimension, BufferedImage puzzle){
		this.setPreferredSize(new Dimension(710, 710));
		this.setBackground(Color.BLACK);
		this.dimension = dimension;
		board = new Figure[dimension][dimension];
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
				completeBoard.add( new Figure(i, j, new ImageIcon(puzzle.getSubimage(x, y, figureWidth, figureHeight)), dimension));
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
		ArrayList<Figure> cellStore = new ArrayList<>(completeBoard);
		
		for(int i = 0; i<dimension; i++){
			for(int j = 0; j<dimension; j++){
				if(i == dimension-1 && j == dimension-1){
					board[i][j] = new Figure(i, j);
					continue;
				}
				int randomDegree = random.nextInt(4) * 90;
				int randomIndex = random.nextInt(completeBoard.size());

				Figure figure = completeBoard.get(randomIndex);
				figure.setPos(i,j);
				figure.setDegrees(randomDegree);
				BufferedImage bf = ImageChanger.rotate((BufferedImage) figure.getImage().getImage(), figure.getDegrees());
				figure.setIcon(new ImageIcon(bf));

				board[i][j] = figure;
				completeBoard.remove(randomIndex);

			}
		}
		completeBoard = cellStore;
		remover();
	}
	public void updateBoard(){
		
		for(int i = 0; i<dimension; i++){
			for(int j = 0; j<dimension; j++){	
				if(board[i][j].getImage() == null){
					JLabel label = new JLabel();
					label.setPreferredSize(new Dimension(figureWidth, figureHeight));
					this.add(label);
					continue;
				}
				this.add(board[i][j]);
			}
		}
		Puzzle.getContainer().validate();
	}
	public void remover(){
		this.removeAll();
		updateBoard();
	}
}
