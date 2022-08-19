package com.noone.jedit.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.noone.jedit.App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;

public class MainController implements Initializable{

	@FXML
	private TabPane tabPane;
	
	private Tab currentTab;
	
	private Map<String, File> files;
	
 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		files = new HashMap<String, File>();
		tabPane.getTabs().add(createTab(null));
	}
	
	@SuppressWarnings("exports")
	public  Tab createTab(File file) {
		Tab tab = new Tab("Untitled " + (tabPane.getTabs().size() + 1));
		GridPane gridPane = new GridPane();
		TextArea textArea = new TextArea();
		RowConstraints rows = new RowConstraints();
		ColumnConstraints columns = new ColumnConstraints();
		
		rows.setPercentHeight(100);
		columns.setPercentWidth(100);
		
		gridPane.getRowConstraints().add(rows);
		gridPane.getColumnConstraints().add(columns);
				
		gridPane.add(textArea, 0, 0);
		tab.setContent(gridPane);
		
		tab.setId(String.valueOf(Math.random() * 100));
		
		files.put(tab.getId(), file);
		
		currentTab = tab;
		
		
		
//		===Load File
		if(file != null) {
			tab.setText(file.getName());
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "";
				String text = "";
				while((line = reader.readLine()) != null) {
					text += line +"\n";
				}
				textArea.setText(text);
				reader.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
//		 ===Tab select change listener
		tab.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Tab tb = (Tab) event.getSource();
				if(tb.isSelected()) {
					currentTab = tb;
				}
			}
		});
		
//		===Tab close listener
		tab.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if(tab.getText().contains("*")) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Save");
					alert.setHeaderText("File is not saved");
					alert.setContentText("Do you want to save?");
					alert.setWidth(550);
					ButtonType type = new ButtonType("Quit without saving" , ButtonData.FINISH);
					alert.getButtonTypes().add(type);
					alert.showAndWait().ifPresent(rs -> {
						if(rs == ButtonType.OK) {
							saveFile();
						}else if(rs.getButtonData().equals(ButtonData.FINISH)) {
							
						}
						
						else {
							event.consume();
						}
					});
					
				}
			}
		});
		
		
//		===TextArea change listener
		textArea.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!oldValue.equals(newValue)) {
					if(!tab.getText().contains("*")) {
						tab.setText(tab.getText() + " * ");
					}
				}
			}
		});
		return tab;
	}
	
	private void addTab(Tab tab) {
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
	}
	
	private void writeFile(File file , String text) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(text);
			writer.flush();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@FXML
	private void createNewTab() {
		addTab(createTab(null));
	}
	
	@FXML
	private void openFile() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(tabPane.getScene().getWindow());
		if(file != null) {
			addTab(createTab(file));
		}
	}
	
	@FXML
	private void saveFile() {
		File file = files.get(currentTab.getId());
		FileChooser chooser = new FileChooser();
		GridPane gp = (GridPane) currentTab.getContent();
		TextArea ta = (TextArea) gp.getChildren().get(0);
		String text = ta.getText();
		try {
			if(file != null) {
				writeFile(file, text);
				currentTab.setText(currentTab.getText().replace("*", ""));
				files.put(currentTab.getId(), file);
			}else {
				File fl = chooser.showSaveDialog(tabPane.getScene().getWindow());
				if(fl != null) {
					writeFile(fl, text);
					currentTab.setText(fl.getName());
					files.put(currentTab.getId(), fl);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private void saveAsFile() {
		File file = new FileChooser().showSaveDialog(tabPane.getScene().getWindow());
		if(file != null) {
			GridPane gp = (GridPane) currentTab.getContent();
			TextArea ta = (TextArea) gp.getChildren().get(0);
			String text = ta.getText();
			writeFile(file, text);
			files.put(currentTab.getId(), file);
		}
		
	}
	
	@FXML
	private void exit() {
		App.exit();
	}
	
	
}
