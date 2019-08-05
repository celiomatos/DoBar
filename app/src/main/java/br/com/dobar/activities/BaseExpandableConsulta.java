package br.com.dobar.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import br.com.dobar.R;
import br.com.dobar.beans.ChildItem;
import br.com.dobar.beans.ChildItemList;
import br.com.dobar.beans.GroupItem;

public class BaseExpandableConsulta extends BaseExpandableListAdapter {

	private List<GroupItem> groupItem;
	private List<ChildItemList> childItem = new ArrayList<ChildItemList>();
	private LayoutInflater minflater;

	/**
	 * 
	 * @param mInflater
	 * @param activity
	 */
	public void setInflater(LayoutInflater mInflater, Activity activity) {
		this.minflater = mInflater;
	}

	/**
	 * 
	 * @param gropList
	 * @param childList
	 */
	public void setBaseListAdapter(List<GroupItem> gropList,
			List<ChildItemList> childList) {
		groupItem = gropList;
		childItem = childList;
	}

	/**
	 * 
	 */
	public Object getChild(int arg0, int arg1) {
		return null;
	}

	/**
	 * 
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	/**
	 * 
	 */
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		List<ChildItem> childTemp = childItem.get(groupPosition).getListChild();

		if (convertView == null) {
			convertView = minflater.inflate(R.layout.childs, null);
		}
		// credor ou vencimento
		TextView txtColum01 = (TextView) convertView
				.findViewById(R.id.child_txtcolum01);
		txtColum01.setText(childTemp.get(childPosition).getColum01());

		// parcela
		TextView txtColum02 = (TextView) convertView
				.findViewById(R.id.child_txtcolum02);
		txtColum02.setText(childTemp.get(childPosition).getColum02());

		// valor da parcela
		TextView txtColum03 = (TextView) convertView
				.findViewById(R.id.child_txtcolum03);
		txtColum03.setText("R$ "+childTemp.get(childPosition).getColum03());

		return convertView;
	}

	/**
	 * 
	 */
	public int getChildrenCount(int groupPosition) {
		return childItem.get(groupPosition).getListChild().size();
	}

	/**
	 * 
	 */
	public Object getGroup(int groupPosition) {
		return null;
	}

	/**
	 * 
	 */
	public int getGroupCount() {
		return groupItem.size();
	}

	/**
	 * 
	 */
	public long getGroupId(int groupPosition) {
		return 0;
	}

	/**
	 * 
	 */
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.grupo, null);
		}
		// coluna 01
		TextView txtColum01 = (TextView) convertView
				.findViewById(R.id.grupo_txtcolum01);
		txtColum01.setText(groupItem.get(groupPosition).getStrGrupo());
		// coluna 02
		TextView txtColum02 = (TextView) convertView
				.findViewById(R.id.grupo_txtcolum02);
		txtColum02.setText("R$ "+ groupItem.get(groupPosition).getStrValor());

		return convertView;
	}

	/**
	 * 
	 */
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * 
	 */
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
