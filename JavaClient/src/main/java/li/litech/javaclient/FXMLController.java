package li.litech.javaclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class FXMLController implements Initializable {

    private String SERVER = "localhost";
    private int PORT = 3000;

    @FXML
    private Label response;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private void handleButtonAction(ActionEvent event) {
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
            response.setText(object.getString("token"));
        } catch (JSONException ex) {
            response.setText(object.getString("error"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
