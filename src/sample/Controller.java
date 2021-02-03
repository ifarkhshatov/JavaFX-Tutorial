
package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.json.JSONObject;

public class Controller {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="amountField"
    private TextField amountField; // Value injected by FXMLLoader

    @FXML // fx:id="ccyOne"
    private TextField ccyOne; // Value injected by FXMLLoader

    @FXML // fx:id="ccyTwo"
    private TextField ccyTwo; // Value injected by FXMLLoader

    @FXML // fx:id="applyBtn"
    private Button applyBtn; // Value injected by FXMLLoader

    @FXML // fx:id="rateCcy"
    private Text rateCcy; // Value injected by FXMLLoader

    @FXML // fx:id="conversionTotal"
    private Text conversionTotal; // Value injected by FXMLLoader

    @FXML // fx:id="dateStamp"
    private Text dateStamp; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        applyBtn.setOnAction(event -> {
            String output = getUrlContent("http://data.fixer.io/api/latest?access_key=" +
                    "27ad197ccf98251c14ebc0b13134de39&symbols="+ccyOne.getText().toUpperCase()+","+ccyTwo.getText().toUpperCase());
            if(!output.isEmpty()) {
                JSONObject obj = new JSONObject(output);
                dateStamp.setText(obj.getString("date"));
                double ccyTwoValue = obj.getJSONObject("rates").getDouble(ccyTwo.getText()) /
                        obj.getJSONObject("rates").getDouble(ccyOne.getText());
                String ccyTwoStr = String.format("%.2f", ccyTwoValue) + " " + ccyTwo.getText();

                rateCcy.setText(ccyTwoStr);

                double amount = Double.parseDouble(amountField.getText());
                double total = ccyTwoValue * amount;
                String totalStr = String.format("%.2f", total) + " " + ccyTwo.getText();
                conversionTotal.setText(totalStr);
            }
        });
    }
    private static String getUrlContent(String urlAddress) {
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Error in conversion");
        }
        return content.toString();
    }

}
