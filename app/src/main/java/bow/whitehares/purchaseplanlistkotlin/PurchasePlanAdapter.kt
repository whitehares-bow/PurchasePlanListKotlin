package bow.whitehares.purchaseplanlistkotlin

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import java.text.SimpleDateFormat

/**
 * Created by white on 2017/12/03.
 */
class PurchasePlanAdapter(data: OrderedRealmCollection<PurchasePlan>): RealmBaseAdapter<PurchasePlan>(data) {
    private class ViewHolder {
        internal var date: TextView? = null
        internal var name: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var viewHolder: ViewHolder? = null

        var view = convertView
        if (view == null) {
            // 初回利用

            // ビュー生成
            view = LayoutInflater.from(parent?.context).inflate(R.layout.simple_list_item_2, parent, false)

            // タグに設定しておくことで、ListView の表示を高速化できる
            viewHolder = ViewHolder()
            viewHolder.date = view.findViewById<View>(R.id.text1) as TextView
            viewHolder.name = view.findViewById<View>(R.id.text2) as TextView

            view.tag = viewHolder
        } else {
            // 再利用
            viewHolder = view.tag as ViewHolder
        }

        val schedule   = adapterData?.get(position)
        val sdf        = SimpleDateFormat("yyyy/MM/dd")
        val formatDate = sdf.format(schedule?.date)

        viewHolder.date?.text = formatDate
        viewHolder.name?.text = schedule?.name

        return view
    }
}