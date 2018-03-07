package li.litech.javaclient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FXMLController implements Initializable {

    private String SERVER = "localhost";
    //private String SERVER = "84.217.10.60";
    private int PORT = 3000;
    private String token = null;

    @FXML
    private Label response;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private void loginAction(ActionEvent event) {
        try {
            String uname = username.getText();
            String pw = password.getText();
            if (uname != "" && pw != "") {
                login(uname, pw);
            }
        } catch (IOException | JSONException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void registerAction(ActionEvent event) {
        try {
            String uname = username.getText();
            String pw = password.getText();
            if (uname != "" && pw != "") {
                register(uname, pw);
            }
        } catch (IOException | JSONException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void getRegistrationsAction(ActionEvent event) {
        try {
            // Create registration list
            ListView<String> registrationList = new ListView<>();
            ArrayList<String> names = getRegistrations();
            ObservableList<String> items = FXCollections.observableArrayList(names);
            registrationList.setItems(items);

            // Create new Scene
            StackPane secondaryLayout = new StackPane();
            secondaryLayout.getChildren().add(registrationList);
            Scene regiScene = new Scene(secondaryLayout, 230, 100);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Registrations");
            newWindow.setScene(regiScene);

            newWindow.show();
        } catch (IOException | JSONException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void register(String username, String password) throws IOException, JSONException {
        String dummyData = "java";
        String payload = "{"
                + "\"username\": \"" + username + "\", "
                + "\"password\": \"" + password + "\", "
                + "\"firstname\": \"" + dummyData + "\", "
                + "\"lastname\": \"" + dummyData + "\", "
                + "\"email\": \"" + dummyData + "\", "
                + "\"ssn\": \"" + dummyData + "\""
                + "}";
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://" + SERVER + ":" + PORT + "/registration");
        request.setEntity(entity);

        HttpResponse res = httpClient.execute(request);
        String json = IOUtils.toString(res.getEntity().getContent());
        JSONObject object = new JSONObject(json);
        try {
            token = object.getString("token");
            response.setText(token);
        } catch (JSONException ex) {
            response.setText(object.getString("error"));
        }
    }

    private void login(String username, String password) throws IOException, JSONException {
        String payload = "{"
                + "\"username\": \"" + username + "\", "
                + "\"password\": \"" + password + "\""
                + "}";
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://" + SERVER + ":" + PORT + "/authentication");
        request.setEntity(entity);

        HttpResponse res = httpClient.execute(request);
        String json = IOUtils.toString(res.getEntity().getContent());
        JSONObject object = new JSONObject(json);
        try {
            token = object.getString("token");
            response.setText(token);
        } catch (JSONException ex) {
            response.setText(object.getString("error"));
        }
    }

    private ArrayList<String> getRegistrations() throws IOException, JSONException {
        ArrayList<String> names = new ArrayList<>();
        String payload = "{"
                + "\"token\": \"" + token + "\""
                + "}";
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://" + SERVER + ":" + PORT + "/applications");
        request.setEntity(entity);

        HttpResponse res = httpClient.execute(request);
        String json = IOUtils.toString(res.getEntity().getContent());
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String name = obj.getString("firstname") + " " + obj.getString("lastname");
                names.add(name);
            }
        } catch (JSONException ex) {
            System.out.println("Couldn't get registrations.");
        }
        return names;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
