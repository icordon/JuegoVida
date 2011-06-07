import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;



@SuppressWarnings("serial")
public class Cuadricula extends JPanel{
	
	private Celda actual;
	private Celda malla[][];
	private Celda mallaAux[][];
	private Rectangle2D marco;
	private int celulas;
	public  static final int NUMCELDAS = 25;
	public static final int TAMCELDA = 20;
	public static final int INICIOMALLA = 50;
	public static final int TAMMALLA = NUMCELDAS * TAMCELDA;
	
	
	public Cuadricula(){
		actual = null;
		celulas = 0;
		malla = new Celda[NUMCELDAS][NUMCELDAS];
		mallaAux = new Celda[NUMCELDAS][NUMCELDAS];
		for (int i=0; i<NUMCELDAS; i++){
			for(int j=0; j<NUMCELDAS; j++){
				malla[i][j] = new Celda(INICIOMALLA+j*TAMCELDA,INICIOMALLA+i*TAMCELDA);
			}
		}
		addMouseListener(new MouseHandler());
	}
	
	private void inicializaMallaAux(){
		for (int i=0; i<NUMCELDAS; i++){
			for(int j=0; j<NUMCELDAS; j++){
				mallaAux[i][j] = new Celda(INICIOMALLA+j*TAMCELDA,INICIOMALLA+i*TAMCELDA);
			}
		}
	}
	
	public void inicializar(){
		for (int i=0; i<NUMCELDAS; i++){
			for(int j=0; j<NUMCELDAS; j++){
				malla[i][j].setEstado(false);
				celulas = 0;
				repaint();
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
	    marco= new Rectangle2D.Double(INICIOMALLA,INICIOMALLA,TAMMALLA,TAMMALLA);
	    g2.draw(marco);
	    
		for (int i=0; i<NUMCELDAS; i++){
			for(int j=0; j<NUMCELDAS; j++){
				actual = malla[i][j];
				
				//Pintamos la cuadrícula de color gris
				g2.setPaint(Color.GRAY);
				g2.draw(actual.getR());
				if(actual.getEstado()){
					
					//Obtenemos un cuadrado mas pequeño que será lo que se pinte 
					// y de color negro
					g2.setPaint(Color.BLACK);
					Rectangle2D rInterior =  new Rectangle2D.Double (
							actual.getR().getX()+1,actual.getR().getY()+1,TAMCELDA-1,TAMCELDA-1);
					g2.fill(rInterior);
				}
			}
		}
	}
	
	public Celda find(Point2D p){
		
		for (int i=0; i<NUMCELDAS; i++){
			for(int j=0; j<NUMCELDAS; j++)
				if(malla[i][j].getR().contains(p)) {
					System.out.println("Posicion  " +  i + " " + j); 
					return malla[i][j];
				}
			}
		return null;
	}
	
	public int  nacidos(){
		inicializaMallaAux();
		int nacidos = 0;
		for(int i=0; i<NUMCELDAS;i++){
			for(int j=0; j<NUMCELDAS; j++){
				int cont = 0;
				if(malla[i][j].getEstado() == false){
					for(int m=i-1;m<=i+1;m++){
						for(int n=j-1;n<=j+1;n++){
							if (m<0 || m>24 || n<0 || n>24 || (m==i && n==j)) continue;
							if(malla[m][n].getEstado() == true) 	cont++;
						}
					}
					if (cont == 3) {
						mallaAux[i][j].setEstado(true);
						nacidos++;
					}
				} else mallaAux[i][j].setEstado(true);
			}
		}
		return nacidos;
		//copiaMallas();
	}
	
	private void copiaMallas(){
		for(int i=0; i<NUMCELDAS; i++){
			for(int j=0; j<NUMCELDAS; j++){
				malla[i][j].setEstado(mallaAux[i][j].getEstado());
			}
		}
	}
	
	public int  muertos(){
		//inicializaMallaAux();
		int muertos = 0;
		for(int i=0; i<NUMCELDAS;i++){
			for(int j=0; j<NUMCELDAS; j++){
				int cont = 0;
				if(malla[i][j].getEstado() == true){
					for(int m=i-1;m<=i+1;m++){
						for(int n=j-1;n<=j+1;n++){
							if (m<0 || m>24 || n<0 || n>24 || (m==i && n==j)) continue;
							if(malla[m][n].getEstado() == true){
								cont++;
							}
						}
					}
					if (cont < 2 || cont > 3) {
						mallaAux[i][j].setEstado(false);
						muertos ++;
					}
				} 
			}
		}
		copiaMallas();
		return muertos;
	}
	
	public int getCelulas(){
		return celulas;
	}
	
	public void refrescaCelulas(){
		celulas = 0;
		for(int i=0; i<NUMCELDAS;i++){
			for(int j=0; j<NUMCELDAS; j++){
				if(malla[i][j].getEstado() == true) celulas++;
			}
		}
	}
	
	private class MouseHandler extends MouseAdapter{
		public void mousePressed(MouseEvent evento) {
			if(marco.contains(evento.getPoint())){
				actual = find (evento.getPoint());
				actual.setEstado(!actual.getEstado());
				if(actual.getEstado() == true) celulas++;
				else celulas--;
				repaint();
			}
		}		
	}	
}
