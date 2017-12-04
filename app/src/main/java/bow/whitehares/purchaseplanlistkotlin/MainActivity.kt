package bow.whitehares.purchaseplanlistkotlin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Realm のインスタンス取得
        realm = Realm.getDefaultInstance()

        val mListView     = findViewById<View>(R.id.listView) as ListView
        val purchasePlans = realm?.where(PurchasePlan::class.java)?.findAll()
        val adapter       = purchasePlans?.let { PurchasePlanAdapter(it) }
        mListView.adapter = adapter

        fab.setOnClickListener { view ->
            startActivity(Intent(view.context, PurchasePlanEditActivity::class.java))
        }

        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val purchasePlan = parent?.getItemAtPosition(position) as PurchasePlan
            startActivity(Intent(parent.context, PurchasePlanEditActivity::class.java).putExtra("purchase_plan_id", purchasePlan.id))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
