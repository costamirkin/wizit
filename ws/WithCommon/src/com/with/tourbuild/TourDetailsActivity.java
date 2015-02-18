package com.with.tourbuild;

import com.example.withcommon.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TourDetailsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_details);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int tourId  = bundle.getInt("tourId", -1);

			if (tourId != -1) {
				Tour tour = CommonShared.getInstance().getmTours().get(tourId);
				TextView tourNameTV   = (TextView)findViewById(R.id.tourNameTV);
				TextView tourDescrTV  = (TextView)findViewById(R.id.tourDescrTV);
			
				tourNameTV.setText(tour.getmTourName());
				tourDescrTV.setText(tour.getmTourDescription());
			}
		}

		
	}

}
