package br.com.dobar.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import br.com.dobar.R;

public abstract class AbstractActivity extends Activity {
	
	protected ImageButton ibtApagar;
	protected ImageButton ibtNovo;
	protected ImageButton ibtSalvar;
	protected ImageButton ibtEditar;
	protected ImageButton ibtSearch;
	protected AlertDialog dialogConfirmacao;
	protected EditText edtSearch;
	protected TextView txtSearch;
	protected ListView listView;
	
	
/**
 * 
 * @param view
 */
	public void selecionarAcao(View view) {

		switch (view.getId()) {
		case R.id.btsearch:
			iniciaTelaDeBusca();
			break;
		case R.id.btnovo:
			novoCadastro();
			break;
		case R.id.btsalvar:
			if (validarDados()) {
				dialogConfirmacao = dialogSalvar();
				dialogConfirmacao.show();
			}
			break;
		case R.id.bteditar:
			editarCadastro();
			break;
		case R.id.btexcluir:
			dialogConfirmacao = dialogExcluir();
			dialogConfirmacao.show();
			break;
		case R.id.btfind:
			getList();
			break;
		}

	}
	
	/**
	 * 
	 * @param consultar
	 * @param novo
	 * @param salvar
	 * @param editar
	 * @param deletar
	 */
	protected void controlaBotoes(boolean consultar, boolean novo,
			boolean salvar, boolean editar, boolean deletar) {
		if (consultar) {
			ibtSearch.setEnabled(true);
			ibtSearch.setImageDrawable(getResources().getDrawable(
					R.drawable.seach));
		} else {
			ibtSearch.setEnabled(false);
			ibtSearch.setImageDrawable(getResources().getDrawable(
					R.drawable.search_d));

		}

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
	 */
	protected void iniciaTelaDeBusca() {
		setContentView(R.layout.search);
		edtSearch = (EditText) findViewById(R.id.edtsearch);
		txtSearch = (TextView) findViewById(R.id.txvsearch);
		listView = (ListView) findViewById(R.id.list);
	}
	/**
	 * 
	 * @return
	 */
	protected AlertDialog dialogSalvar() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirmacaosalvar);
		builder.setPositiveButton(getString(R.string.sim),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						salvarCadastro();
					}
				});
		builder.setNegativeButton(getString(R.string.nao),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialogConfirmacao.dismiss();
					}
				});
		return builder.create();
	}
	
	/**
	 * 
	 * @return
	 */
	protected AlertDialog dialogExcluir() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirmacaoexcluir);
		builder.setPositiveButton(getString(R.string.sim),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						excluirCadastro();
					}
				});
		builder.setNegativeButton(getString(R.string.nao),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialogConfirmacao.dismiss();
					}
				});
		return builder.create();
	}
	/**
	 * 
	 */
	protected abstract void salvarCadastro();
	protected abstract void excluirCadastro();
	protected abstract void novoCadastro();
	protected abstract boolean validarDados();
	protected abstract void editarCadastro();
	protected abstract void getList();
}
