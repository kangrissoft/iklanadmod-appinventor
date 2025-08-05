package id.kangris.iklanadmob.iklanadmob;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.annotations.SimpleFunction;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import android.view.ViewGroup;
import android.view.View;

import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;

import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;

@DesignerComponent(
    version = 1,
    versionName = "1.0.0",
    description = "AdMob Banner Ad Extension for App Inventor. Developed by kangrissoft using Fast-CLI.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "icon.png"
)
public class iklanadmob extends AndroidNonvisibleComponent {

  private AdView adView;
  private boolean testMode = true;
  private String testAdUnitId = "ca-app-pub-3940256099942544/6300978111"; // Google's test banner ad unit ID

  public iklanadmob(ComponentContainer container) {
    super(container.$form());
  }

  @SimpleFunction(description = "Initialize AdMob SDK. Must be called once when the screen initializes.")
  public void Initialize() {
    MobileAds.initialize(this.container.$context(), new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
            // Bisa ditambahkan event block jika perlu
        }
    });
  }

  @SimpleProperty(description = "Set to true to use test ads, false to use real ads.")
  public void TestMode(boolean testMode) {
    this.testMode = testMode;
  }

  @SimpleProperty(description = "Get the current test mode setting.")
  public boolean TestMode() {
    return this.testMode;
  }

  @SimpleFunction(description = "Load a banner ad into a layout.")
  public void LoadBannerAd(String adUnitId, AndroidViewComponent layout) {
    try {
        // Validasi parameter
        if (layout == null) {
            AdFailedToLoad("Layout component cannot be null");
            return;
        }
        
        if (adUnitId == null || adUnitId.isEmpty()) {
            AdFailedToLoad("Ad Unit ID cannot be empty");
            return;
        }
        
        // Dapatkan view dari komponen layout
        ViewGroup adContainer = (ViewGroup) layout.getView();
        
        // Hapus iklan lama jika ada
        DestroyBannerAd();
        
        // Buat objek AdView baru
        adView = new AdView(this.container.$context());
        
        // Set ukuran iklan dan ID unit iklan
        adView.setAdSize(AdSize.BANNER);
        
        // Gunakan test ad unit ID jika dalam test mode
        String finalAdUnitId = testMode ? testAdUnitId : adUnitId;
        adView.setAdUnitId(finalAdUnitId);
        
        // Set AdListener sebelum load ad
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                AdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                AdFailedToLoad(adError.getCode() + ": " + adError.getMessage());
            }
            
            @Override
            public void onAdOpened() {
                AdOpened();
            }
            
            @Override
            public void onAdClicked() {
                AdClicked();
            }
            
            @Override
            public void onAdClosed() {
                AdClosed();
            }
        });
        
        // Buat permintaan iklan
        AdRequest adRequest = new AdRequest.Builder().build();
        
        // Tambahkan AdView ke layout terlebih dahulu
        adContainer.addView(adView);
        
        // Kemudian muat iklan
        adView.loadAd(adRequest);
        
    } catch (Exception e) {
        AdFailedToLoad("Error loading banner ad: " + e.getMessage());
    }
  }


  @SimpleFunction(description = "Destroy the current banner ad and free up resources.")
  public void DestroyBannerAd() {
    if (adView != null) {
        ViewGroup parent = (ViewGroup) adView.getParent();
        if (parent != null) {
            parent.removeView(adView);
        }
        adView.destroy();
        adView = null;
    }
  }

  @SimpleFunction(description = "Pause the banner ad (call this in Screen.OnPause).")
  public void PauseBannerAd() {
    if (adView != null) {
        adView.pause();
    }
  }

  @SimpleFunction(description = "Resume the banner ad (call this in Screen.OnResume).")
    public void ResumeBannerAd() {
    if (adView != null) {
        adView.resume();
    }
  }

  @SimpleFunction(description = "Check if banner ad is currently loaded.")
  public boolean IsBannerAdLoaded() {
    return adView != null;
  }

  @SimpleEvent(description = "Event triggered when an ad has successfully loaded.")
  public void AdLoaded() {
    EventDispatcher.dispatchEvent(this, "AdLoaded");
  }

  @SimpleEvent(description = "Event triggered when an ad has failed to load.")
  public void AdFailedToLoad(String error) {
    EventDispatcher.dispatchEvent(this, "AdFailedToLoad", error);
  }

  @SimpleEvent(description = "Event triggered when an ad is opened.")
  public void AdOpened() {
    EventDispatcher.dispatchEvent(this, "AdOpened");
  }

  @SimpleEvent(description = "Event triggered when an ad is clicked.")
  public void AdClicked() {
    EventDispatcher.dispatchEvent(this, "AdClicked");
  }

  @SimpleEvent(description = "Event triggered when an ad is closed.")
  public void AdClosed() {
    EventDispatcher.dispatchEvent(this, "AdClosed");
  }

}
