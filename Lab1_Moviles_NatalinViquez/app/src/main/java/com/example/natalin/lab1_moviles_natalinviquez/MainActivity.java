package com.example.natalin.lab1_moviles_natalinviquez;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private static final int IMAGE_CAPTURE = 12;

    ArrayList<Persona> listaPersonas = new ArrayList<Persona>();
    Persona persona;
    String nom, prof, sex, img;
    ListView listPersonas;
    ImageView foto;
    EditText agregarNombre;
    EditText agregarProfesion;
    RadioButton female, male;
    Button butAddImage, butSave;
    TextView speechTextView ;
    Dialog matchTextDialog ;
    ListView textListView ;
    ArrayList < String > matchesText ;
    String uri = "";

    List<String> listaSimple =  Arrays.asList("There aren't restaurants");

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listPersonas = (ListView) findViewById(R.id.mainActivity_listViewRestaurants);
        butSave= (Button) findViewById(R.id.butSave);
        butAddImage= (Button)findViewById(R.id.addImage);
        female = (RadioButton) findViewById(R.id.radioFemale);
        male = (RadioButton) findViewById(R.id.radioMale);
        foto= (ImageView) findViewById(R.id.fotoImage);

        agregarNombre = (EditText) findViewById(R.id.addNombre);
        agregarProfesion = (EditText) findViewById(R.id.addProfesion);

        butSave.setOnClickListener(onSave);

        butAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Tomar foto");
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE) ;
                startActivityForResult ( intent , IMAGE_CAPTURE ) ;
            }
        });

        agregarProfesion.setOnClickListener ( new View . OnClickListener () {
            @Override
            public void onClick ( View v ) {
                if( isConnected () ) {
                    Intent intent = new
                            Intent ( RecognizerIntent. ACTION_RECOGNIZE_SPEECH ) ;
                    intent . putExtra ( RecognizerIntent . EXTRA_LANGUAGE_MODEL ,
                            RecognizerIntent . LANGUAGE_MODEL_FREE_FORM ) ;
                    startActivityForResult ( intent , REQUEST_CODE ) ;
                } else {
                    Toast. makeText ( getApplicationContext () , "Please Connect to Internet", Toast . LENGTH_SHORT ) . show () ;
                }
            }
        }) ;
    }

    @Override
    protected void onActivityResult ( int requestCode , int resultCode , Intent
            data ) {
        super . onActivityResult ( requestCode , resultCode , data ) ;
        if ( requestCode == REQUEST_CODE && resultCode == RESULT_OK ){
            matchTextDialog = new Dialog ( MainActivity . this ) ; // Create Dialog
            matchTextDialog . setContentView ( R.layout.dialog_matches_frag) ;
// Link the new Dialog with the dialog_matches frag
            matchTextDialog . setTitle (" Select Matching Text ") ; // Add title tothe Dialog
            textListView = ( ListView )
                    matchTextDialog . findViewById ( R.id.listResult ) ;
            matchesText = data . getStringArrayListExtra ( RecognizerIntent . EXTRA_RESULTS );
// Get data of data
            ArrayAdapter < String > adapter = new ArrayAdapter < String >( this , android . R . layout . simple_list_item_1 , matchesText ) ;
            textListView . setAdapter ( adapter );
            textListView . setOnItemClickListener ( new AdapterView . OnItemClickListener () {
                @Override
                public void onItemClick ( AdapterView <? > parent , View view , int
                        position , long id ) {
                    agregarProfesion . setText (matchesText . get ( position )) ;
                    matchTextDialog . hide () ;
                }
            }) ;
            matchTextDialog . show () ;
        }

        if(requestCode == IMAGE_CAPTURE){
            Uri UriResult = data.getData ();
            if ( resultCode == RESULT_OK ) {
                uri =  UriResult.toString();

                Toast . makeText (this , "Guardado", Toast . LENGTH_LONG ) . show () ;
            } else if ( resultCode == RESULT_CANCELED ) {
                Toast . makeText (this , " Cancelado",
                        Toast . LENGTH_LONG ) . show () ;
            } else {
                Toast . makeText (this , " Error",
                        Toast . LENGTH_LONG ) . show () ;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nom = agregarNombre.getText().toString();
            prof = agregarProfesion.getText().toString();
            if (female.isChecked()) {
                sex = "Female";
            } else if (male.isChecked()) {
                sex = "Male";
            }
            persona = new Persona(nom, prof, sex, uri);
            listaPersonas.add(persona);
            uri="";
            adapterFunction();
        }
    };


    public void adapterFunction(){
        if(listaPersonas.size()>0){
            listPersonas.setAdapter(new viewAdapter(this));      }
        else{
            simpleList();
        }
    }


    public class viewAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        public viewAdapter(Context context){
            layoutInflater = layoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return listaPersonas.size();
        }

        @Override
        public Object getItem(int i) {
            return listaPersonas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view = layoutInflater.inflate(R.layout.estructura,null);
            }
            final int posicionListView = i;
            final TextView titleName = (TextView)view.findViewById(R.id.textName);
            final TextView titleProfesion = (TextView)view.findViewById(R.id.textProf);
            final TextView titleSexo = (TextView)view.findViewById(R.id.textSexo);


            titleName.setText(R.string.stringTitleName);
            titleProfesion.setText(R.string.stringTitleProfesion);
            titleSexo.setText(R.string.stringTitleSexo);

            ImageView image = (ImageView)view.findViewById(R.id.fotoImage);
            TextView name = (TextView)view.findViewById(R.id.textName);
            TextView prof = (TextView)view.findViewById(R.id.textProf);
            TextView genero = (TextView)view.findViewById(R.id.textSexo);

            name.setText(listaPersonas.get(posicionListView).getName());
            prof.setText(listaPersonas.get(posicionListView).getProfesion());
            genero.setText(listaPersonas.get(posicionListView).getSexo());
            image.setImageURI(Uri.parse(listaPersonas.get(posicionListView).getImage()));


            return view;
        }
    }
    public void simpleList(){
        adapter = new ArrayAdapter<String>(this,R.layout.estructura
                , listaSimple);
        listPersonas.setAdapter(adapter);
    }

    public boolean isConnected () {
        ConnectivityManager cm = ( ConnectivityManager )
                getSystemService ( Context . CONNECTIVITY_SERVICE ) ;
        NetworkInfo net = cm . getActiveNetworkInfo () ;
        if ( net != null && net . isAvailable () && net . isConnected () ) {
            return true ;
        } else {
            return false ;
        }
    }
}
