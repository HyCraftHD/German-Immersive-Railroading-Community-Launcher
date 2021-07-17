package com.troblecodings.launcher.javafx;

import com.troblecodings.launcher.Launcher;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginScene extends Scene {
	
	private static StackPane stackpane = new StackPane();
	
	public LoginScene() {
		super(stackpane);
		Launcher.setupScene(this, stackpane);
		
		VBox vbox = new VBox();
		vbox.setMaxHeight(250);
		vbox.setMaxWidth(350);
		vbox.setAlignment(Pos.CENTER);
		stackpane.getChildren().add(vbox);
		
		final Button mojangLoginButton = new Button();
		mojangLoginButton.getStyleClass().add("mojangLoginButton");
		VBox.setMargin(mojangLoginButton, new Insets(20, 0, 20, 0));
		mojangLoginButton.setOnAction(handler -> {
			Launcher.setScene(Launcher.MOJANGLOGINSCENE);
		});
		
		final Button microsoftLoginButton = new Button();
		microsoftLoginButton.getStyleClass().add("microsoftLoginButton");
		VBox.setMargin(microsoftLoginButton, new Insets(20, 0, 20, 0));
		microsoftLoginButton.setOnAction(event -> {
			Launcher.setScene(Launcher.MICROSOFTSCENE);
		});
		
		vbox.getChildren().addAll(mojangLoginButton, microsoftLoginButton);
	}
	
}
