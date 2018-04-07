package br.edu.ufg.adaline;

import java.util.Arrays;

public class Executor {
	
	private static final Double[][] AMOSTRAS = new Double[][] {
		{0d, 0d, 1d, 1d},
		{0d, 1d, 0d, 1d},
		{-1d, -1d, -1d, -1d}
	};
		
	private static final Double[] ESPERADOS = new Double[] {0d, 1d, 1d, 1d};

	public static void main(String[] args) {
		// Treino.
		Adaline adaline = new Adaline(AMOSTRAS, ESPERADOS);
		
		System.out.println("Teste antes do treinamento: ------------------------------------------------- \n");
		Executor.imprimirTesteDeClassificacao(adaline);
		System.out.println(Arrays.toString(adaline.getPesos()));
		
		adaline.treinar();
		
		System.out.println("\n Teste depois do treinamento: --------------------------------------------- \n");
		Executor.imprimirTesteDeClassificacao(adaline);
		System.out.println(Arrays.toString(adaline.getPesos()));
		//System.out.println(adaline.getErro());
		
	}
	
	private static void imprimirTesteDeClassificacao(Adaline perceptron){
		
		perceptron.classificar(new Double[] {0d, 0d, -1d});
		perceptron.classificar(new Double[] {0d, 1d, -1d});
		perceptron.classificar(new Double[] {1d, 0d, -1d});
		perceptron.classificar(new Double[] {1d, 1d, -1d});
	}

}