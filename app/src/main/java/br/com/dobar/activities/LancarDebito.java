package br.com.dobar.activities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dobar.R;
import br.com.dobar.daos.controls.CartaoControl;
import br.com.dobar.daos.controls.CredorControl;
import br.com.dobar.daos.controls.DebitoControl;
import br.com.dobar.daos.controls.DevedorControl;
import br.com.dobar.daos.controls.LimiteControl;
import br.com.dobar.daos.controls.ParcelaControl;
import br.com.dobar.daos.controls.TipoControl;
import br.com.dobar.daos.entities.Cartao;
import br.com.dobar.daos.entities.Credor;
import br.com.dobar.daos.entities.Debito;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Limite;
import br.com.dobar.daos.entities.Parcela;
import br.com.dobar.daos.entities.Tipo;
import br.com.dobar.utils.Constantes;
import br.com.dobar.utils.Utils;

public class LancarDebito extends Activity {

	private AutoCompleteTextView autoCredor;
	private AutoCompleteTextView autoDevedor;
	private Spinner spnCartao;
	private Spinner spnTipo;
	private EditText edtOrdem;
	private TextView txtDataDebito;
	private TextView txtValorDebito;
	private CheckBox chkAlteraOrdem;
	private EditText edtValorDebito;
	private EditText edtNrParcelas;
	private EditText edtDescricao;
	private RadioGroup rdgFormaPag;
	private List<Credor> listCredor = new ArrayList<Credor>();
	private List<Devedor> listDevedor = new ArrayList<Devedor>();
	private List<Cartao> listCartao = new ArrayList<Cartao>();
	private List<Tipo> listTipo = new ArrayList<Tipo>();
	private SharedPreferences preferences;
	private Button btDataDebito;
	private Button btDataPagamento;
	private int anoDebito, mesDebito, diaDebito;
	private int anoPagamento, mesPagamento, diaPagamento;
	private DebitoControl debitoControl;
	private ParcelaControl parcelaControl;
	private Credor credor;
	private Devedor devedor;
	protected ImageButton ibtApagar;
	protected ImageButton ibtNovo;
	protected ImageButton ibtSalvar;
	protected ImageButton ibtEditar;
	private Long iddebito;
	private String parcelas;
	private String extraDtPagamento;
	private Debito debito;
	private ArrayAdapter<String> adapterTipo;
	private ArrayAdapter<String> adapterCartao;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lancadebito);

		debitoControl = new DebitoControl(this);
		parcelaControl = new ParcelaControl(this);
		iniciarComponentes();
		carregarAdapters();
		desbloquearTela(false);

		if (iddebito == 0) {
			controlaBotoes(true, false, false, false);
		} else {
			controlaBotoes(true, false, true, true);
			preencheTelaComDebito();
		}

	}

	/**
	 * 
	 */
	private void iniciarComponentes() {
		// AutoCompleteTextView
		autoCredor = (AutoCompleteTextView) findViewById(R.id.autocredor);
		autoDevedor = (AutoCompleteTextView) findViewById(R.id.autodevedor);
		// Spinner
		spnCartao = (Spinner) findViewById(R.id.spncartao);
		spnTipo = (Spinner) findViewById(R.id.spntipo);
		// EditText
		edtOrdem = (EditText) findViewById(R.id.edtordem);
		edtValorDebito = (EditText) findViewById(R.id.lancadebito_edtvlrdebito);
		edtNrParcelas = (EditText) findViewById(R.id.lancadebito_edtnrparcela);
		edtDescricao = (EditText) findViewById(R.id.lancadebito_edtdesc);
		// preference
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		// Button
		btDataDebito = (Button) findViewById(R.id.lancadebito_btdatadebito);
		btDataPagamento = (Button) findViewById(R.id.lancadebito_btdatapagamento);

		Calendar calendar = Calendar.getInstance();
		diaDebito = calendar.get(Calendar.DAY_OF_MONTH);
		mesDebito = calendar.get(Calendar.MONTH);
		anoDebito = calendar.get(Calendar.YEAR);
		diaPagamento = diaDebito;
		mesPagamento = mesDebito;
		anoPagamento = anoDebito;

		// TextView
		txtDataDebito = (TextView) findViewById(R.id.lancadebito_txtdatadebito);
		txtValorDebito = (TextView) findViewById(R.id.lancadebito_txtvalordebito);

		// CheckBox
		chkAlteraOrdem = (CheckBox) findViewById(R.id.lancadebito_ckbalteraordem);

		// RadioGroup
		rdgFormaPag = (RadioGroup) findViewById(R.id.lancadebito_rgrformpag);

		// ImageButtons
		ibtSalvar = (ImageButton) findViewById(R.id.debito_btsalvar);
		ibtNovo = (ImageButton) findViewById(R.id.debito_btnovo);
		ibtEditar = (ImageButton) findViewById(R.id.debito_bteditar);
		ibtApagar = (ImageButton) findViewById(R.id.debito_btexcluir);

		iddebito = getIntent().getLongExtra(Constantes.IDDEBITO, 0L);
		parcelas = getIntent().getStringExtra(Constantes.TOTALPARCELAS);
		extraDtPagamento = getIntent().getStringExtra(Constantes.DATAPAGAMENTO);
	}

	/**
	 * 
	 */
	private void carregarAdapters() {

		// Credores
		CredorControl credorControl = new CredorControl(this);
		listCredor = credorControl.getListByRazao("", true);

		ArrayAdapter<String> adapterCredor = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				Utils.getListNomeCredores(listCredor));

		autoCredor.setAdapter(adapterCredor);
		// Devedores
		DevedorControl devedorControl = new DevedorControl(this);
		listDevedor = devedorControl.getListByNome("", true);
		ArrayAdapter<String> adapterDevedor = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				Utils.getListNomeDevedores(listDevedor));
		autoDevedor.setAdapter(adapterDevedor);

		// Cartoes
		CartaoControl cartaoControl = new CartaoControl(this);
		listCartao = cartaoControl.getListByOperadora("", true);
		adapterCartao = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				Utils.getListCartoes(listCartao));
		adapterCartao
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCartao.setAdapter(adapterCartao);

		// Tipos
		TipoControl tipoControl = new TipoControl(this);
		listTipo = tipoControl.getListByTipo("");
		adapterTipo = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				Utils.getListTipos(listTipo));
		adapterTipo
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTipo.setAdapter(adapterTipo);
	}

	/**
	 * 
	 * @param view
	 */
	public void executarAcao(View view) {
		switch (view.getId()) {
		case R.id.lancadebito_btdatadebito:
			showDialog(view.getId());
			break;
		case R.id.lancadebito_btdatapagamento:
			showDialog(view.getId());
			break;
		case R.id.lancadebito_rdocarne:
			spnCartao.setEnabled(false);
			txtDataDebito.setText("Primeiro Vencimento");
			txtValorDebito.setText("Valor da Prestacao");
			break;
		case R.id.lancadebito_rdooutro:
			spnCartao.setEnabled(false);
			txtDataDebito.setText("Primeiro Vencimento");
			txtValorDebito.setText("Valor da Prestacao");
			break;
		case R.id.lancadebito_rdocartao:
			spnCartao.setEnabled(true);
			txtDataDebito.setText("Data do Debito");
			txtValorDebito.setText("Valor do Debito");
			break;
		case R.id.lancadebito_ckbalteraordem:
			if (chkAlteraOrdem.isChecked() && iddebito == 0L) {
				edtOrdem.setEnabled(true);
			} else {
				edtOrdem.setEnabled(false);
				edtOrdem.requestFocus();
			}
			break;

		case R.id.debito_btsalvar:
			dialogConfirmacao(Constantes.SALVARDEBITO, R.string.confirmacao);
			break;
		case R.id.debito_btnovo:
			desbloquearTela(true);
			limparTela();
			controlaBotoes(false, true, false, false);
			break;
		case R.id.debito_btcalc:
			Intent calc = new Intent();
			calc.setAction(Intent.ACTION_MAIN);
			calc.addCategory(Intent.CATEGORY_LAUNCHER);
			calc.setComponent(new ComponentName("com.android.calculator2",
					"com.android.calculator2.Calculator"));
			LancarDebito.this.startActivity(calc);
			break;
		case R.id.debito_btcall:
			Intent intentCall = new Intent(Intent.ACTION_DIAL);
			startActivity(intentCall);
			break;
		case R.id.debito_bteditar:
			desbloquearTela(true);
			controlaBotoes(false, true, false, false);
			break;
		case R.id.debito_btexcluir:
			dialogConfirmacao(Constantes.DELETARDEBITO, R.string.deletedebito);
			break;
		}
	}

	/**
	 * 
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case R.id.lancadebito_btdatadebito:
			return new DatePickerDialog(this, setDataDebito, anoDebito,
					mesDebito, diaDebito);
		case R.id.lancadebito_btdatapagamento:
			return new DatePickerDialog(this, setDataPagamento, anoPagamento,
					mesPagamento, diaPagamento);
		default:
			return null;
		}
	}

	/**
	 * 
	 */
	private OnDateSetListener setDataDebito = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			anoDebito = year;
			mesDebito = monthOfYear;
			diaDebito = dayOfMonth;
			btDataDebito.setText(Utils.getDDMMYYYY(diaDebito, mesDebito + 1,
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
			btDataPagamento.setText(Utils.getDDMMYYYY(diaPagamento,
					mesPagamento + 1, anoPagamento));
		}
	};

	/**
	 * 
	 */
	private void setDebito() {
		debito.setCredor(credor);
		// verifica qual radiobutton foi selecionado
		int form = rdgFormaPag.getCheckedRadioButtonId();
		if (form == R.id.lancadebito_rdocartao) {
			debito.setFormPagamento(Constantes.CARTAO);
			debito.setCartao(listCartao.get(spnCartao.getSelectedItemPosition()));
		} else if (form == R.id.lancadebito_rdocarne) {
			debito.setFormPagamento(Constantes.CARNE);
		} else if (form == R.id.lancadebito_rdooutro) {
			debito.setFormPagamento(Constantes.OUTRO);
		}
		debito.setDataDebito(btDataDebito.getText().toString());
		debito.setVlDebito(new BigDecimal(edtValorDebito.getText().toString()));
		debito.setOrdem(Integer.parseInt(edtOrdem.getText().toString()));
		debito.setDevedor(devedor);
		debito.setTipo(listTipo.get(spnTipo.getSelectedItemPosition()));
		debito.setDescricao(edtDescricao.getText().toString().toUpperCase());
	}

	/**
	 * 
	 */
	private void salvar() {
		if (validaDados()) {
			List<Parcela> listParcelas = new ArrayList<Parcela>();
			int existe = 0;
			if (iddebito == 0L) {
				debito = new Debito();
			}
			setDebito();

			Long idDebitoCriado = 0L;
			if (iddebito == 0L) {
				debitoControl.create(debito);
				idDebitoCriado = debitoControl.getDebitoByOrdem(debito
						.getOrdem()).getIddebito();
			} else {
				debito.setIddebito(iddebito);
				idDebitoCriado = iddebito;
				debitoControl.update(debito);
				listParcelas = parcelaControl.getListByIddebito(iddebito);
				existe = listParcelas.size();
			}
			// criando as parcelas
			if (idDebitoCriado > 0L) {

				int numerodeparcelas = Integer.parseInt(edtNrParcelas.getText()
						.toString());

				java.sql.Date strDatePgto = Utils.getSqlDate(btDataPagamento
						.getText().toString());
				java.sql.Date strDateVenc = null;
				BigDecimal vlrparcela = null;
				if (debito.getFormPagamento() != Constantes.CARTAO) {
					strDateVenc = Utils.getSqlDate(btDataDebito.getText()
							.toString());
					vlrparcela = debito.getVlDebito();
				} else {
					strDateVenc = Utils.getSqlDate(getDateVencimento());
					vlrparcela = debito.getVlDebito().divide(
							new BigDecimal(numerodeparcelas), 2,
							RoundingMode.HALF_UP);

				}
				int totalgravados = 0;
				// verificando se devedor possui limite estipulado
				LimiteControl limiteControl = new LimiteControl(this);
				Limite limite = limiteControl.getLimiteByDevedorAndDia(
						devedor.getIdDevedor(), diaPagamento);

				for (int i = 0; i < numerodeparcelas; i++) {

					// setando valores da parcela
					Parcela parcela = new Parcela();
					parcela.setDataPagamento(strDatePgto.toString());
					parcela.setDataVencimento(strDateVenc.toString());
					parcela.setDebito(new Debito());
					parcela.getDebito().setIddebito(idDebitoCriado);
					parcela.setDevedor(devedor);
					parcela.setNrParcela(Utils.completaDigitos(
							String.valueOf((i + 1)), 2)
							+ "/"
							+ Utils.completaDigitos(
									String.valueOf(numerodeparcelas), 2));
					parcela.setVlParcela(vlrparcela);

					if (existe > 0) {
						parcela.setIdparcela(listParcelas.get(i).getIdparcela());
						parcela.setPago(listParcelas.get(i).getPago());
						if (parcela.getPago().equalsIgnoreCase(Constantes.NAO)) {
							parcelaControl.update(parcela);
						}
						existe--;
						totalgravados++;
					} else {
						parcela.setPago(Constantes.NAO);
						long result = parcelaControl.create(parcela);
						if (result != -1) {
							totalgravados++;
						}
					}

					if (limite != null) {
						notificando(limite.getLimitePessoal(), strDatePgto);
					}
					// alterando datas para proximo mes
					strDatePgto = Utils.getProximoMes(strDatePgto.toString());
					strDateVenc = Utils.getProximoMes(strDateVenc.toString());
				}
				// eliminando parcelas que sobraram caso o numero de parcela
				// novas seja menor que a anterior
				if (listParcelas.size() > numerodeparcelas) {
					int dif = listParcelas.size() - numerodeparcelas;
					for (int i = dif + 1; i < listParcelas.size(); i++) {
						parcelaControl.delete(listParcelas.get(i)
								.getIdparcela());
					}

				}
				// finalizando
				if (totalgravados == numerodeparcelas) {

					if (iddebito == 0L) {
						Toast.makeText(this, "Debito lancado com sucesso",
								Toast.LENGTH_SHORT).show();
						String novaOrdem = Utils.completaDigitos(
								String.valueOf(debito.getOrdem() + 1), 10);

						SharedPreferences.Editor editor = preferences.edit();
						editor.putString(Constantes.KEY_ORDEM, novaOrdem);
						editor.commit();
					} else {
						Toast.makeText(this, "Debito atualizado com sucesso",
								Toast.LENGTH_SHORT).show();
					}

					iddebito = 0L;
					controlaBotoes(true, false, false, false);
					desbloquearTela(false);

				} else {
					Toast.makeText(this, "Falha no lancamento de parcelas",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Falha no lancamento do debito",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private String getDateVencimento() {

		Cartao cartaoTemp = listCartao.get(spnCartao.getSelectedItemPosition());
		int[] vetdate = Utils.splitDate(btDataDebito.getText().toString());

		int dia = cartaoTemp.getDiaVencimento();
		int mes = vetdate[Constantes.MES];
		int ano = vetdate[Constantes.ANO];

		if (vetdate[Constantes.DIA] >= cartaoTemp.getDiaFechamento()) {
			if (mes == Constantes.DEZEMBRO) {
				mes = 1;
				ano += 1;
			} else {
				mes += 1;
			}
		}
		return Utils.getDDMMYYYY(dia, mes, ano);
	}

	/**
	 * 
	 * @return
	 */
	private boolean validaDados() {
		// credor
		boolean achou = false;
		for (Credor lstCredor : listCredor) {
			if (autoCredor.getText().toString()
					.equalsIgnoreCase(lstCredor.getRazao())) {
				credor = lstCredor;
				achou = true;
				break;
			}
		}
		if (!achou) {
			boolean permiteCadastrar = preferences.getBoolean(
					Constantes.KEY_CREDOR, false);
			if (permiteCadastrar) {
				Credor credorTemp = new Credor();
				credorTemp.setRazao(autoCredor.getText().toString()
						.toUpperCase().trim());
				credorTemp.setAtivo(Constantes.SIM);
				CredorControl credorControl = new CredorControl(this);
				Long result = credorControl.create(credorTemp);
				if (result != -1) {
					credor = credorControl.getCredorByRazao(credorTemp
							.getRazao());
				} else {
					Toast.makeText(this, "N�o foi possivel criar o Credor",
							Toast.LENGTH_SHORT).show();
					return false;
				}
			} else {
				Toast.makeText(this, "Credor n�o encontrado",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		// data debito
		if (btDataDebito.getText().toString().equalsIgnoreCase("Selecione")) {
			Toast.makeText(this, "Informe data do debito", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		// valor debito
		if (edtValorDebito.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe o valor do debito",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		// numero de parcelas
		if (edtNrParcelas.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe o numero de parcelas",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtNrParcelas.getText().toString().equalsIgnoreCase("0")) {
			Toast.makeText(this, "Numero de parcelas nao pode ser ZERO",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		// data previsao de pagamento
		if (btDataPagamento.getText().toString().equalsIgnoreCase("Selecione")) {
			Toast.makeText(this, "Informe data de pagamento",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// ordem
		if (edtOrdem.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Informe a ordem", Toast.LENGTH_SHORT).show();
			edtOrdem.requestFocus();
			return false;
		}
		if (debitoControl.getDebitoByOrdem(Integer.parseInt(edtOrdem.getText().toString()))!= null && iddebito == 0L) {
			Toast.makeText(this, "Esse numero de ordem ja foi informado",
					Toast.LENGTH_SHORT).show();
			edtOrdem.requestFocus();
			return false;
		}
		// Devedor
		achou = false;
		for (Devedor lstDevedor : listDevedor) {
			if (autoDevedor.getText().toString()
					.equalsIgnoreCase(lstDevedor.getNome())) {
				achou = true;
				devedor = lstDevedor;
				break;
			}
		}
		if (!achou) {
			boolean permiteCadastrar = preferences.getBoolean(
					Constantes.KEY_DEVEDOR, false);
			if (permiteCadastrar) {
				Devedor devedorTemp = new Devedor();
				devedorTemp.setNome(autoDevedor.getText().toString()
						.toUpperCase().trim());
				devedorTemp.setAtivo(Constantes.SIM);
				DevedorControl devedorControl = new DevedorControl(this);
				Long result = devedorControl.create(devedorTemp);
				if (result != -1) {
					devedor = devedorControl.getDevedorByNome(devedorTemp
							.getNome());
				} else {
					Toast.makeText(this, "N�o foi possivel criar o Devedor",
							Toast.LENGTH_SHORT).show();
					return false;
				}
			} else {
				Toast.makeText(this, "Devedor n�o encontrado",
						Toast.LENGTH_SHORT).show();
				autoDevedor.requestFocus();
				return false;
			}
		}
		// Tipo
		if (listTipo.isEmpty()) {
			Toast.makeText(this, "Cadastre um tipo", Toast.LENGTH_SHORT).show();
			spnTipo.requestFocus();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return
	 */
	private void dialogConfirmacao(final int operacao, int msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setPositiveButton(getString(R.string.sim),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (operacao == Constantes.SALVARDEBITO) {
							salvar();
						} else if (operacao == Constantes.DELETARDEBITO) {
							deletarDebito();
							limparTela();
							controlaBotoes(true, false, false, false);
						}
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

	/**
	 * 
	 * @param novo
	 * @param salvar
	 * @param editar
	 * @param deletar
	 */
	private void controlaBotoes(boolean novo, boolean salvar, boolean editar,
			boolean deletar) {

		if (novo) {
			ibtNovo.setEnabled(true);
			ibtNovo.setImageDrawable(getResources()
					.getDrawable(R.drawable.novo));
		} else {
			ibtNovo.setEnabled(false);
			ibtNovo.setImageDrawable(getResources().getDrawable(
					R.drawable.novo_d));

		}
		if (salvar) {
			ibtSalvar.setEnabled(true);
			ibtSalvar.setImageDrawable(getResources().getDrawable(
					R.drawable.gravar));

		} else {
			ibtSalvar.setEnabled(false);
			ibtSalvar.setImageDrawable(getResources().getDrawable(
					R.drawable.gravar_d));
		}
		if (editar) {
			ibtEditar.setEnabled(true);
			ibtEditar.setImageDrawable(getResources().getDrawable(
					R.drawable.editar));
		} else {
			ibtEditar.setEnabled(false);
			ibtEditar.setImageDrawable(getResources().getDrawable(
					R.drawable.editar_d));
		}
		if (deletar) {
			ibtApagar.setEnabled(true);
			ibtApagar.setImageDrawable(getResources().getDrawable(
					R.drawable.apagar));
		} else {
			ibtApagar.setEnabled(false);
			ibtApagar.setImageDrawable(getResources().getDrawable(
					R.drawable.apagar_d));
		}
	}

	/**
	 * 
	 * @param boo
	 */
	private void desbloquearTela(boolean boo) {
		// AutoCompleteTextView
		autoCredor.setEnabled(boo);
		autoDevedor.setEnabled(boo);
		// Spinner
		int form = rdgFormaPag.getCheckedRadioButtonId();
		if (form == R.id.lancadebito_rdocartao) {
			spnCartao.setEnabled(boo);
		} else {
			spnCartao.setEnabled(false);
		}
		spnTipo.setEnabled(boo);
		// EditText
		edtValorDebito.setEnabled(boo);
		edtNrParcelas.setEnabled(boo);
		edtDescricao.setEnabled(boo);
		// Button
		btDataDebito.setEnabled(boo);
		btDataPagamento.setEnabled(boo);
		// CheckBox
		chkAlteraOrdem.setEnabled(boo);
		// RadioGroup
		RadioButton rdoCartao = (RadioButton) findViewById(R.id.lancadebito_rdocartao);
		RadioButton rdoCarne = (RadioButton) findViewById(R.id.lancadebito_rdocarne);
		RadioButton rdoOutro = (RadioButton) findViewById(R.id.lancadebito_rdooutro);
		rdoCarne.setEnabled(boo);
		rdoCartao.setEnabled(boo);
		rdoOutro.setEnabled(boo);
	}

	/**
	 * 
	 */
	private void limparTela() {
		// AutoCompleteTextView
		autoCredor.setText(null);
		autoDevedor.setText(null);

		// EditText
		edtOrdem.setText(Utils.completaDigitos(
				preferences.getString(Constantes.KEY_ORDEM, "0000000000"), 10));
		edtValorDebito.setText(null);
		edtNrParcelas.setText(null);
		edtDescricao.setText(null);

		// Button
		btDataDebito.setText("Selecione");
		btDataPagamento.setText("Selecione");
	}

	/**
	 * 
	 */
	private void preencheTelaComDebito() {
		debito = debitoControl.getDebitoById(iddebito);
		autoCredor.setText(debito.getCredor().getRazao());
		if (debito.getFormPagamento() == Constantes.CARNE) {
			rdgFormaPag.check(R.id.lancadebito_rdocarne);
		}
		if (debito.getFormPagamento() == Constantes.OUTRO) {
			rdgFormaPag.check(R.id.lancadebito_rdooutro);
		}
		edtDescricao.setText(debito.getDescricao());
		spnTipo.setSelection(adapterTipo
				.getPosition(debito.getTipo().getTipo()));
		autoDevedor.setText(debito.getDevedor().getNome());
		edtOrdem.setText(Utils
				.completaDigitos(debito.getOrdem().toString(), 10));
		edtValorDebito.setText(debito.getVlDebito().toString());
		if (debito.getCartao() != null) {
			rdgFormaPag.check(R.id.lancadebito_rdocartao);
			spnCartao.setSelection(adapterCartao.getPosition(debito.getCartao()
					.getOperadora()));
		}
		edtNrParcelas.setText(parcelas);
		btDataDebito.setText(debito.getDataDebito());
		btDataPagamento.setText(extraDtPagamento);
	}

	/**
	 * 
	 */
	private void deletarDebito() {
		int result = debitoControl.delete(iddebito);
		if (result > 0) {
			Toast.makeText(this, "Debito deletado com sucesso",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 */
	private void notificando(int limite, java.sql.Date sqlDatePag) {

		BigDecimal totalAberto = parcelaControl.getTotalByDevedor(
				devedor.getIdDevedor(), sqlDatePag);

		if (totalAberto.intValue() > limite) {
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.dobar,
					"Limite alcan�ado", System.currentTimeMillis());
			notification.sound = Uri.withAppendedPath(
					Audio.Media.INTERNAL_CONTENT_URI, "6");
			notification.flags |= Notification.FLAG_INSISTENT;

			PendingIntent pendingIntent = PendingIntent
					.getActivity(this, 0,
							new Intent(this.getApplicationContext(),
									LancarDebito.class), 0);
//			notification.setLatestEventInfo(this, "Limite ultrapassado em: R$ "
//					+ (totalAberto.intValue() - limite),
//					"\nPagamento: " + Utils.getDDMMYYYY(sqlDatePag.toString()),
//					pendingIntent);

			notification.vibrate = new long[] { 100, 1000, 1000, 1000 };
			notificationManager.notify(R.string.app_name, notification);
		}
	}
}
