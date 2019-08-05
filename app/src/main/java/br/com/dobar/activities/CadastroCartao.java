package br.com.dobar.activities;

import java.math.BigDecimal;
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
import br.com.dobar.daos.controls.CartaoControl;
import br.com.dobar.daos.entities.Cartao;

public class CadastroCartao extends AbstractActivity {
	private Spinner spnBandeiras;
	private Spinner spnEmUso;
	private EditText edtOperadora;
	private EditText edtVencimento;
	private EditText edtFechamento;
	private EditText edtLimite;
	private EditText edtSaldo;
	private EditText edtTelefone;
	private List<Cartao> list;
	private String[] operadoras;
	private Cartao cartao = new Cartao();
	private CartaoControl cartaoControl;
	private ArrayAdapter<CharSequence> adapterBandeiras;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cartaoControl = new CartaoControl(this);
		list = new ArrayList<Cartao>();
		iniciaTelaCadastro();
	}

	/**
	 * 
	 */
	@Override
	protected void salvarCadastro() {
		cartao.setOperadora(edtOperadora.getText().toString().toUpperCase());
		cartao.setBandeira(spnBandeiras.getSelectedItem().toString());
		cartao.setTelefone(edtTelefone.getText().toString());
		cartao.setDiaFechamento(Integer.parseInt(edtFechamento.getText()
				.toString()));
		cartao.setDiaVencimento(Integer.parseInt(edtVencimento.getText()
				.toString()));
		cartao.setLimite(Integer.parseInt(edtLimite.getText().toString()));
		cartao.setEmUso(String.valueOf(spnEmUso.getSelectedItemPosition()));
		long resultado;
		if (cartao.getIdcartao() == null) {
			resultado = cartaoControl.create(cartao);

			if (resultado != -1) {
				Toast.makeText(this, "Cadastrado com Sucesso",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Falha na tentativa de cadastrado!!!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			resultado = cartaoControl.update(cartao);

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
	private void iniciaTelaCadastro() {
		setContentView(R.layout.cadastrocartao);
		// Spinner
		spnBandeiras = (Spinner) findViewById(R.id.spnbandeira);
		spnEmUso = (Spinner) findViewById(R.id.spnemuso);
		// EditText
		edtOperadora = (EditText) findViewById(R.id.edtoperadora);
		edtVencimento = (EditText) findViewById(R.id.edtvencimento);
		edtFechamento = (EditText) findViewById(R.id.edtfechamento);
		edtLimite = (EditText) findViewById(R.id.edtlimite);
		edtSaldo = (EditText) findViewById(R.id.edtsaldo);
		edtTelefone = (EditText) findViewById(R.id.edtcartaotel);
		// ImageButtons
		ibtSalvar = (ImageButton) findViewById(R.id.btsalvar);
		ibtNovo = (ImageButton) findViewById(R.id.btnovo);
		ibtEditar = (ImageButton) findViewById(R.id.bteditar);
		ibtApagar = (ImageButton) findViewById(R.id.btexcluir);
		ibtSearch = (ImageButton) findViewById(R.id.btsearch);
		// ArrayAdapter
		adapterBandeiras = ArrayAdapter.createFromResource(this,
				R.array.bandeiras, android.R.layout.simple_spinner_item);
		adapterBandeiras
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnBandeiras.setAdapter(adapterBandeiras);

		ArrayAdapter<CharSequence> adapterEmUso = ArrayAdapter
				.createFromResource(this, R.array.simnao,
						android.R.layout.simple_spinner_item);
		adapterEmUso
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEmUso.setAdapter(adapterEmUso);

		if (cartao.getIdcartao() == null) {
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
		txtSearch.setText("Operadora");
	}

	/**
	 * 
	 * @param posicao
	 */
	private void preparaEdicao(int posicao) {
		controlaBotoes(true, true, false, true, true);
		cartao = list.get(posicao);
		edtOperadora.setText(cartao.getOperadora());
		edtFechamento.setText(cartao.getDiaFechamento().toString());
		edtVencimento.setText(cartao.getDiaVencimento().toString());
		edtLimite.setText(cartao.getLimite().toString());
		edtTelefone.setText(cartao.getTelefone());
		spnBandeiras.setSelection(adapterBandeiras.getPosition(cartao.getBandeira()));
		spnEmUso.setSelection(Integer.parseInt(cartao.getEmUso()));
		String strSaldo = cartaoControl.getSaldo(cartao.getIdcartao());
		if(strSaldo != null){
		BigDecimal saldo = new BigDecimal(cartao.getLimite())
				.subtract(new BigDecimal(strSaldo));
		edtSaldo.setText(saldo.toString());
		}else{
			edtSaldo.setText(cartao.getLimite().toString());
		}
	}

	/**
	 * faz a busca da lista de cartoes
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected void getList() {
		list.clear();

		list = cartaoControl.getListByOperadora(edtSearch.getText().toString(),
				false);
		operadoras = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			operadoras[i] = list.get(i).getOperadora();
		}
		ArrayAdapter<String> adapterList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, operadoras);
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
	 */
	@Override
	protected void novoCadastro() {
		controlaBotoes(false, false, true, false, false);
		cartao = new Cartao();
		bloquearTela(true);
		limpaCampos();
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
	protected void excluirCadastro() {

		int result = cartaoControl.delete(cartao.getIdcartao());
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
	 * @return
	 */
	@Override
	protected boolean validarDados() {
		if (edtOperadora.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe a operadora", Toast.LENGTH_SHORT)
					.show();
			edtOperadora.requestFocus();
			return false;
		}
		if (edtFechamento.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe o dia de fechamento",
					Toast.LENGTH_SHORT).show();
			edtFechamento.requestFocus();
			return false;
		} else {
			int diafecha = Integer.parseInt(edtFechamento.getText().toString());
			if (diafecha < 1 || diafecha > 31) {
				Toast.makeText(this, "Dia de fechamento invalido",
						Toast.LENGTH_SHORT).show();
				edtFechamento.requestFocus();
				return false;
			}
		}
		if (edtVencimento.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe o dia do vencimento",
					Toast.LENGTH_SHORT).show();
			edtVencimento.requestFocus();
			return false;
		} else {
			int diavenc = Integer.parseInt(edtVencimento.getText().toString());
			if (diavenc < 1 || diavenc > 31) {
				Toast.makeText(this, "Dia vencimento invalido",
						Toast.LENGTH_SHORT).show();
				edtVencimento.requestFocus();
				return false;
			}
		}
		if (edtLimite.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe o limite do cartao",
					Toast.LENGTH_SHORT).show();
			edtLimite.requestFocus();
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	private void limpaCampos() {
		edtOperadora.setText(null);
		edtVencimento.setText(null);
		edtFechamento.setText(null);
		edtLimite.setText(null);
		edtSaldo.setText(null);
		edtTelefone.setText(null);
	}

	/**
	 * 
	 * @param boo
	 */
	private void bloquearTela(boolean boo) {
		edtOperadora.setEnabled(boo);
		edtOperadora.requestFocus();
		edtVencimento.setEnabled(boo);
		edtFechamento.setEnabled(boo);
		edtLimite.setEnabled(boo);
		edtTelefone.setEnabled(boo);
		spnBandeiras.setEnabled(boo);
		spnEmUso.setEnabled(boo);
	}
}
