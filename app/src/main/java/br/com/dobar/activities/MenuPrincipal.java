package br.com.dobar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import br.com.dobar.R;

public class MenuPrincipal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuprincipal);
	}

	/**
	 * 
	 * @param tela
	 */
	public void executarAcao(View tela) {
		switch (tela.getId()) {
		case R.id.menup_cartao:
			startActivity(new Intent(this, CadastroCartao.class));
			break;
		case R.id.menup_credor:
			startActivity(new Intent(this, CadastroCredor.class));
			break;
		case R.id.menup_devedor:
			startActivity(new Intent(this, CadastroDevedor.class));
			break;
		case R.id.menup_tipo:
			startActivity(new Intent(this, CadastroTipo.class));
			break;
		case R.id.menup_limite:
			startActivity(new Intent(this, CadastroLimite.class));
			break;
		case R.id.menup_debito:
			startActivity(new Intent(this, LancarDebito.class));
			break;
		case R.id.menup_configurar:
			startActivity(new Intent(this, Configuracoes.class));
			break;
		case R.id.menup_consulta:
			startActivity(new Intent(this, ParametrosConsulta.class));
			break;
		case R.id.menup_sincro:
			startActivity(new Intent(this, Sincronizar.class));
			break;	
		default:
			break;
		}
	}

}
