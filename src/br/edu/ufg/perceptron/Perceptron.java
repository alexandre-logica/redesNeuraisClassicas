package br.edu.ufg.perceptron;

import java.util.Arrays;

public class Perceptron {

	private static final Double taxaAprendizado = .1d;
	private static final double numeroMaximoEpocas = 10000;
	private static final int numeroEntradasPerceptron = 3;

	private Double[] esperados;
	private Double[][] amostras;
	private Double[] pesos;
	private int epocas;

	public Perceptron(Double[][] amostras, Double[] esperados) {
		this.esperados = esperados;
		this.amostras = amostras;
		this.epocas = 0;
		this.atribuirValoresAleatoriosParaPesos();
	}

	public void treinar() {
		Boolean erro;
		do {
			erro = Boolean.FALSE;
			for (int i = 0; i < esperados.length; i++) {
				Double u = this.somaPonderadaPesos(i);
				Double saida = this.funcaoDeAtivacao(u);
				if (!saida.equals(esperados[i])) {
					this.aprender(saida, i);
					erro = Boolean.TRUE;
				}
			}
			epocas++;
		} while (erro && epocas < numeroMaximoEpocas);
		System.out.println(Arrays.toString(pesos));
		System.out.println("\n Epocas: "+epocas);
	}

	public void classificar(Double[] padrao) {
		Double u = 0d;
		for (int i = 0; i < pesos.length; i++) {
			u += pesos[i] * padrao[i];
		}
		Double sinalSaida = this.funcaoDeAtivacao(u);
		System.out.println(sinalSaida);
	}

	private Double somaPonderadaPesos(int i) {
		Double u = 0d;
		for (int j = 0; j < pesos.length; j++) {
			u += pesos[j] * amostras[j][i];
		}
		return u;
	}

	private void aprender(Double saida, int i) {
		Double fatorAprendizado = Perceptron.taxaAprendizado * (esperados[i] - saida);
		for (int j = 0; j < pesos.length; j++) {
			pesos[j] += fatorAprendizado * amostras[j][i];
		}
	}

	private void atribuirValoresAleatoriosParaPesos() {
		pesos = new Double[numeroEntradasPerceptron];
		for (int i = 0; i < pesos.length; i++) {
			pesos[i] = Math.random() - Math.random();
			//pesos[i] = 0.0;
		}
		//Questão 21
		//pesos[0] = 0.3092;
		//pesos[1] = 0.3129;
		//pesos[2] = -0.8649;
	}

	private Double funcaoDeAtivacao(Double u) {
		return funcaoStep(u);
	}
	
	private Double funcaoStep(Double u) {
		return (u >= 0.0d) ? 1d : 0d;
	}

	public int getEpocas() {
		return epocas;
	}

	public Double[] getPesos() {
		return pesos;
	}

}