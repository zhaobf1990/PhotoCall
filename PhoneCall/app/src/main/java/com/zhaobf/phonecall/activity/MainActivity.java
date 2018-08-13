package com.zhaobf.phonecall.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhaobf.phonecall.R;
import com.zhaobf.phonecall.model.Person;
import com.zhaobf.phonecall.utils.FileUtils;
import com.zhaobf.phonecall.utils.PersonUtils;
import com.zhaobf.phonecall.utils.StringUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private MyAdapter mAdapter;

    List<Person> persons = new ArrayList<>();
    GridView gridView;
    Button mBtnAdd;
    CheckBox checkBox;
    private Button mBtnLoadContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initData();
        gridView = (GridView) findViewById(R.id.gridview);
        mBtnAdd = (Button) findViewById(R.id.add);
        checkBox = (CheckBox) findViewById(R.id.chb_manager);
        mBtnLoadContacts = (Button) findViewById(R.id.loadContacts);
        mAdapter = new MyAdapter(this);


    }


    @Override
    protected void onStart() {
        super.onStart();
        gridView.setAdapter(mAdapter);

//        gridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//                Person person = mAdapter.getItem(((AdapterView.AdapterContextMenuInfo) contextMenuInfo).position);
//
////                if (checkBox.isChecked()) {
////                    Intent intent = new Intent(MainActivity.this, SaveOrUpdateActivity.class);
////                    intent.putExtra("person", person);
////                    startActivityForResult(intent, Constants.Update);
////                } else {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + person.getTel()));
//                startActivity(intent);
////                }
//            }
//        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveOrUpdateActivity.class);
                startActivityForResult(intent, Constants.Update);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAdapter.notifyDataSetChanged();
                if (checkBox.isChecked()) {
                    mBtnAdd.setVisibility(View.VISIBLE);
                    mBtnLoadContacts.setVisibility(View.VISIBLE);
                } else {
                    mBtnAdd.setVisibility(View.GONE);
                    mBtnLoadContacts.setVisibility(View.GONE);
                }
            }
        });

        mBtnLoadContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryContactPhoneNumber();
            }
        });
    }


    private void queryContactPhoneNumber() {
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameFieldColumnIndex);
            String tel = cursor.getString(numberFieldColumnIndex);

            boolean exist = false;
            for (Person person : persons) {
                if (StringUtils.isEquals(person.getTel(), tel)) {
                    exist = true;
                }
            }
            if (!exist) {
                Person person = new Person();
                person.setUuid(UUID.randomUUID().toString());
                person.setNumber(0);
                person.setTel(tel);
                person.setName(name);
                persons.add(person);
            }
        }
        PersonUtils.saveAllPerson(persons);
        mAdapter.notifyDataSetChanged();
    }


    public void initData() {
        persons = PersonUtils.getAllPerson();
//        Person person1 = new Person();
//        person1.setUuid(UUID.randomUUID().toString());
//        person1.setNumber(1);
//        person1.setName("赵佰枫");
//        person1.setTel("18667021101");
//        person1.setHeadImage("/storage/emulated/0/phoneCall/pic/1518592553212.png");
//
//
//        Person person2 = new Person();
//        person1.setUuid(UUID.randomUUID().toString());
//        person2.setNumber(2);
//        person2.setName("赵佰枫");
//        person2.setTel("18667021101");
//        person2.setHeadImage("/storage/emulated/0/phoneCall/pic/1518592553212.png");
//
//
//        Person person3 = new Person();
//        person1.setUuid(UUID.randomUUID().toString());
//        person3.setNumber(3);
//        person3.setName("赵佰枫");
//        person3.setTel("18667021101");
//        person3.setHeadImage("/storage/emulated/0/phoneCall/pic/1518592553212.png");

//        persons.add(person1);
//        persons.add(person2);
//        persons.add(person3);
//        PersonUtils.saveAllPerson(persons);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.Update) {
                Person person = (Person) data.getSerializableExtra("person");
                merge(persons, person);
                PersonUtils.saveAllPerson(persons);
            }
        }
    }

    /**
     * 新增或者更新数据
     *
     * @param persons
     * @param person
     * @return
     */
    public List<Person> merge(List<Person> persons, Person person) {
        if (persons == null || person == null) {
            return new ArrayList<>();
        }
        boolean exist = false;
        for (Person item : persons) {
            if (StringUtils.isEquals(item.getUuid(), person.getUuid())) {
                item.setNumber(person.getNumber());
                item.setTel(person.getTel());
                item.setName(person.getName());
                item.setHeadImage(person.getHeadImage());

                exist = true;
                break;
            }
        }
        if (exist == false) {
            persons.add(person);
        }
        PersonUtils.saveAllPerson(persons);
        mAdapter.notifyDataSetChanged();
        return persons;
    }

    public final class ViewHolder {
        public ImageView head;
        public TextView number;
        public TextView name;
        public Button btEdit;
        public Button btDelete;

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
//            if (persons != null && persons.size() > position) {
//                return persons.get(position).getUuid();
//            }
//            return null;
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.phone_item, null);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                holder.number = (TextView) convertView.findViewById(R.id.number);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.btEdit = (Button) convertView.findViewById(R.id.edit);
                holder.btDelete = (Button) convertView.findViewById(R.id.delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Person item = getItem(position);
            if (StringUtils.isEmpty(item.getHeadImage())) {
                holder.number.setText(item.getNumber() + "");
                holder.number.setVisibility(View.VISIBLE);
                holder.head.setVisibility(View.GONE);
            } else {
                ImageLoader.getInstance().displayImage(Constants.LOCAL_FILE + item.getHeadImage(), holder.head);
                holder.head.setVisibility(View.VISIBLE);
                holder.number.setVisibility(View.GONE);
            }
            if (checkBox.isChecked()) {
                holder.btEdit.setVisibility(View.VISIBLE);
                holder.btDelete.setVisibility(View.VISIBLE);
            } else {
                holder.btEdit.setVisibility(View.GONE);
                holder.btDelete.setVisibility(View.GONE);
            }
            holder.head.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Person person = getItem(position);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + person.getTel()));
                    startActivity(intent);
                    return false;
                }
            });
            holder.number.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Person person = getItem(position);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + person.getTel()));
                    startActivity(intent);
                    return false;
                }
            });
            holder.btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SaveOrUpdateActivity.class);
                    intent.putExtra("person", getItem(position));
                    startActivityForResult(intent, Constants.Update);
                }
            });

            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setMessage("确定要删除[" + getItem(position).getName() + "]吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            persons.remove(getItem(position));
                            PersonUtils.saveAllPerson(persons);
                            mAdapter.notifyDataSetChanged();

                        }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create(); // 创建对话框
                    alertDialog.setCancelable(false);
                    alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK) {
                                return true;
                            } else {
                                return false;
                            }

                        }
                    });
                    alertDialog.show(); // 显示对话框 　

                }
            });
            holder.name.setText("编号:" + item.getNumber() + "  姓名:" + item.getName());
            return convertView;
        }
    }


}
