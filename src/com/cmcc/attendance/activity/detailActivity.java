package com.cmcc.attendance.activity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.huison.tools.Chuli;
import com.lianlin.R;

public class detailActivity extends Activity {
	public static String id,toptitle;
	TextView text_name,text_title;
	String title,content,listenContent,listenContentJson,webContentJson;
	String papercontentUrl="";
	ImageView btn_return;
//	static ImageView imgtop;
	LinearLayout btn_jrgwc,btn_sc,btn_fx;
	
	private String url,contentUrl,workType;
    private MediaPlayer mMediaPlayer;
    private int state = IDLE;
    private static final int PLAYING = 0;
    private static final int PAUSE = 1;
    private static final int STOP = 2;
    private static final int IDLE = 3;

    public static final int UPDATE = 2;
  
    private boolean iffirst = false;  

    private Timer mTimer;    
    private TimerTask mTimerTask;   
    private boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突    
    private SeekBar seekbar;
    private WebView browser;
    private Dialog pg;
	private String dl_msg;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        browser=(WebView)findViewById(R.id.Toweb);
        seekbar = (SeekBar) findViewById(R.id.sb_detail_play_progress);  
        seekbar.setOnSeekBarChangeListener(new MySeekbar()); 
       
        btn_return=(ImageView)findViewById(R.id.detail_btn_return);
        btn_return.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				
			}
        	
        });
        
     
        text_title=(TextView)findViewById(R.id.detail_text_toptitle);
        text_title.setText(toptitle);
        
        
        Intent intent = getIntent();
        Bundle bd = intent.getBundleExtra("bd");// 根据bundle的key得到对应的对象
        url=bd.getString("url");//
        contentUrl = bd.getString("contentUrl");//内容页面
        workType = bd.getString("workType");//作业类型
        /*
        pg=Chuli.c_pg(detailActivity.this, "正在加载听力内容...");
        pg.show();
        Thread t4 = new Thread() {
	      	 @Override
	     	public void run() {	
	      		//getContent();
	      		
	      		try{
	    			listenContentJson = Chuli.HttpGetData(Chuli.yuming + contentUrl, BaseActivity.now_userid);
	    			Log.v("The first time to get the listenContentJson", listenContentJson);
	    		}catch (Exception ee){
	    			Log.v("get content data  listenContent error ", ee.toString());
	    		}
	      		homeworkHandler.post(listenContentGetSuccess); 
	      		 
	      		//getContentWeb();
	      		//homeworkHandler.post(listenContentGetSuccess);
	      	}
        };
        t4.start();  
	    Log.v("T4 thread ", "ddd");	*/
	      	 ///////////////////////////////////////处理web内容开始/////////////////
	  pg=Chuli.c_pg(detailActivity.this, "正在加载作业内容...");
 	  pg.show();	
 	    Thread t3 = new Thread() {
 	   	      	 @Override
 	   	  public void run() {	
 	      		//getContentWeb();
 	   	      	
 	   	      	try{
 	   				webContentJson = Chuli.HttpGetData(Chuli.yuming + url, BaseActivity.now_userid);
 	   				Log.v("The first time to get the Webcontent", webContentJson);
 	   			}catch (Exception ee){
 	   				Log.v("get content data for Web error ", ee.toString());
 	   			}
 	   			homeworkHandler.post(webContentGetSuccess);	
 	   	      		 
 		      	//WebView
 	      		
 		    	        
 		    	        //try{
 		    	        	//papercontentUrl = papercontentJson.getString("content_url");
 		    	        //}catch(Exception ee){
 		    	        	//papercontentUrl = papercontentJson.getString("content_endpoint");//提交地址
 		    	        	//Log.v("加载作业web内容出错", ee.toString());
 		    	        //}
 	     	 }
         };
         t3.start();      
       
        
    	
       
        //JSONObject p3=new JSONObject(a);
/*        
        r_fx=(RelativeLayout)findViewById(R.id.detail_r_fx);
        r_fx.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				 sendWxUrl(1);
				
			}
        	
        });*/
	 }
	final Handler homeworkHandler = new Handler();
	final Runnable listenContentGetSuccess = new Runnable() {
	    public void run() {
	    	
	    	try{
    	        
            	Log.v("listenContentJson  ", listenContentJson);
      			if(listenContentJson!=""){
    	        	JSONObject contentJson=new JSONObject(listenContentJson);        	
    	        	listenContent = contentJson.getString("audio_url");    	        	
            	}else{
            		seekbar.setVisibility(View.GONE );
            		Log.v("listenContentJson is Empty!!", listenContentJson.toString());
            	}
            }catch(Exception ee){
            	seekbar.setVisibility(View.GONE );
            	Log.v("Detail Json Error", ee.toString());
            }
	    	
	    	////////////////////////////////////////////////////////
	    	

	        
	    	if((listenContent!="")&&(listenContent!=null)){
	    		Log.v("The listen Content is ", listenContent.toString());
	  	        play(listenContent);
	  	        }else{
	  	        	seekbar.setVisibility(View.GONE );
	  	        }
	    	
	    }
	};
	final Runnable webContentGetSuccess = new Runnable() {
	    public void run() {
	    	pg.dismiss();
	    	Log.v("webContentJson  ", webContentJson);
            try{
    	        //browser.loadUrl(Chuli.yuming + url);
    	        //browser.loadUrl("http://www.baidu.com"); 
    	        JSONObject papercontentJson=new JSONObject(webContentJson); 
    	        papercontentUrl = papercontentJson.getString("content_url");
            }
    		catch (IllegalArgumentException ee) {
    			Log.v("get content data for Web error ", ee.toString());
                ee.printStackTrace();
             } catch (IllegalStateException ee) {
            	 Log.v("get content data for Web error ", ee.toString());
                ee.printStackTrace();
             }// catch (IOException ee) {
            	// Log.v("get content data for Web error ", ee.toString());
                //ee.printStackTrace();
             //} 
            catch (JSONException e) {
            	 Log.v("get content data for Web error ", e.toString());
				e.printStackTrace();
			}
	    	try{ 
	        	Log.v("paperContentUrl   ", papercontentUrl);
		        browser.loadUrl(papercontentUrl);        
		        //设置可自由缩放网页  
		        browser.getSettings().setSupportZoom(true);  
		        browser.getSettings().setBuiltInZoomControls(true); 
		      
		    // 如果页面中链接，如果希望点击链接继续在当前browser中响应，  
		    // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象         
		        browser.setWebViewClient(new WebViewClient() {  
		        public boolean shouldOverrideUrlLoading(WebView view, String url)  
		        {   
		            //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边  
		            view.loadUrl(url);  
		            return true;  
		        }         
		         });
	        }catch(Exception ee){
	        	Log.v("载入Web内容错误   ", ee.toString());
	        }
	    	
	    	
	    	/////////////////////////////////////////
	    	
	    	//pg=Chuli.c_pg(detailActivity.this, "正在加载听力内容...");
	        //pg.show();
	        Thread t4 = new Thread() {
		      	 @Override
		     	public void run() {	
		      		//getContent();
		      		
		      		try{
		    			listenContentJson = Chuli.HttpGetData(Chuli.yuming + contentUrl, BaseActivity.now_userid);
		    			Log.v("The first time to get the listenContentJson", listenContentJson);
		    		}catch (Exception ee){
		    			Log.v("get content data  listenContent error ", ee.toString());
		    		}
		      		homeworkHandler.post(listenContentGetSuccess); 
		      		 
		      		//getContentWeb();
		      		//homeworkHandler.post(listenContentGetSuccess);
		      	}
	        };
	        t4.start();  
		    Log.v("T4 thread ", "ddd");	
	    }
	};
	

	private void getContent(){
		try{
			listenContentJson = Chuli.HttpGetData(Chuli.yuming + contentUrl, BaseActivity.now_userid);
			Log.v("The first time to get the listenContentJson", listenContentJson);
		}catch (Exception ee){
			Log.v("get content data  listenContent error ", ee.toString());
		}
		homeworkHandler.post(listenContentGetSuccess);
	}
	private void getContentWeb(){
		try{
			webContentJson = Chuli.HttpGetData(Chuli.yuming + url, BaseActivity.now_userid);
			Log.v("The first time to get the Webcontent", webContentJson);
		}//catch (Exception ee){
		//	Log.v("get content data for Web error ", ee.toString());
		//}
		catch (IllegalArgumentException ee) {
			Log.v("get content data for Web error ", ee.toString());
            ee.printStackTrace();
         } catch (IllegalStateException ee) {
        	 Log.v("get content data for Web error ", ee.toString());
            ee.printStackTrace();
         } //catch (IOException ee) {
        	// Log.v("get content data for Web error ", ee.toString());
            //ee.printStackTrace();
         //}
		homeworkHandler.post(webContentGetSuccess);
	}
	
    private void play(String mediaPath) {
    	try {
    		Log.v("The MediaPlayer is playing ", "come here ");
    		
    		if (mMediaPlayer == null || state == STOP) {
    	// 创建MediaPlayer对象并设置Listener    		
    			mMediaPlayer = new MediaPlayer();
    		//Intent intent = getIntent();
           // Bundle bd = intent.getBundleExtra("bd");// 根据bundle的key得到对应的对象
            //url=bd.getString("url");
            //contentUrl = bd.getString("contentUrl");	
    		
	    	//mMediaPlayer = MediaPlayer.create(this, R.raw.test); 
	    	//Uri listenFile = null;
	    	//listenFile = Uri.parse(Chuli.yuming + contentUrl);	    	
	    	//mMediaPlayer = MediaPlayer.create(this, listenFile);//.create(this, R.raw.test);
	    	//MediaPlayer mMediaPlayer = new MediaPlayer();
	    	try{
	    		//mMediaPlayer.setDataSource(Chuli.yuming + contentUrl);//.setDataSource(this, listenFile);
	    		//Uri listenFile = null;
	    		//listenFile = Uri.parse(mediaPath);
	    		Log.v("mMdediaPlayer reset  ", "come here ");
	    		mMediaPlayer.reset();
	    		Log.v("媒体文件地址   ", mediaPath);
	    		 /* 重置MediaPlayer */         
	            

	            //String path="mp3/"+mediaPath;//+".mp3";            
//	            /* 设置要播放的文件的路径 */
	            //AssetFileDescriptor afd = context.getAssets().openFd(path);
	            //mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());        
	            //afd.close();
	            
	            //mMediaPlayer.prepare();
	            //mMediaPlayer.start();    
	    		//mMediaPlayer.setDataSource(this, listenFile);
	    		mMediaPlayer.setDataSource(mediaPath);
	    		mMediaPlayer.prepare();
	    		//mMediaPlayer.prepareAsync();
	    	}//catch(Exception ee){
	    		//Log.v("打开媒体文件错误", ee.toString());
	    		//seekbar.setVisibility(View.GONE);
	    	//}
    	catch (IllegalArgumentException e) {
	    		Log.v("打开媒体文件错误" + mediaPath, e.toString());
	    		seekbar.setVisibility(View.GONE);
                e.printStackTrace();
             } catch (IllegalStateException e) {
            	 Log.v("打开媒体文件错误" + mediaPath, e.toString());
 	    		seekbar.setVisibility(View.GONE);
                e.printStackTrace();
             } catch (IOException e) {
            	 Log.v("打开媒体文件错误" + mediaPath, e.toString());
 	    		seekbar.setVisibility(View.GONE);
                e.printStackTrace();
             }
	    	mMediaPlayer.setOnPreparedListener(preListener);
	    	 Log.v("总进度","k"+mMediaPlayer.getDuration());
	    	seekbar.setMax(mMediaPlayer.getDuration());//设置进度条   
	        //----------定时器记录播放进度---------//     
	        mTimer = new Timer();    
	        mTimerTask = new TimerTask() {    
            @Override    
            public void run() {     
            	try{
                if(isChanging==true) {   
                    return;    
                }  
               
                seekbar.setProgress(mMediaPlayer.getCurrentPosition());  
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            }    
        };   
        mTimer.schedule(mTimerTask, 0, 10);   
        iffirst=true; 
        
    	} else {
    	// 复用MediaPlayer对象
    		mMediaPlayer.reset();
    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	}
	 // MediaPlayer进入prepared状态开始播放
	    private OnPreparedListener preListener = new OnPreparedListener() {
		    public void onPrepared(MediaPlayer arg0) {
		    	try{
			    mMediaPlayer.start();   
			    Log.v("mMedia start ", "come here ");
			    state = PLAYING;
		    	}catch(Exception ee){
		    		Log.v("MediaPlayer start error  ", ee.toString());
		    	}
		    }
	
	    };

    //进度条处理   
    class MySeekbar implements OnSeekBarChangeListener {  
        public void onProgressChanged(SeekBar seekBar, int progress,  
                boolean fromUser) {  
        	
        }  
  
        public void onStartTrackingTouch(SeekBar seekBar) {  
        	  isChanging=true;    

        }  
  
        public void onStopTrackingTouch(SeekBar seekBar) {  
          
        	try{
        	  mMediaPlayer.seekTo(seekbar.getProgress());  
        	  isChanging=false;  
        	}catch(Exception ee){
        		Log.v("seekTo errors ", ee.toString());
        	}
        }    
    }  
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	try{
    	mMediaPlayer.release();
    	mTimer.cancel();
    	mTimerTask.cancel();
    	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
	//go back
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        WebView browser=(WebView)findViewById(R.id.Toweb);  
        // Check if the key event was the Back button and if there's history  
        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {  
            browser.goBack();  
            return true;  
        }  
      //  return true;  
        // If it wasn't the Back key or there's no web page history, bubble up to the default  
        // system behavior (probably exit the activity)  
        return super.onKeyDown(keyCode, event);  
    } 
	 
	
		
		  
	
}