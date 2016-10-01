package com.example.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Message> list;
    private Context context;

    public MessageAdapter(Context context, ArrayList<Message> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 1:
                return new MessageOtherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble_other, parent, false));
            case 0:
                return new MessageSelfViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble_self, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == 0) {
            ((MessageSelfViewHolder) holder).timestamp.setText(list.get(position).getTimestamp());
            ((MessageSelfViewHolder) holder).message.setText(list.get(position).getMessage());
            ((MessageSelfViewHolder) holder).sender.setText(list.get(position).getSender());
        } else {
            ((MessageOtherViewHolder) holder).timestamp.setText(list.get(position).getTimestamp());
            ((MessageOtherViewHolder) holder).message.setText(list.get(position).getMessage());
            ((MessageOtherViewHolder) holder).sender.setText(list.get(position).getSender());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isSelf())
            return 0;
        else
            return 1;
    }

    private class MessageSelfViewHolder extends RecyclerView.ViewHolder {

        private TextView sender, message, timestamp;

        public MessageSelfViewHolder(View itemView) {
            super(itemView);
            sender = (TextView) itemView.findViewById(R.id.senderTextSelf);
            message = (TextView) itemView.findViewById(R.id.message_text_self);
            timestamp = (TextView) itemView.findViewById(R.id.messageTimestampSelf);
        }
    }

    private class MessageOtherViewHolder extends RecyclerView.ViewHolder {

        private TextView sender, message, timestamp;

        public MessageOtherViewHolder(View itemView) {
            super(itemView);
            sender = (TextView) itemView.findViewById(R.id.senderTextOther);
            message = (TextView) itemView.findViewById(R.id.message_text_other);
            timestamp = (TextView) itemView.findViewById(R.id.messageTimestampOther);
        }
    }
}
