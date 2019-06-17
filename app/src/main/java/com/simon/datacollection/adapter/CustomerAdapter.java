package com.simon.datacollection.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simon.datacollection.R;
import com.simon.datacollection.models.CustomerModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    List<CustomerModel> users;
    private Context context;

    public CustomerAdapter(List<CustomerModel> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id_no.setText("ID No.: " + users.get(position).getIdNo());
        holder.name.setText(users.get(position).getfName() + " " + users.get(position).getlName());
        holder.qrCode.setText("QR Code: " +users.get(position).getQrCodeData());
        holder.coordinates.setText(users.get(position).getLatitude() + ","+ users.get(position).getLongitude());

        Bitmap bitmap = BitmapFactory.decodeByteArray(users.get(position).getId_image(), 0, users.get(position).getId_image().length);
        holder.id_card.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView id_no;
        TextView qrCode;
        TextView coordinates;
        ImageView id_card;

        public ViewHolder(View itemView) {
            super(itemView);
            id_no = itemView.findViewById(R.id.id_no);
            name = itemView.findViewById(R.id.name);
            qrCode = itemView.findViewById(R.id.qrCode);
            coordinates = itemView.findViewById(R.id.coordinates);
            id_card = itemView.findViewById(R.id.id_image);
        }
    }
}
