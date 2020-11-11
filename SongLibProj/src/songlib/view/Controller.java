//Daniel Alvarado
//Al Manrique
package songlib.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

public class Controller {
	@FXML
	Button addButton;
	@FXML
	TextField nameBox;
	@FXML
	TextField albumBox;
	@FXML
	TextField artistBox;
	@FXML
	TextField yearBox;
	@FXML
	ListView<String> listView;
	private ObservableList<String> obsList;
	ArrayList<String> arrList = new ArrayList<String>();
	String str = "";
	String str2 = "";
	private static Scanner scn;

	public void start(Stage mainStage) {
		String[] values = new String[4];
		String path = "library.csv";
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				values = line.split(",");
				str = values[0].trim();
				str2 = values[1].trim();
				arrList.add(str + " " + str2 + "\n".trim());
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obsList = FXCollections.observableArrayList(arrList);
		FXCollections.sort(obsList, String.CASE_INSENSITIVE_ORDER);
		listView.setItems(obsList);
		listView.getSelectionModel().select(0);
		if(!obsList.isEmpty()||listView.getSelectionModel().getSelectedItem()!=null) {
		infoButton();
		}
	}

	public void addButton() { // make sure to clear textfeilds and a
		String name = "",artist = "",year = "", album = "";
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		if (nameBox.getText().trim().isEmpty() || artistBox.getText().trim().isEmpty()) {
			alert.setTitle("Missing info");
			alert.setContentText("Song Name or Artist is missing");
			alert.showAndWait();
			// System.out.println("name:"+name);
			// System.out.println("artist:"+artist);
			return;
		} else {
			if (!(albumBox.getText().trim().isEmpty() || albumBox.getText() == null)) {
				album = albumBox.getText();
			}
			if (!(yearBox.getText().trim().isEmpty() || yearBox.getText() == null)) {
				year = yearBox.getText(); // make sure to check if it is an int
				if (!isNum(year)) {
					alert.setTitle("Year is not an integer");
					alert.setContentText("The year entered is not a valid year");
					alert.showAndWait();
					return;
				}
			}
			name = nameBox.getText();
			artist = artistBox.getText();
			// System.out.print(Arrays.toString(obsList.toArray()));
			if (obsList.contains(name.trim() + " " + artist.trim())) {

				alert.setTitle("Duplicate");
				alert.setContentText("No Duplicates");
				alert.showAndWait();
				return;
			} else {

				Alert confirmAlert = new Alert(AlertType.CONFIRMATION,
						"Are you sure you want to add " + name + " by " + artist + "?", ButtonType.YES, ButtonType.NO);
				confirmAlert.setTitle("Are You Sure???");
				confirmAlert.showAndWait();
				if (confirmAlert.getResult() == ButtonType.YES) {

					String newLine = name + "," + artist + "," + album + "," + year;
					String entry = name.trim() + " " + artist.trim();
					obsList.add(entry);
					writeToFile(newLine);
					nameBox.setText(name);
					artistBox.setText(artist);
					albumBox.setText(album);
					yearBox.setText(year);
					listView.getSelectionModel().select(obsList.indexOf(entry));
					sortOBS();
					// Make sure no
				} else {
					return;
				}
			}
		}
	}

	public void editButton() {
		Alert editAlert = new Alert(Alert.AlertType.ERROR);
		editAlert.setHeaderText(null);
		editAlert.setTitle("ERROR");
		if (obsList.isEmpty()||listView.getSelectionModel().getSelectedItem().trim().isBlank()
				|| listView.getSelectionModel().getSelectedItem().trim().isEmpty()) {

			editAlert.setContentText("The List is Empty!!! Please add a song before editing.");
			
			editAlert.showAndWait();
			return;
		}

		String name = "", artist = "", album = "", year = "", editTerm = "", newTerm = "";
		String path = "library.csv";
		editTerm = listView.getSelectionModel().getSelectedItem().trim();
		name = nameBox.getText().trim();
		artist = artistBox.getText().trim();
		String temp = name + " " + artist;
		if (name.equals("") || artist.equals("") || nameBox.getText().trim().isEmpty()
				|| artistBox.getText().trim().isEmpty()) {

			editAlert.setContentText("Song Name or Artist is missing");
			editAlert.showAndWait();
			return;
		}
		
		if (editTerm.equals(temp)) {
			
			obsList.remove(editTerm);
			deleteTerm(editTerm, path);
			if (!(albumBox.getText().trim().isEmpty() || albumBox.getText() == null)) {
				album = albumBox.getText().trim();
			}
			if (!(yearBox.getText().trim().isEmpty() || yearBox.getText() == null)) {
				year = yearBox.getText().trim();// make sure to check if it is an int
				if (!isNum(year)) {
					editAlert.setContentText("The year entered is not a valid year");
					editAlert.showAndWait();
					return;
				}
			}
			
			obsList.add(temp);
			writeToFile(name + "," + artist + "," + album + "," + year);
			sortOBS();
			clear();
			String entry = name.trim() + " " + artist.trim();
			listView.getSelectionModel().select(obsList.indexOf(entry));
			return;
			
		}else {

			Alert confirmAlert = new Alert(AlertType.CONFIRMATION,
					"Are you sure you want to edit " +editTerm + "?", ButtonType.YES, ButtonType.NO);
			confirmAlert.setTitle("Are You Sure???");
			confirmAlert.showAndWait();
			
			if (confirmAlert.getResult() == ButtonType.YES) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				if (obsList.contains(temp)) {
					alert.setTitle("Duplicate");
					alert.setContentText("No Duplicates");
					alert.showAndWait();
					return;
				}
				obsList.remove(editTerm);
				deleteTerm(editTerm, path);
				if (!(albumBox.getText().trim().isEmpty() || albumBox.getText() == null)) {
					album = albumBox.getText().trim();
				}
				if (!(yearBox.getText().trim().isEmpty() || yearBox.getText() == null)) {
					year = yearBox.getText().trim();// make sure to check if it is an int
					if (!isNum(year)) {
						editAlert.setContentText("The year entered is not a valid year");
						editAlert.showAndWait();
						return;
					}
				}
				
				obsList.add(name + " " + artist);
				writeToFile(name + "," + artist + "," + album + "," + year);
				sortOBS();
				clear();
				String entry = name.trim() + " " + artist.trim();
				listView.getSelectionModel().select(obsList.indexOf(entry));
				

			} else {
				return;
			}
		}
	}
	

	
	
	public void deleteButton() {
		String target = "";
		int index = 0;
		Alert alertDel = new Alert(Alert.AlertType.ERROR);
		alertDel.setTitle("ERROR");
		alertDel.setHeaderText(null);
		Alert confirmAlert = new Alert(AlertType.CONFIRMATION,
				"Are you sure you want to delete the selected item?", ButtonType.YES, ButtonType.NO);
		confirmAlert.setHeaderText(null);
		confirmAlert.setTitle("Are You Sure??");
		
		if (obsList.isEmpty()||listView.getSelectionModel().getSelectedItem().trim().isBlank()
				|| listView.getSelectionModel().getSelectedItem().trim().isEmpty()) {
			alertDel.setContentText("The List is Empty!!! Please add a song.");
			alertDel.showAndWait();
			return;
		} else {
			confirmAlert.showAndWait();
			if(confirmAlert.getResult() == ButtonType.YES) {
			target = listView.getSelectionModel().getSelectedItem().trim();
			index = obsList.indexOf(target);
			if (obsList.contains(target)) {
				obsList.remove(target);
				String path = "library.csv";
				deleteTerm(target, path);
				if(!obsList.isEmpty()&&listView.getSelectionModel().getSelectedItem()!=null) {
					listView.getSelectionModel().select(index);// 
					infoButton();
				}else {
					clear();
				}
			}
			}else {
				return;
			}
		}
		// select the next song if you delete from middle of list and select previous
		// song if you delete the last item in list
		//we might need to confiem the delete as well idk.
	}


	public void deleteTerm(String target, String path) {
		String tempFile = "temp.csv";
		File oldFile = new File(path);
		File newFile = new File(tempFile);
		String name = "", artist = "",album = "",year = "",currTerm = "";
		
		try {
			FileWriter writer = new FileWriter(tempFile, true);
			BufferedWriter bWriter = new BufferedWriter(writer);
			PrintWriter pWriter = new PrintWriter(bWriter);
			scn = new Scanner(new File(path));
			scn.useDelimiter("[,\n]");
			while (scn.hasNext()) {
				name = scn.next().trim();
				artist = scn.next().trim();
				album = scn.next().trim();
				year = scn.next().trim();
				currTerm = name.trim() + " " + artist.trim();
				if (!currTerm.equals(target)) {
					pWriter.println(name + "," + artist + "," + album + "," + year);

				}

			}
			scn.close();
			pWriter.flush();
			pWriter.close();
			oldFile.delete();
			File dump = new File(path);
			newFile.renameTo(dump);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error");
		}
	}
	public void sortOBS() {
		FXCollections.sort(obsList, String.CASE_INSENSITIVE_ORDER);
	}

	public void writeToFile(String newLine) {
		try {
			BufferedWriter wri = new BufferedWriter(new FileWriter("library.csv", true));
			wri.write(newLine + "\n");
			wri.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void infoButton() {
		String name = "", artist = "", album = "", year = "", currTerm = "", target = "", path = "library.csv";
		target = listView.getSelectionModel().getSelectedItem().trim();
		clear();
		try {
			scn = new Scanner(new File(path));
			scn.useDelimiter("[,\n]");
			while (scn.hasNext()) {
				name = scn.next().trim();
				artist = scn.next().trim();
				album = scn.next().trim();
				year = scn.next().trim();
				currTerm = name.trim() + " " + artist.trim();
				if (currTerm.equals(target)) {
					nameBox.appendText(name);
					albumBox.appendText(album);
					artistBox.appendText(artist);
					yearBox.appendText(year);
					scn.close();
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clear() {
		nameBox.clear();
		artistBox.clear();
		albumBox.clear();
		yearBox.clear();

	}
	public boolean isNum(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}