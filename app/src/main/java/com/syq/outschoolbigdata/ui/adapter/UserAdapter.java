package com.syq.outschoolbigdata.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.syq.outschoolbigdata.R;
import com.syq.outschoolbigdata.utils.BitmapUtil;
import com.syq.outschoolbigdata.utils.MyLog;
import com.syq.outschoolbigdata.vo.orm.UserBean;

import java.util.ArrayList;

/**
 * Created by yfb on 2018/3/9.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    public static final String TAG = "UserAdapter";

    private ArrayList<UserBean> list;
    private Context context;

    public UserAdapter(Context context,ArrayList<UserBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_students,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserBean ub = list.get(position);

        if(ub.getAvater() == null || "".equals(ub.getAvater()) || "img/headImgDemo.png".equals(ub.getAvater()) || "null".equals(ub.getAvater())){
            MyLog.i(TAG,ub.getAvater()+"@");
            BitmapUtil.displayBitmapFromDrawable(context,R.drawable.default_avater,holder.userImage);
        }else{
            BitmapUtil.displayBitmap(context,ub.getAvater(),holder.userImage);
        }
        holder.nameTv.setText(ub.getName());
        holder.scoreTv.setText(ub.getScore());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView userImage;
        private TextView nameTv;
        private TextView scoreTv;

        public ViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);
            nameTv = (TextView) itemView.findViewById(R.id.user_name_tv);
            scoreTv = (TextView) itemView.findViewById(R.id.user_score_tv);
            itemView.setTag(this);
        }
    }
}
