package com.bcopstein.ExercicioRefatoracaoBanco;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TelaEstatistica {
	private static TelaEstatistica instance;
	private Stage mainStage;
	private Scene cenaEstatisticas;

	private Label saldo;
	private Label quantidade;
	private TextField txtMes;
	private TextField txtAno;
	private Controle controle;

	private TelaEstatistica() {
		controle = Controle.instance();
	}

	public static TelaEstatistica instance() {
		return instance != null ? instance : (instance = new TelaEstatistica());
	}

	public void setMainStage(Stage stage) {
		mainStage = stage;
	}

	public Scene getTelaEstatisticas() {
		ContaDAO conta = controle.getConta();
		GridPane layout = new GridPane();
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPadding(new Insets(10, 10, 10, 10));

		Button btnVoltar = new Button("Voltar");
		btnVoltar.setOnAction(e -> {
			mainStage.setScene(TelaOperacoes.instance().getTelaOperacoes());
		});

		GridPane container = new GridPane();
		container.setHgap(10);
		container.setVgap(10);

		ListView<Operacao> lista = new ListView<>(FXCollections.observableArrayList(conta.getOperacoes()));
		lista.setPrefWidth(320);

		Button btnSaldoMedio = new Button("Calcular Saldo Médio");
		btnSaldoMedio.setOnAction(e -> saldoMedio());
		Button btnCreditos = new Button("Calcular Créditos");
		btnCreditos.setOnAction(e -> creditoTotal());
		Button btnDebitos = new Button("Calcular Débitos");
		btnDebitos.setOnAction(e -> debitoTotal());

		Label lblSaldo = new Label("Saldo: ");
		saldo = new Label("");
		Label lblQuantidade = new Label("Quantidade: ");
		quantidade = new Label("");

		Label lblMes = new Label("Mês: ");
		txtMes = new TextField();
		txtMes.setPrefWidth(50);

		Label lblAno = new Label("Ano: ");
		txtAno = new TextField();
		txtAno.setPrefWidth(50);

		HBox box1 = new HBox(10, lblMes, txtMes, lblAno, txtAno);
		HBox box2 = new HBox(10, lblSaldo, saldo);
		HBox box3 = new HBox(10, lblQuantidade, quantidade);

		container.add(box1, 0, 0);
		container.add(box2, 0, 1);
		container.add(box3, 0, 2);
		container.add(btnSaldoMedio, 0, 3);
		container.add(btnCreditos, 0, 4);
		container.add(btnDebitos, 0, 5);
		container.add(btnVoltar, 0, 6);

		layout.add(lista, 0, 0);
		layout.add(container, 1, 0);

		cenaEstatisticas = new Scene(layout/* , WIDTH,HEIGTH */);
		return cenaEstatisticas;
	}

	/**
	 * Insere o resultado do calculo do saldo médio do usuário no label 'saldo'
	 *
	 */
	private void saldoMedio() {
		if (validaCampos()) {
			int mes = Integer.parseInt(txtMes.getText());
			int ano = Integer.parseInt(txtAno.getText());
			saldo.setText(String.valueOf(controle.calculaSaldoMedioMes(mes, ano)));
		} else
			mostrarErro("Erro ao calcular o saldo médio!");
	}

	/**
	 * Insere o resultado do calculo do crédito total do usuário no label 'saldo'
	 *
	 */
	private void creditoTotal() {
		if (validaCampos()) {
			int mes = Integer.parseInt(txtMes.getText());
			int ano = Integer.parseInt(txtAno.getText());
			saldo.setText(String.valueOf(controle.calculaCreditoTotal(mes, ano)));
			qtdCreditos();
		} else
			mostrarErro("Erro ao calcular o Crédito Total!");
	}

	private void qtdCreditos() {
		if (validaCampos()) {
			int mes = Integer.parseInt(txtMes.getText());
			int ano = Integer.parseInt(txtAno.getText());
			quantidade.setText(String.valueOf(controle.quantidadeCredito(mes, ano)));
		}
	}

	private void qtdDebitos() {
		if (validaCampos()) {
			int mes = Integer.parseInt(txtMes.getText());
			int ano = Integer.parseInt(txtAno.getText());
			quantidade.setText(String.valueOf(controle.quantidadeDebito(mes, ano)));
		}
	}

	/**
	 * Insere o resultado do calculo do bébito total do usuário no label 'saldo'
	 *
	 */
	private void debitoTotal() {
		if (validaCampos()) {
			int mes = Integer.parseInt(txtMes.getText());
			int ano = Integer.parseInt(txtAno.getText());
			saldo.setText(String.valueOf(controle.calculaDebitoTotal(mes, ano)));
			qtdDebitos();
		} else
			mostrarErro("Erro ao calcular o Débito Total!");
	}

	private void mostrarErro(String erro) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(erro);
		alert.setHeaderText(null);
		alert.setContentText("Campos 'Mês' ou 'Ano' preenchidos incorretamente!");
		alert.showAndWait();
	}

	/**
	 * Valida os campos de ano e mês, para garantir que são números
	 *
	 * @return Valor booleano verdadeiro se os campos estiverem válidos
	 */
	private boolean validaCampos() {
		return controle.validaNumero(txtAno.getText()) && controle.validaNumero(txtMes.getText());
	}

}
