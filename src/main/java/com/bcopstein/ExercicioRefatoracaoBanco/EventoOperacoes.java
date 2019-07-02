package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.List;

public class EventoOperacoes implements Evento {

	private List<Operacao> operacoes;

	public EventoOperacoes(List<Operacao> operacoes) {
		this.operacoes = operacoes;
	}

	public List<Operacao> getOperacoes() {
		return operacoes;
	}
}
