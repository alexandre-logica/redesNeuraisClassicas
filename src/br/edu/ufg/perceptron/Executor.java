package br.edu.ufg.perceptron;

import java.util.Arrays;

public class Executor {
	
	private static final Double[][] AMOSTRAS = new Double[][] {
		{0d, 0d, 1d, 1d},
		{0d, 1d, 0d, 1d},
		{1d, 1d, 1d, 1d}
	};
		
	private static final Double[] ESPERADOS = new Double[] {0d, 1d, 1d, 1d};

	public static void main(String[] args) {
		// Treino.
		Perceptron perceptron = new Perceptron(AMOSTRAS, ESPERADOS);
		
		System.out.println("Teste antes do treinamento: ------------------------------------------------- \n");
		Executor.imprimirTesteDeClassificacao(perceptron);
		
		perceptron.treinar();
		
		System.out.println("\n Teste depois do treinamento: --------------------------------------------- \n");
		Executor.imprimirTesteDeClassificacao(perceptron);
		System.out.println(Arrays.toString(perceptron.getPesos()));
		
	}
	
	private static void imprimirTesteDeClassificacao(Perceptron perceptron){
		
		perceptron.classificar(new Double[] {0d, 0d, 1d});
		perceptron.classificar(new Double[] {0d, 1d, 1d});
		perceptron.classificar(new Double[] {1d, 0d, 1d});
		perceptron.classificar(new Double[] {1d, 1d, 1d});
	}

}