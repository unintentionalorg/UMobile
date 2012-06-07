package org.unintentional.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

public class UMobileActivity extends Activity {
	
	 public static final int CAMERA_CODE = 100;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        disableStep2();
        
    }
    
    public void takeSnapshot (View aView){
    	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	startActivityForResult(cameraIntent,CAMERA_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode==CAMERA_CODE && resultCode==RESULT_OK){
    		enableStep2();
    	}
    }
    
    
    public void disableStep2(){
    	findViewById(R.id.email_button).setEnabled(false);
    	findViewById(R.id.descr).setEnabled(false);
    	findViewById(R.id.edit_descr).setEnabled(false);
    	findViewById(R.id.subj).setEnabled(false);
    	findViewById(R.id.edit_subj).setEnabled(false);
    	findViewById(R.id.textView5).setEnabled(false);
    }
    
    public void enableStep2(){
    	findViewById(R.id.email_button).setEnabled(true);
    	findViewById(R.id.descr).setEnabled(true);
    	findViewById(R.id.edit_descr).setEnabled(true);
    	findViewById(R.id.subj).setEnabled(true);
    	findViewById(R.id.edit_subj).setEnabled(true);
    	findViewById(R.id.textView5).setEnabled(true);
    }
    
    public void sendArtwork(View aView){
    	Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show();   
    	cleanAll();
    }
    public void cleanAll(){
    	disableStep2();
    }
    
}