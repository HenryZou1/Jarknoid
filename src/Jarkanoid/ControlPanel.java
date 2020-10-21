package Jarkanoid;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import sun.audio.*;

public class ControlPanel extends JPanel implements KeyListener, MouseListener, Runnable {

    private JFrame frame = new JFrame();
    
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<PowerUP> powerUps = new ArrayList<>();
    private ArrayList<Ball> lasers = new ArrayList<>(), balls = new ArrayList<>();
    private Player player = new Player(250, 700, 72, 10, 5, new ImageIcon("player_short.jpg").getImage());
    private JLabel[] scoreDigits = new JLabel[5];
    private boolean run = false, ballCatch = false, mouseActivate = false;
    private int score = 0, ballCatchedDistance = -1, laserQuantity = 0;
    private String activatedPowerUp = "";
    private JLabel lives = new JLabel(new ImageIcon("Numbers/" + player.lives + ".png")),
            laserBar = new JLabel(new ImageIcon("Laser_bars/0bar.png")), powerIconLabel = new JLabel();
    private final Image[] powerIcons = {new ImageIcon("lazer.gif").getImage(),
        new ImageIcon("enlarge.gif").getImage(), new ImageIcon("catch.gif").getImage(),
        new ImageIcon("slow.gif").getImage(), new ImageIcon("speed.gif").getImage(), new ImageIcon("multi.gif").getImage(),
        new ImageIcon("life.gif").getImage()};
    private final String[] powerUpTypes = {"Lazer", "Enlarge", "Catch", "Slow", "Speed", "Multi", "Life"};
    private long introTime;

    public ControlPanel() {
        JPanel infoPanel = new JPanel();
        JLayeredPane infoPane = new JLayeredPane();
        JLabel infoLabel = new JLabel(new ImageIcon("Sidebar/info_label.jpg"));
        frame.setPreferredSize(new Dimension(870, 850));
        infoPanel.setPreferredSize(new Dimension(200, 800));
        infoPane.setPreferredSize(new Dimension(200, 800));
        introTime = System.currentTimeMillis();

        for (int x = 0; x < 10; x++) {
            blocks.add(new Block((int) (40 + 60 * x), 180, 45, 20, 0));
            blocks.add(new Block((int) (40 + 60 * x), 150, 45, 20, 1));
            blocks.add(new Block((int) (40 + 60 * x), 120, 45, 20, 2));
            blocks.add(new Block((int) (40 + 60 * x), 90, 45, 20, 3));
            blocks.add(new Block((int) (40 + 60 * x), 60, 45, 20, 4));
            blocks.add(new Block((int) (40 + 60 * x), 30, 45, 20, 5));
        }
        for (int x = 0; x < 10; x++) {
            blocks.add(new Block((int) (40 + 60 * x), 360, 45, 20, 0));
            blocks.add(new Block((int) (40 + 60 * x), 330, 45, 20, 1));
            blocks.add(new Block((int) (40 + 60 * x), 300, 45, 20, 2));
            blocks.add(new Block((int) (40 + 60 * x), 270, 45, 20, 3));
            blocks.add(new Block((int) (40 + 60 * x), 240, 45, 20, 4));
            blocks.add(new Block((int) (40 + 60 * x), 210, 45, 20, 5));
        }
        balls.add(new Ball(player.x + player.width / 2, player.y, 12, 12, 0, 0, new ImageIcon("ball.png").getImage()));
        for (int i = scoreDigits.length - 1; i >= 0; i--) {
            scoreDigits[i] = new JLabel(new ImageIcon("Numbers/0.png"));
            infoPane.add(scoreDigits[i], new Integer(1));
            scoreDigits[i].setBounds(20 + 20 * Math.abs(i - 4), 75, 40 + 20 * Math.abs(i - 4), 105);
        }

        infoPane.add(powerIconLabel, new Integer(1));
        infoPane.add(lives, new Integer(1));
        lives.setBounds(60, 120, 90, 150);
        infoPane.add(laserBar, new Integer(1));
        laserBar.setBounds(0, 160, 200, 195);
        infoPane.add(infoLabel, new Integer(0));
        infoLabel.setBounds(0, 0, 200, 800);
        infoPanel.add(infoPane);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.add(infoPanel, "East");
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.pack();
      
        frame.setVisible(true);
        start();
    }

    public void start() {
        Thread thread = new Thread(this);
        run = true;
        thread.start();
        music();
    }

    public void stop() {
        run = false;
    }

    public void restart() {
        run = true;
        player.lives = 5;
        score = 0;
        powerUps.clear();
        blocks.clear();
        balls.clear();
        for (int x = 0; x < 10; x++) {
            blocks.add(new Block((int) (40 + 60 * x), 180, 45, 20, 0));
            blocks.add(new Block((int) (40 + 60 * x), 150, 45, 20, 1));
            blocks.add(new Block((int) (40 + 60 * x), 120, 45, 20, 2));
            blocks.add(new Block((int) (40 + 60 * x), 90, 45, 20, 3));
            blocks.add(new Block((int) (40 + 60 * x), 60, 45, 20, 4));
            blocks.add(new Block((int) (40 + 60 * x), 30, 45, 20, 5));
        }
        for (int x = 0; x < 10; x++) {
            blocks.add(new Block((int) (40 + 60 * x), 360, 45, 20, 0));
            blocks.add(new Block((int) (40 + 60 * x), 330, 45, 20, 1));
            blocks.add(new Block((int) (40 + 60 * x), 300, 45, 20, 2));
            blocks.add(new Block((int) (40 + 60 * x), 270, 45, 20, 3));
            blocks.add(new Block((int) (40 + 60 * x), 240, 45, 20, 4));
            blocks.add(new Block((int) (40 + 60 * x), 210, 45, 20, 5));
        }
        balls.add(new Ball(player.x + player.width / 2, player.y, 12, 12, 0, 0, new ImageIcon("ball.png").getImage()));

    }

    public void run() {

        while (run) {
            try {
                Thread.sleep(9);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            repaint();
            if (player.lives == -1 || blocks.isEmpty()) {
                String text;
                run = false;
                int dialogButton = JOptionPane.YES_NO_OPTION;
                if (blocks.isEmpty()) {
                    text = "You win! Would you like to play again?";
                } else {
                    text = "You lose! Would you like to try again?";
                }
                int dialogResult = JOptionPane.showConfirmDialog(null, text, "Warning", dialogButton);

                if (dialogResult == JOptionPane.YES_OPTION) {

                    restart();
                } else {
                    System.exit(0);
                }

            }
        }
    }

    public void update() {
        //Score update
        laserBar.setIcon(new ImageIcon("Laser_bars/" + laserQuantity + "bar.png"));
        lives.setIcon(new ImageIcon("Numbers/" + player.lives + ".png"));
        char[] digits = new String(new StringBuffer(String.valueOf(score)).reverse()).toCharArray();
        for (int i = 0; i < 5; i++) {
            if (digits.length > i) {
                scoreDigits[i].setIcon(new ImageIcon("Numbers/" + digits[i] + ".png"));
            } else {
                scoreDigits[i].setIcon(new ImageIcon("Numbers/0.png"));
            }
        }
        for (Iterator<Ball> check = balls.iterator(); check.hasNext();) {
            Ball ball = check.next();
            // Left wall impact
            if (ball.x <= 0) {
                soundEffect("Sound/block_Collesion.wav");
                ball.setX(4);
                ball.setXSpeed(-ball.xSpeed);
            }// Right wall impact
            else if (ball.x + ball.width >= getWidth()) {
                soundEffect("Sound/block_Collesion.wav");
                ball.setX(getWidth() - 14);
                ball.setXSpeed(-ball.xSpeed);
            }// Ceiling impact 
            else if (ball.y <= 0) {
                soundEffect("Sound/block_Collesion.wav");
                ball.setY(4);
                ball.setYSpeed(-ball.ySpeed);
            }// Floor impact
            else if (ball.y + ball.height >= getHeight()) {
                check.remove();
            }//Player hit
            else if (player.collision(ball)) {
                if (ball.ySpeed != 0) {
                    soundEffect("Sound/block_Collesion.wav");
                }
                ball.setYSpeed(-ball.ySpeed);
                ball.setXSpeed(ball.xSpeed + (int) (((double) (ball.x - (player.x + (player.width / 2))) / (player.width / 2)) * 5));
                ballCatchedDistance = ball.x - player.x;
                if (ballCatch || activatedPowerUp.equals("Catch")) {
                    ball.playerHasBall = true;
                }
            }// Block collision check 
            else {
                for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
                    Block block = it.next();
                    if (!block.collision(ball).equals("")) {
                        score += 50;
                        soundEffect("Sound/block_Collesion.wav");
                        if (block.lives == 0) {
                            int n = (int) (Math.random() * 20);
                            if (n <= 6) {
                                powerUps.add(new PowerUP(powerIcons[n], powerUpTypes[n], block.x, block.y));
                            }
                            it.remove();

                        } else {
                            block.setLives((int) (block.lives - 1));
                            block.setImage();
                        }
                    }
                }
            }
            //Player has ball
            if (ball.playerHasBall) {
                ball.setYSpeed(0);
                ball.setXSpeed(0);
                ball.setY(player.y - ball.height / 2 - 3);
            }// No vertical or horizontal impact
            ball.setX(ball.x + ball.xSpeed);
            ball.setY(ball.y + ball.ySpeed);
        }
        for (Iterator<Ball> beam = lasers.iterator(); beam.hasNext();) {
            Ball theLaser = beam.next();
            theLaser.setX(theLaser.x + theLaser.xSpeed);
            theLaser.setY(theLaser.y + theLaser.ySpeed);
            for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
                Block block = it.next();
                if (!block.collision(theLaser).equals("")) {
                    score += 50;
                    beam.remove();
                    if (block.lives == 0) {
                        int n = (int) (Math.random() * 20);
                        if (n <= 6) {
                            powerUps.add(new PowerUP(powerIcons[n], powerUpTypes[n], block.x, block.y));
                        }
                        it.remove();
                    } else {
                        block.setLives((int) (block.lives - .5));
                        block.setImage();
                    }
                }
            }
        }
        if (balls.isEmpty()) {
            balls.add(new Ball(player.x + player.width / 2, player.y, 12, 12, 0, 0, new ImageIcon("ball.png").getImage()));
            ballCatchedDistance = player.width / 2 - balls.get(0).width;
            player.lives--;
            activatedPowerUp = "";
            powerUps("");
        }//Power ups
        for (Iterator<PowerUP> power = powerUps.iterator(); power.hasNext();) {
            PowerUP powerUp = power.next();
            if (powerUp.collision(player)) {
                soundEffect("Sound/bonus.wav");
                score += 100;
                if (!activatedPowerUp.equals("")) {
                    powerUps("");
                }
                activatedPowerUp = powerUp.type;
                powerUps(activatedPowerUp);
                power.remove();
            } else {
                powerUp.setY(powerUp.y + powerUp.ySpeed);
            }
        }
        //Mouse movements
        if (mouseActivate) {
            if (MouseInfo.getPointerInfo().getLocation().getX() <= getWidth() - player.width / 2
                    && MouseInfo.getPointerInfo().getLocation().getX() >= player.width / 2) {
                for (Ball ball : balls) {
                    if (ball.playerHasBall) {
                        ballCatchedDistance = ball.x - player.x - player.width / 2;
                        ball.setX((int) ((ballCatchedDistance) + MouseInfo.getPointerInfo().getLocation().getX()));
                    }
                }
                player.setX((int) MouseInfo.getPointerInfo().getLocation().getX() - player.width / 2);
            }
        }

    }

    public void powerUps(String type) {
        if (!type.equals("")) {
            powerIconLabel.setIcon(new ImageIcon(powerIcons[Arrays.asList(powerUpTypes).indexOf(type)]));
        }
        switch (type) {
            case "Lazer":
                player.setImage(new ImageIcon("laser_player_short.jpg").getImage());
                laserQuantity = 20;
                powerIconLabel.setBounds(17, 305, 58, 323);
                break;
            case "Enlarge":
                player.setWidth(144);
                player.setImage(new ImageIcon("player_long.jpg").getImage());
                powerIconLabel.setBounds(17, 280, 58, 298);
                break;
            case "Catch":
                powerIconLabel.setBounds(17, 257, 58, 275);
                ballCatch = true;
                break;
            case "Slow":
                powerIconLabel.setBounds(16, 410, 57, 398);
                for (Ball ball : balls) {
                    if (Math.abs(ball.ySpeed) >= 2) {
                        ball.setYSpeed(ball.ySpeed - (int) Math.signum(ball.ySpeed) * 2);
                    }
                }
                break;
            case "Speed":
                powerIconLabel.setBounds(16, 377, 57, 395);
                for (Ball ball : balls) {
                    if (Math.abs(ball.ySpeed) <= 6) {
                        ball.setYSpeed(ball.ySpeed + (int) Math.signum(ball.ySpeed) * 2);
                    }
                }
                break;
            case "Multi":
                powerIconLabel.setBounds(16, 352, 57, 370);
                balls.add(new Ball(player.x + player.width / 2, player.y - player.height - 10, 12, 12, 0, -4, new ImageIcon("ball.png").getImage()));
                balls.get(balls.size() - 1).setHasBall(false);
                break;
            case "Life":
                powerIconLabel.setBounds(17, 328, 58, 346);
                player.lives++;
                break;
            default:
                player.setWidth(72);
                ballCatch = false;
                player.setImage(new ImageIcon("player_short.jpg").getImage());
                for (Ball ball : balls) {
                    ball.setYSpeed((int) Math.signum(ball.ySpeed) * 4);
                }
                laserQuantity = 0;
                powerIconLabel.setIcon(null);
                break;
        }
    }

    public void music() {
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;

        ContinuousAudioDataStream loop = null;

        try {
            InputStream test = new FileInputStream("Sound/bmg.wav");
            BGM = new AudioStream(test);
            AudioPlayer.player.start(BGM);
        } catch (Exception e) {
        }
        MGP.start(loop);
    }

    public void soundEffect(String music) {
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(new File(music));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (Exception e) {
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (System.currentTimeMillis() - introTime <= 5000) {
            g2.drawImage(new ImageIcon("intro.gif").getImage(), 10, 10, this);
        } else {
            if ((score / 3000) + 1 < 10) {
                g2.drawImage(new ImageIcon("Backgrounds/" + ((int) (score / 3000) + 1) + "_phase_background.gif").getImage(), 10, 10, this);
            } else {
                g2.drawImage(new ImageIcon("Backgrounds/" + ((int) (score / 3000) + 1) + "_phase_background.gif").getImage(), 10, 10, this);
            }
            g2.finalize();
            player.paintComponent(g2);
            for (Block block : blocks) {
                block.paintComponent(g2);
            }
            for (PowerUP powerUp : powerUps) {
                powerUp.paintComponent(g2);
            }
            for (Ball ball : balls) {
                ball.paintComponent(g2);
            }
            for (Ball laser : lasers) {
                laser.paintComponent(g2);
            }
            update();
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT & (player.x + player.width) <= getWidth() - 5) {
            
            player.setX(player.x + 25);
             for (Ball ball : balls) {
                    if (ball.playerHasBall) {
                        ball.setX(ball.x + 25);
                    }
                }

        } else if (ke.getKeyCode() == KeyEvent.VK_LEFT & player.x >= 5) {
            player.setX(player.x - 25);
            for (Ball ball : balls) {
                    if (ball.playerHasBall) {
                        ball.setX(ball.x - 25);
                    }
                }
        } else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            ballCatchedDistance = -1;
            for (Ball ball : balls) {
                if (ball.playerHasBall) {
                    ball.setXSpeed((int) (((double) (ball.x - (player.x + (player.width / 2))) / (player.width / 2)) * 5));
                    ball.setYSpeed(-4);
                    ball.playerHasBall = false;
                }
            }
        }
    }

    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_SPACE & activatedPowerUp.equals("Lazer") && laserQuantity >= 0) {
            soundEffect("Sound/laser.wav");
            lasers.add(new Ball(player.x + 4, player.y, 8, 60, 0, -4, new ImageIcon("laser_beam.png").getImage()));
            lasers.add(new Ball(player.x + player.width - 4, player.y, 8, 60, 0, -4, new ImageIcon("laser_beam.png").getImage()));
            laserQuantity--;

        }
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
        mouseActivate = true;
    }

    public void mouseExited(MouseEvent me) {
        mouseActivate = false;
    }

    public static void main(String[] args) {
        new ControlPanel();
    }
}
