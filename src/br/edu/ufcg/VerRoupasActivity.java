package br.edu.ufcg;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Calibragem;
import br.edu.ufcg.model.Calibragem2;
import br.edu.ufcg.model.Roupa;

public class VerRoupasActivity extends Activity {

	private BDAdapter dao;
	private VisualizadorRoupa visualizadorRoupa;
	public ImageButton anteriorButton;
	public ImageButton proximaButton;
	public ImageButton addButton;
	public ImageButton deleteButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new BDAdapter(getApplicationContext());
		byte[] manequim = dao.getManequimPadrao();

		
		visualizadorRoupa = new VisualizadorRoupa(this, dao.getRoupas());
		visualizadorRoupa.setBackgroundDrawable(carregaDrawable(manequim));
		
		setContentView(visualizadorRoupa);

		addButton = new ImageButton(this);
		addButton.setImageResource(R.drawable.add);
		addButton.setBackgroundColor(Color.TRANSPARENT);
		addButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), TirarFotoRoupaActivity.class);
				startActivity(i);				
			}
		});
		
		
		deleteButton = new ImageButton(this);
		deleteButton.setImageResource(R.drawable.delete);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		deleteButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				visualizadorRoupa.removeImagem();			
			}
		});
		
		proximaButton = new ImageButton(this);
		if (dao.getRoupas().isEmpty()) {
			proximaButton.setImageResource(R.drawable.next_cinza);
		} else {			
			proximaButton.setImageResource(R.drawable.next);
		}
		proximaButton.setBackgroundColor(Color.TRANSPARENT);
		proximaButton.setOnClickListener(new ProximoListener());

		anteriorButton = new ImageButton(this);
		anteriorButton.setImageResource(R.drawable.previous_cinza);
		anteriorButton.setBackgroundColor(Color.TRANSPARENT);
		anteriorButton.setOnClickListener(new VoltaListener());

		Button meioButton = new Button(this);
		meioButton.setText("Calibrar");
		meioButton.setOnClickListener(new CalibrarListener());

		
		RelativeLayout layoutAdd = new RelativeLayout(this);
		LinearLayout linearAdd = new LinearLayout(this);
		linearAdd.addView(addButton);
		linearAdd.setGravity(Gravity.LEFT);
		linearAdd.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutAdd.addView(linearAdd);
		layoutAdd.setGravity(Gravity.BOTTOM);
		
		RelativeLayout layoutDelete = new RelativeLayout(this);
		LinearLayout linearDelete = new LinearLayout(this);
		linearDelete.addView(deleteButton);
		linearDelete.setGravity(Gravity.RIGHT);
		linearDelete.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutDelete.addView(linearDelete);
		layoutDelete.setGravity(Gravity.BOTTOM);
		
		RelativeLayout layoutProximo = new RelativeLayout(this);
		LinearLayout linearProximo = new LinearLayout(this);
		linearProximo.addView(proximaButton);
		linearProximo.setGravity(Gravity.RIGHT);
		linearProximo.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutProximo.addView(linearProximo);
		layoutProximo.setGravity(Gravity.CENTER);

		RelativeLayout layoutAnterior = new RelativeLayout(this);
		LinearLayout linearAnterior = new LinearLayout(this);
		linearAnterior.addView(anteriorButton);
		linearAnterior.setGravity(Gravity.LEFT);
		linearAnterior.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutAnterior.addView(linearAnterior);
		layoutAnterior.setGravity(Gravity.CENTER);

		RelativeLayout layoutMeio = new RelativeLayout(this);
		LinearLayout linearMeio = new LinearLayout(this);
		linearMeio.addView(meioButton);
		linearMeio.setGravity(Gravity.CENTER);
		linearMeio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		layoutMeio.addView(linearMeio);
		layoutMeio.setGravity(Gravity.BOTTOM);

		addContentView(layoutAnterior, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutMeio, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		addContentView(layoutProximo, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutAdd, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addContentView(layoutDelete, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private Drawable carregaDrawable(byte[] imagem) {
		Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
		Matrix matrix = new Matrix();
		
		matrix.setRotate(90);
		Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
				b.getHeight(), matrix, true);
		return new BitmapDrawable(girado);
	}

	private class VoltaListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorRoupa.voltaRoupa();
		}

	}

	private class CalibrarListener implements OnClickListener {

		public void onClick(View arg0) {
			Intent i = new Intent(getApplicationContext(),
					CalibragemRoupasActivity.class);
			i.putExtra("roupa", visualizadorRoupa.getRoupaAtual());
			startActivity(i);
		}

	}

	private class ProximoListener implements OnClickListener {

		public void onClick(View arg0) {
			visualizadorRoupa.proximaRoupa();
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflate = new MenuInflater(this);
//		inflate.inflate(R.menu.ver_roupa_menu, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.removerRoupa:
//			visualizadorRoupa.removeImagem();
//			break;
//		}
//		return true;
//	}

	@Override
	protected void onResume() {
		super.onResume();
		visualizadorRoupa.setRoupas(dao.getRoupas());
		visualizadorRoupa.setPosicaoRoupa(dao.getRoupas().size() - 1);
		visualizadorRoupa.atualizaImagensBotoes();
		
	}

	public class VisualizadorRoupa extends View {

		private List<Roupa> roupas;

		private int posicaoRoupa;
		private Drawable roupaAtual;

		public VisualizadorRoupa(Context context, List<Roupa> roupas) {
			super(context);
			this.roupas = roupas;
			if (!roupas.isEmpty()) {
				roupaAtual = carregaDrawable(roupas.get(posicaoRoupa)
						.getImagem());

				Calibragem calibragemModelo = dao.getCalibragens().get(getRoupaAtual().getCategoria());
				Calibragem2 calibragemRoupa = dao.getCalibragens2().get(
						getRoupaAtual().getId());
				if (calibragemRoupa != null) {
					roupaAtual.setBounds(calibragemRoupa.left, calibragemRoupa.top,
							calibragemRoupa.right, calibragemRoupa.bottom);					
				} else if (calibragemModelo != null){
					roupaAtual.setBounds(calibragemModelo.left, calibragemModelo.top,
							calibragemModelo.right, calibragemModelo.bottom);	
				} else {
					roupaAtual.setBounds(0, 0,
							 100, 100);
				}
			}

			setFocusable(true);
			this.requestFocus();
			this.requestLayout();
		}

		public void removeImagem() {
			if (!roupas.isEmpty()) {
				dao.removeRoupa(roupas.get(posicaoRoupa));
				roupas.remove(posicaoRoupa);
				if (roupas.isEmpty()) {
					atualizaImagensBotoes();
					return;
				} else if (posicaoRoupa == roupas.size()) {
					posicaoRoupa--;
				}
				Roupa roupa = roupas.get(posicaoRoupa);
				roupaAtual = carregaDrawable(roupa.getImagem());
				roupaAtual.setBounds(0, 0, getWidth(), getHeight());
				invalidate();				
			} else {
				Toast.makeText(getContext(), "Não há roupas cadastradas para serem excluídas!", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (!roupas.isEmpty()) {
				Roupa roupa = roupas.get(posicaoRoupa);
				roupaAtual = carregaDrawable(roupa.getImagem());
				Calibragem calibragemModelo = dao.getCalibragens().get(getRoupaAtual().getCategoria());
				Calibragem2 calibragemRoupa = dao.getCalibragens2().get(
						getRoupaAtual().getId());
				if (calibragemRoupa != null) {
					roupaAtual.setBounds(calibragemRoupa.left, calibragemRoupa.top,
							calibragemRoupa.right, calibragemRoupa.bottom);					
				} else if (calibragemModelo != null){
					roupaAtual.setBounds(calibragemModelo.left, calibragemModelo.top,
							calibragemModelo.right, calibragemModelo.bottom);	
				} else {
					roupaAtual.setBounds(0, 0, 100, 100);
				}
				roupaAtual.draw(canvas);				
			}
		}

		public void proximaRoupa() {
			if (posicaoRoupa < roupas.size() - 1) {
				posicaoRoupa++;
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void voltaRoupa() {
			if (posicaoRoupa > 0) {
				posicaoRoupa--;
				invalidate();
			}
			atualizaImagensBotoes();
		}

		public void atualizaImagensBotoes() {
			if (posicaoRoupa > 0) {
				anteriorButton.setImageResource(R.drawable.previous);
			} else {
				anteriorButton.setImageResource(R.drawable.previous_cinza);
			}

			if (posicaoRoupa < roupas.size() - 1) {
				proximaButton.setImageResource(R.drawable.next);
			} else {
				proximaButton.setImageResource(R.drawable.next_cinza);
			}
		}

		private Drawable carregaDrawable(byte[] imagem) {
			Bitmap b = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap girado = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
					b.getHeight(), matrix, true);
			return new BitmapDrawable(girado);
		}

		public Roupa getRoupaAtual() {
			return roupas.get(posicaoRoupa);
		}

		public List<Roupa> getRoupas() {
			return roupas;
		}

		public void setRoupas(List<Roupa> roupas) {
			this.roupas = roupas;
		}

		public int getPosicaoRoupa() {
			return posicaoRoupa;
		}

		public void setPosicaoRoupa(int posicaoRoupa) {
			this.posicaoRoupa = posicaoRoupa;
		}
	}
}