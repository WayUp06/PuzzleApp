package com;

import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Puzzle extends JFrame {

    private static JPanel puzzleArea;
    static StartPuzzle sp;

    private static BufferedImage def = ImageLoader.loadImage("images\\question_1.jpg");


    //private final int width = 670;
    //private final int height = 485;

    private final int width = 1150;
    private final int height = 780;
    private final int iconSize = 30;
    private final int fontSize = 35;
    private final int delay = 1000;

    private static JButton miniImage = new JButton(new ImageIcon(def.getScaledInstance(400, 400, Image.SCALE_DEFAULT)));
    private static Board board = null;
    private static Container container;

    public Puzzle() {

        this.setTitle("Puzzle");
        this.setSize(width, height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        container = this.getContentPane();


        BufferedImage retryIcon = ImageLoader.loadImage("images\\retryIcon.png");
        BufferedImage newGameIcon = ImageLoader.loadImage("images\\newIcon.png");
        BufferedImage keyIcon = ImageLoader.loadImage("images\\key.png");


        JButton retryButton =
            new JButton(new ImageIcon(retryIcon.getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT)));
        retryButton.setName("retry");
        retryButton.addActionListener(new IconTimerListener());

        JButton newGameButton =
            new JButton(new ImageIcon(newGameIcon.getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT)));
        newGameButton.setName("new");
        newGameButton.addActionListener(new IconTimerListener());

        JButton solveButton =
            new JButton(new ImageIcon(keyIcon.getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT)));
        solveButton.setName("solve");
        solveButton.addActionListener(new IconTimerListener());

        miniImage.setName("new");
        miniImage.addActionListener(new IconTimerListener());

        puzzleArea = new JPanel();
        puzzleArea.setOpaque(true);
        puzzleArea.setSize(700, 700);
        puzzleArea.setBackground(Color.WHITE);


        JToolBar toolbar = new JToolBar();
        toolbar.add(newGameButton);
        toolbar.add(retryButton);
        toolbar.add(solveButton);

        puzzleArea.add(new JLabel(new ImageIcon(def.getScaledInstance(700, 700, Image.SCALE_DEFAULT))));

        container.add(toolbar, BorderLayout.NORTH);
        container.add(puzzleArea);
        container.add(miniImage, BorderLayout.EAST);
    }

    class IconTimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object comp = e.getSource();
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;

                if (button.getName().equals("retry") && board != null) {
                    board.messBoard();

                } else if (button.getName().equals("new")) {

                    sp = new StartPuzzle();
                } else if (button.getName().equals("solve") && sp != null) {
                    BufferedImage b = Solution.solve(
                        //Board.getPuzzleForSol(sp.getDimension(), ImageResizer.resizeImage(sp.getImage(), 700, 700)));

                        Board.getPuzzleForSol(sp.getDimension(), ImageResizer.betterResize(sp.getImage(), 700, 700,VALUE_INTERPOLATION_BICUBIC, true)));

                    if (board != null) {
                        container.remove(board);
                    }
                    container.remove(puzzleArea);
                    puzzleArea = new JPanel();
                    puzzleArea.setPreferredSize(new Dimension(700, 700));
                    puzzleArea.add(new JLabel(new ImageIcon(b)));
                    container.add(puzzleArea, BorderLayout.WEST);
                    container.validate();

                }
            }
        }
    }

    public static void start(BufferedImage img, int dimension, BufferedImage mini) {
        miniImage.setIcon(new ImageIcon(mini));
        if (board != null) {
            container.remove(board);
        }
        board = new Board(dimension, img);
        container.remove(puzzleArea);
        container.add(board, BorderLayout.WEST);
        container.validate();
    }

    public static Board getBoard() {
        return board;
    }

    public static Container getContainer() {
        return container;
    }
}