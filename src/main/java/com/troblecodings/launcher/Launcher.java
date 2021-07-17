package com.troblecodings.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.troblecodings.launcher.assets.Assets;
import com.troblecodings.launcher.javafx.CreditsScene;
import com.troblecodings.launcher.javafx.Footer;
import com.troblecodings.launcher.javafx.Header;
import com.troblecodings.launcher.javafx.HomeScene;
import com.troblecodings.launcher.javafx.LoginScene;
import com.troblecodings.launcher.javafx.MicrosoftLoginScene;
import com.troblecodings.launcher.javafx.MojangLoginScene;
import com.troblecodings.launcher.javafx.OptionsScene;
import com.troblecodings.launcher.util.AuthUtil;
import com.troblecodings.launcher.util.FileUtil;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launcher extends Application {
	
	private static Stage stage;
	
	public static HomeScene HOMESCENE;
	public static OptionsScene OPTIONSSCENE;
	public static LoginScene LOGINSCENE;
	public static MojangLoginScene MOJANGLOGINSCENE;
	public static MicrosoftLoginScene MICROSOFTSCENE;
	public static CreditsScene CREDITSSCENE;
	
	private static Logger LOGGER;
	
	public static final Logger getLogger() {
		return LOGGER;
	}
	
	public static final void initializeLogger() {
		System.setProperty("app.root", FileUtil.SETTINGS.baseDir);
		LOGGER = LogManager.getLogger("GIRC");
		LOGGER.info("Starting Launcher!");
	}
	
	public static final Thread SHUTDOWNHOOK = new Thread(FileUtil::saveSettings);
		
	@Override
	public void start(Stage stage) throws Exception {
		Footer.setProgress(0.001);

		OPTIONSSCENE = new OptionsScene();
		HOMESCENE = new HomeScene();
		LOGINSCENE = new LoginScene();
		MOJANGLOGINSCENE = new MojangLoginScene();
		MICROSOFTSCENE = new MicrosoftLoginScene();
		CREDITSSCENE = new CreditsScene();
				 
		Launcher.stage = stage;
		stage.getIcons().add(Assets.getImage("icon.png"));
		stage.setScene(!AuthUtil.checkSession() ? LOGINSCENE:HOMESCENE);
		stage.setWidth(1280);
		stage.setHeight(720);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}
	
	public static void setupScene(Scene scene, StackPane stackpane) {
		stackpane.getChildren().add(new Header(scene));
		stackpane.getChildren().add(new Footer(scene));
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add(Assets.getStyleSheet("style.css"));
	}
	
	public static Scene getScene() {
		return stage.getScene();
	}
	
	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}
	
	public static Stage getStage() {
		return stage;
	}
	
	public static void onError(Throwable e) {
		// Decide what to do on error for now log
		if(e == null)
			LOGGER.error("Error found but was passed null!");
		else if(e.getMessage() == null)
			LOGGER.trace("", e);
		else
			LOGGER.trace(e.getMessage(), e);
	}
	
}
