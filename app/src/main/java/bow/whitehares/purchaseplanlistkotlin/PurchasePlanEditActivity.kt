package bow.whitehares.purchaseplanlistkotlin

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_purchase_plan_edit.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PurchasePlanEditActivity : AppCompatActivity() {
    private var realm: Realm? = null

    var nameEdit:     EditText? = null
    var amountEdit:   EditText? = null
    var dateEdit:     EditText? = null
    var detailEdit:   EditText? = null
    var deleteButton: Button?   = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_plan_edit)

        // Realm のインスタンス取得
        realm = Realm.getDefaultInstance()

        nameEdit     = findViewById<View>(R.id.nameEdit) as EditText
        amountEdit   = findViewById<View>(R.id.amountEdit) as EditText
        dateEdit     = findViewById<View>(R.id.dateEdit) as EditText
        detailEdit   = findViewById<View>(R.id.detailEdit) as EditText
        deleteButton = findViewById<View>(R.id.delete) as Button

        val purchasePlanId = intent.getLongExtra("purchase_plan_id", -1)
        if (purchasePlanId != -1L) {
            val results      = realm?.where(PurchasePlan::class.java)?.equalTo("id", purchasePlanId)?.findAll()
            val purchasePlan = results?.first()

            val sdf  = SimpleDateFormat("yyyy/MM/dd")
            val date = sdf.format(purchasePlan?.date)

            nameEdit?.setText(purchasePlan?.name)
            amountEdit?.setText(purchasePlan?.amount.toString())
            dateEdit?.setText(date)
            detailEdit?.setText(purchasePlan?.detail)

            // 「編集」の場合は削除ボタンを表示
            deleteButton?.visibility = View.VISIBLE
        } else {
            // 「追加」の場合は削除ボタンを非表示
            deleteButton?.visibility = View.INVISIBLE
        }
    }

    fun onSaveTapped(view: View) {
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        var dateParse = Date()

        try {
            dateParse = sdf.parse(dateEdit?.text.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val date = dateParse
        val purchasePlanId = intent.getLongExtra("purchase_plan_id", -1)
        if (purchasePlanId != -1L) {
            val results = realm?.where(PurchasePlan::class.java)?.equalTo("id", purchasePlanId)?.findAll()
            // param: realm
            realm?.executeTransaction {
                val purchasePlan = results?.first()
                purchasePlan?.name   = nameEdit?.text.toString()
                purchasePlan?.amount = amountEdit?.text.toString().toInt()
                purchasePlan?.date   = date
                purchasePlan?.detail = detailEdit?.text.toString()
            }

            Snackbar.make(
                findViewById<View>(android.R.id.content),
                "アップデートしました", Snackbar.LENGTH_LONG
            )
            .setAction("戻る", {
                finish()
            })
            .setActionTextColor(Color.YELLOW)
            .show()
        } else {
            // param: realm
            realm?.executeTransaction {
                val maxId = it.where(PurchasePlan::class.java)?.max("id")
                var nextId = 0L

                if (maxId != null) {
                    nextId = maxId.toLong() + 1
                }

                val purchasePlan = it.createObject(PurchasePlan::class.java, nextId)
                purchasePlan?.name   = nameEdit?.text.toString()
                purchasePlan?.amount = amountEdit?.text.toString().toInt()
                purchasePlan?.date   = date
                purchasePlan?.detail = detailEdit?.text.toString()
            }

            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun onDeleteTapped(view: View) {
        val purchasePlanId = intent.getLongExtra("purchase_plan_id", -1)
        if (purchasePlanId != -1L) {
            realm?.executeTransaction { realm ->
                val purchasePlan = realm.where(PurchasePlan::class.java)
                    .equalTo("id", purchasePlanId)
                    .findFirst()
                purchasePlan?.deleteFromRealm()
            }
        }

        Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT).show()
        finish()
    }
}
