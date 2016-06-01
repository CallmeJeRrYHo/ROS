package com.bishe.hjh.ros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bishe.hjh.ros.bean.Comment;
import com.bishe.hjh.ros.util.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link appraiseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class appraiseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private ListView lv;
    private PtrClassicFrameLayout ptr;
    private List<Comment> list;
    public appraiseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment appraiseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static appraiseFragment newInstance(int param1) {
        appraiseFragment fragment = new appraiseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_appraise, container, false);

        lv= (ListView) v.findViewById(R.id.lv_appraise_appraise);
        ptr= (PtrClassicFrameLayout) v.findViewById(R.id.ptr_comment_appraise);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new resComment().execute(""+mParam1);

            }
        });
        new resComment().execute(""+mParam1);
        return v;
    }
    class resComment extends AsyncTask<String ,String ,String >{


        private String r=null;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/web/servlet/ReadResComment?resId="+params[0]);
                HttpURLConnection con= (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                int code=con.getResponseCode();
                if (code==200){
                    r= StreamUtils.parserStream(con.getInputStream());

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONArray ja;
            list=new ArrayList<>();
            try {
                ja = new JSONArray(r);
                for (int i=0;i<ja.length();i++){
                    Comment c=new Comment(ja.getJSONObject(i));
                    list.add(c);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            lv.setAdapter(new MyAdapter());
            ptr.refreshComplete();
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout ll;
            ViewHolder v;
            if (convertView!=null){
                ll= (RelativeLayout) convertView;
                v= (ViewHolder) ll.getTag();
            }else {
                ll= (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.restaurant_comment_item,null);
                v=new ViewHolder();
                v.civ= (CircleImageView) ll.findViewById(R.id.civ_userImage_item);
                v.user= (TextView) ll.findViewById(R.id.tv_userName_item);
                v.date= (TextView) ll.findViewById(R.id.tv_time_item);
                v.rb= (RatingBar) ll.findViewById(R.id.rb_comment_star_item);
                v.comment= (TextView) ll.findViewById(R.id.tv_comment_item);
                ll.setTag(v);
            }
            v.user.setText(list.get(position).getUserId()+"");
            v.comment.setText(list.get(position).getComment());
            v.date.setText(list.get(position).getDate());
            float f=Float.parseFloat(list.get(position).getStar().toString());
            v.rb.setRating(f);
            return ll;
        }
    }
    class  ViewHolder{
        CircleImageView civ;
        TextView user;
        TextView date;
        RatingBar rb;
        TextView comment;
    }
}
