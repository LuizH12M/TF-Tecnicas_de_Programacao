package com.bcopstein.ExercicioRefatoracaoBanco;

public class EventoSaldo implements Evento {

	private double novoSaldo;

	public EventoSaldo(double novoSaldo) {
		this.novoSaldo = novoSaldo;
	}

	public double getSaldo() {
		return novoSaldo;
	}
}
