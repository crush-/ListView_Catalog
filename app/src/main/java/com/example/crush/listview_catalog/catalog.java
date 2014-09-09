package com.example.crush.listview_catalog;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class catalog extends Activity implements OnItemClickListener {

    String[] member_names;
 //   TypedArray profile_pics;
    String[] statues;
    String[] contactType;
    List<String> file_array = new ArrayList<String>();


    List<RowItem> rowItems;
    ListView mylistview;
    private static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/data/";

    private List<String> create_file_array(){
        File myfile = new File(path);

        for(int i=0;i < myfile.list().length; i++ ){
         file_array.add(myfile.list()[i]);
        }
        return file_array;
    }

    private Drawable create_catalog_icon(String val){

        Bitmap bmp = BitmapFactory.decodeFile(path + val + "/icon.png");
        Drawable d = new BitmapDrawable(getResources(), bmp);
        return d;

    }

    private List<String> create_catalog_text(){
    //    List<String> file_array = create_file_array();
        List<String> mystring = new ArrayList<String>();
        for( String oneItem : file_array ) {

            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;
            String text = "";

            try {
                // Apertura del fichero y creacion de BufferedReader para poder
                // hacer una lectura comoda (disponer del metodo readLine()).
                archivo = new File(path + oneItem + "/text.txt");
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);

                // Lectura del fichero
                String linea;
                while ((linea = br.readLine()) != null)
                    text += linea;
                mystring.add(text);


            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                try {
                    if (null != fr) {
                        fr.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        }
        return mystring;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        rowItems = new ArrayList<RowItem>();

        file_array = create_file_array();
        String[] simpleArray = new String[ file_array.size() ];
        file_array.toArray( simpleArray );

        member_names = simpleArray;

        List<String> myfile2 = create_catalog_text();
        String[] simpleArray2 = new String[ myfile2.size() ];
        myfile2.toArray( simpleArray2 );


        statues = simpleArray2;

        List<String> myArray3 = new ArrayList<String>();
        myArray3.addAll(Arrays.asList("","","","","","","","","","","","","","","","","","","","","",""));

        String[] simpleArray3 = new String[ myArray3.size()];
        contactType = simpleArray3;

        for (int i = 0; i < member_names.length; i++) {
            RowItem item = new RowItem(member_names[i],
                    create_catalog_icon(member_names[i]), statues[i],
                    contactType[i]);
            rowItems.add(item);
        }

        mylistview = (ListView) findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);
 //       profile_pics.recycle();
        mylistview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        String member_name = rowItems.get(position).getMember_name();

        File myfile = new File(path  + member_name + "/Catalog.pdf");
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(myfile), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        startActivity(intent);
    }

}