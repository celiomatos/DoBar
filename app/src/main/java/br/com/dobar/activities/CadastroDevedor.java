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
import br.com.dobar.daos.controls.DevedorControl;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.utils.Constantes;

public class CadastroDevedor extends AbstractActivity {

	private EditText edtNome;
	private EditText edtTelefone;
	private EditText edtEmail;
	private RadioGroup rgrAtivo;
	private Devedor devedor = new Devedor();
	private DevedorControl devedorControl;
	private List<Devedor> list;
	private String[] devedores;
	private RadioButton rdoNao,rdoSim;	

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		devedorControl = new DevedorControl(this);
		list = new ArrayList<Devedor>();
		iniciaTelaCadastro();
	}

	/**
	 * 
	 */
	private void iniciaTelaCadastro() {
		setContentView(R.layout.cadastrodevedor);
		// EditText
		edtNome = (EditText) findViewById(R.id.edtdevnome);
		edtTelefone = (EditText) findViewById(R.id.edtdevtel);
		edtEmail = (EditText) findViewById(R.id.edtdevemail);
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
		
		
		if (devedor.getIdDevedor() == null) {
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
		txtSearch.setText("Nome");
	}

	/**
	 * 
	 */
	@Override
	protected void salvarCadastro() {
		devedor.setNome(edtNome.getText().toString().toUpperCase().trim());
		devedor.setTelefone(edtTelefone.getText().toString().toUpperCase()
				.trim());
		devedor.setEmail(edtEmail.getText().toString().toLowerCase().trim());
		int ativo = rgrAtivo.getCheckedRadioButtonId();
		if (ativo == R.id.rdosim) {
			devedor.setAtivo(Constantes.SIM);
		} else {
			devedor.setAtivo(Constantes.NAO);
		}

		long resultado;

		if (devedor.getIdDevedor() == null) {
			resultado = devedorControl.create(devedor);

			if (resultado != -1) {
				Toast.makeText(this, "Cadastrado com Sucesso",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Falha na tentativa de cadastrado!!!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			resultado = devedorControl.update(devedor);

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
		int result = devedorControl.excluir(devedor.getIdDevedor());
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
		devedor = new Devedor();
		bloquearTela(true);
		limpaCampos();

	}

	/**
	 * 
	 */
	@Override
	protected boolean validarDados() {
		if (edtNome.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe o nome do devedor",
					Toast.LENGTH_SHORT).show();
			edtNome.requestFocus();
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

		list = devedorControl.getListByNome(edtSearch.getText().toString(), false);
		devedores = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			devedores[i] = list.get(i).getNome();
		}
		ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, devedores);
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
		devedor = list.get(posicao);
		edtNome.setText(devedor.getNome());
		edtTelefone.setText(devedor.getTelefone());
		edtEmail.setText(devedor.getEmail());
		if (devedor.getAtivo().equalsIgnoreCase(Constantes.SIM)) {
			rgrAtivo.check(R.id.rdosim);
		} else {
			rgrAtivo.check(R.id.rdonao);
		}
	}

	/**
	 * 
	 */
	private void limpaCampos() {
		edtNome.setText(null);
		edtTelefone.setText(null);
		edtEmail.setText(null);
	}

	/**
	 * 
	 * @param boo
	 */
	private void bloquearTela(boolean boo) {
		edtNome.setEnabled(boo);
		edtNome.requestFocus();
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
