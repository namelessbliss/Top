package adolfopardo.top;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.containerMain)
    CoordinatorLayout containerMain;


    private ArtistaAdapter adapter;

    public static final Artista sArtista = new Artista();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configToolBar();
        configAdapter();
        configRecyclerView();
        generateArtist();
    }

    private void generateArtist() {

        String[] nombres = {"Hime", "Kaguya", "1号 Ichi-gō"};
        String[] apellidos = {"Arikawa", "Arikawa", ""};
        String[] generos = {"Male", "Male", "Female"};
        long[] nacimientos = {1009843200000L, 1041379200000L, 1009843200000L};
        String[] lugares = {"Japan", "Japan", "Japan"};
        short[] estaturas = {165, 163, 164};
        String[] notas = {"Hime is the protagonist of Himegoto. He is a feminine second-year at " +
                "Shimoshina High School, and is also a member of its student council.",
                "Kaguya is " +
                        "Hime's younger brother who cross-dresses by choice because he enjoys the attention " +
                        "he receives from it. ",
                "No. 1 is Kaguya's classmate; her moniker refers to her status as Kaguya's foremost and " +
                        "closest admirer.[vol. 1] While she normally dresses as a boy, when she dresses as " +
                        "a girl, she has a large bust size and is regarded as incredibly pretty. She joined " +
                        "the public morals committee with Kaguya. "};
        String[] fotos = {"http://pm1.narvii.com/5862/dbdf8d4c86aac939fa59b9309c079636a8b6d876_hq.jpg",
                "http://adn.i.ntere.st/p/11065000/image",
                "http://static.zerochan.net/Geboku.Ichi-gou.full.1714362.jpg"};

        for (int i = 0; i < 3; i++) {
            Artista artista = new Artista(i + 1, nombres[i], apellidos[i], generos[i],
                    nacimientos[i], lugares[i], estaturas[i], notas[i], i + 1, fotos[i]);

            adapter.add(artista);

        }
    }

    private void configToolBar() {

        setSupportActionBar(toolbar);
    }

    private void configAdapter() {

        adapter = new ArtistaAdapter(new ArrayList<Artista>(), this);
    }


    private void configRecyclerView() {


        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /***
     *      METODOS IMPLEMENTADOS POR LA INTERFAS  OnItemClickListener
     * ****/
    @Override
    public void onItemClick(Artista artista) {
        sArtista.setId(artista.getId());
        sArtista.setNombre(artista.getNombre());
        sArtista.setApellido(artista.getApellido());
        sArtista.setGenero(artista.getGenero());
        sArtista.setFechaNacimiento(artista.getFechaNacimiento());
        sArtista.setEtatura(artista.getEtatura());
        sArtista.setLugarNacimiento(artista.getLugarNacimiento());
        sArtista.setOrden(artista.getOrden());
        sArtista.setNotas(artista.getNotas());
        sArtista.setFotoUrl(artista.getFotoUrl());

        Intent intent = new Intent(MainActivity.this,DetalleActivity.class);
        startActivity(intent);

    }

    @Override
    public void onLongItemClick(Artista artista) {

    }

    protected void onActivityResult(int requesCode, int resultCode, Intent data){
        super.onActivityResult(requesCode, resultCode, data);

        if (resultCode == RESULT_OK && requesCode == 1){
            adapter.add(sArtista);

        }
    }

    @OnClick(R.id.fab)
    public void addArtist() {
        Intent intent = new Intent(MainActivity.this, AddArtistActivity.class);
        intent.putExtra(Artista.ORDEN, adapter.getItemCount()+1);
        /*startActivity(intent);*/
        startActivityForResult(intent,1);

    }


}
