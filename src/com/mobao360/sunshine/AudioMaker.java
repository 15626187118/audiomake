package com.mobao360.sunshine;

import java.io.File;


import android.app.Activity;
import android.content.Context;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AudioMaker extends Activity {
    /** Called when the activity is first created. */
    static  int frequency = 44100;//分辨率  
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;  
    static final int audioEncodeing = AudioFormat.ENCODING_PCM_16BIT; 
    static final int yMax = 50;//Y轴缩小比例最大值  
    static final int yMin = 1;//Y轴缩小比例最小值  
	
	int minBufferSize;//采集数据需要的缓冲区大小
	AudioRecord audioRecord;//录音
	AudioProcess audioProcess = new AudioProcess();//处理
    Button btnStart,btnExit,btnData;  //开始停止按钮
    SurfaceView sfv;  //绘图所用
 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
   
        setContentView(R.layout.main);
        initControl();
        btnData.setEnabled(false);
        }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
  //初始化控件信息
    private void initControl() {
    	//获取采样率     
        Context mContext = getApplicationContext();
        //按键
        btnStart = (Button)this.findViewById(R.id.btnStart);
        btnExit = (Button)this.findViewById(R.id.btnExit);
       btnData=(Button)this.findViewById(R.id.btndata);
        //按键事件处理
       btnData.setOnClickListener(new ClickEvent());
        btnStart.setOnClickListener(new ClickEvent());
        btnExit.setOnClickListener(new ClickEvent());
        //画笔和画板
        sfv = (SurfaceView)this.findViewById(R.id.SurfaceView01);
        //初始化显示
        audioProcess.initDraw(yMax/2, sfv.getHeight(),mContext,frequency);
        //画板缩放
        
	}
    
    /**
     * 按键事件处理
     */
    class ClickEvent implements View.OnClickListener{
    	@Override
    	public void onClick(View v){
    		Button button = (Button)v;
    		if(button == btnStart){
    			if(button.getText().toString().equals("Start")){
        	        try {
        	        	btnData.setEnabled(true);
            			//录音
            	        minBufferSize = AudioRecord.getMinBufferSize(frequency, 
            	        		channelConfiguration, 
            	        		audioEncodeing);
            	     
            	        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,frequency,
            	        		channelConfiguration,
            	        		audioEncodeing,
            	        		minBufferSize);
            			audioProcess.baseLine = sfv.getHeight()-100;
            			audioProcess.frequence = frequency;
            			audioProcess.start(audioRecord, minBufferSize, sfv);
            			Toast.makeText(AudioMaker.this, 
            					"当前设备支持您所选择的采样率:"+String.valueOf(frequency), 
            					Toast.LENGTH_SHORT).show();
            			btnStart.setText(R.string.btn_exit);
    				} catch (Exception e) {
    					// TODO: handle exception
            			Toast.makeText(AudioMaker.this, 
            					"当前设备不支持你所选择的采样率"+String.valueOf(frequency)+",请重新选择", 
            					Toast.LENGTH_SHORT).show();
    				}
        		}else if (button.getText().equals("Stop")) {
    				btnStart.setText(R.string.btn_start);
    				audioProcess.stop(sfv);
    			}
    		}
    		if(button ==  btnExit) {
    			new AlertDialog.Builder(AudioMaker.this) 
    	         .setTitle("提示") 
    	         .setMessage("确定退出?") 
    	         .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
    	        public void onClick(DialogInterface dialog, int whichButton) { 
    	        setResult(RESULT_OK);//确定按钮事件 
 				AudioMaker.this.finish();
    	         finish(); 
    	         } 
    	         }) 
    	         .setNegativeButton("取消", new DialogInterface.OnClickListener() { 
    	        public void onClick(DialogInterface dialog, int whichButton) { 
    	         //取消按钮事件 
    	         } 
    	         }) 
    	         .show();
			}
    		if(button ==  btnData) {
    			Toast.makeText(AudioMaker.this, 
    					"已记录", 
    					Toast.LENGTH_SHORT).show();
    			audioProcess.data();
    			btnData.setEnabled(false);
    		}
    		
    	}
    }
}