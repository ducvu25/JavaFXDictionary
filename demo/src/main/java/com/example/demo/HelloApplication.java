package com.example.demo;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloApplication extends Application {
    ArrayList<Question> test;
    Boolean eConvertV;
    BorderPane root;
    GridPane displayPanel;

    ArrayList<Button> btns;
    ArrayList<Word> answers;
    int score, index, len;
    void awake(){
        eConvertV = true;
        Manager.Connect();
        btns = new ArrayList<>();
        btns.add(new Button());
        btns.add(new Button());
        btns.add(new Button());
        btns.add(new Button());

        // Xử lý sự kiện khi chọn phương án 1
        btns.get(0).setOnAction(event2 -> {
            checkAnswer(0);
        });

        // Xử lý sự kiện khi chọn phương án 2
        btns.get(1).setOnAction(event2 -> {
            checkAnswer(1);
        });

        // Xử lý sự kiện khi chọn phương án 3
        btns.get(2).setOnAction(event2 -> {
            checkAnswer(2);
        });

        // Xử lý sự kiện khi chọn phương án 4
        btns.get(3).setOnAction(event2 -> {
            checkAnswer(3);
        });
    }
    @Override
    public void start(Stage primaryStage) {
        awake();
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
            search();
            System.out.println("Bấm nút tra cứu");
        });
        // Đăng ký sự kiện cho nút chức năng 2
        btnEdit.setOnAction(event -> {
            chucNang2();
        });
        btnTest.setOnAction(event -> {
            initQuestion();
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
        search();

    }
    // Chuc nang 1
    String valueS1 = "Nhập từ";
    void search(){
        displayPanel.getChildren().clear();
        displayPanel.setHgap(10);

        // Tạo ô nhập dữ liệu
        TextField inputField = new TextField(valueS1);
        inputField.setPrefWidth(200);
        GridPane.setConstraints(inputField, 1, 1);
        Button btnSearch2 = new Button("Tra cứu");
        HBox searchBox = new HBox(inputField, btnSearch2);
        searchBox.setSpacing(10); // Đặt khoảng cách giữa các thành phần trong HBox
        btnSearch2.setOnAction(event2->{
            String s = inputField.getText().toString();
            //System.out.println(s);
            if(s.length() == 0) return;
            if(eConvertV){
                searchE(s);
            }else{
                searchV(s);
            }
        });

        // Tạo hai văn bản và một nút
        Text text1 = new Text("Anh");
        Text text2 = new Text("Việt");
        Button button = new Button(eConvertV ? "->" : "<-");
        button.setOnAction(event2->{
            eConvertV = !eConvertV;
            valueS1 = inputField.getText().toString();
            search();
        });

// Cấu hình vị trí và căn chỉnh các thành phần trong displayPanel
        GridPane.setMargin(text1, new Insets(0, 0, 0, 50));
        GridPane.setConstraints(text1, 0, 2);
        GridPane.setConstraints(text2, 2, 2);
        GridPane.setConstraints(button, 1, 2);
        GridPane.setHalignment(button, HPos.CENTER);

// Cấu hình vị trí và căn chỉnh HBox trong displayPanel
        GridPane.setConstraints(searchBox, 1, 0);

// Thêm các thành phần vào displayPanel
        displayPanel.getChildren().addAll(text1, text2, button, searchBox);

// Đặt displayPanel vào center của root
        root.setCenter(displayPanel);
    }
    void searchE(String s){
        Word x = Manager.searchEnglishWord(s);

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
    }
    void searchV(String s) {
        ArrayList<Word> x = Manager.searchVietnameseWord(s);

        // Tạo TableView
        TableView<Word> tableView = new TableView<>();

        // Tạo cột cho từ và nghĩa
        TableColumn<Word, String> wordColumn = new TableColumn<>("Từ");
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));

        TableColumn<Word, String> meansColumn = new TableColumn<>("Nghĩa");
        meansColumn.setCellValueFactory(new PropertyValueFactory<>("means"));

        tableView.getColumns().addAll(wordColumn, meansColumn);

        // Thêm dữ liệu vào TableView
        tableView.getItems().addAll(x);
        GridPane.setMargin(tableView, new Insets(20, 0, 0, 30));
        // Hiển thị TableView trong cửa sổ mới
        GridPane.setConstraints(tableView, 1, 4);
        displayPanel.getChildren().add(tableView);
    }
    // Thêm, sửa xóa
    void chucNang2(){
        displayPanel.getChildren().clear();
        // Tạo HBox để chứa các nút
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10); // Đặt khoảng cách giữa các nút

// Tạo nút "Thêm"
        Button btnAdd = new Button("Thêm");
        btnAdd.setOnAction(event2 -> {
            addWord();
        });
        buttonBox.getChildren().add(btnAdd); // Thêm nút "Thêm" vào buttonBox

// Tạo nút "Sửa"
        Button btnEdit2 = new Button("Sửa");
        btnEdit2.setOnAction(event2 -> {
            // Xử lý sự kiện khi nút "Sửa" được nhấn
            updateWord();
            System.out.println("Bấm nút Sửa");
        });
        buttonBox.getChildren().add(btnEdit2); // Thêm nút "Sửa" vào buttonBox

// Tạo nút "Xóa"
        Button btnDelete = new Button("Xóa");
        btnDelete.setOnAction(event2 -> {
            deleteWord();
            // Xử lý sự kiện khi nút "Xóa" được nhấn
            System.out.println("Bấm nút Xóa");
        });
        buttonBox.getChildren().add(btnDelete); // Thêm nút "Xóa" vào buttonBox

// Đặt buttonBox vào displayPanel
        GridPane.setConstraints(buttonBox, 1, 0);
        GridPane.setColumnSpan(buttonBox, 2);
        GridPane.setMargin(buttonBox, new Insets(10, 0, 0, 30));
        displayPanel.getChildren().add(buttonBox);

        // Đặt displayPanel vào center của root
        root.setCenter(displayPanel);

        System.out.println("Bấm nút chỉnh sửa");
    }
    void addWord(){
        displayPanel.getChildren().clear();
        // Tạo ô nhập liệu cho ID
        TextField idField = new TextField();
        idField.setPromptText("Nhập ID từ vựng");
        idField.setPrefWidth(100);
        GridPane.setConstraints(idField, 1, 2);
        GridPane.setMargin(idField, new Insets(10, 0, 0, 30));

// Tạo ô nhập liệu cho word
        TextField wordField = new TextField();
        wordField.setPromptText("Nhập từ vựng");
        GridPane.setConstraints(wordField, 1, 3);
        GridPane.setMargin(wordField, new Insets(10, 0, 0, 30));

// Tạo ô nhập liệu cho pronounce
        TextField pronounceField = new TextField();
        pronounceField.setPromptText("Nhập phát âm");
        GridPane.setConstraints(pronounceField, 1, 4);
        GridPane.setMargin(pronounceField, new Insets(10, 0, 0, 30));

// Tạo ô nhập liệu cho means
        TextField meansField = new TextField();
        meansField.setPromptText("Nhập nghĩa");
        GridPane.setConstraints(meansField, 1, 5);
        GridPane.setMargin(meansField, new Insets(10, 0, 0, 30));

// Tạo ô nhập liệu cho eg
        TextField egField = new TextField();
        egField.setPromptText("Nhập ví dụ");
        GridPane.setConstraints(egField, 1, 6);
        GridPane.setMargin(egField, new Insets(10, 0, 0, 30));
        // Tạo RadioButton cho lựa chọn danh từ hoặc động từ
        RadioButton nounRadioButton = new RadioButton("Danh từ");
        RadioButton verbRadioButton = new RadioButton("Động từ");
        GridPane.setConstraints(nounRadioButton, 1, 7);
        GridPane.setMargin(nounRadioButton, new Insets(10, 0, 0, 30));
        GridPane.setConstraints(verbRadioButton, 1, 7);
        GridPane.setMargin(verbRadioButton, new Insets(10, 0, 0, 200));
// Nhóm các RadioButton lại trong ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();
        nounRadioButton.setToggleGroup(toggleGroup);
        verbRadioButton.setToggleGroup(toggleGroup);

// Thiết lập xử lý sự kiện khi người dùng chọn RadioButton
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            TextField pluralFormField = new TextField();
            pluralFormField.setPromptText("Nhập danh từ số nhiều");
            GridPane.setConstraints(pluralFormField, 1, 8);
            GridPane.setMargin(pluralFormField, new Insets(10, 0, 0, 30));

            TextField singularFormField = new TextField();
            singularFormField.setPromptText("Nhập danh từ số ít");
            GridPane.setConstraints(singularFormField, 1, 8);
            GridPane.setMargin(singularFormField, new Insets(10, 0, 0, 230));

            // Hiển thị ô nhập liệu cho động từ (v1, v2, v3)
            TextField v1Field = new TextField();
            v1Field.setPromptText("Nhập v1");
            GridPane.setConstraints(v1Field, 1, 8);
            GridPane.setMargin(v1Field, new Insets(10, 0, 0, 30));

            TextField v2Field = new TextField();
            v2Field.setPromptText("Nhập v2");
            GridPane.setConstraints(v2Field, 1, 8);
            GridPane.setMargin(v2Field, new Insets(10, 0, 0, 130));

            TextField v3Field = new TextField();
            v3Field.setPromptText("Nhập v3");
            GridPane.setConstraints(v3Field, 1, 8);
            GridPane.setMargin(v3Field, new Insets(10, 0, 0, 230));


            boolean nounCheck;
            if (newValue == nounRadioButton) {
                nounCheck = true;
                // Thêm ô nhập liệu cho danh từ vào displayPanel
                displayPanel.getChildren().addAll(pluralFormField, singularFormField);
            }
            else {
                nounCheck = false;

                // Thêm ô nhập liệu cho động từ vào displayPanel
                displayPanel.getChildren().addAll(v1Field, v2Field, v3Field);
            }
            Button btnSave = new Button("Lưu");
            btnSave.setOnAction(e->{
                String id = idField.getText();
                String word = wordField.getText();
                String pro = pronounceField.getText();
                String mean = meansField.getText();
                String eg = egField.getText();
                if(nounCheck){
                    String plu = pluralFormField.getText();
                    String si = singularFormField.getText();
                    if(!(id.isEmpty() || word.isEmpty() || pro.isEmpty() || mean.isEmpty()
                            || eg.isEmpty() || plu.isEmpty() || si.isEmpty())) {
                        Noun no = new Noun(id, word, pro, mean, eg, plu, si);
                        Manager.addWord(no);
                        chucNang2();
                    }
                }else{
                    String v1 = v1Field.getText();
                    String v2 = v2Field.getText();
                    String v3 = v3Field.getText();
                    if(!(id.isEmpty() || word.isEmpty() || pro.isEmpty() || mean.isEmpty()
                            || eg.isEmpty() || v1.isEmpty() || v2.isEmpty() || v3.isEmpty())) {
                        Verb no = new Verb(id, word, pro, mean, eg, v1, v2, v3);
                        Manager.addWord(no);
                        chucNang2();
                    }
                }
                chucNang2();
            });
            GridPane.setConstraints(btnSave, 1, 9);
            GridPane.setMargin(btnSave, new Insets(10, 0, 0, 30));
            Button btnX = new Button("Hủy");
            btnX.setOnAction(e->{
                chucNang2();
            });
            GridPane.setConstraints(btnX, 1, 9);
            GridPane.setMargin(btnX, new Insets(10, 0, 0, 200));
            displayPanel.getChildren().addAll(btnSave, btnX);
        });

// Thêm các ô nhập liệu vào displayPanel
        displayPanel.getChildren().addAll(idField, wordField, pronounceField, meansField, egField, nounRadioButton, verbRadioButton);

// Các phần còn lại của mã của bạn...
        System.out.println("Bấm nút Thêm");
    }
    void updateWord() {
        displayPanel.getChildren().clear();

        // Tạo ô nhập liệu cho ID
        TextField idField = new TextField();
        idField.setPromptText("Nhập ID từ vựng");
        idField.setPrefWidth(100);
        idField.setMaxWidth(100);
        GridPane.setConstraints(idField, 1, 0);
        GridPane.setMargin(idField, new Insets(10, 0, 0, 30));

        // Tạo nút tìm kiếm
        Button searchButton = new Button("Tìm kiếm");
        GridPane.setConstraints(searchButton, 1, 0);
        GridPane.setMargin(searchButton, new Insets(10, 0, 0, 200));

        // Tạo các ô nhập liệu cho thông tin từ vựng
        TextField wordField = new TextField();
        wordField.setPromptText("Nhập từ vựng");
        GridPane.setConstraints(wordField, 1, 1);
        GridPane.setMargin(wordField, new Insets(10, 0, 0, 30));

        TextField pronounceField = new TextField();
        pronounceField.setPromptText("Nhập phát âm");
        GridPane.setConstraints(pronounceField, 1, 2);
        GridPane.setMargin(pronounceField, new Insets(10, 0, 0, 30));

        TextField meansField = new TextField();
        meansField.setPromptText("Nhập nghĩa");
        GridPane.setConstraints(meansField, 1, 3);
        GridPane.setMargin(meansField, new Insets(10, 0, 0, 30));

        TextField egField = new TextField();
        egField.setPromptText("Nhập ví dụ");
        GridPane.setConstraints(egField, 1, 4);
        GridPane.setMargin(egField, new Insets(10, 0, 0, 30));

        // Tạo RadioButton cho lựa chọn danh từ hoặc động từ
        RadioButton nounRadioButton = new RadioButton("Danh từ");
        RadioButton verbRadioButton = new RadioButton("Động từ");
        GridPane.setConstraints(nounRadioButton, 1, 5);
        GridPane.setMargin(nounRadioButton, new Insets(10, 0, 0, 30));
        GridPane.setConstraints(verbRadioButton, 1, 5);
        GridPane.setMargin(verbRadioButton, new Insets(10, 0, 0, 200));
// Nhóm các RadioButton lại trong ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();
        nounRadioButton.setToggleGroup(toggleGroup);
        verbRadioButton.setToggleGroup(toggleGroup);

        TextField pluralFormField = new TextField();
        pluralFormField.setPromptText("Nhập danh từ số nhiều");
        GridPane.setConstraints(pluralFormField, 1, 6);
        GridPane.setMargin(pluralFormField, new Insets(10, 0, 0, 30));

        TextField singularFormField = new TextField();
        singularFormField.setPromptText("Nhập danh từ số ít");
        GridPane.setConstraints(singularFormField, 1, 6);
        GridPane.setMargin(singularFormField, new Insets(10, 0, 0, 230));

        // Hiển thị ô nhập liệu cho động từ (v1, v2, v3)
        TextField v1Field = new TextField();
        v1Field.setPromptText("Nhập v1");
        GridPane.setConstraints(v1Field, 1, 6);
        GridPane.setMargin(v1Field, new Insets(10, 0, 0, 30));

        TextField v2Field = new TextField();
        v2Field.setPromptText("Nhập v2");
        GridPane.setConstraints(v2Field, 1, 6);
        GridPane.setMargin(v2Field, new Insets(10, 0, 0, 130));

        TextField v3Field = new TextField();
        v3Field.setPromptText("Nhập v3");
        GridPane.setConstraints(v3Field, 1, 6);
        GridPane.setMargin(v3Field, new Insets(10, 0, 0, 230));


        // Tạo vùng hiển thị thông báo
        Text updateMessage = new Text();
        updateMessage.setFill(Color.RED);
        GridPane.setConstraints(updateMessage, 1, 7);
        GridPane.setMargin(updateMessage, new Insets(10, 0, 0, 30));

        AtomicBoolean nounCheck = new AtomicBoolean(false);
        // Xử lý sự kiện khi nút tìm kiếm được nhấn
        searchButton.setOnAction(e -> {
            String id = idField.getText();
            if (!id.isEmpty()) {
                Word foundWord = Manager.findWord(id);
                if (foundWord != null) {
                    wordField.setText(foundWord.getWord());
                    pronounceField.setText(foundWord.getPronounce());
                    meansField.setText(foundWord.getMeans());
                    egField.setText(foundWord.getEg());

                    if (foundWord instanceof Noun) {
                        nounCheck.set(true);
                        nounRadioButton.setSelected(true);
                        pluralFormField.setText(((Noun) foundWord).getPluralForm());
                        singularFormField.setText(((Noun) foundWord).getSingularForm());
                        displayPanel.getChildren().removeAll(pluralFormField, singularFormField);
                        displayPanel.getChildren().addAll(pluralFormField, singularFormField);
                    } else {
                        nounCheck.set(false);
                        verbRadioButton.setSelected(true);
                        displayPanel.getChildren().removeAll(v1Field, v2Field, v3Field);
                        v1Field.setText(((Verb) foundWord).getV1());
                        v2Field.setText(((Verb) foundWord).getV2());
                        v3Field.setText(((Verb) foundWord).getV3());
                        displayPanel.getChildren().addAll(v1Field, v2Field, v3Field);
                    }
                    displayPanel.getChildren().addAll(wordField, pronounceField, meansField, egField, nounRadioButton, verbRadioButton);
                    updateMessage.setText("");
                    Button btnSave = new Button("Lưu");
                    btnSave.setOnAction(e2->{
                        String word = wordField.getText();
                        String pro = pronounceField.getText();
                        String mean = meansField.getText();
                        String eg = egField.getText();
                        if(nounCheck.get()){
                            String plu = pluralFormField.getText();
                            String si = singularFormField.getText();
                            if(!(word.isEmpty() || pro.isEmpty() || mean.isEmpty()
                                    || eg.isEmpty() || plu.isEmpty() || si.isEmpty())) {
                                Noun no = new Noun(id, word, pro, mean, eg, plu, si);
                                Manager.updateWord(no);
                                chucNang2();
                            }
                        }else{
                            String v1 = v1Field.getText();
                            String v2 = v2Field.getText();
                            String v3 = v3Field.getText();
                            if(!(word.isEmpty() || pro.isEmpty() || mean.isEmpty()
                                    || eg.isEmpty() || v1.isEmpty() || v2.isEmpty() || v3.isEmpty())) {
                                Verb no = new Verb(id, word, pro, mean, eg, v1, v2, v3);
                                Manager.updateWord(no);
                                chucNang2();
                            }
                        }
                        chucNang2();
                    });
                    GridPane.setConstraints(btnSave, 1, 9);
                    GridPane.setMargin(btnSave, new Insets(10, 0, 0, 30));
                    Button btnX = new Button("Hủy");
                    btnX.setOnAction(e2->{
                        chucNang2();
                    });
                    GridPane.setConstraints(btnX, 1, 9);
                    GridPane.setMargin(btnX, new Insets(10, 0, 0, 200));
                    displayPanel.getChildren().addAll(btnSave, btnX);
                } else {
                    updateMessage.setText("Không tìm thấy từ vựng với ID đã nhập!");
                    updateMessage.setFill(Color.RED);
                }
            }
        });


        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->{
            pluralFormField.setPromptText("Nhập danh từ số nhiều");
            GridPane.setConstraints(pluralFormField, 1, 6);
            GridPane.setMargin(pluralFormField, new Insets(10, 0, 0, 30));

            singularFormField.setPromptText("Nhập danh từ số ít");
            GridPane.setConstraints(singularFormField, 1, 6);
            GridPane.setMargin(singularFormField, new Insets(10, 0, 0, 230));

            v1Field.setPromptText("Nhập v1");
            GridPane.setConstraints(v1Field, 1, 6);
            GridPane.setMargin(v1Field, new Insets(10, 0, 0, 30));

            v2Field.setPromptText("Nhập v2");
            GridPane.setConstraints(v2Field, 1, 6);
            GridPane.setMargin(v2Field, new Insets(10, 0, 0, 130));

            v3Field.setPromptText("Nhập v3");
            GridPane.setConstraints(v3Field, 1, 6);
            GridPane.setMargin(v3Field, new Insets(10, 0, 0, 230));

            if (newValue == nounRadioButton) {
                nounCheck.set(true);
                displayPanel.getChildren().removeAll(pluralFormField, singularFormField);
                displayPanel.getChildren().addAll(pluralFormField, singularFormField);
            }
            else {
                nounCheck.set(false);
                displayPanel.getChildren().removeAll(v1Field, v2Field, v3Field);
                displayPanel.getChildren().addAll(v1Field, v2Field, v3Field);
            }
        });

        // Thêm các thành phần vào displayPanel
        displayPanel.getChildren().addAll(idField, searchButton, updateMessage);

        System.out.println("Bấm nút Cập nhật");
    }
    void deleteWord() {
        displayPanel.getChildren().clear();

        // Tạo ô nhập liệu cho ID
        TextField idField = new TextField();
        idField.setPromptText("Nhập ID từ vựng");
        idField.setPrefWidth(100);
        GridPane.setConstraints(idField, 1, 0);
        GridPane.setMargin(idField, new Insets(10, 0, 0, 30));

        // Tạo nút xóa
        Button deleteButton = new Button("Xóa");
        GridPane.setConstraints(deleteButton, 1, 1);
        GridPane.setMargin(deleteButton, new Insets(10, 0, 0, 200));

        Button backButton = new Button("Trở về");
        GridPane.setConstraints(backButton, 1, 1);
        GridPane.setMargin(backButton, new Insets(10, 0, 0, 30));

        // Tạo vùng hiển thị thông báo
        Text deleteMessage = new Text();
        deleteMessage.setFill(Color.RED);
        GridPane.setConstraints(deleteMessage, 1, 2);
        GridPane.setMargin(deleteMessage, new Insets(10, 0, 0, 30));

        // Xử lý sự kiện khi nút xóa được nhấn
        deleteButton.setOnAction(e -> {
            String id = idField.getText();
            if (!id.isEmpty()) {
                boolean isDeleted = Manager.deleteWord(id);
                if (isDeleted) {
                    deleteMessage.setText("Xóa thành công!");
                    deleteMessage.setFill(Color.GREEN);
                } else {
                    deleteMessage.setText("Không tìm thấy từ vựng với ID đã nhập!");
                    deleteMessage.setFill(Color.RED);
                }
            }
        });
        backButton.setOnAction(e->{
            chucNang2();
        });

        // Thêm các thành phần vào displayPanel
        displayPanel.getChildren().addAll(idField, deleteButton, backButton, deleteMessage);

        System.out.println("Bấm nút Xóa");
    }

    // THI
    void initQuestion(){
        score = 0;
        index = 0;
        test = Manager.createQuiz(5);
        len = test.size();
        Update();
    }
    ArrayList<Word> getAnswer(int i){
        ArrayList<Word> result = new ArrayList<>();
        result.add(Manager.findWord(test.get(index).getIdA()));
        result.add(Manager.findWord(test.get(index).getIdB()));
        result.add(Manager.findWord(test.get(index).getIdC()));
        result.add(Manager.findWord(test.get(index).getIdD()));
        return result;
    }
    void checkAnswer(int i){
        boolean check = test.get(index).getKey().equals(answers.get(i).getID());

        Text txtResult = new Text();
        String s;
        if(check){
            s = "Chính xác!\n";
            score++;
        }else{
            s = "Đáp án sai!\n";
        }
        s += "Giải thích: " + test.get(index).getExplain();
        index++;
        txtResult.setText(s);
        Button btnEnd = new Button("Kết thúc");
        Button btnNext = new Button("Tiếp tục");

        btnEnd.setOnAction(event2->{
            displayPanel.getChildren().clear();
            Button btnNew = new Button("Tạo mới");
            btnNew.setOnAction(event3->{
                initQuestion();
            });
            GridPane.setConstraints(btnNew, 1, 0);
            GridPane.setMargin(btnNew, new Insets(10, 0, 0, 30));
            displayPanel.getChildren().addAll(btnNew);
        });
        btnNext.setOnAction(event2->{
            Update();
        });
        GridPane.setConstraints(btnEnd, 1, 6);
        GridPane.setConstraints(btnNext, 1, 6);
        GridPane.setConstraints(txtResult, 1, 7);
        GridPane.setMargin(btnEnd, new Insets(10, 0, 0, 30));
        GridPane.setMargin(btnNext, new Insets(10, 10, 0, 200));
        GridPane.setMargin(txtResult, new Insets(10, 10, 0, 30));
        displayPanel.getChildren().addAll(btnEnd, btnNext, txtResult);
    }
    void Update(){
        displayPanel.getChildren().clear();
        answers = getAnswer(index);
        // Tạo các thành phần hiển thị
        Text scoreText = new Text("Điểm: " + score);
        Text progressText = new Text("Tiến độ: " + (index +1)+ "/" + len);
        Text questionText = new Text("Câu hỏi: " + test.get(index).getQuestion());

        btns.get(0).setText("A. " + answers.get(0).getWord());
        btns.get(1).setText("B. " + answers.get(1).getWord());
        btns.get(2).setText("C. " + answers.get(2).getWord());
        btns.get(3).setText("D. " + answers.get(3).getWord());

        // Cấu hình vị trí và căn chỉnh các thành phần trong displayPanel
       {
            GridPane.setConstraints(scoreText, 1, 0);
            GridPane.setConstraints(progressText, 1, 0);
            GridPane.setConstraints(questionText, 1, 1);
            GridPane.setConstraints(btns.get(0), 1, 2);
            GridPane.setConstraints(btns.get(1), 1, 3);
            GridPane.setConstraints(btns.get(2), 1, 4);
            GridPane.setConstraints(btns.get(3), 1, 5);
        }
        // Đặt khoảng cách và độ dãn giữa các thành phần
        {
            GridPane.setMargin(scoreText, new Insets(10, 0, 0, 20));
            GridPane.setMargin(progressText, new Insets(10, 0, 0, 200));
            GridPane.setMargin(questionText, new Insets(20, 0, 50, 50));
            GridPane.setMargin(btns.get(0), new Insets(10, 10, 0, 30));
            GridPane.setMargin(btns.get(1), new Insets(10, 0, 0, 30));
            GridPane.setMargin(btns.get(2), new Insets(10, 10, 0, 30));
            GridPane.setMargin(btns.get(3), new Insets(10, 0, 0, 30));
        }
        // Thêm các thành phần vào displayPanel
        displayPanel.getChildren().addAll(scoreText, progressText, questionText, btns.get(0),btns.get(1),btns.get(2),btns.get(3));

        // Đặt displayPanel vào center của root
        root.setCenter(displayPanel);
    }
    public static void main(String[] args) {
        launch(args);
    }
}