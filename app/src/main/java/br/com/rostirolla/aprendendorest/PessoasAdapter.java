package br.com.rostirolla.aprendendorest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PessoasAdapter extends BaseAdapter {

    Context context;
    ArrayList<Pessoa> pessoas;

    public PessoasAdapter(Context context, ArrayList<Pessoa> pessoas) {
        this.context = context;
        this.pessoas = pessoas;
    }

    @Override
    public int getCount() {
        return pessoas.size();
    }

    @Override
    public Object getItem(int i) {
        return pessoas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.adapter_pessoas, viewGroup, false);
        }

        TextView textId = view.findViewById(R.id.textId);
        TextView textNome = view.findViewById(R.id.textNome);
        TextView textIdade = view.findViewById(R.id.textIdade);

        Pessoa pessoa = pessoas.get(i);
        textId.setText(pessoa.id + "");
        textNome.setText(pessoa.nome);
        textIdade.setText(pessoa.idade + "");

        return view;
    }
}
