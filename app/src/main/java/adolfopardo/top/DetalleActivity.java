package adolfopardo.top;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetalleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int RC_PHOTO_PICKER = 21;

    @BindView(R.id.imageFoto)
    AppCompatImageView imageFoto;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.etNombre)
    TextInputEditText etNombre;
    @BindView(R.id.etApellidos)
    TextInputEditText etApellidos;
    @BindView(R.id.etGenero)
    TextInputEditText etGenero;
    @BindView(R.id.etFechaNacimiento)
    TextInputEditText etFechaNacimiento;
    @BindView(R.id.etEdad)
    TextInputEditText etEdad;
    @BindView(R.id.etEstatura)
    TextInputEditText etEstatura;
    @BindView(R.id.etOrden)
    TextInputEditText etOrden;
    @BindView(R.id.etLugarNacimiento)
    TextInputEditText etLugarNacimiento;
    @BindView(R.id.etNotas)
    TextInputEditText etNotas;
    @BindView(R.id.containerMain)
    NestedScrollView containerMain;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Artista mArtista;

    private Calendar mCalendar;

    private MenuItem mMenuItem;

    private boolean mIsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        ButterKnife.bind(this);

        configArtista(getIntent());
        configAtionBar();
        configImageView(mArtista.getFotoUrl());
        configCalendar();


    }

    private void configArtista(Intent intent) {
        //mArtista = MainActivity.sArtista;

        getArtist(intent.getLongExtra(Artista.ID, 0));

        etNombre.setText(mArtista.getNombre());
        etApellidos.setText(mArtista.getApellido());
        etGenero.setText(mArtista.getGenero());
        etFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
                .format(mArtista.getFechaNacimiento()));
        etEdad.setText(getEdad(mArtista.getFechaNacimiento()));
        etEstatura.setText(String.valueOf(mArtista.getEtatura()));
        etOrden.setText(String.valueOf(mArtista.getOrden()));
        etLugarNacimiento.setText(mArtista.getLugarNacimiento());
        etNotas.setText(mArtista.getNotas());
    }

    private void getArtist(long id) {
        mArtista = SQLite
                .select()
                .from(Artista.class)
                .where(Artista_Table.id.is(id))
                .querySingle();
    }

    private String getEdad(long fechaNacimiento) {
        Long time = Calendar.getInstance().getTimeInMillis() / 1000 - fechaNacimiento / 1000;
        final int years = Math.round(time) / 31536000;
        return String.valueOf(years);
    }

    private void configAtionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        configTitle();
    }

    private void configTitle() {
        toolbarLayout.setTitle(mArtista.getNombreCompleto());
    }

    private void configImageView(String fotoUrl) {
        if (fotoUrl != null) {
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();

            Glide.with(this).load(fotoUrl).apply(options).into(imageFoto);
        } else {
            imageFoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_photo_size_select_actual));
        }
        mArtista.setFotoUrl(fotoUrl);

    }

    private void configCalendar() {
        mCalendar = Calendar.getInstance(Locale.ROOT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        mMenuItem = menu.findItem(R.id.action_save);
        mMenuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveOrEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RC_PHOTO_PICKER:
                    savePhotoUrlArtist(data.getDataString());
                    break;

            }
        }
    }

    private void savePhotoUrlArtist(String fotoUrl) {
        try {
            mArtista.setFotoUrl(fotoUrl);
            mArtista.update();
            configImageView(fotoUrl);
            showMessage(R.string.detalle_message_update_success);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(R.string.detalle_message_update_fail);
        }
    }

    @OnClick(R.id.fab)
    public void saveOrEdit() {
        if (mIsEdit) {
            if (validateFields()) {
                mArtista.setNombre(etNombre.getText().toString().trim());
                mArtista.setApellido(etApellidos.getText().toString().trim());
                mArtista.setGenero(etGenero.getText().toString().trim());
                mArtista.setEtatura(Short.valueOf(etEstatura.getText().toString().trim()));
                mArtista.setLugarNacimiento(etLugarNacimiento.getText().toString().trim());
                mArtista.setNotas(etNotas.getText().toString().trim());
                try {
                    mArtista.update();
                    configTitle();
                    showMessage(R.string.detalle_message_update_success);
                    Log.i("DBFlow", "Insercion correcta de datos");
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(R.string.detalle_message_update_fail);
                    Log.i("DBFlow", "Error al insertar datos");
                }
            }

            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_edit));
            enableUIElements(false);
            mIsEdit = false;
        } else {
            mIsEdit = true;
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_check));
            enableUIElements(true);
        }
    }


    private boolean validateFields() {
        boolean isValid = true;
        if (etEstatura.getText().toString().trim().isEmpty() ||
                Integer.valueOf(etEstatura.getText().toString().trim()) < getResources().getInteger(R.integer.estatura_min)) {
            etEstatura.setError(getString(R.string.addArtist_error_estaturaMin));
            etEstatura.requestFocus();
            isValid = false;
        }
        if (etApellidos.getText().toString().trim().isEmpty()) {
            etApellidos.setError(getString((R.string.addArtist_error_required)));
            etApellidos.requestFocus();
            isValid = false;
        }
        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError(getString((R.string.addArtist_error_required)));
            etNombre.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private void enableUIElements(boolean enable) {
        etNombre.setEnabled(enable);
        etApellidos.setEnabled(enable);
        etGenero.setEnabled(enable);
        etFechaNacimiento.setEnabled(enable);
        etEstatura.setEnabled(enable);
        etLugarNacimiento.setEnabled(enable);
        etNotas.setEnabled(enable);

        mMenuItem.setVisible(enable);
        appBar.setExpanded(!enable);
        containerMain.setNestedScrollingEnabled(!enable);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);

        etFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyy", Locale.ROOT)
                .format(mCalendar.getTimeInMillis()));

        mArtista.setFechaNacimiento(mCalendar.getTimeInMillis());
        etEdad.setText(getEdad(mCalendar.getTimeInMillis()));
    }

    @OnClick(R.id.etFechaNacimiento)
    public void onSetFecha() {
        DialogSelectorFecha selectorFecha = new DialogSelectorFecha(); //de la nueva clase del mismo nombre
        selectorFecha.setListener(DetalleActivity.this);

        Bundle args = new Bundle();
        args.putLong(DialogSelectorFecha.FECHA, mArtista.getFechaNacimiento());
        selectorFecha.setArguments(args);
        selectorFecha.show(getSupportFragmentManager(), DialogSelectorFecha.SELECTED_DATE);
    }

    private void showMessage(int resource) {
        Snackbar.make(containerMain, resource, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.imageDeleteFoto, R.id.imageFromGalery, R.id.imageFromUrl})
    public void photoHandler(View view) {
        switch (view.getId()) {
            case R.id.imageDeleteFoto:
                /*Alerta de eliminado*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.detalle_dialogDelete_title)
                        .setMessage(String.format(Locale.ROOT,
                                getString(R.string.detalle_dialogDelete_message),
                                mArtista.getNombreCompleto()))
                        .setPositiveButton(R.string.detalle_dialogDelete_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                savePhotoUrlArtist(null);
                            }
                        })
                        .setNegativeButton(R.string.label_dialog_cancel, null);
                builder.show();
                /*Fin de Alerta de eliminado*/
                break;
            case R.id.imageFromGalery:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //pedir tipo de archivo
                intent.setType("image/jpg");//establecer imagenes jpg
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,
                        getString(R.string.detalle_chooser_title)),RC_PHOTO_PICKER);

                break;
            case R.id.imageFromUrl:
                showAddPhotoDialog();
                break;
        }
    }
    private void showAddPhotoDialog() {
        final EditText etFotoUrl = new EditText(this);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle(R.string.addArtist_dialogUrl_title)
                .setPositiveButton(R.string.Label_dialog_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        savePhotoUrlArtist(etFotoUrl.getText().toString().trim());
                    }
                })
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.setView(etFotoUrl);
        builder.show();
    }
}

