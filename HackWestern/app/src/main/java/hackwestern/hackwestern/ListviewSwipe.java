package hackwestern.hackwestern;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;

/**
 * Created by sharonlee on 15-10-06.
 */
public class ListviewSwipe implements SwipeRefreshLayout.OnRefreshListener {

    textLocListView mylistview;

    public ListviewSwipe(textLocListView mylistview) {
        this.mylistview = mylistview;
    }

    @Override
    public void onRefresh() {
        ArrayList<String> messages = mylistview.getNames();
        mylistview.listview.setAdapter(new ArrayAdapter<String>(mylistview,
                simple_list_item_1, text1, messages));
        Log.d("swiperefresh", "layouthere: " + mylistview.mSwipeRefreshLayout);
        mylistview.mSwipeRefreshLayout.setRefreshing(false);
        ;
    }
}
