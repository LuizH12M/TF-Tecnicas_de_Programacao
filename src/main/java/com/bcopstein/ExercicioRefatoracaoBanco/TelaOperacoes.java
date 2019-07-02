package com.bcopstein.ExercicioRefatoracaoBanco;

import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TelaOperacoes implements Listener {
	private static TelaOperacoes instance;
	private Stage mainStage;
	private Scene cenaOperacoes;
	private TextField tfValorOperacao;
	private TextField tfSaldo;
	private Label lbCategoria;
	private Controle controle;
	private ListView<Operacao> extrato;

	private TelaOperacoes() {
		controle = Controle.instance();
	}

	public static TelaOperacoes instance() {
		return instance != null ? instance : (instance = new TelaOperacoes());
	}

	public void setMainStage(Stage stage) {
		mainStage = stage;
	}

	public Scene getTelaOperacoes() {
		controle.registerListener(this);
		ContaDAO conta = controle.getConta();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		String dadosCorr = conta.getNumero() + " : " + conta.getCorrentista();
		Text scenetitle = new Text(dadosCorr);
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		String limRetDiaria = "Limite de retirada diária: " + conta.getLimRetiradaDiaria();

		lbCategoria = new Label();
		grid.add(lbCategoria, 0, 1);

		Label lim = new Label(limRetDiaria);
		grid.add(lim, 0, 2);

		Label tit = new Label("Últimos movimentos:");
		grid.add(tit, 0, 3);

		extrato = new ListView<>(FXCollections.observableArrayList(conta.getOperacoes()));
		extrato.setPrefHeight(140);
		extrato.setPrefWidth(320);
		grid.add(extrato, 0, 4);

		tfSaldo = new TextField();
		tfSaldo.setDisable(true);
		HBox valSaldo = new HBox(20);
		valSaldo.setAlignment(Pos.BOTTOM_LEFT);
		valSaldo.getChildren().add(new Label("Saldo"));
		valSaldo.getChildren().add(tfSaldo);
		grid.add(valSaldo, 0, 5);

		tfValorOperacao = new TextField();
		HBox valOper = new HBox(30);
		valOper.setAlignment(Pos.BOTTOM_CENTER);
		valOper.getChildren().add(new Label("Valor da operação:"));
		valOper.getChildren().add(tfValorOperacao);
		grid.add(valOper, 1, 1);

		Button btnCredito = new Button("Crédito");
		Button btnDebito = new Button("Débito");
		Button btnVoltar = new Button("Voltar");
		Button btnEstatistica = new Button("Estatísticas");

		HBox hbBtn = new HBox(20);
		hbBtn.setAlignment(Pos.TOP_CENTER);
		hbBtn.getChildren().add(btnCredito);
		hbBtn.getChildren().add(btnDebito);
		hbBtn.getChildren().add(btnVoltar);
		hbBtn.getChildren().add(btnEstatistica);
		grid.add(hbBtn, 1, 2);

		btnCredito.setOnAction(e -> {
			try {
				double valor = Integer.parseInt(tfValorOperacao.getText());
				if (controle.creditoInvalido(valor))
					throw new NumberFormatException("Valor inválido!");
				controle.depositar(valor);
			} catch (NumberFormatException ex) {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Valor inválido!");
				alert.setHeaderText(null);
				alert.setContentText("Valor inválido para operação de crédito!");

				alert.showAndWait();

			}
		});

		btnDebito.setOnAction(e -> {
			try {
				double valor = Integer.parseInt(tfValorOperacao.getText());
				if (controle.retiradaInvalida(valor))
					throw new NumberFormatException("Saldo insuficiente!");
				if (controle.excedeLimiteDiario(valor))
					throw new RuntimeException("Limite diário atingido!");
				controle.retirar(valor);
			} catch (NumberFormatException ex) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Valor inválido!");
				alert.setHeaderText(null);
				alert.setContentText("Valor inválido para operação de débito!");

				alert.showAndWait();
			} catch (RuntimeException ex) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Erro!");
				alert.setHeaderText(null);
				alert.setContentText(ex.getMessage());

				alert.showAndWait();
			}
		});

		btnVoltar.setOnAction(e -> {
			mainStage.setScene(TelaEntrada.instance().getTelaEntrada());
			controle.unregisterListener(this);
		});

		btnEstatistica.setOnAction(e -> {
			TelaEstatistica telaEst = TelaEstatistica.instance();
			telaEst.setMainStage(mainStage);
			Scene tela = telaEst.getTelaEstatisticas();
			mainStage.setScene(tela);
			controle.unregisterListener(this);
		});

		cenaOperacoes = new Scene(grid);
		atualizarCategoria(conta.getStatus());
		atualizarSaldo(conta.getSaldo());
		atualizarExtrato(conta.getOperacoes());
		return cenaOperacoes;
	}

	private void atualizarCategoria(String status) {
		lbCategoria.setText("Categoria: " + status);
	}

	private void atualizarSaldo(double saldo) {
		tfSaldo.setText(String.valueOf(saldo));
	}

	private void atualizarExtrato(Collection<Operacao> collection) {
		extrato.setItems(FXCollections.observableArrayList(collection));
	}

	@Override
	public void changed(Evento e) {
		if (e instanceof EventoStatus)
			atualizarCategoria(((EventoStatus) e).getStatus());
		else if (e instanceof EventoSaldo)
			atualizarSaldo(((EventoSaldo) e).getSaldo());
		else if (e instanceof EventoOperacoes)
			atualizarExtrato(((EventoOperacoes) e).getOperacoes());

	}
}
