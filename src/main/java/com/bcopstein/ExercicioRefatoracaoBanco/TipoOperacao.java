package com.bcopstein.ExercicioRefatoracaoBanco;

public enum TipoOperacao {
	CREDITO(0), DEBITO(1);

	private int iTipo;

	TipoOperacao(int iTipo) {
		this.iTipo = iTipo;
	}

	public int toInt() {
		return iTipo;
	}
}
