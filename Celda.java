import java.awt.geom.Rectangle2D;


public class Celda {
	
	private boolean estado;
	private Rectangle2D r;

	
	public Celda(int x, int y){
		setEstado(false);
		r = new Rectangle2D.Double(x,y,Cuadricula.TAMCELDA,Cuadricula.TAMCELDA);
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public boolean getEstado() {
		return estado;
	}
	
	public Rectangle2D getR(){
		return r;
	}
	
}
