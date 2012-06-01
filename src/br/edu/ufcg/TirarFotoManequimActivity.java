package br.edu.ufcg;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;

public class TirarFotoManequimActivity extends Activity implements ImageListener, OnClickListener {

	private CameraView mPreview;
	private BDAdapter dao;
	public ImageButton fotografarButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		mPreview = (CameraView) findViewById(R.id.imagem_camera);
		mPreview.addImageListener(this);
		
//		LinearLayout layout = new LinearLayout(this);
//		layout.setBackgroundResource(R.drawable.tracejado2);
//		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		fotografarButton = new ImageButton(this);
		fotografarButton.setImageResource(R.drawable.camera);
		fotografarButton.setBackgroundColor(Color.TRANSPARENT);
		fotografarButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				mPreview.takePicture();				
			}
		});

		RelativeLayout layoutFotografar = new RelativeLayout(this);
		LinearLayout linearFotografar = new LinearLayout(this);
		linearFotografar.addView(fotografarButton);
		linearFotografar.setGravity(Gravity.RIGHT);
		linearFotografar.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layoutFotografar.addView(linearFotografar);
		layoutFotografar.setGravity(Gravity.BOTTOM);
		addContentView(layoutFotografar, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this.dao = new BDAdapter(this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflate = new MenuInflater(this);
//		inflate.inflate(R.menu.tirar_foto_menu, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		mPreview.takePicture();
//		return true;
//	}

	public void takeImage(byte[] image) { 
		// Cleaning memory 
		
		System.gc(); 
		mPreview.setVisibility(View.INVISIBLE);
		mPreview.freezeCamera();
		
		Bitmap plecas = BitmapFactory.decodeByteArray(image, 0, image.length);
		Bitmap mudado = Bitmap.createScaledBitmap(plecas, 500, 500, true);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		mudado.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		
		this.dao.inserirManequim(bitmapdata);

		this.finish();
	}

	public void onClick(View v) {
		mPreview.takePicture();

	}


}