package br.com.rostirolla.aprendendorest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastrarPessoaActivity extends AppCompatActivity {

    EditText textNomeCadastrar, textIdadeCadastrar;
    Button btnSalvar;
    String url = "https://restmicroservice.herokuapp.com/pessoa";
    RequestQueue queue;
    Pessoa pessoa = new Pessoa();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_pessoa);

        textNomeCadastrar = findViewById(R.id.textNomeCadastrar);
        textIdadeCadastrar = findViewById(R.id.textIdadeCadastrar);
        btnSalvar = findViewById(R.id.btnSalvar);

        queue = Volley.newRequestQueue(CadastrarPessoaActivity.this);

        checkIfUpdate();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrUpdate();
            }
        });
    }

    private void saveOrUpdate() {
        if (!checkErrors()) {
            pessoa.nome = textNomeCadastrar.getText().toString();
            pessoa.idade = Integer.parseInt(textIdadeCadastrar.getText().toString());
            if (pessoa.id == 0) {
                save();
            } else {
                update();
            }
        }
    }

    private void update() {
        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    pessoa.toJSON(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(CadastrarPessoaActivity.this,
                                    "Pessoa alterada com sucesso!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CadastrarPessoaActivity.this,
                                    "Erro ao alterar:\n" + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            queue.add(request);
        } catch (JSONException e) {
            Toast.makeText(CadastrarPessoaActivity.this,
                    "Erro ao alterar:\n" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void save() {
        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    pessoa.toJSON(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(CadastrarPessoaActivity.this,
                                    "Pessoa cadastrada com sucesso!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CadastrarPessoaActivity.this,
                                    "Erro ao cadastrar:\n" + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            queue.add(request);
        } catch (JSONException e) {
            Toast.makeText(CadastrarPessoaActivity.this,
                    "Erro ao cadastrar:\n" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkErrors() {
        boolean error = false;
        String nome = textNomeCadastrar.getText().toString();
        String idade = textIdadeCadastrar.getText().toString();
        if (nome.equals("")) {
            textNomeCadastrar.setError("Campo obrigatório!");
            error = true;
        }
        if (idade.equals("")) {
            textIdadeCadastrar.setError("Campo obrigatório!");
            error = true;
        }
        return error;
    }

    private void checkIfUpdate() {
        try {
            Bundle bundle = getIntent().getExtras();
            pessoa = (Pessoa) bundle.getSerializable("pessoa");
            textNomeCadastrar.setText(pessoa.nome);
            textIdadeCadastrar.setText(pessoa.idade + "");
            getSupportActionBar().setTitle("Alterar Pessoa");
            getSupportActionBar().setSubtitle("Id: "+pessoa.id);
        }catch (Exception e) {
            getSupportActionBar().setTitle("Cadastrar Pessoa");
        }
    }
}