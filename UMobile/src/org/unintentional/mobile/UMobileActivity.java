package org.unintentional.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    		
    		enableStep2();    		
    	}if (requestCode==EMAIL_CODE){
        	Toast.makeText(this, "Email sent (I presume...)", Toast.LENGTH_SHORT).show();   
        	cleanAll();  		
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
    	
//		strFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp";
//		File file = new File(strFile);
//		if (!file.exists())
//		file.mkdirs();
//		strFile = strFile + "/report.html";
//		createFile();
    	EditText subj = (EditText)findViewById(R.id.edit_subj);
    	EditText descr = (EditText)findViewById(R.id.edit_descr);
    	//Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto", "michele@borioli.net", null));
    	Intent emailIntent = new Intent(Intent.ACTION_SEND);
    	//emailIntent.setType("multipart/mixed");
    	emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"contribute@unintentional.org"});
    	emailIntent.putExtra(Intent.EXTRA_SUBJECT,subj.getText().toString());
    	emailIntent.putExtra(Intent.EXTRA_TEXT,descr.getText().toString());
    	emailIntent.putExtra(Intent.EXTRA_STREAM, fileURI);
    	startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), EMAIL_CODE);
    	
    	

    }
    public void cleanAll(){
    	disableStep2();
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
    
    private void createFile() {
    	try {
    		
    	String data = "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><html><body style='tab-interval: .5in'><div class=\"Section1\"><div>";
    	data += "<p class=\"MsoNormal\" dir=\"LTR\" style=\"text-align: left; unicode-bidi: embed\"><span lang=\"AR-EG\">";
    	FileOutputStream fos = new FileOutputStream(strFile);
    	 
    	Writer out = new OutputStreamWriter(fos, "UTF-8");
    	 
    	data += "Email data" + "<br />";
    	data += "bla bla bla" + "<br />";
    	data += "Footer" + "<br />";
    	 
    	data += "</span></p></div></body></html>";
    	out.write(data);
    	out.flush();
    	out.close();
    	 
    	} catch (Throwable t) {
    	Toast.makeText(this, "Request failed: " + t.toString(),
    	Toast.LENGTH_LONG).show();
    	 Log.d("strFile", t.getMessage());
    	}
    	}
}