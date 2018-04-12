package br.edu.ufg.mlp_rnp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class RedeNeural {

	private double[][] conexoesPrimeiraCamada;
	private double[][] conexoesSegundaCamada;
	private double[][] conexoesSegundaCamadaNovo;
	double[] saidasPrimeiraCamada;
	double[] saidasSegundaCamada;
	double erro = 0.0;
	private int nrNeuroniosPrimeiraCamada;
	private int nrNeuroniosEntrada;
	private int epocas = 0;
	
	public RedeNeural(int nrNeuroniosPrimeiraCamada, int nrNeuroniosEntrada) {
		this.nrNeuroniosPrimeiraCamada = nrNeuroniosPrimeiraCamada;
		this.nrNeuroniosEntrada = nrNeuroniosEntrada;
		this.inicializarConexoesSinapticasDaRede();
	}

	public void treinar(double[] conjuntoTreinamento, double[] valoresEsperados) {
		while (epocas < 10000) {
			saidasPrimeiraCamada = propagarSinalPelaPrimeiraCamada(conjuntoTreinamento);
			saidasSegundaCamada = propagarSinalPelaSegundaCamada(saidasPrimeiraCamada);
			erro = calcularErro(valoresEsperados, saidasSegundaCamada);
			aprender(conjuntoTreinamento, valoresEsperados, saidasPrimeiraCamada, saidasSegundaCamada);
			epocas++;
		}
		erro = arredondar(erro);
		System.out.println("\n Epocas: "+epocas);
	}
	
	public void classificar(double[] entrada) {
		if (epocas > Configuracao.NUMERO_MAXIMO_EPOCAS) {
			System.out.println("Nao foi possivel atingir um ponto de convergencia, verifique os parametros e a estrutura da rede.");
		} else {
			double[] saidasPrimeiraCamada = getSaidaClassificacaoPrimeiraCamada(entrada);
			double[] entradaSegundaCamada = getEntradasSegundaCamada(saidasPrimeiraCamada);
			//double y = propagarSinalPelaSegundaCamada(entradaSegundaCamada);
			//long value = Math.round(y);
			//System.out.println(value);
		}
	}
	private void aprender(double[] conjuntoTreinamento, double[]valoresEsperados, double[] saidasPrimeiraCamada, double[] saidasSegundaCamada) {
		retropropagarErroPelaSegundaCamada(valoresEsperados, saidasPrimeiraCamada, saidasSegundaCamada);
		retropropagarErroPelaPrimeiraCamada(conjuntoTreinamento, valoresEsperados, saidasPrimeiraCamada, saidasSegundaCamada);
		conexoesSegundaCamada = clonarArrays(conexoesSegundaCamadaNovo);
		conexoesSegundaCamadaNovo = new double[nrNeuroniosEntrada][nrNeuroniosPrimeiraCamada + 1];
	}

	private double[] propagarSinalPelaPrimeiraCamada(double[] conjuntoTreinamento) {
		double[] saidasPrimeiraCamada = getSaidaTreinamentoPrimeiraCamada(conjuntoTreinamento);
		return getEntradasSegundaCamada(saidasPrimeiraCamada);
	}

	private double[] propagarSinalPelaSegundaCamada(double[] entradaSegundaCamada) {
		double[] saidasSegundaCamada = new double[nrNeuroniosPrimeiraCamada];
		for (int i = 0; i < Configuracao.NUMERO_NEURONIOS_CAMADA_INTERMEDIARIA; i++) {
			double uh = 0;
			for (int j = 0; j < entradaSegundaCamada.length; j++) {
				uh += entradaSegundaCamada[j] * conexoesSegundaCamada[i][j];
			}
			saidasSegundaCamada[i] = getFuncaoTransferencia(uh);
		}
		return saidasSegundaCamada;
	}

	private double[] getEntradasSegundaCamada(double[] saidasPrimeiraCamada) {
		double[] entradaSegundaCamada = Arrays.copyOf(saidasPrimeiraCamada, saidasPrimeiraCamada.length + 1);
		entradaSegundaCamada[entradaSegundaCamada.length - 1] = Configuracao.BIAS;
		return entradaSegundaCamada;
	}

	private double[] getSaidaTreinamentoPrimeiraCamada(double[] conjuntoTreinamento) {
		double[] saidasPrimeiraCamada = new double[nrNeuroniosPrimeiraCamada];
		for (int i = 0; i < Configuracao.NUMERO_NEURONIOS_CAMADA_ENTRADA; i++) {
			double uh = 0;
			for (int j = 0; j < conjuntoTreinamento.length; j++) {
				uh += conjuntoTreinamento[j] * conexoesPrimeiraCamada[i][j];
			}
			saidasPrimeiraCamada[i] = getFuncaoTransferencia(uh);
		}
		return saidasPrimeiraCamada;
	}

	private double[] getSaidaClassificacaoPrimeiraCamada(double[] entrada) {
		double[] saidasPrimeiraCamada = new double[nrNeuroniosPrimeiraCamada];
		for (int j = 0; j < conexoesPrimeiraCamada.length; j++) {
			double u = 0;
			for (int k = 0; k < conexoesPrimeiraCamada[j].length; k++) {
				u += entrada[k] * conexoesPrimeiraCamada[j][k];
			}
			saidasPrimeiraCamada[j] = getFuncaoTransferencia(u);
		}
		return saidasPrimeiraCamada;
	}
	
	private void retropropagarErroPelaPrimeiraCamada(double[]x, double[] y, double[] h, double[] g) {
		double gradiente[] = new double[g.length];
		double w[][] = conexoesSegundaCamada;
		for (int i = 0; i < g.length; i++) {
			double primeiraParte = 0;
			for(int k = 0; k < g.length; k++){
				primeiraParte += (-(y[k] - g[k]) * (g[k] * (1 - g[k])) * w[k][i]);
			}
			for(int j = 0; j < conexoesPrimeiraCamada[i].length; j++){
				gradiente[i] = primeiraParte * (h[i] * (1 - h[i])) * x[j];
				conexoesPrimeiraCamada[i][j] = conexoesPrimeiraCamada[i][j] - (Configuracao.TAXA_APRENDIZADO * gradiente[i]);
			}
		}
	}

	private void retropropagarErroPelaSegundaCamada(double[] y, double[] h, double[] g) {
		double w[][] = conexoesSegundaCamadaNovo;
		double gradiente[] = new double[w[0].length];
		for (int i = 0; i < g.length; i++) {
			for(int j = 0; j < w[i].length; j++){
				gradiente[i] = -(y[i] - g[i]) * g[i] * (1 - g[i]) * h[j];
				w[i][j] = conexoesSegundaCamada[i][j] - (Configuracao.TAXA_APRENDIZADO * gradiente[i]);
			}
		}
	}

	private double getPrimeiraParcela(double valorEsperado, double valorSaida) {
		return -(valorEsperado - valorSaida);
	}

	private double getFuncaoTransferencia(double u) {
		return 1.0 / (1.0 + Math.exp(-u));
	}

	private void inicializarConexoesSinapticasDaRede() {
		inicializarConexoesDaPrimeiraCamada();
		inicializarConexoesDaSegundaCamada();
	}

	private void inicializarConexoesDaPrimeiraCamada() {
		conexoesPrimeiraCamada = new double[nrNeuroniosEntrada][nrNeuroniosPrimeiraCamada + 1];
/*		for (int i = 0; i < conexoesPrimeiraCamada.length; i++) {
			for (int j = 0; j < conexoesPrimeiraCamada[i].length; j++) {
				conexoesPrimeiraCamada[i][j] = Math.random();
			}
		}*/
/*		conexoesPrimeiraCamada[0][0] = 0.000438568;
		conexoesPrimeiraCamada[0][1] = 0.19956143;
		conexoesPrimeiraCamada[0][2] = 0.35;
		conexoesPrimeiraCamada[1][0] = 0.24975114;
		conexoesPrimeiraCamada[1][1] = 0.29950229;
		conexoesPrimeiraCamada[1][2] = 0.35;*/
		conexoesPrimeiraCamada[0][0] = 0.15;
		conexoesPrimeiraCamada[0][1] = 0.2;
		conexoesPrimeiraCamada[0][2] = 0.35;
		conexoesPrimeiraCamada[1][0] = 0.25;
		conexoesPrimeiraCamada[1][1] = 0.3;
		conexoesPrimeiraCamada[1][2] = 0.35;
	}

	private void inicializarConexoesDaSegundaCamada() {
		conexoesSegundaCamada = new double[nrNeuroniosEntrada][nrNeuroniosPrimeiraCamada + 1];
		conexoesSegundaCamadaNovo = new double[nrNeuroniosEntrada][nrNeuroniosPrimeiraCamada + 1];
/*		for (int i = 0; i < conexoesSegundaCamada.length; i++) {
			conexoesSegundaCamada[i] = Math.random();
		}*/
/*		conexoesSegundaCamada[0][0] = 0.35891648;
		conexoesSegundaCamada[0][1] = 0.408666186;
		conexoesSegundaCamada[0][2] = 0.6;
		conexoesSegundaCamada[1][0] = 0.511301270;
		conexoesSegundaCamada[1][1] = 0.561370121;
		conexoesSegundaCamada[1][2] = 0.6;*/
		conexoesSegundaCamada[0][0] = 0.4;
		conexoesSegundaCamada[0][1] = 0.45;
		conexoesSegundaCamada[0][2] = 0.6;
		conexoesSegundaCamada[1][0] = 0.5;
		conexoesSegundaCamada[1][1] = 0.55;
		conexoesSegundaCamada[1][2] = 0.6;
		
		conexoesSegundaCamadaNovo = clonarArrays(conexoesSegundaCamada);
	}
	
	private double[][] clonarArrays(double[][] array){
		double[][] arrayClonado = new double[array.length][array[0].length];
		
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[i].length; j++) {
				arrayClonado[i][j] = new Double(array[i][j]).doubleValue();
			}
		}
		return arrayClonado;
	}
	
	public void imprimirValoresConexoes() {
		System.out.println("\n Conexoes da primeira camada:");
		for (int i = 0; i < conexoesPrimeiraCamada.length; i++) {
			for (int j = 0; j < conexoesPrimeiraCamada[i].length; j++) {
				System.out.println(conexoesPrimeiraCamada[i][j] + " ");
			}
			System.out.println("\n");
		}

		System.out.println("\n Conexoes da segunda camada:");
		for (int i = 0; i < conexoesSegundaCamada.length; i++) {
			System.out.println(conexoesSegundaCamada[i] + " ");
		}

		System.out.println("\n\n");
	}

	public double[][] getConexoesPrimeiraCamada() {
		return conexoesPrimeiraCamada;
	}

	public void setConexoesPrimeiraCamada(double[][] conexoesPrimeiraCamada) {
		this.conexoesPrimeiraCamada = conexoesPrimeiraCamada;
	}

	private double arredondar(double valor){
		BigDecimal bd = new BigDecimal(valor).setScale(9, RoundingMode.HALF_EVEN);
		return bd.doubleValue();
	}
	
	private double calcularErro(double[] valoresEsperados, double[] valoresSaidas) {
		Double erro = 0.0;
		for (int i = 0; i < valoresEsperados.length; i++) {
			erro += (0.5 * Math.pow((valoresEsperados[i] - valoresSaidas[i]), 2));
		}
		return erro;
	}
}