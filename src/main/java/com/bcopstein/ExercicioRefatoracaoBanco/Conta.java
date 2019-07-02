package com.bcopstein.ExercicioRefatoracaoBanco;

public class Conta {
	public final int SILVER = 0;
	public final int GOLD = 1;
	public final int PLATINUM = 2;
	public final int LIM_SILVER_GOLD = 50000;
	public final int LIM_GOLD_PLATINUM = 200000;
	public final int LIM_PLATINUM_GOLD = 100000;
	public final int LIM_GOLD_SILVER = 25000;

	private int numero;
	private String correntista;
	// @ invariant 0 <= saldo;
	private double saldo;
	private ITipoConta status;

	public Conta(int umNumero, String umNome) {
		numero = umNumero;
		correntista = umNome;
		saldo = 0.0;
		status = FactoryStateConta.instance().getTipoConta(SILVER);
	}

	public Conta(int umNumero, String umNome, double umSaldo, int umStatus) {
		numero = umNumero;
		correntista = umNome;
		saldo = umSaldo;
		status = FactoryStateConta.instance().getTipoConta(umStatus);
	}

	/* @ pure @ */
	public double getSaldo() {
		return saldo;
	}

	/* @ pure @ */
	// @ ensures \result == \old(getNumero());
	public Integer getNumero() {
		return numero;
	}

	/* @ pure @ */
	// @ ensures \result == \old(getCorrentista());
	public String getCorrentista() {
		return correntista;
	}

	/* @ pure @ */
	public int getStatus() {
		return status.getStatus();
	}

	/* @ pure @ */
	public String getStrStatus() {
		return status.getStrStatus();
	}

	/* @ pure @ */
	public double getLimRetiradaDiaria() {
		return status.getLimRetiradaDiaria();
	}

	/*
	 * @ requires status.getStatus() == SILVER; ensures saldo == \old(saldo)+valor;
	 * also requires status.getStatus() == GOLD; ensures saldo == \old(saldo)+(valor
	 * * 1.01); also requires status.getStatus() == PLATINUM; ensures saldo ==
	 * \old(saldo)+(valor * 1.025); also requires status.getStatus() == SILVER &&
	 * (saldo+valor) >= LIM_SILVER_GOLD; ensures saldo == \old(saldo)+valor &&
	 * status.getStatus() == GOLD; also requires status.getStatus() == GOLD &&
	 * (saldo+(valor * 1.01)) >= LIM_GOLD_PLATINUM; ensures saldo ==
	 * \old(saldo)+(valor * 1.01) && status.getStatus() == PLATINUM;
	 * 
	 * @
	 */
	public void deposito(double valor) {
		saldo += status.calculaCredito(valor);
		Controle.instance().notify(new EventoSaldo(saldo));
		if (status.getStatus() == SILVER) {
			if (saldo >= LIM_SILVER_GOLD) {
				status = FactoryStateConta.instance().getTipoConta(GOLD);
				Controle.instance().notify(new EventoStatus(status.toString()));
			}
		} else if (status.getStatus() == GOLD)
			if (saldo >= LIM_GOLD_PLATINUM) {
				status = FactoryStateConta.instance().getTipoConta(PLATINUM);
				Controle.instance().notify(new EventoStatus(status.toString()));
			}

	}

	/*
	 * @ requires (saldo - valor >= 0) && status.getStatus() == GOLD && saldo-valor
	 * < LIM_GOLD_SILVER; ensures saldo == \old(saldo)-valor && status.getStatus()
	 * == SILVER; also requires (saldo - valor >= 0) && status.getStatus() ==
	 * PLATINUM && saldo-valor < LIM_PLATINUM_GOLD; ensures saldo ==
	 * \old(saldo)-valor && status.getStatus() == GOLD; also requires saldo - valor
	 * >= 0 ensures saldo = \old(saldo)-valor;
	 * 
	 * @
	 */
	public void retirada(double valor) {
		if (saldo - valor < 0.0)
			return;
		else {
			saldo = saldo - valor;
			Controle.instance().notify(new EventoSaldo(saldo));
			if (status.getStatus() == PLATINUM) {
				if (saldo < LIM_PLATINUM_GOLD) {
					status = FactoryStateConta.instance().getTipoConta(GOLD);
					Controle.instance().notify(new EventoStatus(status.toString()));
				}
			} else if (status.getStatus() == GOLD)
				if (saldo < LIM_GOLD_SILVER) {
					status = FactoryStateConta.instance().getTipoConta(SILVER);
					Controle.instance().notify(new EventoStatus(status.toString()));
				}
		}
	}

	/*
	 * @ also ensures \result.equals(Conta [numero=" + getNumero() + ",
	 * correntista=" + getCorrentista() + ", saldo=" + getSaldo() + ",
	 * status="	+ getStrStatus() + "]");
	 * 
	 * @
	 */
	@Override
	public String toString() {
		return "Conta [numero=" + numero + ", correntista=" + correntista + ", saldo=" + saldo + ", status="
				+ getStrStatus() + "]";
	}
}
