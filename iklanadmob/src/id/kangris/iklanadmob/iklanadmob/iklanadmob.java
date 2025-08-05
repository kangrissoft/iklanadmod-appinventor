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


@DesignerComponent(
	version = 1,
	versionName = "1.0",
	description = "Developed by kangrissoft using Fast.",
	iconName = "icon.png"
)
public class iklanadmob extends AndroidNonvisibleComponent {

  private AdView adView;

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

  @SimpleFunction(description = "Load a banner ad into a layout.")
  public void LoadBannerAd(String adUnitId, AndroidViewComponent layout) {
    // Dapatkan view dari komponen layout
    ViewGroup adContainer = (ViewGroup) layout.getView();
    
    // Hapus iklan lama jika ada
    if (adView != null) {
        adContainer.removeView(adView);
        adView.destroy();
    }
    
    // Buat objek AdView baru
    adView = new AdView(this.container.$context());
    
    // Set ukuran iklan dan ID unit iklan
    adView.setAdSize(AdSize.BANNER);
    adView.setAdUnitId(adUnitId);
    
    // Buat permintaan iklan
    AdRequest adRequest = new AdRequest.Builder().build();
    
    // Muat iklan ke AdView
    adView.loadAd(adRequest);
    // Set the AdListener to handle events
    adView.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {
        // Kode di sini akan berjalan saat iklan berhasil dimuat.
        // Kita panggil event block AdLoaded yang sudah kita buat.
        AdLoaded();
      }

      @Override
      public void onAdFailedToLoad(LoadAdError adError) {
        // Kode di sini akan berjalan saat iklan gagal dimuat.
        // Kita panggil event block AdFailedToLoad dengan pesan error-nya.
        AdFailedToLoad(adError.getMessage());
      }
    });

    // Tambahkan AdView ke layout di aplikasi
    adContainer.addView(adView);
  }

  @SimpleEvent(description = "Event triggered when an ad has successfully loaded.")
  public void AdLoaded() {
    EventDispatcher.dispatchEvent(this, "AdLoaded");
  }

  @SimpleEvent(description = "Event triggered when an ad has failed to load.")
  public void AdFailedToLoad(String error) {
    EventDispatcher.dispatchEvent(this, "AdFailedToLoad", error);
  }

}
