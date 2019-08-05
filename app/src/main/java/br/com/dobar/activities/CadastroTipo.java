package br.com.dobar.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import br.com.dobar.R;
import br.com.dobar.daos.controls.TipoControl;
import br.com.dobar.daos.entities.Tipo;

public class CadastroTipo extends AbstractActivity {

	private EditText edtTipo;
	private Tipo tipo = new Tipo();
	private TipoControl tipoControl;
	private List<Tipo> list;
	private String[] tipos;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tipoControl = new TipoControl(this);
		list = new ArrayList<Tipo>();
		iniciaTelaCadastro();
	}

	/**
	 * 
	 */
	private void iniciaTelaCadastro() {
		setContentView(R.layout.cadastrotipo);
		// EditText
		edtTipo = (EditText) findViewById(R.id.edttipo);

		// ImageButtons
		ibtSalvar = (ImageButton) findViewById(R.id.btsalvar);
		ibtNovo = (ImageButton) findViewById(R.id.btnovo);
		ibtEditar = (ImageButton) findViewById(R.id.bteditar);
		ibtApagar = (ImageButton) findViewById(R.id.btexcluir);
		ibtSearch = (ImageButton) findViewById(R.id.btsearch);

		if (tipo.getIdTipo() == null) {
			controlaBotoes(true, true, false, false, false);
		} else {
			controlaBotoes(true, true, false, true, true);
		}
	}

	/**
	 * 
	 */
	@Override
	protected void iniciaTelaDeBusca() {
		super.iniciaTelaDeBusca();
		txtSearch.setText("Tipo");
	}

	/**
	 * 
	 */
	@Override
	protected void salvarCadastro() {

		tipo.setTipo(edtTipo.getText().toString().toUpperCase().trim());

		long resultado;

		if (tipo.getIdTipo() == null) {
			resultado = tipoControl.create(tipo);

			if (resultado != -1) {
				Toast.makeText(this, "Cadastrado com Sucesso",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Falha na tentativa de cadastrado!!!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			resultado = tipoControl.update(tipo);

			if (resultado != -1) {
				Toast.makeText(this, "Atualizado com Sucesso",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Falha na tentativa de atualizacao",
						Toast.LENGTH_SHORT).show();
			}
		}
		controlaBotoes(true, true, false, true, false);
		bloquearTela(false);

	}

	/**
	 * 
	 */
	@Override
	protected void excluirCadastro() {
		int result = tipoControl.excluir(tipo.getIdTipo());
		if (result > 0) {
			Toast.makeText(this, "Registro excluido com sucesso",
					Toast.LENGTH_SHORT).show();
			controlaBotoes(true, true, false, false, false);
			limpaCampos();
		} else {
			Toast.makeText(this, "Falha na tentativa de excluir o registro",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 
	 */
	@Override
	protected void novoCadastro() {
		controlaBotoes(false, false, true, false, false);
		tipo = new Tipo();
		bloquearTela(true);
		limpaCampos();

	}

	/**
	 * 
	 */
	@Override
	protected boolean validarDados() {
		if (edtTipo.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe a descricao do tipo",
					Toast.LENGTH_SHORT).show();
			edtTipo.requestFocus();
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	protected void editarCadastro() {
		controlaBotoes(false, false, true, false, false);
		bloquearTela(true);
	}

	/**
	 * 
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected void getList() {
		list.clear();

		list = tipoControl.getListByTipo(edtSearch.getText().toString());
		tipos = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			tipos[i] = list.get(i).getTipo();
		}
		ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, tipos);
		listView.setAdapter(adapterList);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, int posicao,
					long id) {
				iniciaTelaCadastro();
				preparaEdicao(posicao);
			}
		});
	}

	/**
	 * 
	 * @param posicao
	 */
	private void preparaEdicao(int posicao) {
		controlaBotoes(true, true, false, true, true);
		tipo = list.get(posicao);
		edtTipo.setText(tipo.getTipo());

	}

	/**
	 * 
	 */
	private void limpaCampos() {
		edtTipo.setText(null);

	}

	/**
	 * 
	 * @param boo
	 */
	private void bloquearTela(boolean boo) {
		edtTipo.setEnabled(boo);
	}
}
