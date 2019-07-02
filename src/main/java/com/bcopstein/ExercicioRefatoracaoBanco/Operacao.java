package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.Calendar;

public class Operacao {
	private int dia;
	private int mes;
	private int ano;
	private int hora;
	private int minuto;
	private int segundo;
	private int numeroConta;
	private int statusConta;
	private double valorOperacao;
	private TipoOperacao tipoOperacao;

	public Operacao(int dia, int mes, int ano, int hora, int minuto, int segundo, int numeroConta, int statusConta,
			double valorOperacao, TipoOperacao tipoOperacao) {
		super();
		this.dia = dia;
		this.mes = mes;
		this.ano = ano;
		this.hora = hora;
		this.minuto = minuto;
		this.segundo = segundo;
		this.numeroConta = numeroConta;
		this.statusConta = statusConta;
		this.valorOperacao = valorOperacao;
		this.tipoOperacao = tipoOperacao;
	}

	public static Operacao credito(double valor) {
		Calendar date = Calendar.getInstance();
		Operacao op = new Operacao(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.YEAR), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
				date.get(Calendar.SECOND), GerenciadorContas.instance().getContaAtual().getNumero(),
				GerenciadorContas.instance().getContaAtual().getStatus(), valor, TipoOperacao.CREDITO);
		return op;
	}

	public static Operacao debito(double valor) {
		Calendar date = Calendar.getInstance();
		Operacao op = new Operacao(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1,
				date.get(Calendar.YEAR), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
				date.get(Calendar.SECOND), GerenciadorContas.instance().getContaAtual().getNumero(),
				GerenciadorContas.instance().getContaAtual().getStatus(), valor, TipoOperacao.DEBITO);
		return op;
	}

	public int getDia() {
		return dia;
	}

	public int getMes() {
		return mes;
	}

	public int getAno() {
		return ano;
	}

	public int getHora() {
		return hora;
	}

	public int getMinuto() {
		return minuto;
	}

	public int getSegundo() {
		return segundo;
	}

	public int getNumeroConta() {
		return numeroConta;
	}

	public int getStatusConta() {
		return statusConta;
	}

	public double getValorOperacao() {
		return valorOperacao;
	}

	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	@Override
	public String toString() {
		String tipo = "<C>";
		if (tipoOperacao == TipoOperacao.DEBITO)
			tipo = "<D>";
		String line = dia + "/" + mes + "/" + ano + " " + hora + ":" + minuto + ":" + segundo + " " + numeroConta + " "
				+ statusConta + " " + tipo + " " + valorOperacao;
		return line;
	}
}
