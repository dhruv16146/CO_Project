package armsim;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;

public class guicontroller {

	Opcode op=new Opcode();
	public guicontroller() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private TextArea t1,t2,flow;//t2 registers
	
	@FXML
	private Button btn_cycle;
	
	
	@FXML
	void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	void print() {
		String str1="";
		flow.clear();
		t1.clear();
		
		for(int i=0;i<op.h.mem.length;i++) {
			if(4*i==op.R[15]) {
			str1+="->>"+op.h.mem[i]+"\n";
			}
			else {
				str1+="   "+op.h.mem[i]+"\n";
			}
		}
		op.onecycle();
		flow.setText(str1);
		t1.setText(op.print);
		String str="";
		for(int i=0;i<16;i++) {
		str=str+"R"+i+"="+op.R[i]+""+"\n";
		}
		
		t2.setText(str);
		if(op.read_status) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Supply Input");
			alert.setHeaderText("Please Supply Input too console");
			alert.setContentText("Input Required");

			alert.showAndWait();
			
		}
		
	}
	
	

}
