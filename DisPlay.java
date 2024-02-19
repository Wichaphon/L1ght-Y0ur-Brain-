
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
// import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;
import javax.swing.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DisPlay implements MouseListener{
    static DisPlay gameDisPlay;
    static final int tileSize = 16 * 3;
    static final int ScreeenVertical = 16; //sleep 768
    static final int ScreenHorizontal = 20; //stand 960
    static final int ScreenWidth = tileSize * ScreenHorizontal; 
    static final int ScreeenHeight = tileSize * ScreeenVertical;

    private JFrame window; 
    private JPanel contentPane;
    private JLabel textScore, numScore , gameIconinG; // label"score:"  and labelscore
    private JLabel textHighscore, numHighscore; // label"Highscore:" and Labelhighscore
    private JButton btnQuit; // quit button
    private JButton purPad, greenPad, redPad, cyanPad, orgPad, yellPad; // All pads (6 pads)
    private PadType type; // enum Padtype
    private LinkedHashMap<PadType,Integer> pattern = new LinkedHashMap<>();  // for keep pattern
    private ArrayList <Integer> ans = new ArrayList<>(); // for keep pattern that player click
    private ArrayList <PadType> list_pad = new ArrayList<>(); // list of Padtype 
    private ArrayList <Integer> list_amount = new ArrayList<>(); // list of amount to click in one pad
    private ArrayList <PadType> pad = new ArrayList<>(); // for use to blinking effect
    private ArrayList <Integer> idx = new ArrayList<>(); // for use to blinking effect
    private ArrayList <Integer> idx_ans = new ArrayList<>(); 
    private int countgen = 1; // count for generate pattern (level will increase from countgen)
    private int cntrandom = 0; // use as countgen 
    private int cntpatt = 1; // use for count statement blinking effect 
    private int amount; //  use for amount of statement in blinking effect 
    private int cntidx = 0; // use for count index of Arraylist idx in bliking effect
    private int j; //for blinking
    private int cntamount = 0; // for count amount of amount in pattern
    private boolean isGamePhase = false; // for check that is in methodGamePhase use for mouselistener
    private boolean isWin = false;

    private BottonSound musicBotton;
    private BackgroundSound musicBackground;
    
    private Timer blinkTimer;
    static final String highScoreFilePath = "HighScoreSave.txt";

    public DisPlay() {
        // set window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("L1ght Y0ur Brain");
        window.getContentPane().setBackground(Color.decode("#2b2b2b"));
        window.setSize(new Dimension(ScreenWidth, ScreeenHeight));
        window.setVisible(true);
       
        // Create a JPanel to serve as the content pane
        contentPane = new JPanel();
        contentPane.setBounds(0, 0,950, 100);
        contentPane.setBackground(Color.decode("#2b2b2b"));
        contentPane.setLayout(null);
        
        window.setIconImage(new ImageIcon("Image/lyblg.png").getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH));
        window.setLocationRelativeTo(null);
        window.add(contentPane);

        stageDetails();
        loadHighScore();
    }

    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(highScoreFilePath))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                int savedHighScore = Integer.parseInt(line);
                numHighscore.setText(Integer.toString(savedHighScore));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void saveHighScore(int newHighScore) {
        try (FileWriter writer = new FileWriter(highScoreFilePath)) {
            writer.write(Integer.toString(newHighScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void stageDetails(){
        gameIconinG = new JLabel(new ImageIcon("Image\\gameIngame.png"));
        gameIconinG.setBounds(50, 45, 350, 50);
        // set textscore and numscore
        textScore = new JLabel(new ImageIcon("Image\\score.png"));
        numScore = new JLabel("0");
        textScore.setForeground(Color.WHITE);
        numScore.setForeground(Color.WHITE);
        numScore.setFont(new Font("pixellet", Font.BOLD, 24));
        textScore.setBounds(730, 30, 150, 100);
        numScore.setBounds(860, 30,150, 100);

        // set textHighScore and numHighScore
        textHighscore = new JLabel(new ImageIcon("Image\\highscore.png"));
        numHighscore = new JLabel("0");
        textHighscore.setForeground(Color.WHITE);
        numHighscore.setForeground(Color.WHITE);
        numHighscore.setFont(new Font("pixellet", Font.BOLD, 24));
        textHighscore.setBounds(700, 0, 150, 100);
        numHighscore.setBounds(860, 0,150, 100);
        // set Quit button
        btnQuit = new JButton("Quit");
        btnQuit.setFont(new Font("pixellet", Font.BOLD,18 ));
        btnQuit.setBounds(435, 620, 70, 50);
        btnQuit.setPreferredSize(new Dimension(70,50));
        btnQuit.addMouseListener(this);
        btnQuit.setForeground(Color.WHITE);
        btnQuit.setBackground(Color.decode("#2b2b2b"));
        btnQuit.setBorder(BorderFactory.createEmptyBorder());
        btnQuit.setFocusPainted(false);
        btnQuit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // set All pads
        // set purplepad
        UIManager.put("Button.select", Color.decode("#2b2b2b"));
        purPad = new JButton(new ImageIcon("Image\\Finalpad\\purpad1.png"));
        purPad.setBounds(145, 152 ,200, 200);
        purPad.setBorderPainted(false);
        purPad.setFocusPainted(false);
        purPad.setOpaque(false);
        purPad.setContentAreaFilled(false);
        purPad.setBorder(BorderFactory.createEmptyBorder());
        // set greenpad
        greenPad = new JButton(new ImageIcon("Image\\Finalpad\\greenpad1.png"));
        greenPad.setBounds(260, 265, 200, 200);
        greenPad.setBorderPainted(false);
        greenPad.setFocusPainted(false);
        greenPad.setOpaque(false);
        greenPad.setContentAreaFilled(false);
        greenPad.setBackground(Color.decode("#2b2b2b"));
        greenPad.setBorder(BorderFactory.createEmptyBorder());
        
        // set redpad
        redPad = new JButton(new ImageIcon("Image\\Finalpad\\redpad1.png"));
        redPad.setBounds(370, 379, 200, 200);
        redPad.setBorderPainted(false);
        redPad.setFocusPainted(false);
        redPad.setOpaque(false);
        redPad.setContentAreaFilled(false);
        redPad.setBackground(Color.decode("#2b2b2b"));
        redPad.setBorder(BorderFactory.createEmptyBorder());
        // set cyanpad
        cyanPad = new JButton(new ImageIcon("Image\\Finalpad\\cyanpad1.png"));
        cyanPad.setBounds(370, 150, 200, 200);
        cyanPad.setBorderPainted(false);
        cyanPad.setFocusPainted(false);
        cyanPad.setOpaque(false);
        cyanPad.setContentAreaFilled(false);
        cyanPad.setBackground(Color.decode("#2b2b2b"));
        cyanPad.setBorder(BorderFactory.createEmptyBorder());
        // set orangepad
        orgPad = new JButton(new ImageIcon("Image\\Finalpad\\orgpad1.png"));
        orgPad.setBounds(480, 265, 200, 200);
        orgPad.setBorderPainted(false);
        orgPad.setFocusPainted(false);
        orgPad.setOpaque(false);
        orgPad.setContentAreaFilled(false);
        orgPad.setBackground(Color.decode("#2b2b2b"));
        orgPad.setBorder(BorderFactory.createEmptyBorder());
        // set yellowpad
        yellPad = new JButton(new ImageIcon("Image\\Finalpad\\yellpad1.png"));
        yellPad.setBounds(590, 150,200, 200);
        yellPad.setBorderPainted(false);
        yellPad.setFocusPainted(false);
        yellPad.setOpaque(false);
        yellPad.setContentAreaFilled(false);
        yellPad.setBackground(Color.decode("#2b2b2b"));
        yellPad.setBorder(BorderFactory.createEmptyBorder());
        // add to contentPane
        contentPane.add(textScore);
        contentPane.add(numScore);
        contentPane.add(textHighscore);
        contentPane.add(numHighscore);
        contentPane.add(btnQuit);
        contentPane.add(purPad);
        contentPane.add(cyanPad);
        contentPane.add(yellPad);
        contentPane.add(greenPad);
        contentPane.add(orgPad);
        contentPane.add(redPad);
        contentPane.add(gameIconinG);
        // call next method
        patternShow();

    }
    void patternShow(){
        // set for mouselisteners
        isGamePhase = false;
        // call genpattern to generate
        genPattern();
        System.out.println(pattern);
        // traverse linkedHashmap for add element to pad and idx Arraylist to use in blinking effect
        for (Map.Entry<PadType, Integer> patt : pattern.entrySet()){
            type = patt.getKey();
            int n = (int)patt.getValue();
            cntamount += n;
            pad.add(type);
            idx.add(n);
            if (type == PadType.RED){
                if (n > 0){
                    for (int i = n; i > 0; i--){
                        idx_ans.add(1);
                    }
                }
            }
            else if(type == PadType.CYAN){
                if (n > 0){
                    for (int i = n; i > 0; i--){
                        idx_ans.add(2);
                    }
                }
            }
            else if (type == PadType.LIGHT_GREEN){
                if (n > 0){
                    for (int i = n; i > 0; i--){
                        idx_ans.add(3);
                    }
                }
            }
            else if (type == PadType.ORANGE){
                if (n > 0){
                    for (int i = n; i > 0; i--){
                        idx_ans.add(4);
                    }
                }
            }
            else if (type == PadType.PURPLE){
                if (n > 0){
                    for (int i = n; i > 0; i--){
                        idx_ans.add(5);
                    }
                }
            }
            else if (type == PadType.YELLOW){
                if (n > 0){
                    for (int i = n; i > 0; i--){
                        idx_ans.add(6);
                    }
                }
            }
        }
        System.out.println(idx_ans);
        System.out.println("countamount : " + cntamount);
        // call blinking effect
        startBlinkingEffect();
        
    }
    void gamePhase(){
        // set isGamePhase to true 
        isGamePhase = true;
        System.out.println("in gamePhase");
        // set cursor to hand cursor
        purPad.setCursor(new Cursor(Cursor.HAND_CURSOR));
        redPad.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cyanPad.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orgPad.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yellPad.setCursor(new Cursor(Cursor.HAND_CURSOR));
        greenPad.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // add mouselisteners
        purPad.addMouseListener(this);
        redPad.addMouseListener(this);
        cyanPad.addMouseListener(this);
        greenPad.addMouseListener(this);
        orgPad.addMouseListener(this);
        yellPad.addMouseListener(this);
        // add actionlisteners
        PadActionListener action = new PadActionListener();
        redPad.addActionListener(action);
        cyanPad.addActionListener(action);
        greenPad.addActionListener(action);
        purPad.addActionListener(action);
        orgPad.addActionListener(action);
        yellPad.addActionListener(action);
        //set graypad after showpattern and set sound grayshuffle
        musicBotton.playSoundBotton("Sound/grayshuffle.wav", 500);
        redPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
        cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
        greenPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
        orgPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
        yellPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
        purPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
    }
    private void genPattern(){
        // add PadType to list_pad
        list_pad.add(PadType.RED);
        list_pad.add(PadType.CYAN);
        list_pad.add(PadType.LIGHT_GREEN);
        list_pad.add(PadType.ORANGE);
        list_pad.add(PadType.YELLOW);
        list_pad.add(PadType.PURPLE);
        // list_amount add amount by condition n = 2, n = 3, n = 4
        int cnt = countgen;
        cntrandom = countgen;

        System.out.println("----------Round: " + countgen + "---------");
        if (cnt == 2){
            list_amount.add(2);
            pattern.put(randomPadtype(list_pad), randomAmount(list_amount));
        }
        else if (cnt == 3){
            list_amount.add(2);
            list_amount.add(1);
            pattern.put(randomPadtype(list_pad), 2);
            pattern.put(randomPadtype(list_pad), 1);
        }
        else if (cnt == 4){
            list_amount.add(2);
            list_amount.add(2);
            pattern.put(randomPadtype(list_pad),2);
            pattern.put(randomPadtype(list_pad), 2);
        }
        else{
            for (int i = 1; i <= cntrandom; i++){
                list_amount.add(i);
            }
            while (cntrandom > 0){

                int n = randomAmount(list_amount);
                pattern.put(randomPadtype(list_pad),n);
                if (n == cntrandom){
                    break;
                }
                cntrandom -= n;
                if (cntrandom < n){
                    if (cntrandom < 0){
                        cntrandom = cntrandom * -1;
                    }
                    pattern.put(randomPadtype(list_pad), cntrandom);
                    break;
                }
            }
        }
    }
    // random PadType
    private PadType randomPadtype(ArrayList<PadType> list){
        Random rand = new Random();
        int i = rand.nextInt(list.size());
        PadType type = list.get(i);
        if (cntrandom <= 6){
            list.remove(i);   
        }
        return type;
    }
    // random Number
    private Integer randomAmount(ArrayList<Integer> list){
        Random rand = new Random();
        // System.out.println( "Listamount before random: "+ list);
        int i = rand.nextInt(list.size());
        Integer n = list.get(i);
        // System.out.println( "Listamount after random: "+ list);
        return n;
    }

    // for mouselisteners
    @Override
    public void mouseClicked(MouseEvent e) {
       JButton input = (JButton)e.getSource();
       musicBotton = new BottonSound();
        if (input == btnQuit){
            musicBotton.playSoundBotton("Sound/clicksound.wav", 300);
            window.dispose();
            System.out.println("from btnQuit: dispose window");
            System.exit(0);
        }
        // when clicked pad will blinking to show that you has clicked
        else if (input == purPad){
            purPad.setIcon(new ImageIcon("Image\\Finalpad\\purpad2.png"));
            musicBotton.playSoundBotton("Sound/clickpad.wav", 300);
            Timer time = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    purPad.setIcon(new ImageIcon("Image\\Finalpad\\purpad1.png"));
                }
            });
            time.setRepeats(false);
            time.start();
        }
        else if (input == redPad){
            redPad.setIcon(new ImageIcon("Image\\Finalpad\\redpad2.png"));
            musicBotton.playSoundBotton("Sound/clickpad.wav", 300);
            Timer time = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    redPad.setIcon(new ImageIcon("Image\\Finalpad\\redpad1.png"));
                }
            });
            time.setRepeats(false);
            time.start();
        }
        else if (input == cyanPad){
            cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\cyanpad2.png"));
            musicBotton.playSoundBotton("Sound/clickpad.wav", 300);
            Timer time = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\cyanpad1.png"));
                }
            });
            time.setRepeats(false);
            time.start();
        }
        else if (input == greenPad){
            greenPad.setIcon(new ImageIcon("Image\\Finalpad\\greenpad2.png"));
            musicBotton.playSoundBotton("Sound/clickpad.wav", 300);
            Timer time = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    greenPad.setIcon(new ImageIcon("Image\\Finalpad\\greenpad1.png"));
                }
            });
            time.setRepeats(false);
            time.start();
        }
        else if (input == orgPad){
            orgPad.setIcon(new ImageIcon("Image\\Finalpad\\orgpad2.png"));
            musicBotton.playSoundBotton("Sound/clickpad.wav", 300);
            Timer time = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    orgPad.setIcon(new ImageIcon("Image\\Finalpad\\orgpad1.png"));
                }
            });
            time.setRepeats(false);
            time.start();
        }
        else if (input == yellPad){
            yellPad.setIcon(new ImageIcon("Image\\Finalpad\\yellpad2.png"));
            musicBotton.playSoundBotton("Sound/clickpad.wav", 300);
            Timer time = new Timer(300, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    yellPad.setIcon(new ImageIcon("Image\\Finalpad\\yellpad1.png"));
                }
            });
            time.setRepeats(false);
            time.start();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
       
    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // for mouse when entered pad
        JButton input = (JButton)e.getSource();
        if (isGamePhase){
            if (input == btnQuit){
                btnQuit.setFont(new Font("pixellet", Font.BOLD,20 ));
            }
            if (input == redPad){
                redPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad2.png"));
            }
            else if (input == cyanPad){
                cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad2.png"));
            }
            else if (input == greenPad){
                greenPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad2.png"));
            }
            else if (input == purPad){
                purPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad2.png"));
            }
            else if (input == orgPad){
                orgPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad2.png"));
            }
            else if (input == yellPad){
                yellPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad2.png"));
            }
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // for mouse when exited pad
        JButton input = (JButton)e.getSource();
        if (isGamePhase){
            if (input == btnQuit){
                btnQuit.setFont(new Font("pixellet", Font.BOLD,18));
            }
            if (input == redPad){
                redPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
            }
            else if (input == cyanPad){
                cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
            }
            else if (input == greenPad){
                greenPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
            }
            else if (input == purPad){
                purPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
            }
            else if (input == orgPad){
                orgPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
            }
            else if (input == yellPad){
                yellPad.setIcon(new ImageIcon("Image\\Finalpad\\graypad.png"));
            }
        }

    }
    private void startBlinkingEffect() {
        
        // set j as index
        j = 0;
        // set amount as count of amount of pattern in click
        //because it has 2 state that make blinking then amount should * 2 
        amount = idx.get(j) * 2;
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("type = " + pad.get(j));
                System.out.println("amount = " + amount);
                System.out.println("cntpatt = " + cntpatt);
                musicBotton = new BottonSound();
                //pattern show up ------
                if (cntpatt % 2 != 0){
                    if (pad.get(j) == PadType.RED){
                        musicBotton.playSoundBotton("Sound/Pop.wav",400);
                        redPad.setIcon(new ImageIcon("Image\\Finalpad\\redpad2.png"));
                    }
                    else if(pad.get(j) == PadType.CYAN){
                        musicBotton.playSoundBotton("Sound/Pop.wav",400);
                        cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\cyanpad2.png"));
                    }
                    else if (pad.get(j) == PadType.LIGHT_GREEN){
                        musicBotton.playSoundBotton("Sound/Pop.wav",400);
                        greenPad.setIcon(new ImageIcon("Image\\Finalpad\\greenpad2.png"));
                    }
                    else if (pad.get(j) == PadType.ORANGE){
                        musicBotton.playSoundBotton("Sound/Pop.wav",400);
                        orgPad.setIcon(new ImageIcon("Image\\Finalpad\\orgpad2.png"));
                    }
                    else if (pad.get(j) == PadType.PURPLE){
                        musicBotton.playSoundBotton("Sound/Pop.wav",400);
                        purPad.setIcon(new ImageIcon("Image\\Finalpad\\purpad2.png"));
                    }
                    else if (pad.get(j) == PadType.YELLOW){
                        musicBotton.playSoundBotton("Sound/Pop.wav",400);
                        yellPad.setIcon(new ImageIcon("Image\\Finalpad\\yellpad2.png"));
                    }
                }
                if (cntpatt % 2 == 0){
                    if (pad.get(j) == PadType.RED){
                        redPad.setIcon(new ImageIcon("Image\\Finalpad\\redpad1.png"));
                    }
                    else if(pad.get(j) == PadType.CYAN){
                        cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\cyanpad1.png"));
                    }
                    else if (pad.get(j) == PadType.LIGHT_GREEN){
                        greenPad.setIcon(new ImageIcon("Image\\Finalpad\\greenpad1.png"));
                    }
                    else if (pad.get(j) == PadType.ORANGE){
                        orgPad.setIcon(new ImageIcon("Image\\Finalpad\\orgpad1.png"));
                    }
                    else if (pad.get(j) == PadType.PURPLE){
                        purPad.setIcon(new ImageIcon("Image\\Finalpad\\purpad1.png"));
                    }
                    else if (pad.get(j) == PadType.RED){
                        redPad.setIcon(new ImageIcon("Image\\Finalpad\\redpad1.png"));
                    }
                    else if (pad.get(j) == PadType.YELLOW){
                        yellPad.setIcon(new ImageIcon("Image\\Finalpad\\yellpad1.png"));
                    }
                }       
                
                if (cntpatt == amount){
                    j++;
                    cntidx++;
                    if (cntidx > idx.size() - 1){
                        blinkTimer.stop();
                    }
                    try {
                        amount = idx.get(j) * 2;
                        
                    } catch (Exception x) {
                        System.err.println(x);
                        gamePhase();
                    }
                    cntpatt = 1;
                }
                
                else {
                    cntpatt++;
                }
            }
        };
        blinkTimer = new Timer(1000, action);
        blinkTimer.start();

    }
    private class PadActionListener implements ActionListener {
        int redcnt = 0;
        int cyancnt = 0;
        int greencnt = 0;
        int purcnt = 0;
        int orgcnt = 0;
        int yellcnt = 0;
        int totalcnt = 0;
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton input = (JButton)e.getSource();
            musicBotton = new BottonSound();
            // it has bug that when round 3 up it not clear elements
            if (countgen >= 3 && ans.isEmpty()) {
                redcnt = 0;
                cyancnt = 0;
                greencnt = 0;
                purcnt = 0;
                orgcnt = 0;
                yellcnt = 0;
                totalcnt = 0;
            }

            if (input == redPad) {
                redcnt += 1;
                ans.add(1);
            }
            else if (input == cyanPad) {
                cyancnt += 1;
                ans.add(2);
            } 
            else if (input == greenPad) {
                greencnt += 1;
                ans.add(3);
            } 
            else if (input == orgPad) {
                orgcnt += 1;
                ans.add(4);
            } 
            else if (input == purPad) {
                purcnt += 1;
                ans.add(5);
            } 
            else if (input == yellPad) {
                yellcnt += 1;
                ans.add(6);
            }

            totalcnt = redcnt + cyancnt + greencnt + orgcnt + purcnt + yellcnt;
            System.out.println("idx_ans: " + idx_ans);
            System.out.println("ans list : " + ans);
            System.out.println("total cnt :" + totalcnt);

            if (totalcnt == cntamount) {
                for (int i = 0; i < idx_ans.size(); i++){
                    if (idx_ans.get(i) == ans.get(i)){
                        System.out.println("-------------good---------");
                        isWin = true;
                    }
                    else{
                        System.out.println("----------fail-----------");
                        numScore.setText("0");
                        System.out.println("Fail");
                        list_pad.clear();
                        list_amount.clear();
                        pattern.clear();
                        pad.clear();
                        idx.clear();
                        ans.clear();
                        idx_ans.clear();
                        j = 0;
                        countgen = 1;
                        ans.clear();
                        totalcnt = 0; 
                        redcnt = 0; 
                        cyancnt = 0; 
                        greencnt = 0; 
                        purcnt = 0; 
                        orgcnt = 0; 
                        yellcnt = 0;
                        cntamount = 0;
                        cntidx = 0;
                        isGamePhase = false;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                GameOver gameOver = new GameOver();
                                window.dispose();
                                
                            }
                        });
                    }
                }
                if (isWin){
                    if (blinkTimer != null && blinkTimer.isRunning()) {
                        blinkTimer.stop();
                    }
                    System.out.println("You get 10 points");
                    musicBotton.playSoundBotton("Sound/pass.wav", 600);
                    
                    int currentScore = Integer.parseInt(numScore.getText());
                    int updatedScore = currentScore + 10;
                    numScore.setText(Integer.toString(updatedScore));
                    
                    int highscore = Integer.parseInt(numHighscore.getText());
                    if (updatedScore > highscore) {
                        highscore = updatedScore;
                        numHighscore.setText(Integer.toString(highscore));
                        saveHighScore(highscore);
                    }
                    countgen++;
                    // clear all elements 
                    list_pad.clear();
                    list_amount.clear();
                    pattern.clear();
                    pad.clear();
                    idx.clear();
                    ans.clear();
                    idx_ans.clear();
                    totalcnt = 0; 
                    redcnt = 0; 
                    cyancnt = 0; 
                    greencnt = 0; 
                    purcnt = 0; 
                    orgcnt = 0; 
                    yellcnt = 0;
                    cntamount = 0;
                    cntidx = 0;
                    j = 0;
                    e.setSource(null);
                    redPad.setIcon(new ImageIcon("Image\\Finalpad\\redpad1.png"));
                    cyanPad.setIcon(new ImageIcon("Image\\Finalpad\\cyanpad1.png"));
                    greenPad.setIcon(new ImageIcon("Image\\Finalpad\\greenpad1.png"));
                    purPad.setIcon(new ImageIcon("Image\\Finalpad\\purpad1.png"));
                    orgPad.setIcon(new ImageIcon("Image\\Finalpad\\orgpad1.png"));
                    yellPad.setIcon(new ImageIcon("Image\\Finalpad\\yellpad1.png"));
                    // clear Actionlisteners
                    for (ActionListener act : redPad.getActionListeners()){
                        redPad.removeActionListener(act);
                    }
                    for (ActionListener act : cyanPad.getActionListeners()){
                        cyanPad.removeActionListener(act);
                    }
                    for (ActionListener act : greenPad.getActionListeners()){
                        greenPad.removeActionListener(act);
                    }
                    for (ActionListener act : purPad.getActionListeners()){
                        purPad.removeActionListener(act);
                    }
                    for (ActionListener act : orgPad.getActionListeners()){
                        orgPad.removeActionListener(act);
                    }
                    for (ActionListener act : yellPad.getActionListeners()){
                        yellPad.removeActionListener(act);
                    }
                    patternShow();
                }
                
            }
        }
    }
}
