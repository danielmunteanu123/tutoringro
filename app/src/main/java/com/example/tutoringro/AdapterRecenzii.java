package com.example.tutoringro;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.example.tutoringro.Utils.searchString;

public class AdapterRecenzii extends RecyclerView.Adapter<AdapterRecenzii.ViewHolder>{

    private Context c;
    private int mBackground;
    private int[] mMaterialColors;
    public List<ManagerRecenzii> recenzii;




    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView numeTxt, descriereTxt,recomandareTxt,notaTxt;
        private final MaterialLetterIcon mIcon;

        ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.profile_imageView);
            numeTxt = itemView.findViewById(R.id.numeTxt);
            descriereTxt = itemView.findViewById(R.id.descriereTxt);
            recomandareTxt = itemView.findViewById(R.id.recomandareTxt);
            notaTxt = itemView.findViewById(R.id.notaTxt);

        }



    }


    public AdapterRecenzii(ArrayList<ManagerRecenzii> recenzii) {
        this.recenzii = recenzii;

    }
    private void prepareLetterIcons(Context c){
        TypedValue mTypedValue = new TypedValue();
        c.getTheme().resolveAttribute(R.attr.selectableItemBackground,
                mTypedValue, true);
        mMaterialColors = c.getResources().getIntArray(R.array.colors);
        mBackground = mTypedValue.resourceId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.c = parent.getContext();
        prepareLetterIcons(c);
        View view = LayoutInflater.from(c).inflate(R.layout.model_recenzie, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get profesor curent
        final ManagerRecenzii r = recenzii.get(position);

        //legam datele cu widget-urile
        holder.numeTxt.setText("Nume: " +r.getNume_student());
        holder.descriereTxt.setText("Descriere: " +r.getDescriere());
        holder.recomandareTxt.setText("Recomandare: " +r.getRecomandare());
        holder.notaTxt.setText("Nota: " +r.getNota());
        holder.mIcon.setInitials(true);
        holder.mIcon.setInitialsNumber(2);
        holder.mIcon.setLetterSize(25);
        holder.mIcon.setShapeColor(mMaterialColors[new Random().nextInt(
                mMaterialColors.length)]);
        holder.mIcon.setLetter(r.getNume_student());

        if(position % 2 ==0 ) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f3e5f5"));
        }else {
            holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
        }


    }

    @Override
    public int getItemCount() {
        return recenzii.size();
    }

}
//end