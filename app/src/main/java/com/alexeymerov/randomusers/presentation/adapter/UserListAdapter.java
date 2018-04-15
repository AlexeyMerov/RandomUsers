package com.alexeymerov.randomusers.presentation.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexeymerov.randomusers.R;
import com.alexeymerov.randomusers.data.db.entity.UserEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class UserListAdapter
        extends BaseRecyclerAdapter<UserEntity, UserListAdapter.UserViewHolder> {

    private Consumer<Long> mOnItemClick;

    @Override
    public long getItemId(int position) {
        return getItems().get(position).getId();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(getItems().get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_user;
    }

    @Override
    protected void proceedPayloads(@NonNull UserViewHolder holder, int position, @NonNull List<Object> payloads) {

    }

    @Override
    public boolean compareItems(UserEntity oldItem, UserEntity newItem) {
        return false;
    }

    public void setOnItemClick(Consumer<Long> onItemClick) {
        mOnItemClick = onItemClick;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.containerLayout)
        View containerLayout;

        @BindView(R.id.userLetter)
        TextView userLetter;

        @BindView(R.id.userNameTextView)
        TextView userNameTextName;

        @BindView(R.id.userNamePhoneTextView)
        TextView userNamePhoneTextView;

        UserViewHolder(View containerView) {
            super(containerView);
            ButterKnife.bind(this, containerView);
        }

        void bind(UserEntity entity) {
            containerLayout.setOnClickListener(v -> {
                try {
                    mOnItemClick.accept(entity.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            userLetter.setText(String.valueOf(entity.getName().charAt(0)));
            ((GradientDrawable) userLetter.getBackground()).setColor(entity.getColor());
            userNameTextName.setText(entity.getName());
            userNamePhoneTextView.setText(entity.getPhoneNumber());
        }
    }
}
