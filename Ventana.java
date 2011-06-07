import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
public class Ventana extends JFrame {
	public static final int ANCHO=600;
	public static final int ALTO=700;
	
	private Cuadricula cuadricula;
	private Thread t;
	private boolean parar;

	private JButton botonParada;
	private JButton botonLimpiar;
	private JButton botonCorre;
	private JButton botonStep;
	
	private JLabel ciclos, totCiclos, resCiclos, totCelulas;
	private final JPanel panelContadores;
	private final JPanel panelBotones;
	
	private  Integer  numPasos, resPasos, numCelulas, totPasos;
	
	public Ventana(){
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension dimension = kit.getScreenSize();
		int largo = dimension.width;
		int alto = dimension.height;
		setTitle("Juego de la vida");
		setBounds((largo-ANCHO)/2, (alto-ALTO)/2, ANCHO,ALTO);
		setResizable(false);
		
		totPasos = 0;
		numCelulas = 0;
		parar = false;
		
		cuadricula = new Cuadricula();
		JPanel panelInferior = new JPanel();
		panelInferior.setLayout(new BorderLayout());
		
		panelContadores = new JPanel();
		panelBotones = new JPanel();
		
		ciclos = new JLabel("0");
		ciclos.setForeground(Color.BLUE);
		resCiclos = new JLabel("0");
		resCiclos.setForeground(Color.RED);
		totCelulas = new JLabel("0");
		totCiclos = new JLabel("0");
		totCiclos.setForeground(Color.GREEN);

		
		botonStep = new JButton("Paso");
		final JSpinner cuenta = new JSpinner(new SpinnerNumberModel (1,1,1000,1));

		botonCorre = new JButton("Correr");
		botonParada = new JButton("Parar");
		botonLimpiar = new JButton("Limpiar");
		JButton botonExit = new JButton("Salir");
		
		
		//Paso a paso
		botonStep.addActionListener(new
				ActionListener() {
					public void actionPerformed(ActionEvent evento){
						parar = false;
						numPasos = resPasos = 1;
						ciclos.setText(numPasos.toString());
						cuadricula.refrescaCelulas();
						numCelulas = cuadricula.getCelulas();
						totCelulas.setText(numCelulas.toString());
						panelContadores.repaint();
						Pasos();
					}
			});
		
		//N pasos
		botonCorre.addActionListener(new
				ActionListener() {
					public void actionPerformed(ActionEvent evento){
						inhabilitaBotones();
						parar = false;
						numPasos = resPasos = (Integer)cuenta.getValue();
						ciclos.setText(numPasos.toString());
						cuadricula.refrescaCelulas();
						numCelulas = cuadricula.getCelulas();
						totCelulas.setText(numCelulas.toString());
						panelContadores.repaint();
						Pasos();
					}
			});
		
		//Parar
		botonParada.addActionListener(new
				ActionListener() {
					public void actionPerformed(ActionEvent evento){
						parar = true;
						habilitaBotones();
					}
		});
		
		//Limpiar
		botonLimpiar.addActionListener(new
				ActionListener() {
					public void actionPerformed(ActionEvent evento){
						cuadricula.inicializar();
						totPasos = 0;
						numCelulas = 0;
						numPasos = resPasos = 0;
						ciclos.setText(numPasos.toString());
						resCiclos.setText(resPasos.toString());
						totCelulas.setText(numCelulas.toString());
						totCiclos.setText(totPasos.toString());
						panelContadores.repaint();
						
					}
			});
		
		
		//Salir
		botonExit.addActionListener(new
				ActionListener() {
					public void actionPerformed(ActionEvent evento){
						System.exit(0);
					}
			});
		
		panelContadores.add(new JLabel("Ciclos: "));
		panelContadores.add(ciclos);
		panelContadores.add(new JLabel("Quedan: "));
		panelContadores.add(resCiclos);
		panelContadores.add(new JLabel("Céluas: "));
		panelContadores.add(totCelulas);
		panelContadores.add(new JLabel("Ciclos Totales: "));
		panelContadores.add(totCiclos);
		
		panelBotones.add(botonStep);
		panelBotones.add(cuenta);
		panelBotones.add(botonCorre);
		panelBotones.add(botonParada);
		panelBotones.add(botonLimpiar);
		panelBotones.add(botonExit);
		
		panelInferior.add(panelContadores,BorderLayout.CENTER);
		panelInferior.add(panelBotones,BorderLayout.SOUTH);
		
		add(cuadricula,BorderLayout.CENTER);
		add(panelInferior,BorderLayout.SOUTH);
	}

	public boolean pararCiclos(){
		return parar;
	}
	
	public int getCelulas(){
		return numCelulas;
	}
	
	public void actualizaCelulas(int num){
		numCelulas += num;
		totCelulas.setText(numCelulas.toString());
		if (numCelulas == 0) parar = true;
	}
	
	public void decPasos() {
			resPasos--;
			resCiclos.setText(resPasos.toString());
	}
	
	public void actulizaPasosTotales() {
		totPasos++;
		totCiclos.setText(totPasos.toString());
	}
	
	private void inhabilitaBotones(){
		botonStep.setEnabled(false);
		botonLimpiar.setEnabled(false);
		botonCorre.setEnabled(false);
	}
	
	public void habilitaBotones(){
		botonStep.setEnabled(true);
		botonLimpiar.setEnabled(true);
		botonCorre.setEnabled(true);
	}	
	
	public void refrescaContadores(){
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					panelContadores.repaint();
				}
			});
		} catch (InterruptedException e) {
			System.out.println("Interrupcion hilo pintando contadores: "+ e.getMessage());
		} catch (InvocationTargetException e) {
			System.out.println("Error iniciando  hilo pintando contadores: "+ e.getMessage());
		}
		
	}
	
	private  void Pasos() {
		Runnable hVida = new HiloVida(this, cuadricula, numPasos);
		t = new Thread(hVida);
		t.start();
	}
}
