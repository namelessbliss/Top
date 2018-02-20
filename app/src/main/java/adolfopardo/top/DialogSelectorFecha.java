package adolfopardo.top;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrador on 19/02/2018.
 */

public class DialogSelectorFecha extends DialogFragment{
    public static final String FECHA = "fecha";
    public static final String SELECTED_DATE = "selectedDate";

    private DatePickerDialog.OnDateSetListener listener;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance(Locale.ROOT);
        Bundle args = this.getArguments();
        if (args != null){
            long fecha = args.getLong(FECHA);
            calendar.setTimeInMillis(fecha);
        }

        int anyo = calendar.get(calendar.YEAR);
        int mes = calendar.get(calendar.MONTH);
        int dia = calendar.get(calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),listener,anyo,mes,dia);
    }
}
