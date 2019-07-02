package com.bcopstein.ExercicioRefatoracaoBanco;

public interface ITipoConta {
	public String getStrStatus();

	public int getStatus();

	public double getLimRetiradaDiaria();

	public double calculaCredito(double valor);
}
