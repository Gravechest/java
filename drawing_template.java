import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class source {
	public static final IVEC2 reso = new IVEC2(700,700);
	public static Ball[] ball = new Ball[256];
	public static int ballC;
	private static JFrame w;
	public static Point v(){
		Point r = w.getLocation();
		return r;
	}
	public static void main(String[] args){

		w = new JFrame("epische window");
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setSize(reso.x+16,reso.y+39);
		MyPanel p = new MyPanel();
		w.add(p);
		w.setVisible(true);
		for(int i = 0;i < 10;i++){
			ball[ballC] = new Ball(new VEC2(10.0f+i*50.0f,10.0f),new VEC2(1.0f,3.0f),40);
			ballC++;
		}
		for(;;) {

			long time = System.nanoTime();
			for(int i = 0;i < ballC;i++) {
				int l = (int)(ball[i].pos.x*reso.x+ball[i].pos.y);
				for(int i2 = 0;i2 < Mathi.sqrt(ball[i].texture.length);i2++){
					for(int i3 = 0;i3 < Mathi.sqrt(ball[i].texture.length);i3++){
						p.vram[i2*reso.x+i3+l] = 0;
					}
				}
				ball[i].pos.add(ball[i].dir);
				if(ball[i].pos.x<0.0f||ball[i].pos.x+ball[i].sz>reso.x){
					ball[i].pos.sub(ball[i].dir);
					ball[i].dir.x = -ball[i].dir.x;
				}
				if(ball[i].pos.y<0.0f||ball[i].pos.y+ball[i].sz>reso.y){
					ball[i].pos.sub(ball[i].dir);
					ball[i].dir.y = -ball[i].dir.y;
				}
				l = (int)(ball[i].pos.x*reso.x+ball[i].pos.y);
				for(int i2 = 0;i2 < Mathi.sqrt(ball[i].texture.length);i2++){
					for(int i3 = 0;i3 < Mathi.sqrt(ball[i].texture.length);i3++){
						p.vram[i2*reso.x+i3+l] = ball[i].texture[i2*Mathi.sqrt(ball[i].texture.length)+i3];
					}
				}
			}
			p.b.setRGB(0,0,source.reso.x,source.reso.y,p.vram,0,source.reso.x);
			p.repaint();
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

class Ball{
	public int[] texture;
	public VEC2 pos;
	public VEC2 dir;
	public int sz;
	public Ball(VEC2 posp,VEC2 dirp,int sizep){
		texture = new int[sizep*sizep];
		IVEC2 middle = new IVEC2(sizep/2,sizep/2);
		for(int i = 0;i < texture.length;i++){
			int d = 255-IVEC2.distR(new IVEC2(i/sizep,i%sizep),middle)*255/sizep*2;
			if(d>0){
				texture[i] = d;
			}
		}
		pos = posp;
		dir = dirp;
		sz = sizep;
	}
}

class MyPanel extends JPanel{
	public int[] vram = new int[source.reso.mulS()];
	public BufferedImage b = new BufferedImage(source.reso.x,source.reso.y,BufferedImage.TYPE_INT_RGB);
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(b,0,0,this);
	}
}
