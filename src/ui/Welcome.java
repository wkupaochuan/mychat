package ui;



import com.crashlytics.android.Crashlytics;
import im.WeChat;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.donal.wechat.R;

import tools.AppContext;
import tools.AppManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import config.AppActivity;
import config.CommonValue;


/**
 * wechat
 *
 * @author donal
 *
 */
public class Welcome extends AppActivity{
	
	public static final String KEY_HELP_VERSION_SHOWN = "preferences_help_version_shown";
	
	
	/**
	 * 欢迎界面加载
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 用来统计错误的第三方工具
		Crashlytics.start(this);
		
		// 设置布局
		final View view = View.inflate(this, R.layout.welcome_page, null);
		setContentView(view);
		
		// 适应不同屏幕分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
		double screenSize = diagonalPixels / (160*dm.density);
		this.appContext.saveScreenSize(screenSize);
		
		// 设置动画
		AlphaAnimation animation = new AlphaAnimation(0.3f,1.0f);
		animation.setDuration(3000);
		view.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener()
		{
			/**
			 * 动画结束后动作
			 */
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationStart(Animation animation) {}
			
		});
	}
	
	
	/**
	 * 展示欢迎动画后跳转页面
	 */
	private void redirectTo(){
		// 登陆用户直接跳转页面
		if(this.appContext.isLogin()){
			Intent intent = new Intent(this, Tabbar.class);
	        startActivity(intent);
	        AppManager.getAppManager().finishActivity(this);
		}
		// 未登录用户跳转到登陆页面
		else {
			Intent intent = new Intent(this,Login.class);
			startActivity(intent);
			AppManager.getAppManager().finishActivity(this);
		}
    }
	
	
	/**
	 * 展示新内容
	 * @return
	 */
	private boolean showWhatsNewOnFirstLaunch() {
	    try {
		      PackageInfo info = getPackageManager().getPackageInfo(CommonValue.PackageName, 0);
		      int currentVersion = info.versionCode;
		      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		      int lastVersion = prefs.getInt(KEY_HELP_VERSION_SHOWN, 0);
		      if (currentVersion > lastVersion) {
			        prefs.edit().putInt(KEY_HELP_VERSION_SHOWN, currentVersion).commit();
//			        Intent intent = new Intent(this, whatsnew.class);
//			        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//			        startActivity(intent);
//			        finish();
			        return true;
		      	}
	    	} catch (PackageManager.NameNotFoundException e) {
	    		e.printStackTrace();
	    	}
	    return false;
	}
}
