import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bcopstein.ExercicioRefatoracaoBanco.Conta;
import com.bcopstein.ExercicioRefatoracaoBanco.GerenciadorContas;
import com.bcopstein.ExercicioRefatoracaoBanco.GerenciadorOperacoes;
import com.bcopstein.ExercicioRefatoracaoBanco.Operacao;
import com.bcopstein.ExercicioRefatoracaoBanco.Persistencia;
import com.bcopstein.ExercicioRefatoracaoBanco.TipoOperacao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class GerenciadorOperacoesTest {
	private static GerenciadorOperacoes manager;
	private static Conta conta;
	private static final int MES = 11;
	private static final int ANO = 2018;
	private static final double PRECISION = 0.001;

	@BeforeAll
	public static void prepara() {
		conta = new Conta(1, "Teste", 600, 1);
		Map<Integer, Conta> contas = new HashMap<>();
		contas.put(1, conta);
		List<Operacao> operacoes = new LinkedList<>();
		operacoes.add(new Operacao(1, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.CREDITO));
		operacoes.add(new Operacao(2, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.CREDITO));
		operacoes.add(new Operacao(3, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.DEBITO));
		operacoes.add(new Operacao(3, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.DEBITO));
		operacoes.add(new Operacao(4, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.CREDITO));
		operacoes.add(new Operacao(5, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.CREDITO));
		operacoes.add(new Operacao(6, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.CREDITO));
		operacoes.add(new Operacao(6, MES, ANO, 12, 31, 10, 1, 1, 100, TipoOperacao.CREDITO));

		Persistencia mockPersistencia = mock(Persistencia.class);
		GerenciadorContas mockGerenciadorContas = mock(GerenciadorContas.class);

		when(mockPersistencia.loadOperacoes()).thenReturn(operacoes);
		when(mockPersistencia.loadContas()).thenReturn(contas);
		when(mockGerenciadorContas.getContaAtual()).thenReturn(conta);
		manager = new GerenciadorOperacoes(mockPersistencia);
	}

	@Test
	public void calculaTotalCredito() {
		double totalCredito = manager.calculaCreditoTotal(MES, ANO, conta.getNumero());
		assertEquals(600, totalCredito, PRECISION);
	}

	@Test
	public void calculaTotalDebito() {
		double totalCredito = manager.calculaDebitoTotal(MES, ANO, conta.getNumero());
		assertEquals(200, totalCredito, PRECISION);
	}

	@Test
	public void calculaSaldoMedioMes() {
		double totalCredito = manager.calculaSaldoMedioMes(MES, ANO, conta.getNumero());
		assertEquals(353.333, totalCredito, PRECISION);
	}
}