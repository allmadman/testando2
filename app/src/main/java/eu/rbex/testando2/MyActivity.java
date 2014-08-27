package eu.rbex.testando2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class MyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.button1).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Login(v);
            }
        });
        new HttpRequestTask().execute();
        new HttpReqPrecioBitcoin().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new HttpRequestTask().execute();
            new HttpReqPrecioBitcoin().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            return rootView;
        }
    }

    public void Autenticar(){

    }

    private void Login(View v){
        //String url="http://www.rbex.eu/rbexComerceServer/index.php";
        String url="http://atm.rbex.eu/servicio1.php";
        Login login = new Login();
        login.setUsuario("Usuario");
        login.setPassword("Password");
        Servicio1 servicio1 = new Servicio1();
        servicio1.setItem("item");
        servicio1.setCurrency("moneda");

        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        try {
            String response = restTemplate.postForObject(url, servicio1, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            try {
                final String url = "http://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Greeting greeting = restTemplate.getForObject(url, Greeting.class);
                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            TextView greetingIdText = (TextView) findViewById(R.id.id_value);
            TextView greetingContentText = (TextView) findViewById(R.id.content_value);
            greetingIdText.setText(greeting.getId());
            greetingContentText.setText(greeting.getContent());
        }

    }
    private class HttpReqPrecioBitcoin extends AsyncTask<Void, Void, PrecioBitcoin> {
        @Override
        protected PrecioBitcoin doInBackground(Void... params) {
            try {
                final String url = "http://www.rbex.eu/getPrice.php";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                PrecioBitcoin precioBitcoin = restTemplate.getForObject(url, PrecioBitcoin.class);
                return precioBitcoin;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(PrecioBitcoin precioBitcoin) {
            TextView tvPrecioCompra = (TextView) findViewById(R.id.textView2);
            TextView tvPrecioVenta = (TextView) findViewById(R.id.textView4);
            TextView tvFecha = (TextView) findViewById(R.id.textView6);
            tvPrecioCompra.setText(precioBitcoin.getUnBitcoinEuroCompra());
            tvPrecioVenta.setText(precioBitcoin.getUnBitcoinEuroVenta());
            tvFecha.setText(precioBitcoin.getFecha());
        }

    }


}
