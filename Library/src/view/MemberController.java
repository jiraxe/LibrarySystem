package view;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import objects.Member;
import utils.Const;
import utils.Database;

public class MemberController implements Initializable {

	@FXML
	private TableView<Member> tblMember;

	@FXML
	private TextField txtFirstNameMember;

	@FXML
	private TextField txtLastNameMember;

	@FXML
	private TextField txtPhoneMember;

	@FXML
	private TextField txtEmailMember;

	@FXML
	private DatePicker datBirthDateMember;

	@FXML
	private ComboBox<String> cboGenderMember;

	@FXML
	private Button btnAddMember;

	@FXML
	private Button btnSaveMember;

	@FXML
	private Button btnCancelMember;

	private String memberTabState = Const.NORMAL;

	private int currentMemberId = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println(location);
		try {
			fillForm();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public void fillForm() throws Exception {

		cboGenderMember.getItems().addAll(Const.MALE, Const.FEMALE);

		TableColumn<Member, String> memberId = new TableColumn<Member, String>("Id");
		memberId.setCellValueFactory(new PropertyValueFactory<Member, String>("Id"));
		memberId.setVisible(true);

		TableColumn<Member, String> firstNameCol = new TableColumn<Member, String>("Name");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Member, String>("firstName"));

		TableColumn<Member, String> emailCol = new TableColumn<Member, String>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<Member, String>("email"));

		TableColumn<Member, String> genderCol = new TableColumn<Member, String>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<Member, String>("gender"));

		TableColumn<Member, String> phoneCol = new TableColumn<Member, String>("Phone");
		phoneCol.setCellValueFactory(new PropertyValueFactory<Member, String>("phone"));

		ObservableList<Member> data = FXCollections.observableArrayList(Database.listMember);

		tblMember.setItems(data);

		tblMember.getColumns().addAll(memberId, firstNameCol, emailCol, genderCol, phoneCol);

		gridMemberRowChangedEvent();

		tblMember.getSelectionModel().selectFirst();

		txtFirstNameMember.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
		});

	}

	private void gridMemberRowChangedEvent() {
		tblMember.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				currentMemberId = newSelection.getId();
				txtFirstNameMember.setText(newSelection.getFirstName());
				txtLastNameMember.setText(newSelection.getLastName());
				txtPhoneMember.setText(newSelection.getPhone());
				txtEmailMember.setText(newSelection.getEmail());
				datBirthDateMember
						.setValue(newSelection.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				cboGenderMember.setValue(newSelection.getGender());
			}
		});
	}

	public void clickAddMember(ActionEvent event) {
		memberTabState = Const.ADD;
		toggleMember();
		System.out.println("clickAddMember");
	}

	public void clickSaveMember(ActionEvent event) {

		toggleMember();
		System.out.println("clickSaveMember");

		if (memberTabState.equals(Const.ADD)) {
			Member newMember = new Member();
			Database.maxIdMember++;
			newMember.setId(Database.maxIdMember);
			newMember.setEmail(txtEmailMember.getText());
			newMember.setPhone(txtPhoneMember.getText());
			newMember.setFirstName(txtFirstNameMember.getText());
			newMember.setLastName(txtLastNameMember.getText());
			newMember.setBirthDate(
					Date.from(datBirthDateMember.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			newMember.setGender(cboGenderMember.getValue());
			newMember.setCheckedOutBooks(null);
			Database.listMember.add(newMember);
		} else {
			for (Member m : Database.listMember) {
				if (m.getId() == currentMemberId) {
					m.setEmail(txtEmailMember.getText());
					m.setPhone(txtPhoneMember.getText());
					m.setFirstName(txtFirstNameMember.getText());
					m.setLastName(txtLastNameMember.getText());
					m.setBirthDate(
							Date.from(datBirthDateMember.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
					m.setGender(cboGenderMember.getValue());
				}
			}
		}

		ObservableList<Member> data = FXCollections.observableArrayList(Database.listMember);

		tblMember.setItems(data);
		tblMember.refresh();

		memberTabState = Const.NORMAL;

	}

	public void clickCancelMember(ActionEvent event) {
		memberTabState = Const.NORMAL;

		toggleMember();
		System.out.println("clickCancelMember");

	}

	public void textChangedMember(ActionEvent event) {

		System.out.println("textChangedMember");
	}

	private void toggleMember() {

		switch (memberTabState) {
		case Const.NORMAL:
			System.out.println("NORMAL");

			break;
		case Const.ADD:
			System.out.println("ADD");

			break;
		case Const.EDIT:
			System.out.println("EDIT");

			break;

		default:
			break;
		}
	}

}
