package com.od.msbte_exam_pre;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.ViewHolder> {
public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.viewholder> {

    private List<CategoryModel> categoryModelList;

    public CatagoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CatagoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_item,parent,false);
    return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatagoryAdapter.viewholder holder, int position) {
            holder.setData(categoryModelList.get(position).getUrl(),categoryModelList.get(position).getName(),categoryModelList.get(position).getSets());
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView title;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
        }
        private  void setData(String url,String title, int sets){  //Remove final from here if it gives error
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.title.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setIntent = new Intent(itemView.getContext(),SetsActivity.class);
                    setIntent.putExtra("title",title);
                    setIntent.putExtra("sets",sets);
                    itemView.getContext().startActivity(setIntent);
                }
            });

        }

    }

}
