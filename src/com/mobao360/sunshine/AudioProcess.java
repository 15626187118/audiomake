package com.mobao360.sunshine;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.media.AudioRecord;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;

public class AudioProcess {
	double[] reference=new double[31];
	private static final String TAG = "AudioRecord";
	public static final float pi= (float) 3.1415926;
	//应该把处理前后处理后的普线都显示出来
	private ArrayList<short[]> inBuf = new ArrayList<short[]>();//原始录入数据
	private ArrayList<int[]> outBuf = new ArrayList<int[]>();//处理后的数据
	private ArrayList<int[]> outBuf2 = new ArrayList<int[]>();//低频处理后的数据
	private ArrayList<int[]> in = new ArrayList<int[]>();//低频处理后的数据
	  private static final int SPECTROGRAM_COUNT = 31;
	    private static final int LowFreqDividing = 14;
	private boolean isRecording = false;
	private boolean isDataing = true;
	int y2;
	int numnum=0;
	int[] numint={7,9,11,14,17,21,27,34,42,53,67,84,105,132,168,209,264};
	int[] numint0={33,42,53,66,84,105,131,163,209,261,330,418,521,655,827,1041};
	 Global g=new Global();
	 Global g0=new Global();
//	Context mContext = this.getContext();
	//存储读取到的数据
//	FileOutputStream fos;
	//上下文
	Context mContext;
	private int shift = 40;
	public int frequence = 0;
	int resh;
	private int length =0;
	private int length2 = 0;
	//y轴缩小的比例
	public int rateY = 21;
	//y轴基线
	public int baseLine = 0;
	int[] Buf;
	int[] bdata;
	int[] Buf2;
	//初始化画图的一些参数
	public void initDraw(int rateY, int baseLine,Context mContext, int frequence){
		this.mContext = mContext;
		this.rateY = rateY;
		this.baseLine = baseLine;
		this.frequence = frequence;
	}
	//启动程序
	public void start(AudioRecord audioRecord, int minBufferSize, SurfaceView sfvSurfaceView) {
		isRecording = true;
		new RecordThread(audioRecord, minBufferSize).start();
		//new ProcessThread().start();
		new DIG][0RHrawThread(sfvSurfaceView).start();
	}
	//停止程序
	public void stop(SurfaceView sfvSurfaceView){
		isRecording = false;
		inBuf.clear();
		
		//sfvSurfaceView;
		//drawBuf.clear();
		//outBuf.clear();
	}
	//记录数据
		public void data(){
		/*	if(isDataing==true){
			String SDPATH = Environment.getExternalStorageDirectory() + "/formats/lo.txt";//获取文件夹	  
	     	File f = new File(SDPATH); // 输入要删除的文件位置
			if(f.exists())
			f.delete();
			Log.d(TAG,"true");
			
			}*/
			isDataing = true;
		
			
			//sfvSurfaceView;
			//drawBuf.clear();
			//outBuf.clear();
		}
	private void initData(String str) {
		String SDPATH = Environment.getExternalStorageDirectory() + "/formats/";//获取文件夹
	    String filePath = SDPATH;
	    String fileName = "lo.txt";
	     
	    writeTxtToFile(str, filePath, fileName);
	}
	// 将字符串写入到文本文件中
	public void writeTxtToFile(String strcontent, String filePath, String fileName) {
	    //生成文件夹之后，再生成文件，不然会出错
	    makeFilePath(filePath, fileName);
	     
	    String strFilePath = filePath+fileName;
	    // 每次写入时，都换行写
	    String strContent = strcontent + "\r\n";
	    try {
	        File file = new File(strFilePath);
	        if (!file.exists()) {
	            Log.d("TestFile", "Create the file:" + strFilePath);
	            file.getParentFile().mkdirs();
	            file.createNewFile();
	        }
	        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
	        raf.seek(file.length());
	        raf.write(strContent.getBytes());
	        raf.close();
	    } catch (Exception e) {
	        Log.e("TestFile", "Error on write File:" + e);
	    }
	}
	 
	// 生成文件
	public File makeFilePath(String filePath, String fileName) {
	    File file = null;
	    makeRootDirectory(filePath);
	    try {
	        file = new File(filePath + fileName);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return file;
	}
	 
	// 生成文件夹
	public static void makeRootDirectory(String filePath) {
	    File file = null;
	    try {
	        file = new File(filePath);
	        if (!file.exists()) {
	            file.mkdir();
	        }
	    } catch (Exception e) {
	        Log.i("error:", e+"");
	    }
	}
	//录音线程
	class RecordThread extends Thread{
		private AudioRecord audioRecord;
		private int minBufferSize;
		
		public RecordThread(AudioRecord audioRecord,int minBufferSize){
			this.audioRecord = audioRecord;
			this.minBufferSize = minBufferSize;
		}
		
		public void run(){
			try{
				short[] buffer = new short[minBufferSize];
				audioRecord.startRecording();
				//fos = mContext.openFileOutput("da0.txt", Context.MODE_PRIVATE);
				
				 
				while(isRecording){
					int res = audioRecord.read(buffer, 0, minBufferSize);
					
					if(isDataing){
						bdata = new int[res/8];
							 for(int i=0;i<res/8;i++){
								 bdata[i]=(int) ((buffer[i * 8] + buffer[i * 8 + 1] + buffer[i * 8 + 2]
						                    + buffer[i * 8 + 3] + buffer[i * 8 + 4] + buffer[i * 8 + 5]
						                            + buffer[i * 8 + 6] + buffer[i * 8 + 7]) / 8);
									
								
		                    	String str=String.valueOf(bdata[i]);
							 
		                    if(str!="0")
		                    		{initData(str);
		                    		Log.d(TAG,"[["+str+"|//"+buffer[i]);}
		                    	
		    					
							
						 isDataing = false;}
					}
				
                  
					//将数据写入文件,以供分析使用
			/*	for(int i = 0; i < res; i++){
					String str = Short.toString(buffer[i]);
					fos.write(str.getBytes());
					fos.write(' ');
				}*/
//					fos.write('\n');
					
					//将录音结果存放到inBuf中,以备画时域图使用
					synchronized (inBuf){
						inBuf.add(buffer);
					}
					//保证长度为2的幂次数
					length=up2int(res);					
				//	length = 256;
					short[]tmpBuf = new short[length];
					System.arraycopy(buffer, 0, tmpBuf, 0, length);
					Complex[]complexs = new Complex[length];
					int[]outInt = new int[length];
					for(int i=0;i < length; i++){
						
						Short short1 = tmpBuf[i];
						complexs[i] = new Complex(short1.doubleValue());
					}
					fft(complexs,length);
				/*	for (int i = 0; i < length; i++) {
						outInt[i] = complexs[i].getIntValue();
						//Log.d(TAG,"p"+outInt[i]);
					}*/
					int sumi0=0;
					int sums0=0;
					int numb0=0;
					double sumd0=0;
					 g0.num2=16;
					for (int i = 33; i <1041; i++) {
						outInt[i] = complexs[i].getIntValue();
					//		 Log.d(TAG,"__"+g.numi+" "+ String.valueOf(i)+"||||||"+outInt2[i]);
							int numi0=0;
							numi0=(int)g0.numi;
							
							 if(i>=numint0[numi0]&&i<numint0[numi0+1])
							 {
								
								  sumi0+=outInt[i];
								
								 g0.sum=(double)sumi0;
								if(i==(numint0[numi0+1]-1))
									sumi0=0;
								
						//		 Log.d(TAG,"pppp"+String.valueOf(g.sum));
								   
							//	  if(outInt2[i]>0)
									   g0.num++;
									  
								   if(i==(numint0[numi0+1]-1)){
									   if(g0.num==0)g0.sampleratePoint2[g0.num2]=0;
									    else
									    g.sampleratePoint2[g0.num2]=g0.sum/(double)g0.num;
									//    Log.d(TAG,"GGGG"+String.valueOf( g.sampleratePoint2[g0.num2])+"|"+String.valueOf(g0.num2)+"|"+String.valueOf(g0.num));
									}
								
							 }
					
						
							 if(i==(numint0[g0.numi+1]-1)){
								 g0.numi++;
								 g0.num2++;
								  g0.sum=0;
								  g0.num=0;
								}
					}
					
					g0.num=0;
					g0.num2=0;
					g0.numi=0;
					synchronized (outBuf) {
						outBuf.add(outInt);
					}
					short[] buffer2 = new short[length];
					short[]tmpBuf2 = new short[length];
					for(int i=0;i<length/8;i++){
						buffer2[i]=(short) ((buffer[i * 8] + buffer[i * 8 + 1] + buffer[i * 8 + 2]
			                    + buffer[i * 8 + 3] + buffer[i * 8 + 4] + buffer[i * 8 + 5]
			                            + buffer[i * 8 + 6] + buffer[i * 8 + 7]) / 8.0);
						
					}
					for(int i=length/8;i<length;i++){
						buffer2[i]=0;
					}
					System.arraycopy(buffer2, 0, tmpBuf2, 0, length);
					Complex[]complexs2 = new Complex[length];
					int[]outInt2 = new int[length];
					for(int i=0;i < length; i++){
						Short short12 = tmpBuf2[i];
						complexs2[i] = new Complex(short12.doubleValue());
					}
					fft(complexs2,length);
					int sumi=0;
					int sums=0;
					int numb=0;
					double sumd=0;
				
					for (int i = 0; i <264; i++) {
						outInt2[i] = complexs2[i].getIntValue();
					//		 Log.d(TAG,"__"+g.numi+" "+ String.valueOf(i)+"||||||"+outInt2[i]);
							int numi=0;
							numi=(int)g.numi;
							
							 if(i>=numint[numi]&&i<numint[numi+1])
							 {
								
								  sumi+=outInt2[i];
								
								 g.sum=(double)sumi;
								if(i==(numint[numi+1]-1))
									sumi=0;
								
						//		 Log.d(TAG,"pppp"+String.valueOf(g.sum));
								   
							  if(outInt2[i]>0)
									   g.num++;
									   
								   if(i==(numint[numi+1]-1)){
									   if(g.num==0)g.sampleratePoint2[g.num2]=0;
									    else
									    g.sampleratePoint2[g.num2]=g.sum/(double)g.num;
								//	   Log.d(TAG,"GGGG"+String.valueOf( g.sampleratePoint2[g.num2])+"|"+String.valueOf(g.num2)+"|"+String.valueOf(g.num));
									}
								
							 }
					
						
							 if(i==(numint[g.numi+1]-1)){
								 g.numi++;
								 g.num2++;
								  g.sum=0;
								  g.num=0;
								}
					}
					
					g.num=0;
					g.num2=0;
					g.numi=0;
					synchronized (outBuf2) {
						outBuf2.add(outInt2);
					}
				}
//				try {
//					fos.close();
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
				audioRecord.stop();
			}catch (Exception e) {
				// TODO: handle exception
				Log.i("Rec E",e.toString());
			}
			
		}
	}

	//绘图线程
	class DrawThread extends Thread{
		//画板
		private SurfaceView sfvSurfaceView;
		
		//当前画图所在屏幕x轴的坐标
		//画笔
		private Paint mPaint;
		private Paint mPaint2;
		private Paint mPaint3;
		private Paint tPaint;
		
		private Paint dashPaint;
		public DrawThread(SurfaceView sfvSurfaceView) {
			this.sfvSurfaceView = sfvSurfaceView;
			//设置画笔属性
			mPaint = new Paint();
			mPaint.setColor(Color.BLUE);
			mPaint.setStrokeWidth(10);
			mPaint.setAntiAlias(true);
			
			mPaint2 = new Paint();
			mPaint2.setColor(Color.RED);
			mPaint2.setStrokeWidth(10);
			mPaint2.setAntiAlias(true);
			
			mPaint3 = new Paint();
			mPaint3.setColor(Color.GREEN);
			mPaint3.setStrokeWidth(10);
			mPaint3.setAntiAlias(true);
			
			tPaint = new Paint();
			tPaint.setColor(Color.YELLOW);
			tPaint.setStrokeWidth(1);
			tPaint.setAntiAlias(true);
		
			dashPaint = new Paint();
			dashPaint.setStyle(Paint.Style.STROKE);
			dashPaint.setColor(Color.GRAY);
			Path path = new Path();
	        path.moveTo(0, 10);
	        path.lineTo(480,10); 
	        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
	        dashPaint.setPathEffect(effects);
		}
		
		@SuppressWarnings("unchecked")
		public void run() {
			while (isRecording) {
			
			
				ArrayList<int[]>buf = new ArrayList<int[]>();
				synchronized (outBuf) {
					if (outBuf.size() == 0) {
						continue;
					}
					buf = (ArrayList<int[]>)outBuf.clone();
					outBuf.clear();
				}
				//根据ArrayList中的short数组开始绘图
				for(int i = 0; i < buf.size(); i++){
					int[]tmpBuf = buf.get(i);
					
					SimpleDraw(tmpBuf, rateY, baseLine);
				}
				
			}
		}
		
		/** 
         * 绘制指定区域 
         *  
         * @param start 
         *            X 轴开始的位置(全屏) 
         * @param buffer 
         *             缓冲区 
         * @param rate 
         *            Y 轴数据缩小的比例 
         * @param baseLine 
         *            Y 轴基线 
         */ 
		private void SimpleDraw(int[] buffer, int rate, int baseLine){
			double ds=17;
			double zb=4;
			double zb2=3;
			Canvas canvas = sfvSurfaceView.getHolder().lockCanvas(
					new Rect(0, 0, buffer.length,sfvSurfaceView.getHeight()));
			canvas.drawColor(Color.BLACK);
			
			
			baseLine=sfvSurfaceView.getHeight()-20;
			double  dbaseLine=(double)baseLine;
			double  dbaseLine2=dbaseLine/ds*zb;
			double  dbaseLine3=dbaseLine/ds*zb2;
			int ibaseLine2=(int)dbaseLine2;
			int ibaseLine3=(int)dbaseLine3;
			canvas.drawText("SPL/dB",0,ibaseLine2,  tPaint);
			for(int i=0;i<12;i++){
				String str=String.valueOf((i+1)*10);
				  double j=(double)(i+1);
				  double dline=(dbaseLine-dbaseLine/ds*j);
				int line=(int)dline;
				canvas.drawText(str, 10, line, tPaint);
			}
			for(int i=1;i<=3;i++){
				  double dline2=(dbaseLine/ds)/2;
				int line=(int)dline2;
				canvas.drawLine(shift, ibaseLine3-i*line, shift-5, ibaseLine3-i*line, tPaint);
				if(i==3){
					canvas.drawText("15", 10, ibaseLine3-(i-1)*line, tPaint);;
				}
			}
			int[] loc = new int[SPECTROGRAM_COUNT];//<span style="font-size: 13.3333px; font-family: Arial, Helvetica, sans-serif;">SPECTROGRAM_COUNT = 31，即段数</span>
		    double[] sampleratePoint = new double[SPECTROGRAM_COUNT];
			int[] snum=new int[31];
			 for (int i = 0; i < 31; i++){
			        //20000表示的最大频点20KHZ,这里的20-20K之间坐标的数据成对数关系,这是音频标准
			        double F = Math.pow(20000 / 20, 1.0 / 31);//方法中20为低频起点20HZ，31为段数
			        sampleratePoint[i] = 20 * Math.pow(F, i);//乘方，20为低频起点
			    
			 }
			 String[] X_axis = {"20Hz",  "80Hz",  "315Hz", "1250Hz", "5000Hz","20000Hz"};
			int width=sfvSurfaceView.getWidth()-shift-40;
			int ww=width/31;
			
			int width2=width/5;
			for(int i=0;i<6;i++){
				canvas.drawText(X_axis[i],shift+i* 78-15,baseLine+10,tPaint);
			}
			//canvas.drawText("原点(0,0)", 0, 7, 5, baseLine + 15, tPaint);
			canvas.drawText("频率(HZ)", 0, 6, sfvSurfaceView.getWidth() - 50, baseLine + 30, tPaint);
			canvas.drawLine(shift, ibaseLine2, shift, baseLine, tPaint);
			canvas.drawLine(shift, ibaseLine3, shift, 0, tPaint);
			canvas.drawLine(shift, baseLine, sfvSurfaceView.getWidth(), baseLine, tPaint);
			canvas.drawLine(shift, ibaseLine3, sfvSurfaceView.getWidth(), ibaseLine3, tPaint);
			canvas.save();
			canvas.rotate(30, shift, ibaseLine2);
			canvas.drawLine(shift, ibaseLine2, shift, ibaseLine2+10, tPaint);
			canvas.rotate(-60, shift, ibaseLine2);
			canvas.drawLine(shift, ibaseLine2, shift,ibaseLine2+10, tPaint);
			canvas.rotate(30, shift, ibaseLine2);
			canvas.rotate(30, sfvSurfaceView.getWidth()-1, baseLine);
			canvas.drawLine(sfvSurfaceView.getWidth() - 1, baseLine, sfvSurfaceView.getWidth() - 11, baseLine, tPaint);
			canvas.rotate(-60, sfvSurfaceView.getWidth()-1, baseLine);
			canvas.drawLine(sfvSurfaceView.getWidth() - 1, baseLine, sfvSurfaceView.getWidth() - 11, baseLine, tPaint);
			canvas.rotate(30, sfvSurfaceView.getWidth()-1, baseLine);
			canvas.rotate(30, shift, 0);
			canvas.drawLine(shift, 0, shift,10, tPaint);
			canvas.rotate(-60, shift, 0);
			canvas.drawLine(shift, 0, shift,10, tPaint);
			canvas.rotate(30, shift, 0);
		//	canvas.rotate(30, shift, ibaseLine3);
			canvas.rotate(30, sfvSurfaceView.getWidth()-1, ibaseLine3);
			canvas.drawLine(sfvSurfaceView.getWidth() - 1, ibaseLine3, sfvSurfaceView.getWidth() - 11, ibaseLine3, tPaint);
			canvas.rotate(-60, sfvSurfaceView.getWidth()-1, ibaseLine3);
			canvas.drawLine(sfvSurfaceView.getWidth() - 1, ibaseLine3, sfvSurfaceView.getWidth() - 11, ibaseLine3, tPaint);
			canvas.restore();
			int y;
			
	
			for(int i=0;i<31;i++){
				double vo=0;
				vo = 20 * Math.log10(g.sampleratePoint2[i]);
				reference[i]=vo;
				double num1=12,num11=120,num22=(double)baseLine;
				double y22=num22*(vo/num11)*(num1/ds);
			    y2=baseLine-(int)y22;
				canvas.drawLine(13*i + shift+5, baseLine, 13*i +shift+5, y2, mPaint);
			}
			calculate ca=new calculate(reference);
			double re=ca.rank();
			//Log.d(TAG, ">>>>>>" +re);
			double num10=12,num110=120,num220=(double)baseLine;
			
			double y220=num220*(re/num110)*(num10/ds);
		   int y20=baseLine-(int)y220;
			canvas.drawLine(shift,y20,sfvSurfaceView.getWidth()-1,y20,tPaint);//参考值
			double num111=30;
			
			for(int i=0;i<31;i++){
				int sam=(int)g.sampleratePoint2[i];
				double v1=0 ;
				if(sam!=0)
				 v1 = 20 * Math.log10(g.sampleratePoint2[i]);
		
				g.reference2[i]=v1-re;
				
				double y221=dbaseLine3*(g.reference2[i]/num111);
				int y21=(int)y221;
		//		Log.d(TAG, i+">>>>>>" +y21+"|"+y221);
				if(y21>=0)
				{
					int yy=ibaseLine3-y21;
					if(yy<(ibaseLine3/2))
						yy=ibaseLine3;//大于15db即为0db
					canvas.drawLine(13*i + shift+5, ibaseLine3, 13*i +shift+5, yy, mPaint2);
				}
				else{
					int yy2=ibaseLine3+y21;
					if(yy2<(ibaseLine3/2))
						yy2=ibaseLine3;//大于15db即为0db
					canvas.drawLine(13*i + shift+5, ibaseLine3, 13*i +shift+5, yy2, mPaint3);
				}
			}
			sfvSurfaceView.getHolder().unlockCanvasAndPost(canvas);
		}
	
		
	}
	
	 
	/**
	 * 向上取最接近iint的2的幂次数.比如iint=320时,返回256
	 * @param iint
	 * @return
	 */
	private int up2int(int iint) {
		int ret = 1;
		while (ret<=iint) {
			ret = ret << 1;
		}
		return ret>>1;
	}
	
	//快速傅里叶变换
	public void fft(Complex[] xin,int N)
	{
	    int f,m,N2,nm,i,k,j,L;//L:运算级数
	    float p;
	    int e2,le,B,ip;
	    Complex w = new Complex();
	    Complex t = new Complex();
	    N2 = N / 2;//每一级中蝶形的个数,同时也代表m位二进制数最高位的十进制权值
	    f = N;//f是为了求流程的级数而设立的
	    for(m = 1; (f = f / 2) != 1; m++);                             //得到流程图的共几级
	    nm = N - 2;
	    j = N2;
	    /******倒序运算——雷德算法******/
	    for(i = 1; i <= nm; i++)
	    {
	        if(i < j)//防止重复交换
	        {
	            t = xin[j];
	            xin[j] = xin[i];
	            xin[i] = t;
	        }
	        k = N2;
	        while(j >= k)
	        {
	            j = j - k;
	            k = k / 2;
	        }
	        j = j + k;
	    }
	    /******蝶形图计算部分******/
	    for(L=1; L<=m; L++)                                    //从第1级到第m级
	    {
	    	e2 = (int) Math.pow(2, L);
	        //e2=(int)2.pow(L);
	        le=e2+1;
	        B=e2/2;
	        for(j=0;j<B;j++)                                    //j从0到2^(L-1)-1
	        {
	            p=2*pi/e2;
	            w.real = Math.cos(p * j);
	            //w.real=Math.cos((double)p*j);                                   //系数W
	            w.image = Math.sin(p*j) * -1;
	            //w.imag = -sin(p*j);
	            for(i=j;i<N;i=i+e2)                                //计算具有相同系数的数据
	            {
	                ip=i+B;                                           //对应蝶形的数据间隔为2^(L-1)
	                t=xin[ip].cc(w);
	                xin[ip] = xin[i].cut(t);
	                xin[i] = xin[i].sum(t);
	            }
	        }
	    }
	}
}

class Global
{
public static double sum= 0;
public static int sumi= 0;
public static int num= 0;
public static int num2= 0;
public static int numi= 0;
double[] sampleratePoint2 = new double[32];
double[] reference2 = new double[32];
}
