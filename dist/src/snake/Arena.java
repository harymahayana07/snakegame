/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snake;

/**
 *
 * @author NDIAPPINK
 */
import cls.ClassDB;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.awt.im.InputMethodJFrame;
public class Arena extends JPanel implements ActionListener{
InputStream in;
JFrame ex;
    private final int Lebar = 400;
    private final int Tinggi = 400;
    private final int UkuranBola = 10;
    private final int ALL_DOTS = 1000;
    private final int RAND_POS = 30;
    private final int DELAY = 100;
private int scorenya,scoretinggi,highscore = 0;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private int dots;
    private int minuman_x;
    private int minuman_y;
    private boolean ArahKiri = false;
    private boolean ArahKanan = true;
    private boolean ArahAtas = false;
    private boolean ArahBawah = false;
    private boolean inGame = true;
    String Nama="";
    private Timer timer;
    private Image ball;
    private Image minuman;
    private Image kepala;
    private int key;
   
    public Arena() {
        
        nama();
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(Lebar, Tinggi));
        loadImages();
        initGame();
    }

    private void nama(){
        Nama = JOptionPane.showInputDialog(this, "Nama Anda");
        if (Nama == null) {
        System.exit(0);
        }
        else{
            if(Nama.equals("")){
        JOptionPane.showMessageDialog(this,"Masukkan Nama Anda");
        nama();
            }
            else{
                try{
              Connection c=ClassDB.getkoneksi();
        Statement st=(Statement)c.createStatement();
        String ceknama="Select * from score where nama = '" + Nama.toString()+"'";
            ResultSet r=st.executeQuery(ceknama);
            if (r.next()){      
                    return;     
            }
            else{
                 try {           
            st.executeUpdate("Insert into score(nama) values('" + Nama.toString() + "')");         
                 }   
                 catch(Exception e){
            System.out.println(e);
        }   
            }
        
         }catch(Exception e){
             System.out.println(e);
         }       
       }
         
    }
    
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/minum.png");
        minuman = iia.getImage();

        ImageIcon iih = new ImageIcon("src/kanan.png");
        kepala = iih.getImage();
    }

    private void initGame() {

        dots = 5;

        for (int z = 0; z < dots; z++) {
            x[z] = Lebar/20;
            y[z] = Tinggi/2;
        }

        LokasiMinuman();

        timer = new Timer(DELAY, this);
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    private void updatescore(){
         try {            
            Connection c=ClassDB.getkoneksi();
           Statement s=(Statement)c.createStatement();
        String cektinggi="Select * from score where nama = '" + Nama.toString() +"'";
            ResultSet r=s.executeQuery(cektinggi);
            if (r.next()){
             scoretinggi = Integer.parseInt(r.getString("score"));
             if (scorenya <= scoretinggi){
                  return;  
             }
             else{
                  String sqel = "UPDATE score Set score ='" + scorenya +"' where nama = '" + Nama.toString()+ "'";     
            s.executeUpdate(sqel); 
             }
                      
            }            
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(minuman, minuman_x, minuman_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(kepala, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
             String msg = "Score = "+scorenya;
             
        Font small = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, 5, Tinggi - (Tinggi-10));
        
        try {            
            
            Connection c=ClassDB.getkoneksi();
            Statement s= c.createStatement();
            String sql="Select * from score where score = (select max(score) from score)";
            ResultSet r=s.executeQuery(sql);
            if (r.next()){
                highscore = Integer.parseInt(r.getString("score"));
                String scr = "Score Tertinggi "+r.getString("nama")+" = "+highscore;
     
               g.drawString(scr, (Lebar - metr.stringWidth(scr)) -10, Tinggi -5);
            }
            else{
                String scr = "Score Tertinggi = 0";
     
               g.drawString(scr, (Lebar - metr.stringWidth(scr)) -10, Tinggi -5);
            }
            
            r.close();
            s.close();
           
        }catch(Exception e) {
            System.out.println(e);
        }

        } else {
            gameOver(g);         
        }        
    }
    private void gameOver(Graphics g) {
        updatescore();
        if (scorenya <= highscore){
            String msg = "Score Anda = "+ scorenya;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (Lebar - metr.stringWidth(msg)) / 2, Tinggi / 2);
       
        }
        else{
            String msgg = "Congratulation High Score = "+ scorenya;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.blue);
        g.setFont(small);
        g.drawString(msgg, (Lebar - metr.stringWidth(msgg)) / 2, Tinggi / 2);
       
        }
       
           
    }
    
       
    private void CekMinuman() {

        if ((x[0] == minuman_x) && (y[0] == minuman_y)) {

            dots++;
            scorenya = scorenya + 5;
            try        
    {            
    in = new FileInputStream(new File("src\\slurp.wav"));            
    AudioStream audios = new AudioStream(in);            
    AudioPlayer.player.start(audios);        
    }        
    catch(Exception e)                
    {                    
        System.out.println(e);          
    }
            LokasiMinuman();
        }
    }

    private void pindah() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (ArahKiri) {
            x[0] -= UkuranBola;
        }

        if (ArahKanan) {
            x[0] += UkuranBola;
        }

        if (ArahAtas) {
            y[0] -= UkuranBola;
        }

        if (ArahBawah) {
            y[0] += UkuranBola;
        }
    }

    private void CekTabrakan() {

        for (int z = dots; z > 0; z--) {

            if ((z > 5) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                try        
    {            
    in = new FileInputStream(new File("src\\beep.wav"));            
    AudioStream audios = new AudioStream(in);            
    AudioPlayer.player.start(audios);        
    }        
    catch(Exception e)                
    {                    
                
    }
            }
        }

        if (y[0] >= Tinggi) {
            inGame = false;
            try        
    {            
    in = new FileInputStream(new File("src\\beep.wav"));            
    AudioStream audios = new AudioStream(in);            
    AudioPlayer.player.start(audios);        
    }        
    catch(Exception e)                
    {                    
                
    }
        }

        if (y[0] < 0) {
            inGame = false;
            try        
    {            
    in = new FileInputStream(new File("src\\beep.wav"));            
    AudioStream audios = new AudioStream(in);            
    AudioPlayer.player.start(audios);        
    }        
    catch(Exception e)                
    {                    
                
    }
        }

        if (x[0] >= Lebar) {
            inGame = false;
            try        
    {            
    in = new FileInputStream(new File("src\\beep.wav"));            
    AudioStream audios = new AudioStream(in);            
    AudioPlayer.player.start(audios);        
    }        
    catch(Exception e)                
    {                    
                
    }
        }

        if (x[0] < 0) {
            inGame = false;
            try        
    {            
    in = new FileInputStream(new File("src\\beep.wav"));            
    AudioStream audios = new AudioStream(in);            
    AudioPlayer.player.start(audios);        
    }        
    catch(Exception e)                
    {                    
                
    }
        }
        
        if(!inGame) {
            timer.stop();
        }
    }

    private void LokasiMinuman() {

        int r = (int) (Math.random() * RAND_POS);
        minuman_x = ((r * UkuranBola));

        r = (int) (Math.random() * RAND_POS);
        minuman_y = ((r * UkuranBola));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            CekMinuman();
            CekTabrakan();
            pindah();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!ArahKanan)) {
                ArahKiri = true;
                ArahAtas = false;
                ArahBawah = false;
                 ImageIcon kiri = new ImageIcon("src/kiri.png");
        kepala = kiri.getImage();
            }

            if ((key == KeyEvent.VK_RIGHT) && (!ArahKiri)) {
                ArahKanan = true;
                ArahAtas = false;
                ArahBawah = false;
                 ImageIcon kanan = new ImageIcon("src/kanan.png");
        kepala = kanan.getImage();
            }

            if ((key == KeyEvent.VK_UP) && (!ArahBawah)) {
                ArahAtas = true;
                ArahKanan = false;
                ArahKiri = false;
               ImageIcon atas = new ImageIcon("src/atas.png");
        kepala = atas.getImage();
            }

            if ((key == KeyEvent.VK_DOWN) && (!ArahAtas)) {
                ArahBawah = true;
                ArahKanan = false;
                ArahKiri = false;
                 ImageIcon bawah = new ImageIcon("src/bawah.png");
        kepala = bawah.getImage();
            }
            if ((key == KeyEvent.VK_P) ) {
               
               if(timer.isRunning()){
                   timer.stop();                 
               }  
               else{
                   timer.start();     
               }    
            }    
        }
    }
}
