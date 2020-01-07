import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;


public class UI extends JFrame {

	private static final long serialVersionUID = -5848684373497959505L;
	public static final String TITULO = "Teste de balística";
	public static final int LARGURA = 1000;
	public static final int ALTURA = 700;
	
	private JTextField alturaInicial = new JTextField();
	private JTextField distanciaMinima = new JTextField();
	private JTextField alturaMaxima = new JTextField();
	private JTable tabelaResultado = new JTable(new ResultadoTableModel());
	private JButton buttonMelhorResultado = new JButton("");
	private TextArea console = new TextArea();
	private List<Resultado> listaResultado = new ArrayList<Resultado>();
	private Resultado melhorResultado = null;
	private PrintStream outputStream = 
		new PrintStream(new FilteredStream(new ByteArrayOutputStream()));
	
	public UI() {
		
		super(UI.TITULO);
		
	}
	
	public void criarJanela() {
	
	    Container c = getContentPane();

	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(UI.TITULO);
        setSize(UI.LARGURA, UI.ALTURA);
        c.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Simulacao", criarTabSimulacao());       
        tabbedPane.add("Resultado", criarTabResultado());        	
        c.add(tabbedPane);
        
        setVisible(true);
		
	}
	
	private class PaintPanel extends JPanel {
	
		private static final long serialVersionUID = 3477747788988500598L;
		private Double h0;
		private Double distanciaMinima;
		private Double alturaMaxima;
		private Resultado resultado;
		private Dimension size;
		
		public PaintPanel(Double h0, Double distanciaMinima, Double alturaMaxima, Resultado resultado) {

			this.h0 = h0;
			this.distanciaMinima = distanciaMinima;
			this.alturaMaxima = alturaMaxima;
			this.resultado = resultado;
		}
		protected void paintComponent(Graphics g) {
		    
			this.size = getSize();
			super.paintComponent(g); 
	        g.setColor(Color.green);
		    g.fillRect(1, size.height-10, size.width, 10);
		    g.setColor(Color.black);
		    g.fillRect(1, convertY(h0), 50, size.height-convertY(h0)-10);
		    g.setColor(Color.orange);
		    g.drawLine(1, convertY(alturaMaxima), size.width, convertY(alturaMaxima));
		    g.setColor(Color.red);
		    g.drawLine(convertX(distanciaMinima), size.height-10, convertX(distanciaMinima), size.height-20);
		    g.setColor(Color.blue);
		    Integer x0, y0, x1, y1;
		    List<Ponto> lista = new ArrayList<Ponto>(resultado.getPontos().values());
		    Ponto p = lista.get(0);
		    x1 = convertX(p.getX());
		    y1 = convertY(p.getY());
		    for (Double t : resultado.getPontos().keySet()) {
		    
		    	x0 = x1;
		    	y0 = y1;
		    	
		    	p = resultado.getPontos().get(t);
		    	x1 = convertX(p.getX());
		    	y1 = convertY(p.getY());
		    	
		    	g.drawLine(x0, y0, x1, y1);
		    	
		    }
		    
		    
		}
		
		private Integer convertX(Double value) {
			
			return value.intValue()*(size.width-40)/distanciaMinima.intValue()+20;
			
		}
		
		private Integer convertY(Double value) {
			
			return size.height-(value.intValue()*(size.height-20)/(alturaMaxima).intValue())-10;
			
		}
	} 
	
	private void draw(Resultado resultado) {
		
		JDialog d = new JDialog((JFrame)null, resultado.toString(), true);
		d.setSize(UI.LARGURA, UI.ALTURA);
		d.setLayout(new GridLayout(1, 1));
		PaintPanel ppanel = new PaintPanel(Double.parseDouble(alturaInicial.getText()), 
				Double.parseDouble(distanciaMinima.getText()), 
				Double.parseDouble(alturaMaxima.getText()), 
				resultado);
		d.add(ppanel);
		d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		d.setVisible(true);
	}
	
	
	private JPanel criarTabResultado() {
		 
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(this.tabelaResultado);
		tabelaResultado.addMouseListener(new TabelaResultadoMouseAdapter());
		panel.add(BorderLayout.CENTER, scrollPane);
		panel.add(BorderLayout.PAGE_END, buttonMelhorResultado);
		buttonMelhorResultado.addActionListener(new MelhorResultadoBtnActionListener());
		return panel;
	     
	}
	
	

	private JPanel criarTabSimulacao() {
    
        JPanel panel = new JPanel(false);
        panel.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(false);
        inputPanel.setLayout(new GridLayout(1, 9));
        inputPanel.add(criarLabel("Altura Inicial"));
        this.alturaInicial.setDocument(new NumericDocument(4, false));
        this.alturaInicial.setText(Simulador.H0.toString());
        inputPanel.add(this.alturaInicial);
        
        inputPanel.add(criarLabel("Distância mínima"));
        this.distanciaMinima.setDocument(new NumericDocument(4, false));
        this.distanciaMinima.setText(Simulador.DISTANCIA_MINIMA.toString());
        inputPanel.add(this.distanciaMinima);
        
        inputPanel.add(criarLabel("Altura máxima"));
        this.alturaMaxima.setDocument(new NumericDocument(4, false));
        this.alturaMaxima.setText(Simulador.ALTURA_MAXIMA.toString());
        inputPanel.add(this.alturaMaxima);

        inputPanel.add(criarLabel("      "));
        JButton simularBtn = new JButton("Simular");
        simularBtn.addActionListener(new SimularBtnActionListener());
        inputPanel.add(simularBtn);
        
        panel.add(BorderLayout.PAGE_START, inputPanel);
        

        System.setOut(this.outputStream);
        panel.add(BorderLayout.CENTER, this.console);
        this.console.setEditable(false);
        return panel;
			
	}
	
	private class ResultadoTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 5376728876513990259L;
		private String[] columnNames = {"Ângulo", "Velocidade", "MinX", "MaxY"};
		
		    public String getColumnName(int col) {
		        return columnNames[col].toString();
		    }
		    public int getRowCount() { return listaResultado.size(); }
		    public int getColumnCount() { return columnNames.length; }
		    public Object getValueAt(int row, int col) {
		    	Object valor = null;
		    	Resultado resultado = listaResultado.get(row);
		    	if (col == 0) {
		    		
		    		valor = String.format("%.2f", resultado.getAngulo());
		    		
		    	} else if (col == 1) {
		    		
		    		valor = String.format("%.4f", resultado.getVelocidade());
		    		
		    	} else if (col == 2) {
		    		
		    		valor = String.format("%.4f", resultado.getMaxX());
		    		
		    	} else {
		    		
		    		valor = String.format("%.4f", resultado.getMaxY());
		    		
		    	}
		    	
		    	return valor;
		    }
		    public boolean isCellEditable(int row, int col) { return false; }
		   
		}
		
	private class SimularBtnActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e){

        	long time = System.currentTimeMillis();

        	console.setText("");
        	Simulador sim = new Simulador(
        			Double.parseDouble(alturaInicial.getText()), 
        			Double.parseDouble(distanciaMinima.getText()), 
        			Double.parseDouble(alturaMaxima.getText()));
        	listaResultado = sim.processaLote();
        	melhorResultado = sim.analisaResultado(listaResultado);
    		Logger.log("Tempo de simulação e análise: %d segundos", (System.currentTimeMillis() - time)/1000);
    		tabelaResultado.invalidate();
    		tabelaResultado.repaint();
    		buttonMelhorResultado.setText("O melhor resultado foi: " + melhorResultado);
        	
        	
        	
        }
    }
	
	private class MelhorResultadoBtnActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e){

        	if (melhorResultado != null) {
        		
        		draw(melhorResultado);
        		
        	}
        	
        }

    }
	
	
	
	private class TabelaResultadoMouseAdapter extends MouseAdapter {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if ( e.getClickCount() == 2 ) {
				
				draw(listaResultado.get(tabelaResultado.getSelectedRow()));

			}
		}
	}
	
	private JLabel criarLabel(String rotulo) {
		
		JLabel jlabel = new JLabel(rotulo);
		jlabel.setHorizontalAlignment(JLabel.RIGHT);
		return jlabel;
		
	}
	class FilteredStream extends FilterOutputStream {
        public FilteredStream(OutputStream aStream) {
            super(outputStream);
          }

        public void write(byte b[]) throws IOException {
            String aString = new String(b);
            console.append(aString);
        }

        public void write(byte b[], int off, int len) throws IOException {
            String aString = new String(b , off , len);
            console.append(aString);
            
        }
	}
	
	public static void main(String[] args) {
		
		UI ui = new UI();
		ui.criarJanela();
		
	}
	
	
}
