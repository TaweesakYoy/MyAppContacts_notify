package taweesak.com.myappcontactsnotify.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

import taweesak.com.myappcontactsnotify.Activity.EditActivity;
import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.Data.Contact;
import taweesak.com.myappcontactsnotify.R;

public class RecyclerFavoriteAdapter extends RecyclerSwipeAdapter<RecyclerFavoriteAdapter.VhFav> {

    Context mContext;
    ArrayList<Contact> mContact = new ArrayList<Contact>();

    public RecyclerFavoriteAdapter(Context mContext, ArrayList<Contact> data) {
        this.mContext = mContext;
        // Check Favorite true or flase
        for (Contact c: data) {
            if (c.getFavorite() == 1)
                mContact.add(c);
        }
    }

    @Override
    public VhFav onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);
        VhFav vhFav = new VhFav(itemView);
        return vhFav;
    }

    @Override
    public void onBindViewHolder(VhFav viewHolder, int position) {

        //viewHolder.textViewName.setText(mContact.get(position).getFirstName());
        viewHolder.textViewName.setText(mContact.get(position).getFirstName()+ "  "+mContact.get(position).getLastName());

        byte[] bytes = mContact.get(position).getImage();
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            viewHolder.img.setImageBitmap(bitmap);
            viewHolder.imgR.setImageBitmap(bitmap);
            viewHolder.imgL.setImageBitmap(bitmap);
        }
        else {
            viewHolder.img.setImageResource(R.drawable.ic_person);
        }

        // Event
        //new SwipeEventVhFav(viewHolder);
        new SwipeEventVhFav(viewHolder);

        // mItemManger is member in RecyclerSwipeAdapter Class// เมื่อเลือก Item อื่น ---item ก่อนหน้านี้จะเลื่อนกลับ
        mItemManger.bindView(((VhFav) viewHolder).itemView, position);

    }

    @Override
    public int getItemCount() {
        return mContact.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class SwipeEventVhFav {
        public SwipeEventVhFav(VhFav viewHolder) {
            // Drag From Left
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.swipe_item_list_left_bottom_wrapper));

            // Drag From Right
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.item_list_bottom_wrapper));

            // Handling different events when swiping
            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });


        }


    }

    // ViewHolder
    public class VhFav extends RecyclerView.ViewHolder{

        private LinearLayout item;
        private TextView textViewName;
        private TextView textViewPhone;
        private TextView textViewEmail;
        private TextView textViewLine;
        private ImageView img;
        private ImageView imgR;
        private ImageView imgL;
        private ImageView icon_line, icon_phone, icon_mail;
        private ImageView swipe_item_edit,swipe_item_delete;

        public SwipeLayout swipeLayout;

        public VhFav(View itemView) {
            super(itemView);

            //swipe
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);

            img = (ImageView) itemView.findViewById(R.id.contact_img);
            imgR = (ImageView) itemView.findViewById(R.id.contact_img_R);
            imgL = (ImageView) itemView.findViewById(R.id.contact_img_L);

            icon_line = itemView.findViewById(R.id.icon_line);
            icon_phone = itemView.findViewById(R.id.icon_phone);
            icon_mail = itemView.findViewById(R.id.icon_mail);


            textViewName = itemView.findViewById(R.id.tv_name_priview);

            swipe_item_edit = itemView.findViewById(R.id.swipe_item_edit);
            swipe_item_delete = itemView.findViewById(R.id.swipe_item_delete);

            //Event email
            icon_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse(Uri.parse("mailto:")+mContact.get(getAdapterPosition()).getEmail()));
                    v.getContext().startActivity(emailIntent);
                }
            });

            // Event Call
            icon_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( Intent.ACTION_DIAL );
                    intent.setData( Uri.parse("tel:"+mContact.get(getAdapterPosition()).getPhone().toString()) );
                    v.getContext().startActivity( intent );
                }
            });

            // Event Line
            icon_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = v.getContext().getPackageManager().getLaunchIntentForPackage("//line.me/ti/p/~"+mContact.get(getAdapterPosition()).getLine().toString());
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        v.getContext().startActivity(intent);
                    } catch (NullPointerException e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setData(Uri.parse("http:////line.me/ti/p/~0940430942"));
                        intent.setData(Uri.parse("http://line.me/ti/p/~"+mContact.get(getAdapterPosition()).getLine().toString()));
                        v.getContext().startActivity(intent);
                    }
                }
            });

            // Event Edit
            swipe_item_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(v.getContext(), "click "+mData.get(getAdapterPosition()).getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), EditActivity.class);
                    intent.putExtra("id", String.valueOf(mContact.get(getAdapterPosition()).getId()));
                    v.getContext().startActivity(intent);
                    ((Activity) v.getContext()).finish();

                }
            });

            //Event Delete
            swipe_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle("Are you sure?");

                    builder.setMessage("To delete : "+mContact.get(getAdapterPosition()).getFirstName()+" "+mContact.get(getAdapterPosition()).getLastName());
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //MainActivity.myDb.deleteData(EditActivity.currentContact.getId());
                            MainActivity.myDb.deleteData(mContact.get(getAdapterPosition()).getId());
                            MainActivity.getThemAll();
                            Intent i = new Intent(v.getContext(), MainActivity.class);
                            v.getContext().startActivity(i);
                            ((Activity) v.getContext()).finish();

                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();

                }
            });

        }
    }
}
