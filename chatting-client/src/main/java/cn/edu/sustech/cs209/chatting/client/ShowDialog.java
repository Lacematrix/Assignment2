package cn.edu.sustech.cs209.chatting.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ShowDialog {

  public ShowDialog(String s) {
    init(s);
  }

  public void init(String s) {
    Stage exit = new Stage();
    HBox box = new HBox(10);
    box.setAlignment(Pos.CENTER);
    Label label = new Label();
    label.setText(s);
    box.getChildren().add(label);
    exit.setScene(new Scene(box));
    exit.showAndWait();
  }
}
