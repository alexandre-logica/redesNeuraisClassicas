package br.edu.ufg.mlp_rnp;

import java.io.FileNotFoundException;

public class Executor {

	public static void main(String[] args) throws FileNotFoundException {

		RedeNeural rede = new RedeNeural(Configuracao.NUMERO_NEURONIOS_CAMADA_ENTRADA, Configuracao.NUMERO_NEURONIOS_CAMADA_INTERMEDIARIA);
		
		System.out.println("Teste antes do treinamento: ------------------------------------------------- \n");
		Executor.imprimirTesteDeClassificacao(rede);
		
		rede.treinar(Configuracao.TREINO, Configuracao.ESPERADOS);
		
		System.out.println("\n Teste depois do treinamento: ------------------------------------------------ \n");
		Executor.imprimirTesteDeClassificacao(rede);
	}
	
	private static void imprimirTesteDeClassificacao(RedeNeural rede){
		
		rede.classificar(new double[] {0.0, 0.0, 1.0});
		rede.classificar(new double[] {0.0, 1.0, 1.0});
		rede.classificar(new double[] {1.0, 0.0, 1.0});
		rede.classificar(new double[] {1.0, 1.0, 1.0});
	}

}