
import java.util.ArrayList;
import java.util.List;

public class Simulador {

	// Parâmetros da simulação.
	public static Double H0 = 3.0;
	public static Double ALTURA_MAXIMA = 500.0;
	public static Double DISTANCIA_MINIMA = 3000.0;
	public static Double ANGULO_MAXIMO = 80.0;
	public static Double ANGULO_MINIMO = 0.0;
	public static Double PASSO = 0.1;
	
	private Double h0;
	private Double distancia_minima;
	private Double altura_maxima;


	
	/**
	 * Cria um simulador para um determinado cenário.
	 */
	public Simulador(Double h0, Double distancia_minima, Double altura_maxima) {
	
		this.h0 = h0;
		this.distancia_minima = distancia_minima;
		this.altura_maxima = altura_maxima;
	
	}
	

	/**
	 * Testa velocidades incrementais até achar a menor velocidade que atinge a marca.
	 */
	public Resultado simular(Double angulo) {
	
		Logger.log("Simulando ângulo %.2f graus", angulo);
		Double s = 0.0;
		Double h = this.h0;
		Double velocidade0 = 0.0;
		Resultado resultado = null;
		while (s < this.distancia_minima) {

			resultado = new Resultado(velocidade0, angulo);
			s = 0.0;
			h = this.h0;
			Double t = 0.0;
			while (h > 0 && h < this.altura_maxima) {
			
				s = Fisica.distancia(angulo, velocidade0, t);
				h = Fisica.altura(angulo, velocidade0, t, h0);
				t += Simulador.PASSO;
				resultado.adicionaPonto(t, new Ponto(s, h));
				
			}

			if (resultado.getMaxX() >= this.distancia_minima &&
				resultado.getMaxY() <= this.altura_maxima) {
			
				Logger.log(resultado.toString());
				return resultado;

			} else {
				
				if (h > this.altura_maxima) {
					
					// Se passou da altura limite para de calcular (aumentar a velocidade apenas 
					// fará o objeto ir mais alto.
					Logger.log("Passou da altura limite: %.4fm < %.4fm -> v = %.4fm/s", 
							this.altura_maxima, h, velocidade0);
					break;
					
				}
				
				velocidade0 += Simulador.PASSO;	
				
			}

		}
	
		return null;
	}

	
	/**
	 * Simula todos os ângulos.
	 */
	public List<Resultado> processaLote() {
		
		List<Resultado> lista = new ArrayList<Resultado>();

		// Varia o ângulo de 0 a 80 e simula para achar a menor velocidade.
		Logger.log("Processando ângulos entre %.2f graus e %.2f graus", 
				Simulador.ANGULO_MINIMO, Simulador.ANGULO_MAXIMO);
		for (Double angulo = Simulador.ANGULO_MINIMO; angulo <= Simulador.ANGULO_MAXIMO; angulo+=Simulador.PASSO) {
			
			Resultado resultado = this.simular(angulo);

			if (resultado != null) 	{
		
				lista.add(resultado);

			}
		}
		
		return lista;
	
	}
	
	/**
	 * Analisa a lista e encontra a menor velocidade inicial. 
	 */
	public Resultado analisaResultado(List<Resultado> lista) {
		
		Logger.log("Procurando a menor velocidade incial em %d elementos", lista.size());
		Resultado resultado = null;
		Double menorVelocidade = Double.MAX_VALUE;
		
		for (Resultado temp : lista) {
			
			if (temp.getVelocidade() < menorVelocidade) {
				
				menorVelocidade = temp.getVelocidade();
				resultado = temp;
				
			}
			
		}
		
		Logger.log("Melhor simulação encontrada: \n%s", resultado);
		System.out.println(resultado.getPontos().size());
		return resultado;
		
	}

	
	
	public static void main(String... args) throws Exception {
		
		long time = System.currentTimeMillis();
		Simulador sim = new Simulador(Simulador.H0, Simulador.DISTANCIA_MINIMA, Simulador.ALTURA_MAXIMA);
		List<Resultado> lista = sim.processaLote();
		sim.analisaResultado(lista);
		Logger.log("Tempo de simulação e análise: %d segundos", (System.currentTimeMillis() - time)/1000);
		
			
	}

}
