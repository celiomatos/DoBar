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
import android.widget.Spinner;
import android.widget.Toast;
import br.com.dobar.R;
import br.com.dobar.daos.controls.DevedorControl;
import br.com.dobar.daos.controls.LimiteControl;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Limite;
import br.com.dobar.utils.Utils;

public class CadastroLimite extends AbstractActivity {

	private Spinner spnDia;
	private Spinner spnDevedores;
	private EditText edtValor;
	private Limite limite = new Limite();
	private LimiteControl limiteControl;
	private List<Limite> list;
	private List<Devedor> listDevedor = new ArrayList<Devedor>();
	private String[] limites;
	private ArrayAdapter<Integer> adapterDias;
	private ArrayAdapter<String> adapterDevedores;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		limiteControl = new LimiteControl(this);
		list = new ArrayList<Limite>();
		iniciaTelaCadastro();
	}

	/**
	 * 
	 */
	private void iniciaTelaCadastro() {
		setContentView(R.layout.cadastrolimite);
		// EditText		
		edtValor = (EditText) findViewById(R.id.edtvalor);
		//Spinners
		spnDia = (Spinner) findViewById(R.id.spndia);
		spnDevedores = (Spinner) findViewById(R.id.spndevedor);
		//ArrayAdapter

		adapterDias = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, Utils.getListDias());
		adapterDias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDia.setAdapter(adapterDias);
		
		DevedorControl devedorControl = new DevedorControl(this);
		listDevedor = devedorControl.getListByNome("", true);
		
		adapterDevedores = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utils.getListNomeDevedores(listDevedor));
		adapterDevedores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDevedores.setAdapter(adapterDevedores);
		

		// ImageButtons
		ibtSalvar = (ImageButton) findViewById(R.id.btsalvar);
		ibtNovo = (ImageButton) findViewById(R.id.btnovo);
		ibtEditar = (ImageButton) findViewById(R.id.bteditar);
		ibtApagar = (ImageButton) findViewById(R.id.btexcluir);
		ibtSearch = (ImageButton) findViewById(R.id.btsearch);

		if (limite.getIdLimite() == null) {
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
		txtSearch.setText("Devedor");
	}

	/**
	 * 
	 */
	@Override
	protected void salvarCadastro() {

		limite.setDia(Integer.parseInt(spnDia.getSelectedItem().toString()));
		limite.setLimitePessoal(Integer.parseInt(edtValor.getText().toString()));
		limite.setDevedor(new Devedor());
		limite.getDevedor().setIdDevedor(listDevedor.get(spnDevedores.getSelectedItemPosition()).getIdDevedor());
		

		long resultado;

		if (limite.getIdLimite() == null) {
			resultado = limiteControl.create(limite);

			if (resultado != -1) {
				Toast.makeText(this, "Cadastrado com Sucesso",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Falha na tentativa de cadastrado!!!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			resultado = limiteControl.update(limite);

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
		int result = limiteControl.excluir(limite.getIdLimite());
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
		limite = new Limite();
		bloquearTela(true);
		limpaCampos();

	}

	/**
	 * 
	 */
	@Override
	protected boolean validarDados() {
		
		if (edtValor.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe o limite de compra para este dia",
					Toast.LENGTH_SHORT).show();
			edtValor.requestFocus();
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

		list = limiteControl.getList(edtSearch.getText().toString());
		limites = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			String registro = (list.get(i).getDia() > 9 ? list.get(i).getDia() : "0" + list.get(i).getDia())
					+ " : R$ "	+ list.get(i).getLimitePessoal() + " : " + list.get(i).getDevedor().getNome();
			limites[i] = registro;
		}
		ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, limites);
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
		limite = list.get(posicao);
		spnDia.setSelection(adapterDias.getPosition(limite.getDia()));
		edtValor.setText(limite.getLimitePessoal().toString());
		spnDevedores.setSelection(adapterDevedores.getPosition(limite.getDevedor().getNome()));

	}

	/**
	 * 
	 */
	private void limpaCampos() {

		edtValor.setText(null);
	}

	/**
	 * 
	 * @param boo
	 */
	private void bloquearTela(boolean boo) {
		edtValor.setEnabled(boo);
		spnDevedores.setEnabled(boo);
		spnDia.setEnabled(boo);
	}
		
}
