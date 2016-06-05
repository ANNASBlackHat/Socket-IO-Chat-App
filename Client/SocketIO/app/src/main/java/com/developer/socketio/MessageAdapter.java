package com.developer.socketio;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ANNASBlackHat on 6/4/2016.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == Message.TYPE_MESSAGE){
            return new ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false));
        }else
            return new ViewHolderConnect(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_connect, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(holder instanceof ViewHolderItem){
            ViewHolderItem item = (ViewHolderItem) holder;
            item.txtUname.setText(message.getUsername());
            item.txtMsg.setText(message.getMessage());
        }else{
            ViewHolderConnect connect = (ViewHolderConnect) holder;
            if(message.getType() == Message.TYPE_CONNECT){
                if(message.getUsername().equals(RegisterActivity.USERNAME))
                    connect.txtUsername.setText("Welcome "+message.getUsername());
                else
                    connect.txtUsername.setText(message.getUsername()+" join chat");
            }else{
                connect.txtUsername.setText(message.getUsername()+" left chat");
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder{

        TextView txtMsg;
        TextView txtUname;

        public ViewHolderItem(View itemView) {
            super(itemView);
            txtMsg = (TextView) itemView.findViewById(R.id.message);
            txtUname = (TextView) itemView.findViewById(R.id.username);
        }
    }

    class ViewHolderConnect extends RecyclerView.ViewHolder{

        TextView txtUsername;
        public ViewHolderConnect(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.username);
        }
    }
}
