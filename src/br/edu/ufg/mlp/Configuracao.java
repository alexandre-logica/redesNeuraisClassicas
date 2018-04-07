package br.edu.ufg.mlp;

public class Configuracao {

	public static final double TAXA_APRENDIZADO = 0.9;
	
	//XOR
	//public static final double ERRO_MAXIMO = 0.126;
	//OR
	public static final double ERRO_MAXIMO = 0.094;
	//AND
	//public static final double ERRO_MAXIMO = 0.145;
	
	public static final double NUMERO_MAXIMO_EPOCAS = 100000;
	
	public static final int NUMERO_NEURONIOS_CAMADA_INTERMEDIARIA = 4;
	public static final int NUMERO_NEURONIOS_CAMADA_ENTRADA = 3;
	
	public static final double TREINO[][] = {
			{0, 0, 1, 1},
			{0, 1, 0, 1},
			{1, 1, 1, 1}
		};
		
	public static final double ESPERADOS[] = {0, 1, 1, 1};
	
}
