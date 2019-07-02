package com.bcopstein.ExercicioRefatoracaoBanco;

public class EventoMaiorSaldo implements Evento {

	private String nome;
	private double saldo;

	public EventoMaiorSaldo(String nome, double saldo) {
		this.nome = nome;
		this.saldo = saldo;
	}

	public String getNome() {
		return nome;
	}

	public double getSaldo() {
		return saldo;
	}

}
