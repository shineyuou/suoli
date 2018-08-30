package com.annis.appbase.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.annis.appbase.R;

import java.io.Serializable;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * MVP activity基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    //**************** Android M Permission (Android 6.0权限控制代码封装)
    private int permissionRequestCode = 88;
    private PermissionCallback permissionRunnable;



    protected abstract int getLayout();

    protected abstract TitleBean getMyTitle();

    protected abstract T getPresenter();

    protected abstract void initViewAndEvent();

    protected abstract void laodData();

    protected Activity mContext;
    private Unbinder mUnBinder;
    protected T mPresenter;

    public interface PermissionCallback {
        void hasPermission();

        void noPermission();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏  ,没有时间栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initData();
        initViewAndEvent();
        laodData();
    }

    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.act_base, null, true);
        getLayoutInflater().inflate(getLayout(), view.findViewById(R.id.base_content_container), true);
        setContentView(view);
        mUnBinder = ButterKnife.bind(this);
        setMyTitle(view);
    }

    /**
     * 设置标题
     * 如果有返回标题靠左显示,否则居中, 右边如果有view 传入View对象
     *
     * @param view
     */
    private void setMyTitle(View view) {
        TitleBean title = getMyTitle();
        if (title == null) {
            view.findViewById(R.id.base_title_container).setVisibility(View.GONE);
        } else {
            if (title.getBackColor() == 0) {
                view.findViewById(R.id.base_title_container).setBackgroundColor(getDarkColorPrimary());
            } else {
                view.findViewById(R.id.base_title_container).setBackgroundColor(title.getBackColor());
            }
            if (title.getTitleColor() != 0) {
                ((TextView) view.findViewById(R.id.middle_title)).setTextColor(title.getTitleColor());
                ((TextView) view.findViewById(R.id.left_title)).setTextColor(title.getTitleColor());
            }
            if (!title.isBack()) {
                view.findViewById(R.id.toolbar_back_container).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.toolbar_back_container).setOnClickListener(v -> finish());
            }
            if (!TextUtils.isEmpty(title.getLeftTitle())) {
                ((TextView) view.findViewById(R.id.left_title)).setText(title.getLeftTitle());
            }
            if (!TextUtils.isEmpty(title.getTitle())) {
                ((TextView) view.findViewById(R.id.middle_title)).setText(title.getTitle());
            }
            if (title.getRightView() != null) {
                ((ViewGroup) view.findViewById(R.id.right_view)).addView(title.getRightView());
            }
            if (title.getRightViewId() != 0) {
                getLayoutInflater().inflate(title.getRightViewId(), view.findViewById(R.id.right_view), true);
            }
        }
        title = null;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mContext = this;
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 获取主题颜色
     *
     * @return
     */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (disposables != null) {
            disposables.dispose();
        }
        super.onDestroy();
    }

    /**
     * 管理Destroy取消订阅者者
     */
    CompositeDisposable disposables;

    public void addDisposables(Disposable d) {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.add(d);
    }

    public void removeDisposables(Disposable d) {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.remove(d);
    }

    /****************   常用操作   ****************/
    public void startAcitvity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    public void startAcitvity(Class clazz, Serializable object) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("object", object);
        startActivity(intent);
    }

    public void startAcitvity(Class clazz, String filepath) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("filepath", filepath);
        startActivity(intent);
    }

    public void startAcitvity(Class clazz, Parcelable object) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("object", object);
        startActivity(intent);
    }

    public void startAcitvity(Class clazz, Parcelable object, String filepath) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("object", object);
        intent.putExtra("filepath",filepath);
        startActivity(intent);
    }

    public Parcelable getIntentObj() {
        return getIntent().getParcelableExtra("object");
    }

    public List getIntentList() {
        return (List) getIntent().getSerializableExtra("object");
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * Android M运行时权限请求封装
     *
     * @param permissionDes 权限描述
     * @param runnable      请求权限回调
     * @param permissions   请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
     */
    public void performCodeWithPermission(@NonNull String permissionDes, PermissionCallback runnable, @NonNull String... permissions) {
        if (permissions.length == 0)
            return;
        this.permissionRunnable = runnable;
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || checkPermissionGranted(permissions)) {
            if (permissionRunnable != null) {
                permissionRunnable.hasPermission();
                permissionRunnable = null;
            }
        } else {
            //permission has not been granted.
            requestPermission(permissionDes, permissionRequestCode, permissions);
        }

    }

    private boolean checkPermissionGranted(String[] permissions) {
        boolean flag = true;
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private void requestPermission(String permissionDes, final int requestCode, final String[] permissions) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.

            //            Snackbar.make(getWindow().getDecorView(), requestName,
            //                    Snackbar.LENGTH_INDEFINITE)
            //                    .setAction(R.string.common_ok, new View.OnClickListener() {
            //                        @Override
            //                        public void onClick(View view) {
            //                            ActivityCompat.requestPermissions(BaseAppCompatActivity.this,
            //                                    permissions,
            //                                    requestCode);
            //                        }
            //                    })
            //                    .show();
            //如果用户之前拒绝过此权限，再提示一次准备授权相关权限
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(permissionDes)
                    .setCancelable(false)
                    .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
                        }
                    }).show();

        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == permissionRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (permissionRunnable != null) {
                    permissionRunnable.hasPermission();
                    permissionRunnable = null;
                }
            } else {
                Toast.makeText(this, "暂无权限执行相关操作！", Toast.LENGTH_SHORT).show();
                if (permissionRunnable != null) {
                    permissionRunnable.noPermission();
                    permissionRunnable = null;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    //********************** END Android M Permission ****************************************
}
