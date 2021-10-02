package com.lucasvieira.requisieshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lucasvieira.requisieshttp.api.CEPService;
import com.lucasvieira.requisieshttp.api.DataService;
import com.lucasvieira.requisieshttp.model.CEP;
import com.lucasvieira.requisieshttp.model.Foto;
import com.lucasvieira.requisieshttp.model.Postagem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;
    private TextView textoCep;
    private Retrofit retrofit;
    private List<Foto> listaFotos = new ArrayList<>();
    private List<Postagem> listaPostagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);
        textoCep = findViewById(R.id.textCep);

        retrofit = new Retrofit.Builder()
//                .baseUrl("https://viacep.com.br/ws/")
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                recuperarCEPRetrofit();
//                recuperarListaRetrofit();
                salvarPostagem();

/*                if (textoCep.length() != 8) {
                    Toast.makeText(MainActivity.this, "confira o CEP!", Toast.LENGTH_SHORT).show();
                } else {

                }*/

                /*Sem retrofit
                    MyTask task = new MyTask();
                    String cep = textoCep.getText().toString();
                    String urlCep = "https://viacep.com.br/ws/" + cep + "/json/";
                    task.execute(urlCep);
                */
            }
        });

    }

    private void salvarPostagem() {

        //Configura  objeto postagem
//        Postagem postagem = new Postagem("1234", "Título postagem!", "Corpo postagem!");

        //recupera o serviço e salva postagem
        DataService service = retrofit.create(DataService.class);
        Call<Postagem> call = service.salvarPostagem("1234", "Título postagem!", "Corpo postagem!");

        call.enqueue(new Callback<Postagem>() {
            @Override
            public void onResponse(Call<Postagem> call, Response<Postagem> response) {

                if (response.isSuccessful()){
                    Postagem postagemResposta = response.body();
                    textoResultado.setText(
                            "Código: " + response.code() +
                            " id: " + postagemResposta.getId() +
                            " Title: " + postagemResposta.getTitle()
                    );
                }
            }

            @Override
            public void onFailure(Call<Postagem> call, Throwable t) {

            }
        });


    }

    private void recuperarListaRetrofit(){

        DataService service = retrofit.create(DataService.class);
//        Call<List<Foto>> call = service.recuperarFotos();
        Call<List<Postagem>> call = service.recuperarPostagens();

        call.enqueue(new Callback<List<Postagem>>() {
            @Override
            public void onResponse(Call<List<Postagem>> call, Response<List<Postagem>> response) {
                if (response.isSuccessful()){
                    listaPostagens = response.body();

                    for (int i = 0; i < listaFotos.size(); i++){
                        Foto foto = listaFotos.get(i);
                        Log.d("resultado", "resultado: " + foto.getId() + " / " + foto.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Postagem>> call, Throwable t) {

            }
        });

    }

    private void recuperarCEPRetrofit(){

        CEPService cepService = retrofit.create( CEPService.class );
        Call<CEP> call = cepService.recuperarCEP("11390010");

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful()){
                    CEP cep = response.body();
                    textoResultado.setText(cep.getLogradouro() + ", " + cep.getBairro() + ", " + cep.getLocalidade() + " - " + cep.getUf());
                }

            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {

            }
        });

    }

    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                // Recupera os dados em Bytes
                inputStream = conexao.getInputStream();

                //inputStreamReader lê os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                //Objeto utilizado para leitura dos caracteres do InpuStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha = "";

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String logradouro = null;
            String bairro = null;
            String localidade = null;
            String uf = null;
            String enderecoCompleto = null;

            try {
                JSONObject jsonObject = new JSONObject(resultado);

                logradouro = jsonObject.getString("logradouro");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");
                enderecoCompleto = logradouro + ", " + bairro + ", " + localidade + " - " + uf;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            textoResultado.setText(enderecoCompleto);
        }
    }

}