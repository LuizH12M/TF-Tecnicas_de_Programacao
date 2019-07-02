package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.ArrayList;
import java.util.List;

public class Controle {
	private static Controle instance;
	private Persistencia persistencia;
	private GerenciadorContas gerenciadorContas;
	private GerenciadorOperacoes gerenciadorOperacoes;
	private List<Listener> listeners;

	private Controle() {
		persistencia = Persistencia.instance();
		persistencia.loadOperacoes();
		persistencia.loadContas();
		gerenciadorContas = GerenciadorContas.instance();
		gerenciadorOperacoes = GerenciadorOperacoes.instance();
		listeners = new ArrayList<>();
	}

	public static Controle instance() {
		return instance != null ? instance : (instance = new Controle());
	}

	public void salvar() {
		persistencia.saveContas(gerenciadorContas.getContas());
		persistencia.saveOperacoes(gerenciadorOperacoes.getOperacoes());
	}

	public boolean login(int nroConta) {
		boolean login = gerenciadorContas.selecionarConta(nroConta);
		return login;
	}

	public ContaDAO getConta() {
		return new ContaDAO(gerenciadorContas.getContaAtual(),
				gerenciadorOperacoes.getOperacoesDaConta(gerenciadorContas.getContaAtual().getNumero()));
	}

	public double getSaldoConta() {
		return gerenciadorContas.getContaAtual().getSaldo();
	}

	public int getStatusConta() {
		return gerenciadorContas.getContaAtual().getStatus();
	}

	public String getStrStatusConta() {
		return gerenciadorContas.getContaAtual().getStrStatus();
	}

	public void retirar(double valor) {
		gerenciadorContas.getContaAtual().retirada(valor);
		gerenciadorOperacoes.addOperacao(Operacao.debito(valor));
	}

	public void depositar(double valor) {
		gerenciadorContas.getContaAtual().deposito(valor);
		gerenciadorOperacoes.addOperacao(Operacao.credito(valor));
	}

	public boolean excedeLimiteDiario(double valor) {
		return Validacao.excedeLimiteDiario(valor);
	}

	public boolean retiradaInvalida(double valor) {
		return Validacao.retiradaInvalida(valor);
	}

	public boolean creditoInvalido(double valor) {
		return Validacao.creditoInvalido(valor);
	}

	public boolean validaNumero(String texto) {
		return Validacao.validaNumero(texto);
	}

	public List<Operacao> getOperacoesConta() {
		return gerenciadorOperacoes.getOperacoesDaConta(gerenciadorContas.getContaAtual().getNumero());
	}

	public double calculaSaldoMedioMes(int mes, int ano) {
		int numConta = gerenciadorContas.getContaAtual().getNumero();
		return gerenciadorOperacoes.calculaSaldoMedioMes(mes, ano, numConta);
	}

	public double calculaCreditoTotal(int mes, int ano) {
		int numConta = gerenciadorContas.getContaAtual().getNumero();
		return gerenciadorOperacoes.calculaCreditoTotal(mes, ano, numConta);
	}

	public long quantidadeCredito(int mes, int ano) {

		int numConta = gerenciadorContas.getContaAtual().getNumero();
		long qtdCredito = gerenciadorOperacoes.qtdCredito(mes, ano, numConta);
		return qtdCredito;
	}

	public long quantidadeDebito(int mes, int ano) {

		int numConta = gerenciadorContas.getContaAtual().getNumero();
		long qtdDebito = gerenciadorOperacoes.qtdDebito(mes, ano, numConta);
		return qtdDebito;
	}

	public double calculaDebitoTotal(int mes, int ano) {
		int numConta = gerenciadorContas.getContaAtual().getNumero();
		return gerenciadorOperacoes.calculaDebitoTotal(mes, ano, numConta);
	}

	public void registerListener(Listener l) {
		listeners.add(l);
	}

	public void unregisterListener(Listener l) {
		listeners.remove(l);
	}

	public void notify(Evento e) {
		listeners.forEach(l -> l.changed(e));
	}

	public ContaDAO getContaMaiorSaldoMedioMes() {
		return new ContaDAO(gerenciadorOperacoes.getContaMaiorSaldoMedioMes(), new ArrayList<Operacao>());
	}
}