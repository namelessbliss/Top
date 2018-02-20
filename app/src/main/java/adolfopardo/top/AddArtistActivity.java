package adolfopardo.top;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddArtistActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.imageFoto)
    AppCompatImageView imageFoto;
    @BindView(R.id.etNombre)
    TextInputEditText etNombre;
    @BindView(R.id.etApellidos)
    TextInputEditText etApellidos;
    @BindView(R.id.etFechaNacimiento)
    TextInputEditText etFechaNacimiento;
    @BindView(R.id.etEstatura)
    TextInputEditText etEstatura;
    @BindView(R.id.etLugarNacimiento)
    TextInputEditText etLugarNacimiento;
    @BindView(R.id.etNotas)
    TextInputEditText etNotas;
    @BindView(R.id.etGenero)
    TextInputEditText etGenero;

    private Artista mArtista;
    private Calendar mCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);
        ButterKnife.bind(this);

        configActionBar();
        configArtista(getIntent());
        configCalendar();

    }

    private void configActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void configArtista(Intent intent) {
        mArtista = new Artista();
        mArtista.setFechaNacimiento(System.currentTimeMillis());
        mArtista.setOrden(intent.getIntExtra(Artista.ORDEN, 0));
    }


    private void configCalendar() {
        mCalendar = Calendar.getInstance(Locale.ROOT);
        etFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
                .format(System.currentTimeMillis()));

    }
    //MOSTRAR EL MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // CERRAR LA ACTIVIDAD
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                saveArtist();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void saveArtist() {
        /*mArtista.setNombre(etNombre.getText().toString().trim());
        mArtista.setApellido(etApellidos.getText().toString().trim());
        mArtista.setGenero(etGenero.getText().toString().trim());
        mArtista.setEtatura(Short.valueOf(etEstatura.getText().toString().trim()));
        mArtista.setLugarNacimiento(etLugarNacimiento.getText().toString().trim());
        mArtista.setNotas(etNotas.getText().toString().trim());*/

        if (validateFields()) {
            MainActivity.sArtista.setNombre(etNombre.getText().toString().trim());
            MainActivity.sArtista.setApellido(etApellidos.getText().toString().trim());
            MainActivity.sArtista.setGenero(etGenero.getText().toString().trim());
            MainActivity.sArtista.setEtatura(Short.valueOf(etEstatura.getText().toString().trim()));
            MainActivity.sArtista.setLugarNacimiento(etLugarNacimiento.getText().toString().trim());
            MainActivity.sArtista.setNotas(etNotas.getText().toString().trim());
            MainActivity.sArtista.setOrden(mArtista.getOrden());
            MainActivity.sArtista.setFotoUrl(mArtista.getFotoUrl());

            setResult(RESULT_OK);

            finish();
        }


    }
//VALIDACION DE CAMPOS
    private boolean validateFields() {
        boolean isValid =true;
        if (etEstatura.getText().toString().trim().isEmpty() ||
                Integer.valueOf(etEstatura.getText().toString().trim()) < getResources().getInteger(R.integer.estatura_min)) {
            etEstatura.setError(getString(R.string.addArtist_error_estaturaMin));
            etEstatura.requestFocus();
            isValid = false;
        }
        if (etApellidos.getText().toString().trim().isEmpty()){
            etApellidos.setError(getString((R.string.addArtist_error_required)));
            etApellidos.requestFocus();
            isValid =false;
        }
        if (etNombre.getText().toString().trim().isEmpty()){
            etNombre.setError(getString((R.string.addArtist_error_required)));
            etNombre.requestFocus();
            isValid =false;
        }

        return isValid;
    }

    /**
     * CONFIGURACION DE SELECTOR DE FECHA
     ***/
    @OnClick(R.id.etFechaNacimiento)
    public void onSetFecha() {
        DialogSelectorFecha selectorFecha = new DialogSelectorFecha(); //de la nueva clase del mismo nombre
        selectorFecha.setListener(AddArtistActivity.this);

        Bundle args = new Bundle();
        args.putLong(DialogSelectorFecha.FECHA, mArtista.getFechaNacimiento());
        selectorFecha.setArguments(args);
        selectorFecha.show(getSupportFragmentManager(), DialogSelectorFecha.SELECTED_DATE);
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
    }

    @OnClick({R.id.imageDeleteFoto, R.id.imageFromGalery, R.id.imageFromUrl})
    public void ImageEvents(View view) {
        switch (view.getId()) {
            case R.id.imageDeleteFoto:
                break;
            case R.id.imageFromGalery:
                break;
            case R.id.imageFromUrl:
                showAddPhotoDialog();
                break;
        }
    }

    private void showAddPhotoDialog() {
        final EditText etFotoUrl = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.addArtist_dialogUrl_title)
                .setPositiveButton(R.string.Label_dialog_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        configImageView(etFotoUrl.getText().toString().trim());
                    }
                })
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.setView(etFotoUrl);
        builder.show();
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
}