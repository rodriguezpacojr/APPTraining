package com.training.training;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutOf extends AppCompatActivity
{
    @BindView(R.id.txtwebTec) TextView txtwebTec;
    @BindView(R.id.txtemailDev) TextView txtemailDev;
    @BindView(R.id.txtphoneDev) TextView txtphoneDev;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_of);

        ButterKnife.bind(this);

        Linkify.addLinks(txtwebTec, Linkify.WEB_URLS);
        Linkify.addLinks(txtemailDev, Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(txtphoneDev, Linkify.PHONE_NUMBERS);
    }
}
