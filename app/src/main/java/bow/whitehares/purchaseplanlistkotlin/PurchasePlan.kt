package bow.whitehares.purchaseplanlistkotlin

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by white on 2017/12/03.
 */
open class PurchasePlan: RealmObject() {
    @PrimaryKey
    var id:     Long   = 0 // ID
    var name:   String = "" // 商品名
    var amount: Int    = 0 // 金額
    var date:   Date?  = null // 予定日
    var detail: String = "" // 詳細
}