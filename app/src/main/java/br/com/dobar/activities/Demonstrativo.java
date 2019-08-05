package br.com.dobar.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;
import br.com.dobar.R;
import br.com.dobar.beans.ChildItem;
import br.com.dobar.beans.ChildItemList;
import br.com.dobar.beans.GroupItem;
import br.com.dobar.daos.controls.ParcelaControl;
import br.com.dobar.daos.entities.Parcela;
import br.com.dobar.utils.Constantes;
import br.com.dobar.utils.Utils;

public class Demonstrativo extends ExpandableListActivity {

	private String strSql;
	private String credores;
	private String devedores;
	private String emitepor;
	private List<Parcela> list;
	private BaseExpandableConsulta expConsulta;
	private TextView txtcredor;
	private TextView txtdevedor;
	private TextView txttotal;
	private BigDecimal totalGeral = new BigDecimal(0);
	private List<GroupItem> listGroup;
	private List<ChildItemList> listChildList;
	private int group;
	private int child;
	private ParcelaControl parcelaControl = new ParcelaControl(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demonstrativo);
		iniciaComponentes();

	}

	/**
	 * 
	 */
	private void iniciaComponentes() {
		strSql = getIntent().getStringExtra(Constantes.SQL);
		credores = getIntent().getStringExtra(Constantes.CREDORES);
		devedores = getIntent().getStringExtra(Constantes.DEVEDORES);
		emitepor = getIntent().getStringExtra(Constantes.EMISSAO);

		txtcredor = (TextView) findViewById(R.id.demo_credor);
		txtcredor.setText("Credor: " + credores);
		txtdevedor = (TextView) findViewById(R.id.demo_devedor);
		txtdevedor.setText("Devedor: " + devedores);
		txttotal = (TextView) findViewById(R.id.demo_total);

		getListas();

	}

	/**
	 * 
	 */
	private void getListas() {
		ParcelaControl parcelaControl = new ParcelaControl(this);
		list = parcelaControl.getListCustom(strSql);

		// escolha como sera exibido a consulta
		if (emitepor.equalsIgnoreCase(Constantes.CREDORES)) {
			setListPorCredores();
		} else {
			setListPorDevedores();
		}

		expConsulta = new BaseExpandableConsulta();
		renderizaTela();
		registerForContextMenu(getExpandableListView());
	}

	/**
	 * 
	 */
	private void renderizaTela() {
		expConsulta.setBaseListAdapter(listGroup, listChildList);
		expConsulta
				.setInflater(
						(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
						this);
		getExpandableListView().setAdapter(expConsulta);

	}

	/**
	 * 
	 */
	private void setListPorCredores() {

		listGroup = new ArrayList<GroupItem>();
		listChildList = new ArrayList<ChildItemList>();
		for (Parcela parcela : list) {
			if (parcela.getDebito().getFormPagamento() != Constantes.CARTAO) {
				GroupItem groupItem = new GroupItem();
				groupItem.setStrGrupo(parcela.getDebito().getCredor()
						.getRazao());
				groupItem.setStrEmail(parcela.getDebito().getCredor()
						.getEmail());
				groupItem.setStrFone(parcela.getDebito().getCredor()
						.getTelefone());
				if (!listGroup.contains(groupItem)) {
					listGroup.add(groupItem);
				}

			} else {
				GroupItem groupItem = new GroupItem();
				groupItem.setStrGrupo("CARTAO: "
						+ parcela.getDebito().getCartao().getOperadora());
				groupItem.setStrFone(parcela.getDebito().getCartao()
						.getTelefone());
				if (!listGroup.contains(groupItem)) {
					listGroup.add(groupItem);
				}
			}
		}

		for (GroupItem groupItem : listGroup) {
			List<ChildItem> listChild = new ArrayList<ChildItem>();
			BigDecimal totalGrupo = new BigDecimal(0);
			for (Parcela parcela : list) {
				if (parcela.getDebito().getFormPagamento() != Constantes.CARTAO) {
					if (parcela.getDebito().getCredor().getRazao()
							.equalsIgnoreCase(groupItem.getStrGrupo())) {

						ChildItem childItem = new ChildItem();

						childItem.setIdgroup(parcela.getDebito().getIddebito());
						childItem.setIdchild(parcela.getIdparcela());
						childItem.setColum01(Utils.getDDMMYYYY(parcela
								.getDataVencimento()));
						childItem.setColum02(parcela.getNrParcela());
						childItem.setColum03(parcela.getVlParcela()
								.setScale(2, BigDecimal.ROUND_UP).toString());
						childItem.setDtPagamento(parcela.getDataPagamento());

						listChild.add(childItem);

						totalGrupo = totalGrupo.add(parcela.getVlParcela());
					}
				} else {
					if (groupItem.getStrGrupo().equals(
							"CARTAO: "
									+ parcela.getDebito().getCartao()
											.getOperadora())) {

						ChildItem childItem = new ChildItem();

						childItem.setIdgroup(parcela.getDebito().getIddebito());
						childItem.setIdchild(parcela.getIdparcela());
						childItem.setColum01(parcela.getDebito().getCredor()
								.getRazao());
						childItem.setColum02(parcela.getNrParcela());
						childItem.setColum03(parcela.getVlParcela()
								.setScale(2, BigDecimal.ROUND_UP).toString());
						childItem.setDtPagamento(parcela.getDataPagamento());
						listChild.add(childItem);

						totalGrupo = totalGrupo.add(parcela.getVlParcela());
					}
				}
			}
			ChildItemList childItemList = new ChildItemList();
			childItemList.setListChild(listChild);
			listChildList.add(childItemList);
			groupItem.setStrValor(totalGrupo.setScale(2, BigDecimal.ROUND_UP)
					.toString());
			totalGeral = totalGeral.add(totalGrupo);
		}
		txttotal.setText("Total R$ "
				+ totalGeral.setScale(2, BigDecimal.ROUND_UP).toString());

	}

	/**
	 * 
	 */
	private void setListPorDevedores() {

		listGroup = new ArrayList<GroupItem>();
		listChildList = new ArrayList<ChildItemList>();
		for (Parcela parcela : list) {

			GroupItem groupItem = new GroupItem();
			groupItem.setStrGrupo(parcela.getDevedor().getNome());
			groupItem.setStrEmail(parcela.getDevedor().getEmail());
			groupItem.setStrFone(parcela.getDevedor().getTelefone());

			if (!listGroup.contains(groupItem)) {
				listGroup.add(groupItem);
			}

		}

		for (GroupItem groupItem : listGroup) {
			List<ChildItem> listChild = new ArrayList<ChildItem>();
			BigDecimal totalGrupo = new BigDecimal(0);
			for (Parcela parcela : list) {

				if (parcela.getDevedor().getNome()
						.equalsIgnoreCase(groupItem.getStrGrupo())) {
					ChildItem childItem = new ChildItem();
					childItem.setIdgroup(parcela.getDebito().getIddebito());
					childItem.setIdchild(parcela.getIdparcela());
					childItem.setColum01(parcela.getDebito().getCredor()
							.getRazao());
					childItem.setColum02(parcela.getNrParcela());
					childItem.setColum03(parcela.getVlParcela()
							.setScale(2, BigDecimal.ROUND_UP).toString());
					childItem.setDtPagamento(parcela.getDataPagamento());
					listChild.add(childItem);
					totalGrupo = totalGrupo.add(parcela.getVlParcela());
				}
			}
			ChildItemList childItemList = new ChildItemList();
			childItemList.setListChild(listChild);
			listChildList.add(childItemList);
			groupItem.setStrValor(totalGrupo.setScale(2, BigDecimal.ROUND_UP)
					.toString());
			totalGeral = totalGeral.add(totalGrupo);
		}
		txttotal.setText("Total R$ "
				+ totalGeral.setScale(2, BigDecimal.ROUND_UP).toString());
	}

	/**
	 * 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

		int tipo = ExpandableListView
				.getPackedPositionType(info.packedPosition);

		if (tipo == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			getMenuInflater().inflate(R.menu.childopcoes, menu);
		} else {
			getMenuInflater().inflate(R.menu.groupopcoes, menu);
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
				.getMenuInfo();
		group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		child = ExpandableListView.getPackedPositionChild(info.packedPosition);

		switch (item.getItemId()) {
		case R.id.menu_groupopcoes_pagar:
			dialogConfirmacao(Constantes.PAGARFATURA, R.string.confirmpagamento);
			break;
		case R.id.menu_groupopcoes_estornar:
			dialogConfirmacao(Constantes.ESTORNARFATURA,
					R.string.confirmestorno);
			break;
		case R.id.menu_groupopcoes_enviar:
			enviarMGS();
			break;
		case R.id.menu_groupopcoes_telefonar:
			telefonar();
			break;
		case R.id.menu_childopcoes_editar_debito:
			editaDebito();
			break;
		case R.id.menu_childopcoes_editar_parcela:
			Intent editaParcela = new Intent(this, EditaParcela.class);
			editaParcela.putExtra(Constantes.IDPARCELA, listChildList.get(group).getListChild().get(child).getIdchild());
			startActivity(editaParcela);
			break;
		case R.id.menu_childopcoes_detalhes:

			break;

		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 
	 */
	private void pagarFatura() {
		StringBuilder sqlPagarFatura = new StringBuilder(
				"update tbparcela set pago = '1' where _id in(");
		List<ChildItem> listTemp = listChildList.get(group).getListChild();
		for (int i = 0; i < listTemp.size(); i++) {
			if (i < listTemp.size() - 1) {
				sqlPagarFatura.append(listTemp.get(i).getIdchild());
				sqlPagarFatura.append(",");
			} else {
				sqlPagarFatura.append(listTemp.get(i).getIdchild());
				sqlPagarFatura.append(")");
			}
		}

		totalGeral = totalGeral.subtract(new BigDecimal(listGroup.get(group)
				.getStrValor()));

		parcelaControl.executaSQL(sqlPagarFatura.toString());

		listChildList.remove(group);
		listGroup.remove(group);

		txttotal.setText("Total R$ "
				+ totalGeral.setScale(2, BigDecimal.ROUND_UP).toString());

		renderizaTela();

	}

	/**
	 * 
	 */
	private void estornarFatura() {
		StringBuilder sqlEstornarFatura = new StringBuilder(
				"update tbparcela set pago = '0' where _id in(");
		List<ChildItem> listTemp = listChildList.get(group).getListChild();
		for (int i = 0; i < listTemp.size(); i++) {
			if (i < listTemp.size() - 1) {
				sqlEstornarFatura.append(listTemp.get(i).getIdchild());
				sqlEstornarFatura.append(",");
			} else {
				sqlEstornarFatura.append(listTemp.get(i).getIdchild());
				sqlEstornarFatura.append(")");
			}
		}

		totalGeral = totalGeral.subtract(new BigDecimal(listGroup.get(group)
				.getStrValor()));

		parcelaControl.executaSQL(sqlEstornarFatura.toString());

		listChildList.remove(group);
		listGroup.remove(group);

		txttotal.setText("Total R$ "
				+ totalGeral.setScale(2, BigDecimal.ROUND_UP).toString());

		renderizaTela();
	}

	/**
	 * 
	 */
	private void enviarMGS() {
		String strEmail = listGroup.get(group).getStrEmail();
		String strCredor = listGroup.get(group).getStrGrupo();
		String strTotal = listGroup.get(group).getStrValor();
		// corpo da msg
		StringBuilder strDetalhe = new StringBuilder();
		strDetalhe.append("Total R$ ");
		strDetalhe.append(strTotal);
		strDetalhe.append("\n\n");
		for (ChildItem chiltTemp : listChildList.get(group).getListChild()) {
			strDetalhe
					.append(Utils.completaEspacos(chiltTemp.getColum01(), 15));
			strDetalhe.append("\t");
			strDetalhe.append(chiltTemp.getColum02());
			strDetalhe.append("\t");
			strDetalhe.append(chiltTemp.getColum03());
			strDetalhe.append("\n");
		}

		Intent msg = new Intent(Intent.ACTION_SEND);
		msg.putExtra(Intent.EXTRA_EMAIL, new String[] { strEmail });
		msg.putExtra(Intent.EXTRA_SUBJECT, "Fatura " + strCredor);
		msg.putExtra(Intent.EXTRA_TEXT, strDetalhe.toString());
		msg.setType("text/plain");
		startActivity(Intent.createChooser(msg, "Escolha como enviar"));
	}

	/**
	 * 
	 */
	private void telefonar() {
		Intent intentCall = new Intent(Intent.ACTION_DIAL);
		intentCall.setData(Uri
				.parse("tel:" + listGroup.get(group).getStrFone()));
		startActivity(intentCall);
	}

	/**
	 * 
	 * @param operacao
	 * @param grupo
	 * @return
	 */
	private void dialogConfirmacao(final int operacao, int msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setPositiveButton(getString(R.string.sim),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (operacao == Constantes.PAGARFATURA) {
							pagarFatura();
						} else {
							estornarFatura();
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
	 */
	private void editaDebito() {
		Intent lancarDebito = new Intent(this, LancarDebito.class);
		Long iddebito = listChildList.get(group).getListChild().get(child)
				.getIdgroup();
		String parcelas[] = listChildList.get(group).getListChild().get(child)
				.getColum02().split("/");
		String dtPagamento = Utils.getMesAnterior(listChildList.get(group)
				.getListChild().get(child).getDtPagamento(),
				Integer.parseInt(parcelas[0]) - 1);
		lancarDebito.putExtra(Constantes.IDDEBITO, iddebito);
		lancarDebito.putExtra(Constantes.TOTALPARCELAS, parcelas[1]);
		lancarDebito.putExtra(Constantes.DATAPAGAMENTO, dtPagamento);
		startActivity(lancarDebito);
	}

}
