package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.Calendar;

public class Validacao {
	private static Controle controle = Controle.instance();

	private Validacao() {
	}

	// Apenas para testes
	public static void setControle(Controle controle) {
		Validacao.controle = controle;
	}

	/**
	 * Verifica se o usuário execeu seu limite diário de saque
	 *
	 * @param valor a ser sacado
	 * @return True se execedeu, False caso contrário
	 */
	public static boolean excedeLimiteDiario(double valor) {
		ContaDAO conta = controle.getConta();
		Calendar date = Calendar.getInstance();
		return conta.getOperacoes().stream()
				.filter(op -> op.getAno() == date.get(Calendar.YEAR) && op.getMes() == date.get(Calendar.MONTH) + 1
						&& op.getDia() == date.get(Calendar.DAY_OF_MONTH)
						&& op.getTipoOperacao() == TipoOperacao.DEBITO)
				.mapToDouble(op -> op.getValorOperacao()).sum() + valor > conta.getLimRetiradaDiaria();
	}

	/**
	 * Verifica se o valor a ser retirado é valído
	 *
	 * @param valor a ser retirado
	 * @return True se o valor for menor que o saldo atual e positivo, False caso
	 *         contrário
	 */
	public static boolean retiradaInvalida(double valor) {
		return valor < 0 || valor > controle.getSaldoConta();
	}

	/**
	 * Verifica se o valor a ser creditado é válido
	 *
	 * @param valor a ser creditado
	 * @return True se for positivo, False caso contrário
	 */
	public static boolean creditoInvalido(double valor) {
		return valor < 0;
	}

	public static boolean validaNumero(String text) {
		return text.matches("[0-9]+");
	}

}
