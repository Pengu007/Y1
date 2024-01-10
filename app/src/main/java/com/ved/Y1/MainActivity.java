package com.ved.Y1;


import android.os.Build;
import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactActivityDelegate;

import expo.modules.ReactActivityDelegateWrapper;

public class MainActivity extends ReactActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.AppTheme);
    super.onCreate(null);
  }

  @Override
  protected String getMainComponentName() {
    return "main";
  }

  @Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    String mainComponentName = getMainComponentName();
    if (mainComponentName == null) {
      mainComponentName = "main";
    }
    return new ReactActivityDelegateWrapper(this, isUsingNewArchitecture(), new DefaultReactActivityDelegate(
            this,
            mainComponentName,
            // If you opted-in for the New Architecture, we enable the Fabric Renderer.
            DefaultNewArchitectureEntryPoint.getFabricEnabled(), // fabricEnabled
            // If you opted-in for the New Architecture, we enable Concurrent React (i.e. React 18).
            DefaultNewArchitectureEntryPoint.getConcurrentReactEnabled() // concurrentRootEnabled
    ));
  }


  private boolean isUsingNewArchitecture() {
    return true;
  }

  @Override
  public void invokeDefaultOnBackPressed() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
      if (!moveTaskToBack(false)) {
        super.invokeDefaultOnBackPressed();
      }
      return;
    }

    super.invokeDefaultOnBackPressed();
  }
}