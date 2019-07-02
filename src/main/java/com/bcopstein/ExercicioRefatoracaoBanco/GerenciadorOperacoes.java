package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorOperacoes {
	private static GerenciadorOperacoes instance;
	private List<Operacao> operacoes;

	private GerenciadorOperacoes() {
		this(Persistencia.instance());
	}

	// Apenas para testes
	public GerenciadorOperacoes(Persistencia persistencia) {
		operacoes = persistencia.loadOperacoes();
	}

	public static GerenciadorOperacoes instance() {
		if (instance == null)
			instance = new GerenciadorOperacoes();
		return instance;
	}

	public List<Operacao> getOperacoesDaConta(int nroConta) {
		return operacoes.stream().filter(op -> op.getNumeroConta() == nroConta).collect(Collectors.toList());
	}

	public List<Operacao> getOperacoes() {
		return operacoes;
	}

	/**
	 * Calcula o valor total de créditos de um mês/ano específico
	 *
	 * @param mes
	 * @param ano
	 * @return total de crédito do mês/ano informados
	 */
	public double calculaCreditoTotal(int mes, int ano, int numConta) {
		return getOperacoesDaConta(numConta).stream()
				.filter(op -> op.getAno() == ano && op.getMes() == mes && op.getTipoOperacao() == TipoOperacao.CREDITO)
				.mapToDouble(op -> op.getValorOperacao()).sum();
	}

	public long qtdCredito(int mes, int ano, int numConta) {
		long count = getOperacoesDaConta(numConta).stream()
				.filter(op -> op.getAno() == ano && op.getMes() == mes && op.getTipoOperacao() == TipoOperacao.CREDITO)
				.count();

		return count;
	}

	public long qtdDebito(int mes, int ano, int numConta) {
		long count = getOperacoesDaConta(numConta).stream()
				.filter(op -> op.getAno() == ano && op.getMes() == mes && op.getTipoOperacao() == TipoOperacao.DEBITO)
				.count();

		return count;
	}

	/**
	 * Calcula o valor total de débito de um mês/ano específico
	 *
	 * @param mes
	 * @param ano
	 * @return total de débito do mês/ano informados
	 */
	public double calculaDebitoTotal(int mes, int ano, int numConta) {
		return getOperacoesDaConta(numConta).stream()
				.filter(op -> op.getAno() == ano && op.getMes() == mes && op.getTipoOperacao() == TipoOperacao.DEBITO).

				mapToDouble(op -> op.getValorOperacao()).sum();// Usar a classe enum para o tipo (ao invez de 0)!!!!

	}

	/**
	 * Calcula Saldo Médio do mês/ano informado
	 *
	 * @param mes
	 * @param ano
	 * @return saldo médio do mês/ano informado
	 */
	public double calculaSaldoMedioMes(int mes, int ano, int numConta) {
		double saldoAnterior = calculaSaldoAnterior(getOperacoesDaConta(numConta), mes, ano);
		double saldo = 0;
		double saldoDia;
		int dias = getDiasDoMes(mes);
		List<Operacao> opsMes = getOperacoesDaConta(numConta).stream()
				.filter((Operacao op) -> op.getMes() == mes && op.getAno() == ano).collect(Collectors.toList());
		List<Operacao> opsDias;
		for (int i = 1; i <= dias; i++) {
			int dia = i;
			opsDias = opsMes.stream().filter(op -> op.getDia() == dia).collect(Collectors.toList());
			saldoDia = saldoAnterior + calculaSaldo(opsDias);
			saldo += saldoDia;
			if (saldoDia != saldoAnterior)
				saldoAnterior = saldoDia;
		}
		saldo = saldo / dias;
		return saldo;
	}

	/**
	 * Calcula o saldo anterior até um ano/mês específico
	 *
	 * @param operacoes lista de operações
	 * @param mes
	 * @param ano
	 * @return Saldo anterior ao ano/mês informado
	 */
	private double calculaSaldoAnterior(Collection<Operacao> operacoes, int mes, int ano) {
		double saldo = 0;
		for (Operacao op : operacoes)
			if (op.getAno() <= ano) {
				if (op.getAno() == ano && op.getMes() >= mes)
					break;
				else if (op.getTipoOperacao() == TipoOperacao.DEBITO)
					saldo -= op.getValorOperacao();
				else
					saldo += op.getValorOperacao();
			} else
				break;
		return saldo;
	}

	/**
	 * Calcula o saldo de uma lista informada por parâmetro
	 *
	 * @param operacoes Lista de operações a ser calculada
	 * @return Saldo da lista informada
	 */
	private double calculaSaldo(List<Operacao> operacoes) {
		double saldo = 0;
		for (Operacao op : operacoes)
			if (op.getTipoOperacao() == TipoOperacao.DEBITO)
				saldo -= op.getValorOperacao();
			else
				saldo += op.getValorOperacao();
		return saldo;
	}

	/**
	 * Informa a quantidade de dias de um mes
	 *
	 * @param mes Mês que deseja saber a quantidade de dias
	 * @return quantidade de dias no mês informado
	 */
	private int getDiasDoMes(int mes) {
		Calendar data = new GregorianCalendar();
		data.set(Calendar.MONTH, mes - 1);
		return data.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Adiciona a operação recebida na lista de operações, que depois será salvada
	 * de volta no arquivo.
	 *
	 * @param op Operação a ser salva
	 */
	public void addOperacao(Operacao op) {
		operacoes.add(op);
		Conta conta = GerenciadorContas.instance().getContaAtual();
		Controle.instance().notify(new EventoOperacoes(getOperacoesDaConta(conta.getNumero())));
		Controle.instance().notify(new EventoMaiorSaldo(conta.getCorrentista(), conta.getSaldo()));
	}

	/**
	 * Encontra a conta com o maior saldo médio do mês
	 *
	 * @return Conta com o maior saldo médio do mês
	 */
	public Conta getContaMaiorSaldoMedioMes() {
		Iterator<Conta> it = GerenciadorContas.instance().getContas().iterator();
		if (!it.hasNext())
			return null;
		Conta conta = it.next();
		Calendar cal = Calendar.getInstance();
		while (it.hasNext()) {
			Conta c = it.next();
			if (calculaSaldoMedioMes(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),
					c.getNumero()) > calculaSaldoMedioMes(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),
							conta.getNumero()))
				conta = c;
		}
		return conta;
	}
}
