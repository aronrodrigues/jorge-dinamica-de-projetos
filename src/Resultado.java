
import java.util.Map;
import java.util.TreeMap;

public class Resultado {

	private Double velocidade;
	private Double angulo;
	private Double maxX = 0.0;
	private Double maxY = 0.0;
	private Map<Double, Ponto> pontos;

	public Resultado(Double velocidade, Double angulo) {
	
		this.velocidade = velocidade;
		this.angulo = angulo;
		this.pontos = new TreeMap<Double, Ponto>();
	
	}

	public void adicionaPonto(Double t, Ponto p) {
	
		this.maxX = Math.max(p.getX(), this.maxX);
		this.maxY = Math.max(p.getY(), this.maxY);
		this.pontos.put(t, p);

	}

	public Double getVelocidade() { 
		
		return this.velocidade; 
	
	}
	
	public Double getAngulo() { 
		
		return this.angulo;  
	
	}
	
	
	public Double getMaxX() { 
		
		return this.maxX; 
	
	}
	
	
	public Double getMaxY() { 
		
		return this.maxY; 
	
	}
	
	
	public Map<Double, Ponto> getPontos() { 
		
		return this.pontos; 
	
	}


	public String toString() {
	
		return String.format("Resultado[%.2f graus]: Velocidade=%.4fm/s (maxX=%.4fm, maxY=%.4fm)", this.angulo, this.velocidade, this.maxX, this.maxY);


	}

	public void limpaPontos() {
		
		this.pontos.clear();
		
	}




}
