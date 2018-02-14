package com.zhaobf.phonecall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaobf.phonecall.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyAdapter mAdapter;

    List<Person> persons = new ArrayList<>();
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initData();
        gridView = (GridView) findViewById(R.id.gridview);

        mAdapter = new MyAdapter(this);


    }


    @Override
    protected void onStart() {
        super.onStart();
        gridView.setAdapter(mAdapter);

        gridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                Person person = mAdapter.getItem(((AdapterView.AdapterContextMenuInfo) contextMenuInfo).position);
                Toast.makeText(MainActivity.this, person.getNumber(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + person.getNumber()));
                startActivity(intent);

            }
        });
    }


    public void initData() {
        Person person1 = new Person();
        person1.setId(1);
        person1.setName("赵佰枫");
        person1.setNumber("18667021101");


        Person person2 = new Person();
        person2.setId(2);
        person2.setName("赵佰枫");
        person2.setNumber("18667021101");

        Person person3 = new Person();
        person3.setId(2);
        person3.setName("赵佰枫");
        person3.setNumber("18667021101");

        persons.add(person1);
        persons.add(person2);
        persons.add(person3);

    }

    public final class ViewHolder {
        public ImageView head;
        public TextView personId;

    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Person getItem(int position) {
            if (persons != null && persons.size() > position) {
                return persons.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (persons != null && persons.size() > position) {
                return persons.get(position).getId();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.phone_item, null);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                holder.personId = (TextView) convertView.findViewById(R.id.person_id);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Person item = getItem(position);
            holder.personId.setText(item.getId() + "");
            return convertView;
        }
    }


}
