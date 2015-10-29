package net.nym.wificrack.common;

import android.support.v7.widget.RecyclerView;

import net.nym.wificrack.bean.Entities;
import net.nym.wificrack.bean.Entity;

/**
 * @author nym
 * @date 2015/10/13 0013.
 * @since 1.0
 */
public abstract class BaseRecyclerAdapter<T extends Entity,M extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<M> {

    public final static String TAG = BaseRecyclerAdapter.class.getSimpleName();
    protected Entities<T> mData;
//    protected Context mContext;

    public BaseRecyclerAdapter(Entities<T> data)
    {
//        this.mContext = context;
        this.mData = data;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }



//    // Provide a reference to the type of views that you are using
//    // (custom viewholder)
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(View v) {
//            super(v);
//        }
//    }

}
