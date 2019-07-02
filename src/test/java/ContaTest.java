import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bcopstein.ExercicioRefatoracaoBanco.Conta;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class ContaTest {
	private static Conta conta;
	private final double PRECISION = 0.001;

	@ParameterizedTest
	@CsvSource({ "40000,0,5000,0", "45000,0,4999,0", "45000,0,5000,1", "150000,1,48000,1", "150000,1,50000,2" })
	public void depositoTrocaStatus(double saldo, int statusAnterior, double deposito, int novoStatus) {
		conta = new Conta(1, "Teste", saldo, statusAnterior);
		conta.deposito(deposito);
		assertThat(conta.getStatus(), is(novoStatus));
	}

	@ParameterizedTest
	@CsvSource({ "45000,0,5000,50000", "50000,1,5000,55050", "200000,2,5000,205125" })
	public void depositoValorAtual(double saldo, int status, double deposito, double saldoAtual) {
		conta = new Conta(1, "Teste", saldo, status);
		conta.deposito(deposito);
		assertEquals(conta.getSaldo(), saldoAtual, PRECISION);
	}

	@ParameterizedTest
	@CsvSource({ "40000,0,5000,0", "30000,1,5000,1", "30000,1,5001,0", "150000,2,50000,2", "150000,2,50001,1" })
	public void retiradaTrocaStatus(double saldo, int statusAnterior, double retirada, int novoStatus) {
		conta = new Conta(1, "Teste", saldo, statusAnterior);
		conta.retirada(retirada);
		assertThat(conta.getStatus(), is(novoStatus));
	}

	@ParameterizedTest
	@CsvSource({ "10000,0,5000,5000", "100000,1,50000,50000", "250000,2,50000,200000" })
	public void retiradaValorAtual(double saldo, int status, double retirada, double saldoAtual) {
		conta = new Conta(1, "Teste", saldo, status);
		conta.retirada(retirada);
		assertThat(conta.getSaldo(), is(saldoAtual));
	}
}
