package br.edu.ufcg.async;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import br.edu.ufcg.model.Loja;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 
 * Classe respons�vel por transformar a resposta
 * da requisi��o de recuperar Lojas ao servidor
 * em uma lista de lojas que poder� ser usada
 * pela aplica��o. 
 * 
 * @author Erickson Santos
 * @since Projeto 2
 *
 */
public class GetLojasHandler extends AsyncHttpResponseHandler {

	private static final String NOME_LOJA = "nome";
	private static final String ID = "id";
	private static final String LOGO_LOJA = "logo";

	private List<Loja> lojas;
	private Listener listener;

	public GetLojasHandler(Listener r) {
		this.listener = r;
	}

	@Override
	public void onSuccess(String response) {
		List<Loja> Lojas = new ArrayList<Loja>();
		try {
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				Lojas.add(recuperaLoja(item));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.lojas = Lojas;
		System.out.println("lojas: " + lojas);
		listener.notifica();
	}

	@Override
	public void onFailure(Throwable t, String arg1) {
		t.printStackTrace();
	}

	private Loja recuperaLoja(JSONObject item) throws JSONException {
		int id = item.getInt(ID);
		String nomeLoja = item.getString(NOME_LOJA);
		byte[] logoLoja = Base64.decode(item.getString(LOGO_LOJA), Base64.DEFAULT);
		return new Loja(id, nomeLoja, logoLoja);
	}

	public List<Loja> getLojas() {
		return this.lojas;
	}

}