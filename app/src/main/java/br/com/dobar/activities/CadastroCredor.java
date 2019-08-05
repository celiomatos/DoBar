package br.com.dobar.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import br.com.dobar.R;
import br.com.dobar.daos.controls.CredorControl;
import br.com.dobar.daos.entities.Credor;
import br.com.dobar.utils.Constantes;

public class CadastroCredor extends AbstractActivity {

	private EditText edtRazao;
	private EditText edtTelefone;
	private EditText edtEmail;
	private RadioGroup rgrAtivo;
	private Credor credor = new Credor();
	private CredorControl credorControl;
	private List<Credor> list;
	private String[] credoras;
	private RadioButton rdoNao,rdoSim;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		credorControl = new CredorControl(this);
		list = new ArrayList<Credor>();
		iniciaTelaCadastro();		
	}

	/**
	 * 
	 */
	private void iniciaTelaCadastro() {
		setContentView(R.layout.cadastrocredor);
		// EditText
		edtRazao = (EditText) findViewById(R.id.edtrazao);
		edtTelefone = (EditText) findViewById(R.id.edttelcredor);
		edtEmail = (EditText) findViewById(R.id.edtcredoremail);
		rgrAtivo = (RadioGroup) findViewById(R.id.rgrativo);

		// ImageButtons
		ibtSalvar = (ImageButton) findViewById(R.id.btsalvar);
		ibtNovo = (ImageButton) findViewById(R.id.btnovo);
		ibtEditar = (ImageButton) findViewById(R.id.bteditar);
		ibtApagar = (ImageButton) findViewById(R.id.btexcluir);
		ibtSearch = (ImageButton) findViewById(R.id.btsearch);
		//RadioButton
		rdoSim = (RadioButton) findViewById(R.id.rdosim);
		rdoNao = (RadioButton) findViewById(R.id.rdonao);
		
		if (credor.getIdCredor() == null) {
			controlaBotoes(true, true, false, false, false);
		} else {
			controlaBotoes(true, true, false, true, true);
		}
		bloquearTela(false);
	}

	/**
	 * 
	 */
	@Override
	protected void iniciaTelaDeBusca() {
		super.iniciaTelaDeBusca();
		txtSearch.setText("Razao");
	}

	/**
	 * 
	 */
	@Override
	protected void salvarCadastro() {

		credor.setRazao(edtRazao.getText().toString().toUpperCase().trim());
		credor.setTelefone(edtTelefone.getText().toString().toUpperCase().trim());
		credor.setEmail(edtEmail.getText().toString().toLowerCase().trim());
		int ativo = rgrAtivo.getCheckedRadioButtonId();
		if (ativo == R.id.rdosim) {
			credor.setAtivo(Constantes.SIM);
		} else {
			credor.setAtivo(Constantes.NAO);
		}

		long resultado;

		if (credor.getIdCredor() == null) {
			resultado = credorControl.create(credor);

			if (resultado != -1) {
				Toast.makeText(this, "Cadastrado com Sucesso",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Falha na tentativa de cadastrado!!!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			resultado = credorControl.update(credor);

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
		int result = credorControl.excluir(credor.getIdCredor());
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
		credor = new Credor();
		bloquearTela(true);
		limpaCampos();

	}

	/**
	 * 
	 */
	@Override
	protected boolean validarDados() {
		if (edtRazao.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe a razao da credora",
					Toast.LENGTH_SHORT).show();
			edtRazao.requestFocus();
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

		list = credorControl.getListByRazao(edtSearch.getText().toString(), false);
		credoras = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			credoras[i] = list.get(i).getRazao();
		}
		ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, credoras);
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
		credor = list.get(posicao);
		edtRazao.setText(credor.getRazao());
		edtTelefone.setText(credor.getTelefone());
		edtEmail.setText(credor.getEmail());
		if (credor.getAtivo().equalsIgnoreCase(Constantes.SIM)) {
			rgrAtivo.check(R.id.rdosim);
		} else {
			rgrAtivo.check(R.id.rdonao);
		}
	}

	/**
	 * 
	 */
	private void limpaCampos() {
		edtRazao.setText(null);
		edtTelefone.setText(null);
		edtEmail.setText(null);
	}

	/**
	 * 
	 * @param boo
	 */
	private void bloquearTela(boolean boo) {
		edtRazao.setEnabled(boo);
		edtRazao.requestFocus();
		edtTelefone.setEnabled(boo);
		edtEmail.setEnabled(boo);
		rdoSim.setEnabled(boo);
		rdoNao.setEnabled(boo);
	}
	/**
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menusend, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menusend_tel:
			Intent intentCall = new Intent(Intent.ACTION_DIAL);
			intentCall.setData(Uri.parse("tel:" + edtTelefone.getText().toString()));
			startActivity(intentCall);
			break;

		case R.id.menusend_email:
			Intent intentMsg = new Intent(Intent.ACTION_SEND);
			intentMsg.putExtra(Intent.EXTRA_EMAIL, new String[]{edtEmail.getText().toString()});
			intentMsg.setType("text/plain");
			startActivity(Intent.createChooser(intentMsg, "Escolha como enviar"));
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
