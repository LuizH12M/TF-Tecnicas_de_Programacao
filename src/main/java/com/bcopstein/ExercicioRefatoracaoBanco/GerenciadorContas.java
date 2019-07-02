package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.Collection;
import java.util.Map;

public class GerenciadorContas {
	private static GerenciadorContas instance;
	private Map<Integer, Conta> contas;
	private Conta contaAtual;

	private GerenciadorContas() {
		contas = Persistencia.instance().loadContas();
	}

	public static GerenciadorContas instance() {
		if (instance == null)
			instance = new GerenciadorContas();
		return instance;
	}

	public boolean selecionarConta(int nroConta) {
		Conta conta = contas.get(nroConta);
		if (conta == null)
			return false;
		contaAtual = conta;
		return true;
	}

	public Conta getContaAtual() {
		return contaAtual;
	}

	public Collection<Conta> getContas() {
		return contas.values();
	}
}
