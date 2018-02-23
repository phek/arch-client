package li.litech.javaclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, JSONException {
        String payload = "data={"
                + "\"username\": \"admin\", "
                + "\"password\": \"admin\", "
                + "}";
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:3000/registration");
        request.addHeader("content-type", "application/x-www-form-urlencoded");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        String json = IOUtils.toString(response.getEntity().getContent());
        JSONObject object = new JSONObject(json);
        System.out.println(object.getString("token"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
