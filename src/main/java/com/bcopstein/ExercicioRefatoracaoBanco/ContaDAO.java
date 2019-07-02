package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.Collection;

public class ContaDAO {

	private Collection<Operacao> operacoes;
	private int numero;
	private String correntista;
	private double limRetiradaDiaria;
	private double saldo;
	private String status;

	public ContaDAO(Conta conta, Collection<Operacao> operacoes) {
		this.operacoes = operacoes;
		numero = conta.getNumero();
		correntista = conta.getCorrentista();
		limRetiradaDiaria = conta.getLimRetiradaDiaria();
		saldo = conta.getSaldo();
		status = conta.getStrStatus();
	}

	public int getNumero() {
		return numero;
	}

	public String getCorrentista() {
		return correntista;
	}

	public double getLimRetiradaDiaria() {
		return limRetiradaDiaria;
	}

	public Collection<Operacao> getOperacoes() {
		return operacoes;
	}

	public double getSaldo() {
		return saldo;
	}

	public String getStatus() {
		return status;
	}

}
