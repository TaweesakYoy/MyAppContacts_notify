package taweesak.com.myappcontactsnotify.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

import taweesak.com.myappcontactsnotify.Activity.EditActivity;
import taweesak.com.myappcontactsnotify.Activity.MainActivity;
import taweesak.com.myappcontactsnotify.Appointment.CreateNote;
import taweesak.com.myappcontactsnotify.Data.Contact;
import taweesak.com.myappcontactsnotify.R;
import taweesak.com.myappcontactsnotify.reminder.ReminderAddActivity;


public class RecyclerMainViewAdapter extends RecyclerSwipeAdapter<RecyclerMainViewAdapter.MyViewHolder> {

    public Context mContext;
    public static List<Contact> mData;

    public RecyclerMainViewAdapter(Context mContext, List<Contact> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textViewName.setText(mData.get(position).getFirstName() + "  " + mData.get(position).getLastName());

        byte[] bytes = mData.get(position).getImage();
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.img.setImageBitmap(bitmap);
            holder.imgR.setImageBitmap(bitmap);
            holder.imgL.setImageBitmap(bitmap);
        } else {
            holder.img.setImageResource(R.drawable.ic_person);
        }

        // Event
        new SwipeEventVH1(holder);

        // mItemManger is member in RecyclerSwipeAdapter Class// เมื่อเลือก Item อื่น ---item ก่อนหน้านี้จะเลื่อนกลับ
        mItemManger.bindView(((MyViewHolder) holder).itemView, position);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends ViewHolder {

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
        private ImageView swipe_item_alarm;

        private TextView add_first_name;
        private TextView add_last_name;

        public SwipeLayout swipeLayout;

        public MyViewHolder(final View itemView) {
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

            swipe_item_alarm = itemView.findViewById(R.id.swipe_item_alarm);

            // send Data to Notify ********test
            swipe_item_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = textViewName.getText().toString();

                    Bundle bundle = new Bundle();

                    Drawable drawable=img.getDrawable();
                    Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();


                    //Intent intent = new Intent(v.getContext(),CreateNote.class);
                    Intent intent = new Intent(v.getContext(),ReminderAddActivity.class);
                    //intent.putExtras(bundle);
                    intent.putExtra("name",name);
                    intent.putExtra("image", b);
                    Toast.makeText(v.getContext(), "set alarm", Toast.LENGTH_SHORT).show();

                    v.getContext().startActivity(intent);
                    //((Activity) v.getContext()).finish();

                }
            });

            //Event email
            icon_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    final Contact mailContacts = mData.get(getAdapterPosition());
                    if(mailContacts.getEmail().isEmpty()){
                        //**********
                        AlertDialog.Builder builderMail = new AlertDialog.Builder(v.getContext());

                        builderMail.setTitle("Your email address is emtry");

                        builderMail.setMessage("You can insert email address");
                        builderMail.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(v.getContext(), EditActivity.class);
                                i.putExtra("id", String.valueOf(mailContacts.getId()));
                                v.getContext().startActivity(i);
                                ((Activity) v.getContext()).finish();

                            }
                        });
                        builderMail.setNegativeButton("No", null);
                        builderMail.show();
                        //**********
                    }else{
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse(Uri.parse("mailto:")+mailContacts.getEmail()));
                        v.getContext().startActivity(emailIntent);
                    }

                }
            });


            // Event Call
            icon_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String phoneContact = mData.get(getAdapterPosition()).getPhone();
                    final Contact phoneContacts = mData.get(getAdapterPosition());

                    if (phoneContacts.getPhone().isEmpty()){
                        //**********
                        AlertDialog.Builder builderPhone = new AlertDialog.Builder(v.getContext());

                        builderPhone.setTitle("Your phone number is emtry");

                        builderPhone.setMessage("You can insert phone number");
                        builderPhone.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(v.getContext(), EditActivity.class);
                                i.putExtra("id", String.valueOf(phoneContacts.getId()));
                                v.getContext().startActivity(i);
                                ((Activity) v.getContext()).finish();

                            }
                        });
                        builderPhone.setNegativeButton("No", null);
                        builderPhone.show();
                        //**********
                    }else {
                        Intent intent = new Intent( Intent.ACTION_DIAL );

                        intent.setData( Uri.parse("tel:"+phoneContact.toString()) );
                        v.getContext().startActivity( intent );
                    }
                }
            });

            // Event Line
            icon_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    final Contact lineContacts = mData.get(getAdapterPosition());

                    if(lineContacts.getLine().isEmpty()){
                        //**********
                        AlertDialog.Builder builderLine = new AlertDialog.Builder(v.getContext());

                        builderLine.setTitle("Your line id is emtry");

                        builderLine.setMessage("You can insert line id");
                        builderLine.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(v.getContext(), EditActivity.class);
                                i.putExtra("id", String.valueOf(lineContacts.getId()));
                                v.getContext().startActivity(i);
                                ((Activity) v.getContext()).finish();

                            }
                        });
                        builderLine.setNegativeButton("No", null);
                        builderLine.show();
                        //**********
                    }else{
                        try {
                            Intent intent = v.getContext().getPackageManager().getLaunchIntentForPackage("//line.me/ti/p/~"+lineContacts.getLine().toString());
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            v.getContext().startActivity(intent);
                        } catch (NullPointerException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            //intent.setData(Uri.parse("http:////line.me/ti/p/~0940430942"));
                            intent.setData(Uri.parse("http://line.me/ti/p/~"+lineContacts.getLine().toString()));
                            v.getContext().startActivity(intent);
                        }
                    }



                }
            });

            // Event Edit
            swipe_item_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(v.getContext(), "click "+mData.get(getAdapterPosition()).getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), EditActivity.class);
                    intent.putExtra("id", String.valueOf(mData.get(getAdapterPosition()).getId()));
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

                    builder.setMessage("To delete : "+mData.get(getAdapterPosition()).getFirstName()+" "+mData.get(getAdapterPosition()).getLastName());
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //MainActivity.myDb.deleteData(EditActivity.currentContact.getId());
                            MainActivity.myDb.deleteData(mData.get(getAdapterPosition()).getId());
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

    public class SwipeEventVH1 {
        public SwipeEventVH1(MyViewHolder holder) {
            // Drag From Left
            holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.swipeLayout.findViewById(R.id.swipe_item_list_left_bottom_wrapper));

            // Drag From Right
            holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.item_list_bottom_wrapper));

            // Handling different events when swiping
            holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        // swipe_list_item.xml
        return R.id.swipe;
    }
}
