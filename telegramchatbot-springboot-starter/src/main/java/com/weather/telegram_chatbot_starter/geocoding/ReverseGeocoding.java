package com.weather.telegram_chatbot_starter.geocoding;

import java.io.IOException;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class ReverseGeocoding { 

	public String getCity(double lat, double lng) throws ApiException, InterruptedException, IOException {

		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyD2vVD-ZgpKbAvjtk158jIu72UwYm44fp0").build();
		GeocodingResult[] results = GeocodingApi.newRequest(context).latlng(new LatLng(lat, lng)).language("en")
				.resultType(AddressType.COUNTRY, AddressType.ADMINISTRATIVE_AREA_LEVEL_2).await();

		String city = results[0].addressComponents[0].longName;

		return city;
	}

}
