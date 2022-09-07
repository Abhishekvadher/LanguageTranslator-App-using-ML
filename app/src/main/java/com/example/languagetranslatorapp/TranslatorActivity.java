package com.example.languagetranslatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TranslatorActivity extends AppCompatActivity {

    private Spinner fromSpinner,toSpinner;
    private EditText editTextSource;
    private Button translateBtn;
    private TextView translatedText;

    String[] fromLang = {"Select From","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Hindi","Urdu","Gujarati"};
    String[] toLang = {"Select To","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Hindi","Urdu","Gujarati"};

    private static final int REQUEST_CODE = 1;
    String languageCode,fromLanguageCode,toLanguageCode ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        editTextSource = findViewById(R.id.editSource);
        translateBtn = findViewById(R.id.translateBtn);
        translatedText = findViewById(R.id.translateResult);
        FromSpinner();
        ToSpinner();
        ButtonVerification();
    }

    public void FromSpinner(){

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                fromLanguageCode = GetLanguageCode(fromLang[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item,fromLang);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);
    }

    public void ToSpinner(){

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                toLanguageCode = GetLanguageCode(toLang[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinner_item,toLang);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);
    }

    public void ButtonVerification(){

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                translatedText.setText("");

                if(editTextSource.getText().toString().isEmpty()){
                    Toast.makeText(TranslatorActivity.this, "Please Enter Text", Toast.LENGTH_SHORT).show();
                }else if (fromLanguageCode.isEmpty()){
                    Toast.makeText(TranslatorActivity.this, "Please Select From Language", Toast.LENGTH_SHORT).show();
                }else if (toLanguageCode.isEmpty()){
                    Toast.makeText(TranslatorActivity.this, "Please Select To Language", Toast.LENGTH_SHORT).show();
                }else{

                    TranslationProcess(fromLanguageCode,toLanguageCode,editTextSource.getText().toString());
                }
            }
        });

    }

    private String GetLanguageCode(String language) {

        String langcode = "";
        switch (language){

            case "English":
                langcode = TranslateLanguage.ENGLISH;
                break;

            case "Afrikaans":
                langcode = TranslateLanguage.AFRIKAANS;
                break;

            case "Arabic":
                langcode = TranslateLanguage.ARABIC;
                break;

            case "Belarusian":
                langcode = TranslateLanguage.BELARUSIAN;
                break;

            case "Bengali":
                langcode = TranslateLanguage.BENGALI;
                break;

            case "Catalan":
                langcode = TranslateLanguage.CATALAN;
                break;

            case "Hindi":
                langcode = TranslateLanguage.HINDI;
                break;

            case "Urdu":
                langcode = TranslateLanguage.URDU;
                break;

            case "Gujarati":
                langcode = TranslateLanguage.GUJARATI;
                break;

            default:
                langcode = "";

        }
        return langcode;
    }

    private void TranslationProcess(String fromLanguageCode, String toLanguageCode, String src) {

        translatedText.setText("Please wait a moment...");
        try {
            TranslatorOptions options = new TranslatorOptions
                    .Builder()
                    .setSourceLanguage(fromLanguageCode)
                    .setTargetLanguage(toLanguageCode)
                    .build();

            Translator translator = Translation.getClient(options);
            DownloadConditions conditions = new DownloadConditions.Builder().build();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    translatedText.setText("Translating...");
                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {

                            translatedText.setText(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(TranslatorActivity.this, "Failed To Translate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(TranslatorActivity.this, "Failed To Download Language", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}