package com.bcopstein.ExercicioRefatoracaoBanco;

public class FactoryStateConta {
	private static FactoryStateConta instance;

	private FactoryStateConta() {

	}

	public static FactoryStateConta instance() {
		return instance != null ? instance : (instance = new FactoryStateConta());
	}

	public ITipoConta getTipoConta(int tipo) {
		switch (tipo) {
		case 1:
			return new ContaGold();
		case 2:
			return new ContaPlatinum();
		default:
			return new ContaSilver();
		}
	}

	/* Classes para Status Conta */

	public class ContaSilver implements ITipoConta {
		private final int STATUS = 0;
		private final double LIMITE_DIARIO = 10_000;

		@Override
		public String getStrStatus() {
			return "Silver";
		}

		@Override
		public int getStatus() {
			return STATUS;
		}

		@Override
		public double getLimRetiradaDiaria() {
			return LIMITE_DIARIO;
		}

		@Override
		public double calculaCredito(double valor) {
			return valor;
		}
	}

	public class ContaGold implements ITipoConta {
		private final int STATUS = 1;
		private final double LIMITE_DIARIO = 100_000;

		@Override
		public String getStrStatus() {
			return "Gold";
		}

		@Override
		public int getStatus() {
			return STATUS;
		}

		@Override
		public double getLimRetiradaDiaria() {
			return LIMITE_DIARIO;
		}

		@Override
		public double calculaCredito(double valor) {
			return valor * 1.01;
		}
	}

	public class ContaPlatinum implements ITipoConta {
		private final int STATUS = 2;
		private final double LIMITE_DIARIO = 500_000;

		@Override
		public String getStrStatus() {
			return "Platinum";
		}

		@Override
		public int getStatus() {
			return STATUS;
		}

		@Override
		public double getLimRetiradaDiaria() {
			return LIMITE_DIARIO;
		}

		@Override
		public double calculaCredito(double valor) {
			return valor * 1.025;
		}
	}
}
