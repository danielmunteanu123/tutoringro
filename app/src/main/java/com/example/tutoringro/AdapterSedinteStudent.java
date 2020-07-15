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

public class AdapterSedinteStudent extends RecyclerView.Adapter<AdapterSedinteStudent.ViewHolder>{

    private Context c;
    private int mBackground;
    private int[] mMaterialColors;
    public List<ManagerProfesori> profesori;


    interface ItemClickListener {
        void onItemClick(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private final TextView numeTxt, domiciliuTxt,materiiTxt;
        private final MaterialLetterIcon mIcon;
        private ItemClickListener itemClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.mMaterialLetterIcon);
            numeTxt = itemView.findViewById(R.id.numeTxt);
            domiciliuTxt = itemView.findViewById(R.id.orasTxt);
            materiiTxt = itemView.findViewById(R.id.materiiTxt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }

        void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }


    public AdapterSedinteStudent(ArrayList<ManagerProfesori> profesori) {
        this.profesori = profesori;

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
        View view = LayoutInflater.from(c).inflate(R.layout.model, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get profesor curent
        final ManagerProfesori s = profesori.get(position);

        //legam datele cu widget-urile
        holder.numeTxt.setText("Nume:" +s.getNume());
        holder.domiciliuTxt.setText("Domiciliu:" +s.getDomiciliu());
        holder.materiiTxt.setText("Materii:" +s.getMaterii());
        holder.mIcon.setInitials(true);
        holder.mIcon.setInitialsNumber(2);
        holder.mIcon.setLetterSize(25);
        holder.mIcon.setShapeColor(mMaterialColors[new Random().nextInt(
                mMaterialColors.length)]);
        holder.mIcon.setLetter(s.getNume());

        if(position % 2 ==0 ) {
            holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
        }else {
            holder.itemView.setBackgroundColor(Color.parseColor("#EFFFFF"));
        }
        //get nume si oras
        String nume = s.getNume().toLowerCase(Locale.getDefault());

        //highlight name text while searching
        if (nume.contains(searchString) && !(searchString.isEmpty())) {
            int startPos = nume.indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanString = Spannable.Factory.getInstance().
                    newSpannable(holder.numeTxt.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.numeTxt.setText(spanString);
        } else {
            //Utils.show(ctx, "Search string empty");
        }

        //deshide detailactivity when clicked
        holder.setItemClickListener(pos -> Utils.sendProfesorToActivity(c, s,
                DetaliiSedintaStudent.class));
    }

    @Override
    public int getItemCount() {
        return profesori.size();
    }

}
//end