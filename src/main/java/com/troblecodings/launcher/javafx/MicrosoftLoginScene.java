package com.troblecodings.launcher.javafx;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import com.sun.javafx.webkit.WebConsoleListener;
import com.troblecodings.launcher.Launcher;
import com.troblecodings.launcher.util.AuthUtil;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import net.hycrafthd.minecraft_authenticator.Constants;
import net.hycrafthd.minecraft_authenticator.login.AuthenticationException;
import net.hycrafthd.minecraft_authenticator.login.Authenticator;
import netscape.javascript.JSObject;

public class MicrosoftLoginScene extends Scene {
	
	private static StackPane stackpane = new StackPane();
	
	public class JavaBridge
	{
	    public void log(String text)
	    {
	        System.out.println(text);
	    }
	}

	// Maintain a strong reference to prevent garbage collection:
	// https://bugs.openjdk.java.net/browse/JDK-8154127
	private final JavaBridge bridge = new JavaBridge();
	
	public MicrosoftLoginScene() {
		super(stackpane);
		Launcher.setupScene(this, stackpane);
		
		// Currently buggy login takes several tries
		final CookieManager manager = new CookieManager();
		manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(manager);
		
		VBox vbox = new VBox();
		vbox.setMaxHeight(500);
		vbox.setMaxWidth(500);
		vbox.setAlignment(Pos.CENTER);
		stackpane.getChildren().add(vbox);
		
		WebView webView = new WebView();
		
		webView.getEngine().setJavaScriptEnabled(true);
		webView.getEngine().load(Authenticator.microsoftLogin().toString());
		
		WebConsoleListener.setDefaultListener((x, message, lineNumber, sourceId) -> {
		    System.out.println(message + "[at " + lineNumber + "]");
		});
		
		webView.setContextMenuEnabled(true);
		
		webView.getEngine().getHistory().getEntries().addListener((ListChangeListener<WebHistory.Entry>) event -> {
			if (event.next() && event.wasAdded()) {
				for (WebHistory.Entry entry : event.getAddedSubList()) {
					if (entry.getUrl().startsWith(Constants.MICROSOFT_OAUTH_REDIRECT_URL)) {
						final String authCode = entry.getUrl().substring(entry.getUrl().indexOf("=") + 1, entry.getUrl().indexOf("&"));
						
						try {
							Authenticator authenticator = Authenticator.ofMicrosoft(authCode).run();
							AuthUtil.saveAuthenticationFile(authenticator.getResultFile());
							Platform.runLater(() -> {
								Launcher.setScene(Launcher.HOMESCENE);
							});
						} catch (AuthenticationException ex) {
							// Platform.runLater(() -> error.setText("Wrong credentials!"));
							Launcher.onError(ex);
						}
					}
				}
			}
		});
		
		vbox.getChildren().add(webView);
	}
}
