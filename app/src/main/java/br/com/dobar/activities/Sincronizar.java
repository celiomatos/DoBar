package br.com.dobar.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.dobar.R;
import br.com.dobar.daos.controls.CartaoControl;
import br.com.dobar.daos.controls.CredorControl;
import br.com.dobar.daos.controls.DebitoControl;
import br.com.dobar.daos.controls.DevedorControl;
import br.com.dobar.daos.controls.ParcelaControl;
import br.com.dobar.daos.controls.TipoControl;
import br.com.dobar.daos.entities.Cartao;
import br.com.dobar.daos.entities.Credor;
import br.com.dobar.daos.entities.Debito;
import br.com.dobar.daos.entities.Devedor;
import br.com.dobar.daos.entities.Parcela;
import br.com.dobar.daos.entities.Tipo;
import br.com.dobar.utils.Constantes;
import br.com.dobar.utils.Utils;

public class Sincronizar extends Activity {

	private Button btVencimentoInicial, btVencimentoFinal;
	private EditText edtFile;
	private int diaVencInicial, mesVencInicial, anoVencInicial;
	private int diaVencFinal, mesVencFinal, anoVencFinal;
	private List<Devedor> listDevedores;
	private Spinner spnDevedores;
	private RadioGroup rgrSituacao, rgrLerOrEnviar;
	private CheckBox ckbPelaRede;
	private RadioButton rdoTodos, rdoAberto, rdoFechado;
	private Debito debito;
	private Parcela parcela;
	private Devedor devedor;
	private Credor credor;
	private Cartao cartao;
	private Tipo tipo;
	private ParcelaControl parcelaControl = new ParcelaControl(this);
	private DevedorControl devedorControl = new DevedorControl(this);
	private DebitoControl debitoControl = new DebitoControl(this);
	private CredorControl credorControl = new CredorControl(this);
	private CartaoControl cartaoControl = new CartaoControl(this);
	private TipoControl tipoControl = new TipoControl(this);
	private ProgressDialog mprogressDialog;

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sincronizar);
		iniciarComponentes();
	}

	/**
	 * 
	 */
	private void iniciarComponentes() {
		// Buttons
		btVencimentoInicial = (Button) findViewById(R.id.sincronizar_btvenc_inicial);
		btVencimentoFinal = (Button) findViewById(R.id.sincronizar_btvenc_final);
		// EditText
		edtFile = (EditText) findViewById(R.id.sincronizar_edtfile);

		Calendar calendar = Calendar.getInstance();

		diaVencInicial = calendar.get(Calendar.DAY_OF_MONTH);
		mesVencInicial = calendar.get(Calendar.MONTH);
		anoVencInicial = calendar.get(Calendar.YEAR);
		diaVencFinal = diaVencInicial;
		mesVencFinal = mesVencInicial;
		anoVencFinal = anoVencInicial;

		// RadioGroup
		rgrSituacao = (RadioGroup) findViewById(R.id.sincronizar_rgrsituacao);
		rdoTodos = (RadioButton) findViewById(R.id.sincronizar_rdotodos);
		rdoAberto = (RadioButton) findViewById(R.id.sincronizar_rdoaberto);
		rdoFechado = (RadioButton) findViewById(R.id.sincronizar_rdofechado);

		rgrLerOrEnviar = (RadioGroup) findViewById(R.id.sincronizar_rgrler_or_enviar);
		// CheckBox
		ckbPelaRede = (CheckBox) findViewById(R.id.sincronizar_ckbpelarede);
		// Spinner
		spnDevedores = (Spinner) findViewById(R.id.sincronizar_spndevedor);		
		listDevedores = devedorControl.getListByNome("", true);
		List<String> listNomeDevedors = Utils
				.getListNomeDevedores(listDevedores);
		listNomeDevedors.add(0, "TODOS");

		ArrayAdapter<String> adapterDevedores = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listNomeDevedors);
		adapterDevedores
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDevedores.setAdapter(adapterDevedores);

	}

	/**
	 * 
	 * @param view
	 */
	public void executarAcao(View view) {
		switch (view.getId()) {
		case R.id.sincronizar_btvenc_inicial:
			showDialog(view.getId());
			break;
		case R.id.sincronizar_btvenc_final:
			showDialog(view.getId());
			break;
		case R.id.sincronizar_edtfile:
			fileChooser();
			break;
		case R.id.sincronizar_btsincronizar:
			runSincro();
			break;
		case R.id.sincronizar_ckbpelarede:
			isPelaRede();
			break;
		case R.id.sincronizar_rdoLer:
			isPelaRede();
			break;
		case R.id.sincronizar_rdoEnviar:
			isPelaRede();
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		// botao vencimento inicial
		case R.id.sincronizar_btvenc_inicial:
			return new DatePickerDialog(this, setVencimentoInicial,
					anoVencInicial, mesVencInicial, diaVencInicial);
			// botao vencimento final
		case R.id.sincronizar_btvenc_final:
			return new DatePickerDialog(this, setVencimentoFinal, anoVencFinal,
					mesVencFinal, diaVencFinal);
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
	 * 
	 * @return
	 */
	private String montaSQL() {

		String strWhere = "";
		String strLogico = "";

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
		}
		return strWhere;
	}

	/**
	 * 
	 */
	private void runSincro() {
		int lerOrEnviar = rgrLerOrEnviar.getCheckedRadioButtonId();

		if (lerOrEnviar == R.id.sincronizar_rdoEnviar) {			
			List<Parcela> list = parcelaControl.getListCustom(montaSQL());

			if (ckbPelaRede.isChecked()) {
				Toast.makeText(this, "Enviando pela rede: "+ list.toString(), Toast.LENGTH_LONG).show();
			} else {
				StringBuilder strArquivo = new StringBuilder();
				for (Parcela parcela : list) {
					//parcela
					strArquivo.append(parcela.getVlParcela());
					strArquivo.append(";");
					strArquivo.append(parcela.getNrParcela());					
					strArquivo.append(";");
					strArquivo.append(parcela.getDataVencimento());					
					strArquivo.append(";");
					strArquivo.append(parcela.getPago());					
					strArquivo.append(";");
					strArquivo.append(parcela.getDataPagamento());
					strArquivo.append(";");
					//debito
					strArquivo.append(parcela.getDebito().getDataDebito());
					strArquivo.append(";");
					strArquivo.append(parcela.getDebito().getFormPagamento());
					strArquivo.append(";");
					strArquivo.append(parcela.getDebito().getVlDebito());
					strArquivo.append(";");
					strArquivo.append(parcela.getDebito().getDescricao());
					strArquivo.append(";");
					strArquivo.append(parcela.getDebito().getOrdem());
					strArquivo.append(";");
					//credor
					strArquivo.append(parcela.getDebito().getCredor().getRazao());
					strArquivo.append(";");
					strArquivo.append(parcela.getDebito().getCredor().getTelefone());
					strArquivo.append(";");
					strArquivo.append(parcela.getDebito().getCredor().getAtivo());
					strArquivo.append(";");
					strArquivo.append(parcela.getDebito().getCredor().getEmail());
					strArquivo.append(";");
					//devedor
					strArquivo.append(parcela.getDevedor().getNome());
					strArquivo.append(";");
					strArquivo.append(parcela.getDevedor().getTelefone());
					strArquivo.append(";");
					strArquivo.append(parcela.getDevedor().getAtivo());
					strArquivo.append(";");
					strArquivo.append(parcela.getDevedor().getEmail());
					strArquivo.append(";");
					//tipo
					strArquivo.append(parcela.getDebito().getTipo().getTipo());
					strArquivo.append(";");
					//cartao
					if (parcela.getDebito().getFormPagamento() == Constantes.CARTAO) {
						strArquivo.append(parcela.getDebito().getCartao().getOperadora());
						strArquivo.append(";");
						strArquivo.append(parcela.getDebito().getCartao().getBandeira());
						strArquivo.append(";");
						strArquivo.append(parcela.getDebito().getCartao().getEmUso());
						strArquivo.append(";");
						strArquivo.append(parcela.getDebito().getCartao().getTelefone());
						strArquivo.append(";");
						strArquivo.append(parcela.getDebito().getCartao().getDiaFechamento());
						strArquivo.append(";");
						strArquivo.append(parcela.getDebito().getCartao().getDiaVencimento());
						strArquivo.append(";");
						strArquivo.append(parcela.getDebito().getCartao().getLimite());
						strArquivo.append("\n");
					}else{
						strArquivo.append(";;;;;;\n");
					}
					
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
				String nmArquivo = simpleDateFormat.format(new Date());
				File arquivo = new File(Environment.getExternalStorageDirectory(),nmArquivo+".txt");
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(arquivo);
					fos.write(strArquivo.toString().getBytes());
					fos.flush();
					fos.close();
					Toast.makeText(this, "Arquivo Gerado com Sucesso", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {				
					e.printStackTrace();
				}				
								
			}
		} else {
			if (ckbPelaRede.isChecked()) {
				Toast.makeText(this, "Enviando parametros pela rede",
						Toast.LENGTH_LONG).show();
			} else {
		        //cria o progress dialog
		        mprogressDialog = ProgressDialog.show(this, "Aguarde", "Processando...");		         
		        new Thread() {
		            public void run() {
		                try{
		    				lerArquivo();	    				
		                } catch (Exception e) {
		                }
		                //encerra progress dialog
		                mprogressDialog.dismiss();
		            }
		        }.start();	
			}
		}
	}

	/**
	 * 
	 */
	private void isPelaRede() {
		int lerOrEnviar = rgrLerOrEnviar.getCheckedRadioButtonId();
		if (lerOrEnviar == R.id.sincronizar_rdoLer) {
			if (ckbPelaRede.isChecked()) {
				bloqueiaTela(true);
			} else {
				bloqueiaTela(false);
			}
		} else {
				bloqueiaTela(true);
				
		}
	}

	/**
	 * 
	 * @param boo
	 */
	private void bloqueiaTela(boolean boo) {
		rdoTodos.setEnabled(boo);
		rdoAberto.setEnabled(boo);
		rdoFechado.setEnabled(boo);
		spnDevedores.setEnabled(boo);
		btVencimentoInicial.setEnabled(boo);
		btVencimentoFinal.setEnabled(boo);
		edtFile.setEnabled(!boo);
	}
	/**
	 * 
	 */
	private void fileChooser(){
		Intent fileChooser = new Intent(this, FileChooser.class);
		startActivityForResult(fileChooser, Constantes.FILE_CHOOSER);
	}

	/**
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if((requestCode == Constantes.FILE_CHOOSER) &&(resultCode == RESULT_OK)){
			String arquivoSelecionado = data.getStringExtra("fileSelected");
			edtFile.setText(arquivoSelecionado);			
		}
	}
	/**
	 * 
	 */
	private void lerArquivo(){
		try{
			File arquivo = new File(edtFile.getText().toString());
			BufferedReader bufferedReader = new BufferedReader(new FileReader(arquivo));
			while(bufferedReader.ready()){
				String linha = bufferedReader.readLine();
				String campo[] = linha.split(";");
				//devedor
				Devedor devedorTemp = new Devedor(campo[14], campo[15], campo[17], campo[16]);
				setDevedor(devedorTemp);
				//credor
				Credor credorTemp = new Credor(campo[10], campo[11], campo[13], campo[12]);
				setCredor(credorTemp);
				//tipo
				Tipo tipoTemp = new Tipo(campo[18]);
				setTipo(tipoTemp);
				//cartao
				int formaPagamento = Integer.parseInt(campo[6]); 
				if(formaPagamento == Constantes.CARTAO){
					Cartao cartaoTemp = new Cartao(campo[19], campo[20], campo[23], campo[24], campo[21], campo[25], campo[22]);
					setCartao(cartaoTemp);
				}
				//debito
				Debito debitoTemp = new Debito(campo[5], campo[6], campo[7], campo[8], campo[9]);
				setDebito(debitoTemp);
				//parcela
				Parcela parcelaTemp = new Parcela(campo[0], campo[1], campo[2], campo[3], campo[4]);
				setParcela(parcelaTemp);
				
			}			
			bufferedReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param devedor
	 */
	private void setDevedor(Devedor devedor){
		this.devedor = new Devedor();
		this.devedor = devedorControl.getDevedorByNome(devedor.getNome());
		if(this.devedor == null){			
			devedorControl.create(devedor);
			this.devedor = devedorControl.getDevedorByNome(devedor.getNome());
		}else{
			devedor.setIdDevedor(this.devedor.getIdDevedor());			
			devedorControl.update(devedor);
		}
	}
	/**
	 * 
	 * @param credor
	 */
	private void setCredor(Credor credor){
		this.credor = new Credor();
		this.credor = credorControl.getCredorByRazao(credor.getRazao());
		if(this.credor == null){			
			credorControl.create(credor);
			this.credor = credorControl.getCredorByRazao(credor.getRazao());
		}else{
			credor.setIdCredor(this.credor.getIdCredor());			
			credorControl.update(credor);
		}
	}
	/**
	 * 
	 * @param tipo
	 */
	private void setTipo(Tipo tipo){
		this.tipo = new Tipo();
		this.tipo = tipoControl.getTipoByTipo(tipo.getTipo());
		if(this.tipo == null){
			tipoControl.create(tipo);
			this.tipo = tipoControl.getTipoByTipo(tipo.getTipo());
		}else{
			tipo.setIdTipo(this.tipo.getIdTipo());
			tipoControl.update(tipo);
		}
	}
	/**
	 * 
	 * @param cartao
	 */
	private void setCartao(Cartao cartao){
		this.cartao = new Cartao();
		this.cartao = cartaoControl.getCartaoByOperadoraBanderira(cartao.getOperadora(), cartao.getBandeira());
		if(this.cartao == null){
			cartaoControl.create(cartao);
			this.cartao = cartaoControl.getCartaoByOperadoraBanderira(cartao.getOperadora(), cartao.getBandeira());
		}else{
			cartao.setIdcartao(this.cartao.getIdcartao());
			cartaoControl.update(cartao);
		}
	}
	/**
	 * 
	 * @param debito
	 */
	private void setDebito(Debito debito){
		this.debito = new Debito();
		this.debito = debitoControl.getDebitoByOrdem(debito.getOrdem());
		if(this.debito == null){
			debito.setCartao(cartao);
			debito.setCredor(credor);
			debito.setDevedor(devedor);
			debito.setTipo(tipo);			
			debitoControl.create(debito);
			this.debito = debitoControl.getDebitoByOrdem(debito.getOrdem());
		}else{
			debito.setIddebito(this.debito.getIddebito());
			debito.setCartao(cartao);
			debito.setCredor(credor);
			debito.setDevedor(devedor);
			debito.setTipo(tipo);			
			debitoControl.update(debito);
		}
	}
	/**
	 * 
	 * @param parcela
	 */
	private void setParcela(Parcela parcela){
		this.parcela = new Parcela();
		this.parcela = parcelaControl.getParcelaByIddebitoNrparcela(debito.getIddebito(), parcela.getNrParcela());
		if(this.parcela == null){
			parcela.setDebito(debito);
			parcela.setDevedor(devedor);
			parcelaControl.create(parcela);
			this.parcela = parcelaControl.getParcelaByIddebitoNrparcela(debito.getIddebito(), parcela.getNrParcela());
		}else{
			parcela.setIdparcela(this.parcela.getIdparcela());
			parcela.setDebito(debito);
			parcela.setDevedor(devedor);
			parcelaControl.update(parcela);
		}
	}
}
