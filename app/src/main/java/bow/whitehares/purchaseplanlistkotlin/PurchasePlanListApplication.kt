package bow.whitehares.purchaseplanlistkotlin

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by white on 2017/12/03.
 */
class PurchasePlanListApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Realm の設定
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}