/*  Copyright Michele Borioli (Unintentional.org)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


package org.unintentional.mobile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.unintentional.mobile.R.id;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class UMobileActivity extends Activity {
	
	 public static final int CAMERA_CODE = 100;
	 public static final int EMAIL_CODE = 200;
	 private Uri fileURI;
	 private String strFile;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        disableStep2();
        disableAgainButton();
        
    }
    
    public void takeSnapshot (View aView){
    	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileURI = getOutputImageFileUri(); // create a file to save the image
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileURI); // set the image file name
    	startActivityForResult(cameraIntent,CAMERA_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode==CAMERA_CODE && resultCode==RESULT_OK){
    		step1Completed();
    		enableStep2();
    		
    		
    	}if (requestCode==EMAIL_CODE){
        	Toast.makeText(this, "Email sent (I presume...)", Toast.LENGTH_SHORT).show();   
        	step2Completed();
        	enableAgainButton();  		
    	}
    }
    
    public void step1Completed(){
    	RelativeLayout theLayout = (RelativeLayout)findViewById(R.id.relative_layout);
		ImageView check_ok = new ImageView(this);
		check_ok.setImageResource(R.drawable.check);
		check_ok.setId(998);
		RelativeLayout.LayoutParams pr = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		pr.setMargins(0, 0, 18, 0);
		pr.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		pr.addRule(RelativeLayout.ALIGN_TOP,R.id.take_snapshot);
		check_ok.setLayoutParams(pr);
		theLayout.addView(check_ok);
		findViewById(R.id.take_snapshot).setEnabled(false);
    	
    }
    
    public void enableStep1(){
    	((RelativeLayout)findViewById(R.id.relative_layout)).removeView(findViewById(998));
    	findViewById(R.id.take_snapshot).setEnabled(true);
    }
    
    public void step2Completed(){
    	RelativeLayout theLayout = (RelativeLayout)findViewById(R.id.relative_layout2);
		ImageView check_ok = new ImageView(this);
		check_ok.setImageResource(R.drawable.check);
		check_ok.setId(999);
		RelativeLayout.LayoutParams pr = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		pr.setMargins(0, 0, 18, 0);
		pr.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		pr.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		pr.addRule(RelativeLayout.ALIGN_TOP,R.id.take_snapshot);
		check_ok.setLayoutParams(pr);
		theLayout.addView(check_ok);
		findViewById(R.id.email_button).setEnabled(false);
    }
    
    public void disableStep2(){
    	((EditText)findViewById(R.id.edit_descr)).setText(null);
    	findViewById(R.id.email_button).setEnabled(false);
    	findViewById(R.id.edit_descr).setEnabled(false);
    	findViewById(R.id.Step_2).setEnabled(false);
    	findViewById(R.id.Step_2_descr).setEnabled(false);
    	findViewById(R.id.Step_2_descr_help).setEnabled(false);
    	findViewById(R.id.Step_2_help).setEnabled(false);
    	((RelativeLayout)findViewById(R.id.relative_layout2)).removeView(findViewById(999));
    }
    
    public void enableStep2(){

    	findViewById(R.id.email_button).setEnabled(true);
    	findViewById(R.id.edit_descr).setEnabled(true);
    	findViewById(R.id.Step_2).setEnabled(true);
    	findViewById(R.id.Step_2_descr).setEnabled(true);
    	findViewById(R.id.Step_2_descr_help).setEnabled(true);
    	findViewById(R.id.Step_2_help).setEnabled(true);
    }
    
    
    public void enableAgainButton(){
    	findViewById(R.id.again_button).setEnabled(true);
    }
    
    public void disableAgainButton(){
    	findViewById(R.id.again_button).setEnabled(false);
    }
    
    

    
    public void sendArtwork(View aView){
    	EditText descr = (EditText)findViewById(R.id.edit_descr);
    	Intent emailIntent = new Intent(Intent.ACTION_SEND);
    	emailIntent.setType("message/rfc822");  // attachment is a jpeg
    	emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"contribute@unintentional.org"}); 
    	emailIntent.putExtra(Intent.EXTRA_SUBJECT,"A contribution"); //get subject from one EditText in the UI
    	emailIntent.putExtra(Intent.EXTRA_TEXT,descr.getText().toString()); //get body from one EditText in the UI
      	emailIntent.putExtra(Intent.EXTRA_STREAM, fileURI); // add attachment
    	startActivityForResult(Intent.createChooser(emailIntent, "Choose Email application:"), EMAIL_CODE);

    }
    
    
    public void cleanAll(){
    	enableStep1();
    	disableStep2();
    	disableAgainButton();
    }
    
    public void again(View aView){
    	cleanAll();
    }
    
    
    /** Create a file Uri for saving an image or video */
    private  Uri getOutputImageFileUri(){
          return Uri.fromFile(getOutputImageFile());
    }

    /** Create a File for saving an image or video */
    private  File getOutputImageFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg");        
        return mediaFile;
    }
    

}