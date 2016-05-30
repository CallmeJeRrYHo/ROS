package com.bishe.hjh.ros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.hjh.ros.bean.Comment;
import com.bishe.hjh.ros.bean.Order;
import com.bishe.hjh.ros.util.StreamUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Order mParam1;

    private ListView food;
    private EditText comment;
    private Button commit;
    private Comment c;
    private Double star=0.0;
    private Double preStar=0.0;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(Order param1) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=new Comment();
        if (getArguments() != null) {
            mParam1 = (Order) getArguments().getSerializable(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_comment, container, false);
        food= (ListView) v.findViewById(R.id.lv_food_comment);
        comment= (EditText) v.findViewById(R.id.et_res_comment_comment);

        commit= (Button) v.findViewById(R.id.btn_commit_comment);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setComment(comment.getText().toString());
                c.setOrderId(mParam1.getOrderId());
                c.setResId(mParam1.getRestaurant().getId());
                c.setUserId(mParam1.getUser().getUserId());
                c.setStar(star/mParam1.getOrderFoods().size());
                try {
                    new AsyncTask<String ,String ,String>(){

                        @Override
                        protected String doInBackground(String... params) {
                            String r=null;

                            try {
                                URL url=new URL("http://"+getResources().getString(R.string.ip)+":8080/web/servlet/CommentCommit?comment="+params[0]);
                                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");
                                connection.setConnectTimeout(5000);
                                connection.setReadTimeout(5000);
                                int code=connection.getResponseCode();
                                if (code==200){
                                    r= StreamUtils.parserStream(connection.getInputStream());
                                    if (r.equals("success"))
                                        return r;

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
                            if (s.equals("success")) {
                                Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }else {
                                Toast.makeText(getActivity(), "评论失败", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }.execute(c.toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        food.setAdapter(new commentAdapter());
        return v;
    }

    class commentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mParam1.getOrderFoods().size();
        }

        @Override
        public Object getItem(int position) {
            return mParam1.getOrderFoods().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout ll;
            ViewHolder v;
            if (convertView!=null){
                ll= (LinearLayout) convertView;
                v= (ViewHolder) ll.getTag();
            }else {
                ll= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.comment_food_item,null);
                v=new ViewHolder();
                v.name= (TextView) ll.findViewById(R.id.tv_food_item);
                v.star= (RatingBar) ll.findViewById(R.id.rt_food_item);
                ll.setTag(v);
            }
            v.star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    star=star+rating-preStar;
                    preStar=0.0+rating;
                    Log.d("222222", "onRatingChanged: "+star);
                }
            });
            v.name.setText(mParam1.getOrderFoods().get(position).getFoodId().getName());
            return ll;
        }
    }

    static class ViewHolder{
        TextView name;
        RatingBar star;
    }
}
