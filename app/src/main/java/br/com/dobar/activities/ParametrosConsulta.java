package br.com.dobar.activities;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import br.com.dobar.R;
import br.com.dobar.daos.controls.CartaoControl;
import br.com.dobar.daos.controls.CredorControl;
import br.com.dobar.daos.controls.DevedorControl;
import br.com.dobar.daos.entities.Cartao;
import br.com.dobar.daos.entities.Credor;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.utils.Constantes;
import br.com.dobar.utils.Utils;

public class ParametrosConsulta extends Activity {

	private int diaVencInicial, mesVencInicial, anoVencInicial;
	private int diaVencFinal, mesVencFinal, anoVencFinal;
	private int diaPagInicial, mesPagInicial, anoPagInicial;
	private int diaPagFinal, mesPagFinal, anoPagFinal;
	private Button btVencimentoInicial, btVencimentoFinal;
	private Button btPagamentoInicial, btPagamentoFinal;
	private Spinner spnCredores, spnDevedores, spnEmitir;
	private RadioGroup rgrSituacao;
	private List<Credor> listCredores;
	private List<Cartao> listCartao;
	private List<Devedor> listDevedores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parametrosdeconsulta);
		iniciarComponentes();
	}

	/**
	 * 
	 */
	private void iniciarComponentes() {
		// Buttons
		btVencimentoInicial = (Button) findViewById(R.id.param_consulta_btvenc_inicial);
		btVencimentoFinal = (Button) findViewById(R.id.param_consulta_btvenc_final);
		btPagamentoInicial = (Button) findViewById(R.id.param_consulta_btpag_inicial);
		btPagamentoFinal = (Button) findViewById(R.id.param_consulta_btpag_final);
		// Spinner
		spnCredores = (Spinner) findViewById(R.id.param_consulta_spncredor);
		spnDevedores = (Spinner) findViewById(R.id.param_consulta_spndevedor);
		spnEmitir = (Spinner) findViewById(R.id.param_consulta_spnemitir);
		// RadioGroup
		rgrSituacao = (RadioGroup) findViewById(R.id.param_consulta_rgrsituacao);

		Calendar calendar = Calendar.getInstance();

		btVencimentoFinal.setText(Utils.getUltimoDiaMesAtual());

		diaVencInicial = calendar.get(Calendar.DAY_OF_MONTH);
		mesVencInicial = calendar.get(Calendar.MONTH);
		anoVencInicial = calendar.get(Calendar.YEAR);
		diaVencFinal = diaVencInicial;
		mesVencFinal = mesVencInicial;
		anoVencFinal = anoVencInicial;
		diaPagInicial = diaVencInicial;
		mesPagInicial = mesVencInicial;
		anoPagInicial = anoVencInicial;
		diaPagFinal = diaVencInicial;
		mesPagFinal = mesVencInicial;
		anoPagFinal = anoVencInicial;
		// Credores
		CredorControl credorControl = new CredorControl(this);
		listCredores = credorControl.getListByRazao("", true);
		CartaoControl cartaoControl = new CartaoControl(this);
		listCartao = cartaoControl.getListByOperadora("", true);
		List<String> listNomeCredores = Utils.getListNomeCredores(listCredores);
		listNomeCredores.addAll(Utils.getListCartoesWithCartao(listCartao));
		listNomeCredores.add(0, "TODOS");
		ArrayAdapter<String> adapterCredores = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listNomeCredores);
		adapterCredores
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCredores.setAdapter(adapterCredores);
		// Devedores
		DevedorControl devedorControl = new DevedorControl(this);
		listDevedores = devedorControl.getListByNome("", true);
		List<String> listNomeDevedors = Utils
				.getListNomeDevedores(listDevedores);
		listNomeDevedors.add(0, "TODOS");
		ArrayAdapter<String> adapterDevedores = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listNomeDevedors);
		adapterDevedores
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDevedores.setAdapter(adapterDevedores);
		// emitir
		ArrayAdapter<CharSequence> adapterEmitir = ArrayAdapter
				.createFromResource(this, R.array.emitirpor,
						android.R.layout.simple_spinner_item);
		adapterEmitir
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEmitir.setAdapter(adapterEmitir);
	}

	/**
	 * 
	 * @param view
	 */
	public void executarAcao(View view) {
		switch (view.getId()) {
		case R.id.param_consulta_btvenc_inicial:
			showDialog(view.getId());
			break;
		case R.id.param_consulta_btvenc_final:
			showDialog(view.getId());
			break;
		case R.id.param_consulta_btpag_inicial:
			showDialog(view.getId());
			break;
		case R.id.param_consulta_btpag_final:
			showDialog(view.getId());
			break;
		case R.id.param_consulta_btvisualizar:
			executaSQL();
			break;
		}
	}

	/**
	 * 
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		// botao vencimento inicial
		case R.id.param_consulta_btvenc_inicial:
			return new DatePickerDialog(this, setVencimentoInicial,
					anoVencInicial, mesVencInicial, diaVencInicial);
			// botao vencimento final
		case R.id.param_consulta_btvenc_final:
			return new DatePickerDialog(this, setVencimentoFinal, anoVencFinal,
					mesVencFinal, diaVencFinal);
			// botao pagamento inicial
		case R.id.param_consulta_btpag_inicial:
			return new DatePickerDialog(this, setPagamentoInicial,
					anoPagInicial, mesPagInicial, diaPagInicial);
			// botao pagamento final
		case R.id.param_consulta_btpag_final:
			return new DatePickerDialog(this, setPagamentoFinal, anoPagFinal,
					mesPagFinal, diaPagFinal);
		default:
			return null;
		}
	}

	/**
	 * data vencimento inicial
	 */
	private OnDateSetListener setVencimentoInicial = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			anoVencInicial = year;
			mesVencInicial = monthOfYear;
			diaVencInicial = dayOfMonth;
			btVencimentoInicial.setText(Utils.getDDMMYYYY(diaVencInicial,
					mesVencInicial + 1, anoVencInicial));
		}
	};

	/**
	 * 
	 */
	private OnDateSetListener setVencimentoFinal = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			anoVencFinal = year;
			mesVencFinal = monthOfYear;
			diaVencFinal = dayOfMonth;
			btVencimentoFinal.setText(Utils.getDDMMYYYY(diaVencFinal,
					mesVencFinal + 1, anoVencFinal));
		}
	};

	/**
	 * pagamento inicial
	 */
	private OnDateSetListener setPagamentoInicial = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			anoPagInicial = year;
			mesPagInicial = monthOfYear;
			diaPagInicial = dayOfMonth;
			btPagamentoInicial.setText(Utils.getDDMMYYYY(diaPagInicial,
					mesPagInicial + 1, anoPagInicial));
		}
	};

	/**
	 * 
	 */
	private OnDateSetListener setPagamentoFinal = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			anoPagFinal = year;
			mesPagFinal = monthOfYear;
			diaPagFinal = dayOfMonth;
			btPagamentoFinal.setText(Utils.getDDMMYYYY(diaPagFinal,
					mesPagFinal + 1, anoPagFinal));
		}
	};

	/**
	 * 
	 */
	private void executaSQL() {

		String credores = spnCredores.getSelectedItem().toString();
		String devedores = spnDevedores.getSelectedItem().toString();
		String emitepor = spnEmitir.getSelectedItem().toString();

		String strWhere = "";
		String strLogico = "";
		// Credores
		if ((spnCredores.getSelectedItemPosition() != 0)
				&& (!spnCredores.getSelectedItem().toString()
						.startsWith("CARTAO:"))) {
			strWhere = "deb.idcredor = "
					+ listCredores.get(
							spnCredores.getSelectedItemPosition() - 1)
							.getIdCredor() + " ";
			strLogico = "and ";
		}
		// Cartoes
		if ((spnCredores.getSelectedItemPosition() != 0)
				&& (spnCredores.getSelectedItem().toString()
						.startsWith("CARTAO:"))) {
			strWhere = "deb.idcartao = "
					+ listCartao.get(
							spnCredores.getSelectedItemPosition()
									- listCredores.size() - 1).getIdcartao()
					+ " ";
			strLogico = "and ";
		}
		// Devedores
		if (spnDevedores.getSelectedItemPosition() != 0) {
			strWhere += strLogico
					+ "parc.iddevedor = "
					+ listDevedores.get(
							spnDevedores.getSelectedItemPosition() - 1)
							.getIdDevedor() + " ";
			strLogico = "and ";
		}
		int situacao = rgrSituacao.getCheckedRadioButtonId();
		// nao pagos
		if (situacao == R.id.param_consulta_rdoaberto) {
			strWhere += strLogico + "parc.pago = '0' ";
			strLogico = "and ";
		}
		// pagos
		if (situacao == R.id.param_consulta_rdofechado) {
			strWhere += strLogico + "parc.pago = '1' ";
			strLogico = "and ";
		}
		// vencimento maior ou igual que
		if (!btVencimentoInicial.getText().toString()
				.equalsIgnoreCase("Selecione")) {
			strWhere += strLogico
					+ "parc.datavencimento >= '"
					+ Utils.getSqlDate(btVencimentoInicial.getText().toString())
					+ "' ";
			strLogico = "and ";
		}
		// vencimento menor ou igual que
		if (!btVencimentoFinal.getText().toString()
				.equalsIgnoreCase("Selecione")) {
			strWhere += strLogico + "parc.datavencimento <= '"
					+ Utils.getSqlDate(btVencimentoFinal.getText().toString())
					+ "' ";
			strLogico = "and ";
		}

		// pagamento maior ou igual que
		if (!btPagamentoInicial.getText().toString()
				.equalsIgnoreCase("Selecione")) {
			strWhere += strLogico + "parc.datapagamento >= '"
					+ Utils.getSqlDate(btPagamentoInicial.getText().toString())
					+ "' ";
			strLogico = "and ";
		}
		// pagamento menor ou igual que
		if (!btPagamentoFinal.getText().toString()
				.equalsIgnoreCase("Selecione")) {
			strWhere += strLogico + "parc.datapagamento <= '"
					+ Utils.getSqlDate(btPagamentoFinal.getText().toString())
					+ "' ";
		}

		Intent demonstrativo = new Intent(this, Demonstrativo.class);
		demonstrativo.putExtra(Constantes.SQL, strWhere);
		demonstrativo.putExtra(Constantes.CREDORES, credores);
		demonstrativo.putExtra(Constantes.DEVEDORES, devedores);
		demonstrativo.putExtra(Constantes.EMISSAO, emitepor);
		startActivity(demonstrativo);
	}
}
