package br.com.dobar.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.dobar.R;
import br.com.dobar.daos.controls.DevedorControl;
import br.com.dobar.daos.controls.ParcelaControl;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Parcela;
import br.com.dobar.utils.Constantes;
import br.com.dobar.utils.Utils;

public class EditaParcela extends Activity {
	private Long idparcela;
	private Spinner spnDevedores;
	private List<Devedor> listDevedor = new ArrayList<Devedor>();
	private ArrayAdapter<String> adapterDevedores;
	private Parcela parcela = new Parcela();
	private ParcelaControl parcelaControl = new ParcelaControl(this);
	private Button btVencimento;
	private Button btPagamento;
	private EditText edtValor;
	private RadioGroup rgrPago;
	private int anoDebito, mesDebito, diaDebito;
	private int anoPagamento, mesPagamento, diaPagamento;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editaparcela);
		btVencimento = (Button) findViewById(R.id.editaparcela_btvencimento);
		btPagamento = (Button) findViewById(R.id.editaparcela_btpagamento);
		edtValor = (EditText) findViewById(R.id.editaparcela_edtvalor);
		rgrPago = (RadioGroup) findViewById(R.id.editaparcela_rgrpago);
		iniciaTela();
	}

	/**
	 * 
	 */
	private void iniciaTela() {
		idparcela = getIntent().getLongExtra(Constantes.IDPARCELA, 0L);
		spnDevedores = (Spinner) findViewById(R.id.editaparcela_spndevedor);

		DevedorControl devedorControl = new DevedorControl(this);
		listDevedor = devedorControl.getListByNome("", true);
		adapterDevedores = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				Utils.getListNomeDevedores(listDevedor));
		adapterDevedores
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDevedores.setAdapter(adapterDevedores);
		parcela = parcelaControl.getByIdparcela(idparcela);
		btVencimento.setText(Utils.getDDMMYYYY(parcela.getDataVencimento()));
		btPagamento.setText(Utils.getDDMMYYYY(parcela.getDataPagamento()));
		edtValor.setText(parcela.getVlParcela().toString());
		spnDevedores.setSelection(adapterDevedores.getPosition(parcela
				.getDevedor().getNome()));

		if (parcela.getPago().equalsIgnoreCase(Constantes.SIM)) {
			rgrPago.check(R.id.editaparcela_rdosim);
		} else {
			rgrPago.check(R.id.editaparcela_rdonao);
		}

		Calendar calendar = Calendar.getInstance();
		diaDebito = calendar.get(Calendar.DAY_OF_MONTH);
		mesDebito = calendar.get(Calendar.MONTH);
		anoDebito = calendar.get(Calendar.YEAR);
		diaPagamento = diaDebito;
		mesPagamento = mesDebito;
		anoPagamento = anoDebito;
	}

	/**
	 * 
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case R.id.editaparcela_btvencimento:
			return new DatePickerDialog(this, setDataVencimento, anoDebito,
					mesDebito, diaDebito);
		case R.id.editaparcela_btpagamento:
			return new DatePickerDialog(this, setDataPagamento, anoPagamento,
					mesPagamento, diaPagamento);
		default:
			return null;
		}
	}

	/**
	 * 
	 */
	private OnDateSetListener setDataVencimento = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			anoDebito = year;
			mesDebito = monthOfYear;
			diaDebito = dayOfMonth;
			btVencimento.setText(Utils.getDDMMYYYY(diaDebito, mesDebito + 1,
					anoDebito));
		}
	};
	/**
	 * 
	 */
	private OnDateSetListener setDataPagamento = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			anoPagamento = year;
			mesPagamento = monthOfYear;
			diaPagamento = dayOfMonth;
			btPagamento.setText(Utils.getDDMMYYYY(diaPagamento,
					mesPagamento + 1, anoPagamento));
		}
	};

	/**
	 * 
	 * @param view
	 */
	public void executarAcao(View view) {
		switch (view.getId()) {
		case R.id.editaparcela_btvencimento:
			showDialog(view.getId());
			break;
		case R.id.editaparcela_btpagamento:
			showDialog(view.getId());
			break;
		case R.id.editaparcela_btsalvar:
			dialogConfirmacao();
			break;
		}
	}

	/**
	 * 
	 */
	public void editParcela() {
		if (!edtValor.getText().toString().equalsIgnoreCase("")) {
			java.sql.Date strDatePgto = Utils.getSqlDate(btPagamento.getText()
					.toString());
			java.sql.Date strDateVenc = Utils.getSqlDate(btVencimento.getText()
					.toString());
			parcela.setDevedor(listDevedor.get(spnDevedores
					.getSelectedItemPosition()));
			parcela.setDataPagamento(strDatePgto.toString());
			parcela.setDataVencimento(strDateVenc.toString());
			parcela.setVlParcela(new BigDecimal(edtValor.getText().toString()));
			int sit = rgrPago.getCheckedRadioButtonId();
			if (sit == R.id.editaparcela_rdosim) {
				parcela.setPago(Constantes.SIM);
			} else {
				parcela.setPago(Constantes.NAO);
			}
			int result = parcelaControl.update(parcela);
			if (result != -1) {
				Toast.makeText(this, "Parcela editada com sucesso",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Nao foi possivel editar parcela",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Informe o valor da parcela",
					Toast.LENGTH_SHORT).show();
			edtValor.requestFocus();
		}
	}

	/**
	 * 
	 * @return
	 */
	private void dialogConfirmacao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirmeditaparcela);
		builder.setPositiveButton(getString(R.string.sim),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						editParcela();
					}
				});
		builder.setNegativeButton(getString(R.string.nao),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
}
