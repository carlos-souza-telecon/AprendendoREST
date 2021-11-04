package br.com.rostirolla.aprendendorest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Pessoa implements Serializable {

    public long id;
    public String nome;
    public int idade;

    public Pessoa() {}

    public Pessoa(String nome, int idade) {
        this.id = 0;
        this.nome = nome;
        this.idade = idade;
    }

    public Pessoa(long id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }

    public static Pessoa fromJSON(JSONObject json) throws JSONException {
        Pessoa pessoa = new Pessoa();
        pessoa.id = json.getLong("id");
        pessoa.nome = json.getString("nome");
        pessoa.idade = json.getInt("idade");
        return pessoa;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("nome", this.nome);
        json.put("idade", this.idade);
        return json;
    }
}
