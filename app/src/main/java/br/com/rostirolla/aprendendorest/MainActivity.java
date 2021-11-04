package br.com.rostirolla.aprendendorest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    ListView listPessoas;
    ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
    PessoasAdapter adapter;
    String url = "https://restmicroservice.herokuapp.com/pessoa";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(MainActivity.this);

        listPessoas = findViewById(R.id.listPessoas);
        adapter = new PessoasAdapter(MainActivity.this, pessoas);
        listPessoas.setAdapter(adapter);

        listPessoas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pessoa pessoa = pessoas.get(i);
                deletePessoaAPI(pessoa.id);
                return true;
            }
        });

        listPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pessoa pessoa = pessoas.get(i);
                Intent intent =
                        new Intent(MainActivity.this,
                                CadastrarPessoaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pessoa", pessoa);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPessoasAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_pessoa:
                Intent intent =
                        new Intent(MainActivity.this,
                                CadastrarPessoaActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPessoasAPI() {
        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pessoas.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json = response.getJSONObject(i);
                                Pessoa pessoa = Pessoa.fromJSON(json);
                                pessoas.add(pessoa);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "Erro ao consultar API:\n" + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(request);
    }

    private void deletePessoaAPI(long id) {
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url + "/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,
                                "Pessoa excluída com sucesso!",
                                Toast.LENGTH_SHORT).show();
                        getPessoasAPI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "Erro ao excluir na API:\n" + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e("erro", error.getMessage());
                        Log.e("erro", error.getStackTrace().toString());
                    }
                }
        );
        queue.add(request);
    }
}