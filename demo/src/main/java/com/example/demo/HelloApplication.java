package com.example.demo;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class HelloApplication extends Application {
    ArrayList<Question> test;
    Boolean eConvertV;
    BorderPane root;
    GridPane displayPanel;
    int score, index, len;
    @Override
    public void start(Stage primaryStage) {
        eConvertV = true;
        Manager.Connect();
// Tạo giao diện chính
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // Tạo panel chứa các nút chức năng
        VBox functionPanel = new VBox();
        functionPanel.setSpacing(10);
        Button btnSearch = new Button("Tra từ điển");
        Button btnEdit = new Button("Chỉnh sửa");
        Button btnTest = new Button("Bài thi");
        // Đặt kích thước đồng nhất cho các nút chức năng
        btnSearch.setPrefSize(100, 50);
        btnEdit.setPrefSize(100, 50);
        btnTest.setPrefSize(100, 50);


        // Đăng ký sự kiện cho nút chức năng 1
        btnSearch.setOnAction(event -> {
            displayPanel.getChildren().clear();
            displayPanel.setHgap(10);
            
            // Tạo ô nhập dữ liệu
            TextField inputField = new TextField();
            inputField.setPrefWidth(200);
            GridPane.setConstraints(inputField, 1, 1);
            Button btnSearch2 = new Button("Tra cứu");
            HBox searchBox = new HBox(inputField, btnSearch2);
            searchBox.setSpacing(10); // Đặt khoảng cách giữa các thành phần trong HBox
            btnSearch2.setOnAction(event2->{
                String s = inputField.getText().toString();
                System.out.println(s);
                if(s.length() == 0) return;
                Word x;
                if(eConvertV) {
                    x = Manager.searchEnglishWord(s);
                }else {
                    x = Manager.searchVietnameseWord(s);
                }
                if(x == null) return;
                String type = (x instanceof Noun) ? "n" : "v";

                TreeItem<String> rootItem = new TreeItem<>("Thông tin từ");
                TreeItem<String> wordItem = new TreeItem<>("Từ: " + x.getWord());
                TreeItem<String> typeItem = new TreeItem<>("Từ loại: " + type);
                TreeItem<String> pronounceItem = new TreeItem<>("Cách phát âm: " + x.getPronounce());
                TreeItem<String> meansItem = new TreeItem<>("Nghĩa: " + x.getMeans());
                TreeItem<String> egItem = new TreeItem<>("Ví dụ: " + x.getEg());

                rootItem.getChildren().addAll(wordItem, typeItem, pronounceItem, meansItem, egItem);

                TableView<Word> tableView = new TableView<>();
                if (x instanceof Noun) {
                    GridPane.setColumnSpan(tableView, 2);
                    Noun noun = (Noun) x;
                    TableColumn<Word, String> pluralColumn = new TableColumn<>("Danh từ số nhiều");
                    pluralColumn.setCellValueFactory(new PropertyValueFactory<>("pluralForm"));

                    TableColumn<Word, String> singularColumn = new TableColumn<>("Danh từ số ít");
                    singularColumn.setCellValueFactory(new PropertyValueFactory<>("singularForm"));

                    tableView.getColumns().addAll(pluralColumn, singularColumn);

                    tableView.getItems().setAll(noun);
                } else if (x instanceof Verb) {
                    Verb verb = (Verb) x;
                    GridPane.setColumnSpan(tableView, 3);
                    TableColumn<Word, String> v1Column = new TableColumn<>("V1");
                    v1Column.setCellValueFactory(new PropertyValueFactory<>("v1"));

                    TableColumn<Word, String> v2Column = new TableColumn<>("V2");
                    v2Column.setCellValueFactory(new PropertyValueFactory<>("v2"));

                    TableColumn<Word, String> v3Column = new TableColumn<>("V3");
                    v3Column.setCellValueFactory(new PropertyValueFactory<>("v3"));

                    tableView.getColumns().addAll(v1Column, v2Column, v3Column);

                    tableView.getItems().setAll(verb);
                }

                TreeView<String> treeView = new TreeView<>(rootItem);
                treeView.setShowRoot(false);

                GridPane.setConstraints(treeView, 1, 4);
                GridPane.setColumnSpan(treeView, 2);
                GridPane.setConstraints(tableView, 1, 6);
                displayPanel.getChildren().add(treeView);
                displayPanel.getChildren().add(tableView);
            });

            // Tạo hai văn bản và một nút
            Text text1 = new Text("Anh");
            Text text2 = new Text("Việt");
            Button button = new Button("->");
            button.setOnAction(event2->{
                eConvertV = !eConvertV;
                button.setText(eConvertV ? "->" : "<-");
            });



// Cấu hình vị trí và căn chỉnh các thành phần trong displayPanel
            GridPane.setMargin(text1, new Insets(0, 0, 0, 50));
            GridPane.setConstraints(text1, 0, 2);
            GridPane.setConstraints(text2, 2, 2);
            GridPane.setConstraints(button, 1, 2);
//            GridPane.setHalignment(text1, HPos.CENTER);
//            GridPane.setHalignment(text2, HPos.CENTER);
            GridPane.setHalignment(button, HPos.CENTER);

// Cấu hình vị trí và căn chỉnh HBox trong displayPanel
            GridPane.setConstraints(searchBox, 1, 0);

// Thêm các thành phần vào displayPanel
            displayPanel.getChildren().addAll(text1, text2, button, searchBox);

// Đặt displayPanel vào center của root
            root.setCenter(displayPanel);
            System.out.println("Bấm nút tra cứu");
        });
        // Đăng ký sự kiện cho nút chức năng 2
        btnEdit.setOnAction(event -> {
            displayPanel.getChildren().clear();

            // Tạo ô nhập dữ liệu
            TextField idField = new TextField();
            idField.setPromptText("Nhập ID từ vựng");
            idField.setPrefWidth(200);
            GridPane.setConstraints(idField, 1, 1);

            Button btnEdit2 = new Button("Chỉnh sửa");
            HBox editBox = new HBox(idField, btnEdit2);
            editBox.setSpacing(10); // Đặt khoảng cách giữa các thành phần trong HBox

            btnEdit2.setOnAction(event2 -> {
                String id = idField.getText().trim();
                if (id.isEmpty()) return;

                // Thực hiện xử lý chỉnh sửa thông tin từ vựng theo ID ở đây
                // Sử dụng id để tìm từ vựng cần chỉnh sửa và thực hiện các thao tác cần thiết

                System.out.println("Bấm nút chỉnh sửa với ID: " + id);
            });

            // Cấu hình vị trí và căn chỉnh các thành phần trong displayPanel
            GridPane.setConstraints(editBox, 1, 0);
            displayPanel.getChildren().add(editBox);

            // Đặt displayPanel vào center của root
            root.setCenter(displayPanel);

            System.out.println("Bấm nút chỉnh sửa");
        });
        btnTest.setOnAction(event -> {
            initQuestion();
            Update();

            System.out.println("Bấm nút bài thi");
        });
        // Thêm các nút chức năng vào panel
        functionPanel.getChildren().addAll(btnSearch, btnEdit, btnTest);
        // Đặt panel chứa các nút chức năng vào left của root
        root.setLeft(functionPanel);

        // Tạo panel hiển thị màn hình
        displayPanel = new GridPane();
        root.setCenter(displayPanel);

        Scene scene = new Scene(root, 550, 400);
        primaryStage.setTitle("Từ điển Anh - Việt");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    void initQuestion(){
        score = 0;
        index = 0;
        test = Manager.createQuiz(10);
        len = test.size();
    }
    ArrayList<Word> getAnswer(int i){
        ArrayList<Word> result = new ArrayList<>();
        result.add(Manager.findWord(test.get(index).getIdA()));
        result.add(Manager.findWord(test.get(index).getIdB()));
        result.add(Manager.findWord(test.get(index).getIdC()));
        result.add(Manager.findWord(test.get(index).getIdD()));
        return result;
    }
    Boolean checkAnswer(String id){
        return test.get(index).getKey().equals(id);
    }
    void Update(){
        displayPanel.getChildren().clear();
        ArrayList<Word> answers = getAnswer(index);
        // Tạo các thành phần hiển thị
        Text scoreText = new Text("Điểm: " + score);
        Text progressText = new Text("Tiến độ: " + (index +1)+ "/" + len);
        Text questionText = new Text("Câu hỏi: " + test.get(index).getQuestion());

        Button option1Button = new Button("A. " + answers.get(0).getWord());
        Button option2Button = new Button("B. " + answers.get(1).getWord());
        Button option3Button = new Button("C. " + answers.get(2).getWord());
        Button option4Button = new Button("D. " + answers.get(3).getWord());

        // Tạo đối tượng Label để hiển thị thông báo
        Label resultLabel = new Label();

// Tạo đối tượng Button để hiển thị nút "Show Đáp án"
        Button showAnswerButton = new Button("Show Đáp án");

        // Xử lý sự kiện khi chọn phương án 1
        option1Button.setOnAction(event2 -> {
            if(checkAnswer(answers.get(0).getID())){
                score++;
            }else{

            }
            index++;
            Update();
        });

        // Xử lý sự kiện khi chọn phương án 2
        option2Button.setOnAction(event2 -> {
            if(checkAnswer(answers.get(1).getID())){
                score++;
            }else{

            }
            index++;
            Update();
        });

        // Xử lý sự kiện khi chọn phương án 3
        option3Button.setOnAction(event2 -> {
            if(checkAnswer(answers.get(2).getID())){
                score++;
            }else{

            }
            index++;
            Update();
        });

        // Xử lý sự kiện khi chọn phương án 4
        option4Button.setOnAction(event2 -> {
            if(checkAnswer(answers.get(3).getID())){
                score++;
            }else{

            }
            index++;
            Update();
        });

        // Cấu hình vị trí và căn chỉnh các thành phần trong displayPanel
        GridPane.setConstraints(scoreText, 1, 0);
        GridPane.setConstraints(progressText, 2, 0);
        GridPane.setConstraints(questionText, 1, 1);
        GridPane.setColumnSpan(questionText, 2);
        GridPane.setConstraints(option1Button, 1, 2);
        GridPane.setConstraints(option2Button, 1, 3);
        GridPane.setConstraints(option3Button, 1, 4);
        GridPane.setConstraints(option4Button, 1, 5);
        GridPane.setConstraints(resultLabel, 1, 6);
        GridPane.setConstraints(showAnswerButton, 1, 7);

        // Đặt khoảng cách và độ dãn giữa các thành phần
        GridPane.setMargin(scoreText, new Insets(10, 0, 0, 20));
        GridPane.setMargin(progressText, new Insets(10, 100, 0, 100));
        GridPane.setMargin(questionText, new Insets(20, 0, 50, 50));
        GridPane.setMargin(option1Button, new Insets(10, 10, 0, 30));
        GridPane.setMargin(option2Button, new Insets(10, 0, 0, 30));
        GridPane.setMargin(option3Button, new Insets(10, 10, 0, 30));
        GridPane.setMargin(option4Button, new Insets(10, 0, 0, 30));

        // Thêm các thành phần vào displayPanel
        displayPanel.getChildren().addAll(scoreText, progressText, questionText, option1Button, option2Button, option3Button, option4Button);

        // Đặt displayPanel vào center của root
        root.setCenter(displayPanel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}