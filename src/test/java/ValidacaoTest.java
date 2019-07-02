import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import com.bcopstein.ExercicioRefatoracaoBanco.Conta;
import com.bcopstein.ExercicioRefatoracaoBanco.ContaDAO;
import com.bcopstein.ExercicioRefatoracaoBanco.Controle;
import com.bcopstein.ExercicioRefatoracaoBanco.Operacao;
import com.bcopstein.ExercicioRefatoracaoBanco.Persistencia;
import com.bcopstein.ExercicioRefatoracaoBanco.TipoOperacao;
import com.bcopstein.ExercicioRefatoracaoBanco.Validacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class ValidacaoTest {
	private static final double SALDO = 10000;
	Conta conta = new Conta(1, "Teste");

	Controle mockControle = mock(Controle.class);

	@BeforeEach
	public void prepara() {
		conta.deposito(SALDO);
		List<Operacao> operacoes = new LinkedList<>();
		operacoes.add(new Operacao(1, 1, 2018, 12, 31, 10, 1, 1, SALDO, TipoOperacao.CREDITO));

		Persistencia mockPersistencia = mock(Persistencia.class);
		when(mockPersistencia.loadOperacoes()).thenReturn(operacoes);

		when(mockControle.getSaldoConta()).thenReturn(conta.getSaldo());
		when(mockControle.getConta()).thenReturn(new ContaDAO(conta, operacoes));
		Validacao.setControle(mockControle);
	}

	@ParameterizedTest
	@CsvSource({ "-500,true", "10,false", "0,false" })
	public void retiradaInvalida(double retirada, boolean esperado) {
		boolean resultado = Validacao.retiradaInvalida(retirada);
		assertThat(resultado, is(esperado));
	}

	@ParameterizedTest
	@CsvSource({ "10000,false", "10001,true", "9999,false" })
	public void excedeLimiteDiario(double retirada, boolean esperado) {
		boolean resultado = Validacao.excedeLimiteDiario(retirada);
		assertThat(resultado, is(esperado));
	}

	@ParameterizedTest
	@CsvSource({ "0,false", "-1,true", "1,false" })
	public void creditoInvalido(double retirada, boolean esperado) {
		boolean resultado = Validacao.creditoInvalido(retirada);
		assertThat(resultado, is(esperado));
	}

	@ParameterizedTest
	@CsvSource({ "1001,true", "1002l,false", "q1000,false", "'', false" })
	public void validaNumero(String numero, boolean esperado) {
		boolean resultado = Validacao.validaNumero(numero);
		assertThat(resultado, is(esperado));
	}
}
