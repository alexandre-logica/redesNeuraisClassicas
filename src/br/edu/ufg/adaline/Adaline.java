package br.edu.ufg.adaline;

public class Adaline {

	private static final double taxaAprendizagem = 0.1;
	private static final int numeroMaximoEpocas = 10000;
	private static final int numeroEntradas = 3;
	private static final Double precisao = 0.0000001;

	private Double[] esperados;
	private Double[][] amostras;
	private Double[] pesos;
	private int epocas;
	private Double erro;

	public Adaline(Double[][] amostras, Double[] esperados) {
		this.esperados = esperados;
		this.amostras = amostras;
		this.epocas = 0;
		this.atribuirValoresAleatoriosParaPesos();
	}

	public void treinar() {
		//treinarGD();
		treinarSGD();
	}
	
	public void treinarSGD() {
		double eqmAnterior = 0, eqmAtual = 0;
		do {
			eqmAnterior = calcularEQM();
			for (int i = 0; i < amostras[0].length; i++) {
				for (int j = 0; j < pesos.length; j++) {
					Double u = pesos[j] * amostras[j][i];
					pesos[j] = pesos[j] + taxaAprendizagem * (esperados[i] - u) * amostras[j][i]; 
				}
			}
			epocas++;
			eqmAtual = calcularEQM();
			erro = Math.abs(eqmAtual - eqmAnterior); 
		} while (erro > precisao && epocas < numeroMaximoEpocas);
		System.out.println("\n Epocas: "+epocas);
	}
	
	public void treinarGD() {
		double eqmAnterior = 0, eqmAtual = 0, pesosGD;
		do {
			eqmAnterior = calcularEQM();
			for (int j = 0; j < pesos.length; j++) {
				pesosGD = pesoPorEpoca(j);
				pesos[j] = pesos[j] + (taxaAprendizagem * pesosGD); 
			}
			epocas++;
			eqmAtual = calcularEQM();
			erro = Math.abs(eqmAtual - eqmAnterior); 
		} while ( erro > precisao && epocas < numeroMaximoEpocas);
		System.out.println("\n Epocas: "+epocas);
	}
	
	public Double calcularEQM() {
		Double eqm = 0.0;
		for (int i = 0; i < amostras[0].length; i++) {
			for (int j = 0; j < pesos.length; j++) {
				Double u = pesos[j] * amostras[j][i];
				eqm = eqm + 0.5 * Math.pow((esperados[i] - u), 2);
			}
		}
		eqm = eqm / amostras[0].length;
		return eqm;
	}
	
	public Double pesoPorEpoca(int j) {
		Double eqm = 0.0;
		for (int i = 0; i < amostras[0].length; i++) {
			Double u = pesos[j] * amostras[j][i];
			eqm = eqm + ((esperados[i] - u) * amostras[j][i]);
		}
		return eqm;
	}

	public void classificar(Double[] padrao) {
		Double u = 0d;
		for (int i = 0; i < pesos.length; i++) {
			u += pesos[i] * padrao[i];
		}
		//u = u-1;
		Double sinalSaida = this.funcaoDeAtivacao(u);
		System.out.println(sinalSaida+" | "+u);
	}

	private void atribuirValoresAleatoriosParaPesos() {
		pesos = new Double[numeroEntradas];
		for (int i = 0; i < pesos.length; i++) {
			pesos[i] = Math.random() - Math.random();
			//pesos[i] = 0.0;
		}
		//Questão 21
/*		pesos[0] = 0.3092;
		pesos[1] = 0.3129;
		pesos[2] = -0.8649;*/
	}

	private Double funcaoDeAtivacao(double u) {
		return (u >= 1.0) ? 1.0 : 0.0;
	}
	
	public int getEpocas() {
		return epocas;
	}

	public Double[] getPesos() {
		return pesos;
	}
	public Double getErro() {
		return erro;
	}
}