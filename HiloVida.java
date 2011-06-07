


public class HiloVida implements Runnable{
	
	private static final int DELAY = 500;
	
	private Ventana ventana;
	private Cuadricula cuadricula;
	private int pasos;
	
	public HiloVida(Ventana ventana, Cuadricula cuadricula, int pasos) {
		this.ventana = ventana;
		this.cuadricula = cuadricula;
		this.pasos = pasos;
	}
	
	@Override
	public void run() {
		try {
				for (int i=0; i<pasos; i++){
					if(ventana.pararCiclos() || (ventana.getCelulas() == 0)) break;
					int numNacidos = cuadricula.nacidos();
					int numMuertos = cuadricula.muertos();
					ventana.actualizaCelulas(numNacidos-numMuertos);
					ventana.decPasos();
					ventana.actulizaPasosTotales();
					ventana.refrescaContadores();
					cuadricula.repaint();
					//ventana.incCiclos();
					Thread.sleep(DELAY);
				}
				ventana.habilitaBotones();
			} catch (InterruptedException e) {
				System.out.println("Interrupcion: " + e.getMessage());
			}
	}
}
