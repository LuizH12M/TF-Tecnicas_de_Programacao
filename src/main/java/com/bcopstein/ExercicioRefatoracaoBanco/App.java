package com.bcopstein.ExercicioRefatoracaoBanco;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

	private TelaEntrada telaEntrada;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("$$ Banco NOSSA GRANA $$");

		telaEntrada = TelaEntrada.instance();
		telaEntrada.setMainStage(primaryStage);
		primaryStage.setScene(telaEntrada.getTelaEntrada());
		primaryStage.show();
	}

	@Override
	public void stop() {
		Controle.instance().salvar();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
