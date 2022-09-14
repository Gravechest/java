import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Main{
    private static JFrame window;
    public static final IntVec2D wndres = new IntVec2D(850,850);
    public static Button button[] = new Button[16];
    public static MyPanel wincontext = new MyPanel();
    public int colorSel = 0;
    public static void main(String args[]){
        window = new JFrame("mastermind");
        window.setSize(wndres.x+16,wndres.y-11);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.add(wincontext);
        button[Button.buttonC] = new Button(new IntVec2D(700,40),50,new RGB(255,0,0),0);
        button[Button.buttonC] = new Button(new IntVec2D(700,90),50,new RGB(0,255,0),1);
        button[Button.buttonC] = new Button(new IntVec2D(700,140),50,new RGB(0,0,255),2);
        button[Button.buttonC] = new Button(new IntVec2D(700,190),50,new RGB(255,255,0),3);
        button[Button.buttonC] = new Button(new IntVec2D(700,240),50,new RGB(255,0,255),4);
        button[Button.buttonC] = new Button(new IntVec2D(700,290),50,new RGB(255,255,255),5);

        Draw.drawCircle(new IntVec2D(700,390),50,new RGB(255,0,0));

        Draw.drawRect(new IntVec2D(780,10),new IntVec2D(10,460),new RGB(128,128,128));
        Draw.drawRect(new IntVec2D(670,360),new IntVec2D(110,10),new RGB(128,128,128));
        Draw.drawRect(new IntVec2D(660,10),new IntVec2D(10,460),new RGB(128,128,128));
        Draw.drawRect(new IntVec2D(670,10),new IntVec2D(110,10),new RGB(128,128,128));
        Draw.drawRect(new IntVec2D(670,460),new IntVec2D(110,10),new RGB(128,128,128));

        Draw.drawRect(new IntVec2D(695,480),new IntVec2D(85,10),new RGB(158,78,25));
        Draw.drawRect(new IntVec2D(780,480),new IntVec2D(10,360),new RGB(158,78,25));
        Draw.drawRect(new IntVec2D(695,830),new IntVec2D(85,10),new RGB(158,78,25));
        Draw.drawRect(new IntVec2D(695,480),new IntVec2D(10,360),new RGB(158,78,25));

        Draw.drawRect(new IntVec2D(695,565),new IntVec2D(85,10),new RGB(158,78,25));
        Draw.drawRect(new IntVec2D(695,650),new IntVec2D(85,10),new RGB(158,78,25));
        Draw.drawRect(new IntVec2D(695,735),new IntVec2D(85,10),new RGB(158,78,25));

        wincontext.b.setRGB(0,0,wndres.x,wndres.y,MyPanel.videoMem,0,wndres.y);
        wincontext.repaint();
        for(;;){
            long time = System.nanoTime();
            try{
                int time2 = 17000000;
                time2 -= System.nanoTime()-time;
                if(time2<0){
                    time2 = 0;
                }
                Thread.sleep(time2/1000000,time2%1000000);
            }
            catch(InterruptedException e){

            }
        }
    }
}

class Button{
    static int buttonC;
    IntVec2D pos;
    int size;
    int id;
    Button(IntVec2D pos,int size,RGB color,int id){
        this.pos = pos;
        this.id = id;
        this.size = size;
        Draw.drawCircle(pos,size,color);
        buttonC++;
    }
}

class Draw{
    private static int RGBtoInt(RGB color){
        return color.b+(color.g<<8)+(color.r<<16);
    }
    static void drawRect(IntVec2D offset,IntVec2D size,RGB color){
        int fcolor = Draw.RGBtoInt(color);
        for(int i = offset.x;i < offset.x+size.x;i++){
            for(int i2 = offset.y;i2 < offset.y+size.y;i2++) {
                Main.wincontext.videoMem[i * Main.wndres.x + i2] = fcolor;
            }
        }
    }
    static void drawCircle(IntVec2D offset,int size,RGB color){
        IntVec2D middle = new IntVec2D(offset.x+size/2,offset.y+size/2);
        int fcolor = Draw.RGBtoInt(color);
        for(int i = offset.x;i < offset.x+size;i++){
            for(int i2 = offset.y;i2 < offset.y+size;i2++){
                IntVec2D relPixelCoords = IntVec2D.subVec2D(new IntVec2D(i,i2),middle);
                float distToOrigin = (float)Math.sqrt(relPixelCoords.x*relPixelCoords.x+relPixelCoords.y*relPixelCoords.y);
                if(distToOrigin < (float)size/2) {
                    Main.wincontext.videoMem[i * Main.wndres.x + i2] = fcolor;
                }
            }
        }
    }
}

class RGB{
    int r;
    int g;
    int b;
    RGB(int r,int g,int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
}

class IntVec2D{
    int x;
    int y;
    static IntVec2D subVec2D(IntVec2D p1,IntVec2D p2){
        return new IntVec2D(p1.x-p2.x,p1.y-p2.y);
    }
    static IntVec2D addVec2D(IntVec2D p1,IntVec2D p2){
        return new IntVec2D(p1.x+p2.x,p1.y+p2.y);
    }
    IntVec2D(int x,int y){
        this.x = x;
        this.y = y;
    }
}

class MyPanel extends JPanel implements MouseListener{
    public static int videoMem[] = new int[Main.wndres.x*Main.wndres.y];
    public BufferedImage b = new BufferedImage(Main.wndres.x,Main.wndres.y,BufferedImage.TYPE_INT_RGB);
    MyPanel(){
        addMouseListener(this);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(b,0,0,this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        mousePos.x -= Main.wincontext.getLocationOnScreen().x;
        mousePos.y -= Main.wincontext.getLocationOnScreen().y;
        for(int i = 0;i < Button.buttonC;i++){
            if(mousePos.y>Main.button[i].pos.x&&mousePos.y<Main.button[i].pos.x+Main.button[i].size&&mousePos.x>Main.button[i].pos.y&&mousePos.x<Main.button[i].pos.y+Main.button[i].size) {
                switch(Main.button[i].id){
                    case 0:
                        Draw.drawCircle(new IntVec2D(700,390),50,new RGB(255,0,0));
                        break;
                    case 1:
                        Draw.drawCircle(new IntVec2D(700,390),50,new RGB(0,255,0));
                        break;
                    case 2:
                        Draw.drawCircle(new IntVec2D(700,390),50,new RGB(0,0,255));
                        break;
                    case 3:
                        Draw.drawCircle(new IntVec2D(700,390),50,new RGB(255,255,0));
                        break;
                    case 4:
                        Draw.drawCircle(new IntVec2D(700,390),50,new RGB(255,0,255));
                        break;
                    case 5:
                        Draw.drawCircle(new IntVec2D(700,390),50,new RGB(255,255,255));
                        break;
                }
                Main.wincontext.b.setRGB(0,0,Main.wndres.x,Main.wndres.y,MyPanel.videoMem,0,Main.wndres.y);
                Main.wincontext.repaint();
                break;
            }
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

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
