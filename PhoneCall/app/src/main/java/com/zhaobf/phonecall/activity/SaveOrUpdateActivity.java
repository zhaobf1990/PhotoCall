package com.zhaobf.phonecall.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhaobf.phonecall.model.Person;
import com.zhaobf.phonecall.utils.CameraUtil;
import com.zhaobf.phonecall.R;
import com.zhaobf.phonecall.utils.FileUtils;
import com.zhaobf.phonecall.utils.StringUtils;
import com.zhaobf.phonecall.utils.TgPictureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaveOrUpdateActivity extends AppCompatActivity {

    /**
     * 相册
     */
    public static final int REQUEST_CODE_ALBUM = 1;
    /**
     * 摄像头
     */
    public static final int REQUEST_CODE_CAMERA = 0;
    private String mHeadImage;
    private CameraUtil mImageUtil;

    Button mBtnTakePhoto;
    Button mBtnAlbum;
    private EditText mEtNumber;
    private EditText mEtName;
    private EditText mEtTel;
    private Button mBtnBack;
    private Button mBtnSave;
    private ImageView mImage;
    private Person person;
    private ArrayAdapter<Integer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_or_update);

        Intent intent = getIntent();
        if (intent.hasExtra("person")) {
            person = (Person) getIntent().getSerializableExtra("person");
        } else {
            person = new Person();
        }


//        Toast.makeText(this, person.getName(), Toast.LENGTH_SHORT).show();

        mBtnTakePhoto = (Button) findViewById(R.id.take_photo);
        mBtnAlbum = (Button) findViewById(R.id.album);
        mEtNumber = (EditText) findViewById(R.id.number);
        mEtTel = (EditText) findViewById(R.id.tel);
        mEtName = (EditText) findViewById(R.id.name);
        mBtnBack = (Button) findViewById(R.id.back);
        mBtnSave = (Button) findViewById(R.id.save);
        mImage = (ImageView) findViewById(R.id.image);

        mImageUtil = new CameraUtil(this);

//        List<Integer> numbers = getNumbers();

//        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, numbers);
//        mEtNumber.setAdapter(adapter);
        initListener();
        showInView(person);
    }

    private void showInView(Person person) {

        if (person.getNumber() != null) {
            mEtNumber.setText(person.getNumber() + "");
        }
        mEtName.setText(person.getName());
        mHeadImage = person.getHeadImage();
        ImageLoader.getInstance().displayImage(Constants.LOCAL_FILE + person.getHeadImage(), mImage);
        mEtTel.setText(person.getTel());
    }

    private List<Integer> getNumbers() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        return numbers;
    }

    private void initListener() {
        mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = TgPictureUtil.getTSignPicTmpFile();
                mHeadImage = f.getPath();
                mImageUtil.openCamera(SaveOrUpdateActivity.this, REQUEST_CODE_CAMERA, f);
            }
        });
        mBtnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageUtil.openPhotosSingle(SaveOrUpdateActivity.this, REQUEST_CODE_ALBUM);
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = mEtNumber.getText().toString();
                String name = mEtName.getText().toString();
                String tel = mEtTel.getText().toString();
                if (StringUtils.isEmpty(number)) {
                    Toast.makeText(SaveOrUpdateActivity.this, "编号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isEmpty(name)) {
                    Toast.makeText(SaveOrUpdateActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isEmpty(tel)) {
                    Toast.makeText(SaveOrUpdateActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                person.setNumber(Integer.valueOf(mEtNumber.getText().toString()));
                person.setName(mEtName.getText().toString());
                person.setTel(mEtTel.getText().toString());
                if (!StringUtils.isEmpty(mHeadImage)) {
                    person.setHeadImage(mHeadImage);
                }


                if (StringUtils.isEmpty(person.getUuid())) {
                    person.setUuid(UUID.randomUUID().toString());
                }

                Intent intent = new Intent();
                intent.putExtra("person", person);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                Toast.makeText(this, mHeadImage, Toast.LENGTH_SHORT).show();
                ImageLoader.getInstance().displayImage(Constants.LOCAL_FILE + mHeadImage, mImage);
//                mImage.setImageURI(Uri.fromFile(new File(mHeadImage)));
            } else if (requestCode == REQUEST_CODE_ALBUM) {
                mHeadImage = CameraUtil.getPath(getApplicationContext(), data.getData());
                Toast.makeText(this, mHeadImage, Toast.LENGTH_SHORT).show();
                File newFile = TgPictureUtil.getTSignPicTmpFile();
                FileUtils.copyFile(mHeadImage, newFile.getPath());
                mHeadImage = newFile.getPath();
                ImageLoader.getInstance().displayImage(Constants.LOCAL_FILE + mHeadImage, mImage);

//                mImage.setImageURI(Uri.fromFile(new File(mHeadImage)));
            }

        }
    }
}
