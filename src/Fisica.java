

public class Fisica {

	public static Double GRAVIDADE = 9.81;
	public static Double C = -0.0018311;
	
	/**
	 * Calcula a velocidade no eixo X.
	 */
	public static Double velocidadeX(Double angulo, Double velocidade) {
	
		return velocidade * Math.cos(angulo * Math.PI / 180);
	
	}
	
	/**
	 * Calcula a velocidade no eixo Y.
	 */
	public static Double velocidadeY(Double angulo, Double velocidade) {
	
		return velocidade * Math.sin(angulo * Math.PI / 180);

	}

	/**
	 * Calcula e
	 */
	public static Double e(Double t) {
	
		return (Math.pow(Math.E, Fisica.C * t));
	
	}
	
	/**
	 * Calcula a distância do objeto no tempo T.
	 */
	public static Double distancia(Double angulo, Double velocidade0, Double tempo) {
	
		Double v0x = Fisica.velocidadeX(angulo, velocidade0);
		return v0x * ((Fisica.e(tempo)/Fisica.C) - (1/Fisica.C));
	
	}

	/**
	 * Calcula a altura do objeto no tempo T.
	 */
	public static Double altura(Double angulo, Double velocidade0, Double tempo, Double h0) {
	
		Double v0y = velocidadeY(angulo, velocidade0);
		return ((((Fisica.C * v0y - Fisica.GRAVIDADE) * 
				(Fisica.e(tempo) - 1) / Fisica.C) + 
				(Fisica.GRAVIDADE * tempo)) / Fisica.C) + h0;
	
	}
}
