package com.zerobounce.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ZeroBounceApi {
	private final String api_key;
	private final String baseUrl = "https://api.zerobounce.net/v2";
	private final HttpClient httpClient;
	private final DateFormat dateTimeMillisFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ENGLISH);
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	/**
	 * Get an instance of the API helper class
	 *
	 * @param apiKey - your private API key
	 */
	public ZeroBounceApi(String apiKey, int timeoutSeconds) {
		this.api_key = apiKey;

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeoutSeconds * 1000).build();
		this.httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
	}

	/**
	 * @return - the number of credits remaining on your account
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public int getCredits() throws IOException, URISyntaxException {
		URI uri = new URIBuilder(baseUrl + "/getcredits")
				.addParameter("api_key", this.api_key)
				.build();
		HttpGet httpGet = new HttpGet(uri);

		HttpResponse httpResponse = httpClient.execute(httpGet);

		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new IllegalStateException(httpResponse.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(httpResponse.getEntity()));
		} else {
			return new JSONObject(EntityUtils.toString(httpResponse.getEntity())).getInt("Credits");
		}
	}

	/**
	 * @param email - the email you want to validate
	 * @return - a JSONObject with all of the information for the specified email
	 * @throws IOException
	 * @throws URISyntaxException
	 */

	public ZeroBounceResponse validate(String email, String ip) throws IOException, URISyntaxException {
		URI uri = new URIBuilder(baseUrl + "/validate")
				.addParameter("api_key", this.api_key)
				.addParameter("email", email)
				.addParameter("ip_address", ip)
				.build();

		HttpGet httpGet = new HttpGet(uri);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));

				String emailAddress = jsonObject.isNull("address") ? null : jsonObject.getString("address");
				String status = jsonObject.isNull("status") ? null : jsonObject.getString("status");
				String subStatus = jsonObject.isNull("sub_status") ? null : jsonObject.getString("sub_status");
				String account = jsonObject.isNull("account") ? null : jsonObject.getString("account");
				String domain = jsonObject.isNull("domain") ? null : jsonObject.getString("domain");
				String mxRecord = jsonObject.isNull("mx_record") ? null : jsonObject.getString("mx_record");
				Boolean mxFound = jsonObject.isNull("mx_found") ? null : jsonObject.getBoolean("mx_found");
				Boolean freeEmail = jsonObject.isNull("free_email") ? null : jsonObject.getBoolean("free_email");
				String didYouMean = jsonObject.isNull("did_you_mean") ? null : jsonObject.getString("did_you_mean");
				String domainAgeDays = jsonObject.isNull("domain_age_days") ? null : jsonObject.getString("domain_age_days");
				String smtpProvider = jsonObject.isNull("smtp_provider") ? null : jsonObject.getString("smtp_provider");
				String firstName = jsonObject.isNull("firstname") ? null : jsonObject.getString("firstname");
				String lastName = jsonObject.isNull("lastname") ? null : jsonObject.getString("lastname");
				String gender = jsonObject.isNull("gender") ? null : jsonObject.getString("gender");
				String country = jsonObject.isNull("country") ? null : jsonObject.getString("country");
				String city = jsonObject.isNull("city") ? null : jsonObject.getString("city");
				String zipcode = jsonObject.isNull("zipcode") ? null : jsonObject.getString("zipcode");
				String region = jsonObject.isNull("region") ? null : jsonObject.getString("region");

				Date processedAt = null;
				try {
					processedAt = jsonObject.isNull("processed_at") ? null : dateTimeMillisFormat.parse(jsonObject.getString("processed_at"));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				return new ZeroBounceResponse(emailAddress, status, subStatus, account, domain, mxFound, freeEmail, didYouMean, domainAgeDays, mxRecord, smtpProvider, firstName, lastName, gender, processedAt, country, city, zipcode, region);
			} else {
				throw new IllegalStateException(httpResponse.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(httpResponse.getEntity()));
			}
		} catch (SocketTimeoutException ex) {
			return new ZeroBounceResponse(email.toLowerCase(), "Unknown", "timeout_exceeded", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		} catch (Exception ex) {
			return new ZeroBounceResponse(email.toLowerCase(), "Unknown", "exception_occurred", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		}
	}

	/**
	 * Response wrapper for /validate endpoint
	 */
	protected class ZeroBounceResponse {
		private final String emailAddress;
		private final String status;
		private final String subStatus;
		private final String account;
		private final String domain;
		private final Boolean mxFound;
		private final Boolean freeEmail;
		private final String didYouMean;
		private final String domainAgeDays;
		private final String mxRecord;
		private final String smtpProvider;
		private final String firstName;
		private final String lastName;
		private final String gender;
		private final Date processedAt;
		private final String country;
		private final String city;
		private final String zipcode;
		private final String region;

		public ZeroBounceResponse(String emailAddress, String status, String subStatus, String account, String domain, Boolean mxFound, Boolean freeEmail, String didYouMean, String domainAgeDays, String mxRecord, String smtpProvider, String firstName, String lastName, String gender, Date processedAt, String country, String city, String zipcode, String region) {
			this.emailAddress = emailAddress;
			this.status = status;
			this.subStatus = subStatus;
			this.account = account;
			this.domain = domain;
			this.mxFound = mxFound;
			this.freeEmail = freeEmail;
			this.didYouMean = didYouMean;
			this.domainAgeDays = domainAgeDays;
			this.mxRecord = mxRecord;
			this.smtpProvider = smtpProvider;
			this.firstName = firstName;
			this.lastName = lastName;
			this.gender = gender;
			this.processedAt = processedAt;
			this.country = country;
			this.city = city;
			this.zipcode = zipcode;
			this.region = region;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public String getStatus() {
			return status;
		}

		public String getSubStatus() {
			return subStatus;
		}

		public String getAccount() {
			return account;
		}

		public String getDomain() {
			return domain;
		}

		public Boolean GetFreeEmail() {
			return freeEmail;
		}

		public Boolean GetMXFound() {
			return mxFound;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getMXRecord() {
			return mxRecord;
		}

		public String getSMTPProvider() {
			return smtpProvider;
		}

		public String getDidYouMean() {
			return didYouMean;
		}

		public String getDomainAgeDays() {
			return domainAgeDays;
		}

		public String getLastName() {
			return lastName;
		}

		public String getGender() {
			return gender;
		}

		public Date getProcessedAt() {
			return processedAt;
		}

		public String getCountry() {
			return country;
		}

		public String getCity() {
			return city;
		}

		public String getZipcode() {
			return zipcode;
		}

		public String getRegion() {
			return region;
		}


		@Override
		public String toString() {
			return "ZeroBounceResponseWithIp{" +
					"emailAddress='" + emailAddress + '\'' +
					", status='" + status + '\'' +
					", sub_status='" + subStatus + '\'' +
					", account='" + account + '\'' +
					", domain='" + domain + '\'' +
					", firstname='" + firstName + '\'' +
					", lastname='" + lastName + '\'' +
					", gender='" + gender + '\'' +
					", smtp_provider='" + smtpProvider + '\'' +
					", free_email='" + freeEmail + '\'' +
					", domain_age_days='" + domainAgeDays + '\'' +
					", did_you_mean='" + didYouMean + '\'' +
					", mx_record='" + mxRecord + '\'' +
					", mx_found=" + mxFound +
					", processed_at=" + processedAt +
					", country='" + country + '\'' +
					", city='" + city + '\'' +
					", zipcode='" + zipcode + '\'' +
					", region='" + region + '\'' +
					'}';
		}
	}
}
