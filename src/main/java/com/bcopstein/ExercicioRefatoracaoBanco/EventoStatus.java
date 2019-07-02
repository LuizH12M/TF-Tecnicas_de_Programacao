package com.bcopstein.ExercicioRefatoracaoBanco;

public class EventoStatus implements Evento {

	private String novoStatus;

	public EventoStatus(String novoStatus) {
		this.novoStatus = novoStatus;
	}

	public String getStatus() {
		return novoStatus;
	}
}
