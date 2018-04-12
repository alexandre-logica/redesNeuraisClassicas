package br.edu.ufg.mlp_rnp;

public class Configuracao {

	public static final double TAXA_APRENDIZADO = 0.5;
	
	//XOR
	//public static final double ERRO_MAXIMO = 0.126;
	//OR
	public static final double ERRO_MAXIMO = 0.000035085;
	//AND
	//public static final double ERRO_MAXIMO = 0.145;
	
	public static final double NUMERO_MAXIMO_EPOCAS = 10000;
	public static final double BIAS = 1;
	public static final int NUMERO_NEURONIOS_CAMADA_INTERMEDIARIA = 2;
	public static final int NUMERO_NEURONIOS_CAMADA_ENTRADA = 2;
	
	public static final double TREINO[] = {0.05, 0.1, BIAS};
		
	public static final double ESPERADOS[] = {0.01, 0.99};
	
}
