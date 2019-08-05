package br.com.dobar.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import br.com.dobar.R;

public class Configuracoes extends PreferenceActivity {

	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.configuracoes);
	}

}
